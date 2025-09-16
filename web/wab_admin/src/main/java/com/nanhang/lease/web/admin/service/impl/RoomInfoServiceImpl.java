package com.nanhang.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.graph.Graph;
import com.nanhang.lease.model.entity.*;
import com.nanhang.lease.model.enums.ItemType;
import com.nanhang.lease.web.admin.mapper.RoomInfoMapper;
import com.nanhang.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanhang.lease.web.admin.vo.attr.AttrValueVo;
import com.nanhang.lease.web.admin.vo.graph.GraphVo;
import com.nanhang.lease.web.admin.vo.room.RoomItemVo;
import com.nanhang.lease.web.admin.vo.room.RoomQueryVo;
import com.nanhang.lease.web.admin.vo.room.RoomSubmitVo;
import kotlin.jvm.internal.Lambda;
import org.apache.tomcat.util.net.SSLUtilBase;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {



    //自动注入图片Service
    @Autowired
    private  GraphInfoService graphInfoService;
    //自动注入属性Service
    @Autowired
    private  AttrValueService attrValueService;
    //自动注入配套Service
    @Autowired
    private RoomFacilityService roomFacilityService;
    ////自动注入标签信息Service
    @Autowired
    private RoomLabelService     roomLabelService;
    //自动注入支付方式Service
    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;
    //自动注入可选租期Service
    @Autowired
    private RoomLeaseTermService roomLeaseTermService;
    //自动注入配套信息Service
    @Autowired
    private RoomAttrValueService roomAttrValueService;

    @Autowired
    private RoomInfoMapper roomInfoMapper;


    
    public RoomInfoServiceImpl(GraphInfoService graphInfoService, AttrValueService attrValueService) {
        this.graphInfoService = graphInfoService;
        this.attrValueService = attrValueService;
    }

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {

        //判断id知否为null,如果为null就直接添加，如果不为null就先删除list再添加
        boolean updateBl = roomSubmitVo.getId() != null;
        //调用父类的saveOrUpdate方法，会更具roomSubmitVo.getId判断是否存在，如果存在会修改，如果不存在会添加
        super.saveOrUpdate(roomSubmitVo);

        if(updateBl){
            //删除图片列表
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId,roomSubmitVo.getId());
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

//检查 //删除属性列表
            LambdaQueryWrapper<RoomAttrValue> attrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            attrValueLambdaQueryWrapper.eq(RoomAttrValue::getAttrValueId,roomSubmitVo.getId());
            attrValueService.removeById(attrValueLambdaQueryWrapper);
            //删除配套信息
            LambdaQueryWrapper<RoomFacility> facilityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            facilityInfoLambdaQueryWrapper.eq(RoomFacility::getRoomId,roomSubmitVo.getId());
            roomFacilityService.remove(facilityInfoLambdaQueryWrapper);


            //删除标签信息
            LambdaQueryWrapper<RoomLabel> roomLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLabelLambdaQueryWrapper.eq(RoomLabel::getRoomId,roomSubmitVo.getId());
            roomLabelService.remove(roomLabelLambdaQueryWrapper);

            //删除支付方式列表
            LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPaymentTypeLambdaQueryWrapper.eq(RoomPaymentType::getRoomId,roomSubmitVo.getId());
            roomPaymentTypeService.remove(roomPaymentTypeLambdaQueryWrapper);

            //删除可选租期列表
            LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLeaseTermLambdaQueryWrapper.eq(RoomLeaseTerm::getRoomId,roomSubmitVo.getId());
            roomLeaseTermService.remove(roomLeaseTermLambdaQueryWrapper);


        }

    //添加属性
        //将图片列表添加到集合中
        List<GraphVo> graphVoList= roomSubmitVo.getGraphVoList();

        //判断图片列表是否为空
        if (!CollectionUtils.isEmpty(graphVoList)) {
            List<GraphInfo> graphInfoList = new ArrayList<>();
            //


            for (GraphVo graphVo : graphVoList) {
                //创建对象
                GraphInfo graphInfo = new GraphInfo();
                //配置图片名称
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setUrl(graphVo.getUrl());
                //添加到集合中
                graphInfoList.add(graphInfo);
            }
            //将配置好的图片列表批量添加到数据库的graph_info表中
            graphInfoService.saveBatch(graphInfoList);
        }

        //添加属性信息
        List<Long> attrValueIdList = roomSubmitVo.getAttrValueIds();
            //判断集合是否为空
        if (!CollectionUtils.isEmpty(attrValueIdList)) {
            //添加属性信息列表
            List<RoomAttrValue> roomAttrValueList = new ArrayList<>();


            for (Long l : attrValueIdList) {
                RoomAttrValue roomAttrValueBuild= RoomAttrValue.builder()
                        .roomId(roomSubmitVo.getId())
                        .attrValueId(l)
                        .build();
                //将属性信息添加到集合中
                roomAttrValueList.add(roomAttrValueBuild);
            }
            //添加属性信息列表  ,save用于添加单个对象，saveBatch用于添加集合
            roomAttrValueService.saveBatch(roomAttrValueList);
        }

        //添加配套信息
        List<Long> facilityIdList = roomSubmitVo.getFacilityInfoIds();
            //判断是否为null
        if(!CollectionUtils.isEmpty(facilityIdList)){
            List<RoomFacility> roomFacilityList = new ArrayList<>();
            for (Long l : facilityIdList) {
                RoomFacility roomFacilityBuild = RoomFacility.builder()
                        .roomId(roomSubmitVo.getId())
                        .facilityId(l)
                        .build();
                roomFacilityList.add(roomFacilityBuild);

                //添加配套信息
                roomFacilityService.saveBatch(roomFacilityList);
            }

        }

        //添加标签信息
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();

        if(!CollectionUtils.isEmpty(labelInfoIds)){
            List<RoomLabel> roomLabelList = new ArrayList<>();
            for (Long labelInfoId : labelInfoIds) {

                RoomLabel roomLabelBuild = RoomLabel.builder()
                        .roomId(roomSubmitVo.getId())
                        .labelId(labelInfoId)
                        .build();
                roomLabelList.add(roomLabelBuild);

            }
            roomLabelService.saveBatch(roomLabelList);


        }

        //支付方式列表
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if(!CollectionUtils.isEmpty(paymentTypeIds)){
            List<RoomPaymentType> roomPaymentTypeList = new ArrayList<>();

            for (Long paymentTypeId : paymentTypeIds) {
                RoomPaymentType roomPaymentTypeBuild =
                        RoomPaymentType.builder()
                                .paymentTypeId(paymentTypeId)
                                .roomId(roomSubmitVo.getId())
                                .build();
                roomPaymentTypeList.add(roomPaymentTypeBuild);
            }
                roomPaymentTypeService.saveBatch(roomPaymentTypeList);

        }

        //添加可选租期列表
        List<Long> leaseTermIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(leaseTermIds)){
            List<RoomLeaseTerm> roomLeaseTermList = new ArrayList<>();

            for (Long leaseTermId : leaseTermIds) {
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder()
                        .leaseTermId(leaseTermId)
                        .roomId(roomSubmitVo.getId())
                        .build();
                roomLeaseTermList.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(roomLeaseTermList);
        }

    }

    //根据条件分页查询公寓列表
    @Override
    public IPage<RoomItemVo> selectIPage(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo) {

        return roomInfoMapper.selectIPage(roomItemVoPage,queryVo);


    }
}




