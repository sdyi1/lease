package com.nanhang.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.common.exception.LeaseException;
import com.nanhang.lease.common.result.ResultCodeEnum;
import com.nanhang.lease.model.entity.*;
import com.nanhang.lease.model.enums.ItemType;
import com.nanhang.lease.model.enums.ReleaseStatus;
import com.nanhang.lease.web.admin.mapper.*;
import com.nanhang.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.nanhang.lease.web.admin.vo.fee.FeeKeyVo;
import com.nanhang.lease.web.admin.vo.fee.FeeValueVo;
import com.nanhang.lease.web.admin.vo.graph.GraphVo;
import kotlin.jvm.internal.Lambda;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {


    //    针对表【apartment_fee_value(公寓&杂费关联表)】的数据库操作Service
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    //    针对表【graph_info(公寓&图片关联表)】的数据库操作Service
    @Autowired
    private GraphInfoService graphInfoService;
    //    针对表【apartment_label(公寓标签关联表)】的数据库操作Service
    @Autowired
    private ApartmentLabelService apartmentLabelService;

    //    针对表【apartment_facility(公寓&配套关联表)】的数据库操作Service
    @Autowired
    private ApartmentFacilityService apartmentFacilityService;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    //针对表【facility_info(配套信息表)】的数据库操作Service
    @Autowired
    private FacilityInfoService facilityInfoService;
    //针对表【label_info(标签信息表)】的数据库操作Service
    @Autowired
    private LabelInfoService labelInfoService;
    //针对表【label_info(标签信息表)】的数据库操作Mapper
    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private FeeValueMapper feeValueMapper;
    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private FeeKeyService feeKeyService;

    //用于判断公寓可租赁房间数量
    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private FeeValueService feeValueService;;



    //"保存或更新公寓信息"
    @Override
        public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
    //添加逻辑：1
    //修改逻辑：2


        /*  前端如果是添加传给我们的数据转换成对象的时候Id是null,
            只有添加入数据库才会通过主键自增给数据添加Id，
            所以这里可以利用这个判断是否是添加*/

        //1:判断公寓对象是否存在：id=null  说明是添加  返回false
        //2判断公寓对象是否存在：id!=null  说明是修改  返回true
        boolean update = apartmentSubmitVo.getId() != null;

        //利用Controller注解的知识点来先添加公寓基本信息（如下）也就是在apartment_info表中添加一行公寓数据
         /*使用自带的方法，会更具@TablezName的表注解来映射数据
        而这里的表没有ApartmentSubmitVo属性对应的列，所以我们不能使用自带的方法而是需要字节写方法*/

        /*在判断好了是否原本存在得到结果用于判断是执行添加还是修改曹祖后，将基本添加到apartment_info表中，参考上面注释*/
        super.saveOrUpdate(apartmentSubmitVo);

            //判断update的值
        if(update){
            //1:update==false，是添加，就不用删除，跳过判断
            //2:update==true

        //删除图片列表
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                //配置条件
                //首先判断图片类型是公寓类型，防止删除房间类型的图片，因为房间类型的图片id可能和公寓类型的图片Id一样
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT)
                //其次判断图片的所有对象id是否和公寓的id相等，防止删除其他公寓的图片
                    .eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
                //根据配置好的条件删除图片
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

        //删除标签列表
            LambdaQueryWrapper<ApartmentLabel> ApartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                //设置条件
            ApartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId,apartmentSubmitVo.getId());
                //删除
            apartmentLabelService.remove(ApartmentLabelLambdaQueryWrapper);
        //删除杂费列表

                //创建条件对象配置条件
            LambdaQueryWrapper<ApartmentFeeValue> FeeKeyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            FeeKeyLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId,apartmentSubmitVo.getId());
                //删除
                apartmentFeeValueService.remove(FeeKeyLambdaQueryWrapper);


        //删除配套列表
            //创建条件对象配置调价
            LambdaQueryWrapper<ApartmentFacility> FacilityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            FacilityInfoLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId,apartmentSubmitVo.getId());
        apartmentFacilityService.remove(FacilityInfoLambdaQueryWrapper);

        }

        //1:添加剩下的属性
        //2:添加剩下的属性

        //添加图片列表


            //获取前端传入的图片集合
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
            //前端传入的GraphVo只有 图片名称和 url,我们是将 图片传入到表graph_info中，通过图片类型属性
            //需要我们手动添加图片类型和图片对象id
            //图片类型是公寓类型
            //图片对象id是公寓的id

        if(!CollectionUtils.isEmpty(graphVoList)){
            //为什么需要List：我们可以通过Service的SaveList批量将图片添加到表graph_info中
            List<GraphInfo> graphInfoList = new ArrayList<>();
            //遍历集合，创建GraphInfo对象，传入图片名称，图片类型，图片对象id，图片url
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                //设置图片名称
                graphInfo.setName(graphVo.getName());
                //设置图片类型
                graphInfo.setItemType(ItemType.APARTMENT);
                //设置图片所属对象Id（图片属于哪个公寓）
                graphInfo.setItemId(apartmentSubmitVo.getId());
                //设置图片url
                graphInfo.setUrl(graphVo.getUrl());
                //将graphInfo对象添加到集合中
                graphInfoList.add(graphInfo);
            }
            //批量添加图片
            graphInfoService.saveBatch(graphInfoList);

        }


//-------------------------------------------------------------------------------------------------------------------------


        //添加标签列表

        List<Long> labelIds = apartmentSubmitVo.getLabelIds();

        if(!CollectionUtils.isEmpty(labelIds)) {

            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            //遍历前端传来的标签id集合，每个id创建成ApartmentLabel对象，添加到标签表中，通过apartmentId来怕毛短该标签属于哪个公寓
            for (Long labelId : labelIds) {

                //使用构造器模式创建对象，设置属性
                ApartmentLabel apartmentLabel = ApartmentLabel.builder()
                        .apartmentId(apartmentSubmitVo.getId())
                        .labelId(labelId)
                        .build();
                //将打包好的对象添加到集合中
                apartmentLabelList.add(apartmentLabel);
            }
            //批量添加标签列表
            apartmentLabelService.saveBatch(apartmentLabelList);
        }


//-------------------------------------------------------------------------------------------------------------------------


        //添加配套列表

            List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
           //先判断前端传进来的公寓配套数据不为空
            if(!CollectionUtils.isEmpty(facilityInfoIds)){

                //创建集合，用于存储配套对象
                List<ApartmentFacility> apartmentFacilityList = new ArrayList<>();

                for (Long facilityInfoId : facilityInfoIds) {
                    //创建对象,添加数据
                    ApartmentFacility apartmentFacility =
                            ApartmentFacility.builder()
                                    .apartmentId(apartmentSubmitVo.getId())
                                    .facilityId(facilityInfoId)
                                    .build();
                    //将对象添加到集合中
                    apartmentFacilityList.add(apartmentFacility);

                }
                //批量添加配套列表
                apartmentFacilityService.saveBatch(apartmentFacilityList);

            }



//-------------------------------------------------------------------------------------------------------------------------

        //添加杂费列表
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();

            //判断前端传进来的杂费数据不为空
        if(!CollectionUtils.isEmpty(feeValueIds)) {
            //创建集合存放杂费对象
            List<ApartmentFeeValue> apartmentFeeValues = new ArrayList<>();

            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder()
                        .apartmentId(apartmentSubmitVo.getId())
                        .feeValueId(feeValueId)
                        .build();
                //将对象添加到集合中
                apartmentFeeValues.add(apartmentFeeValue);

            }
            apartmentFeeValueService.saveBatch(apartmentFeeValues);


        }


    }


//     "根据条件分页查询公寓列表"
    @Override
    public IPage<ApartmentItemVo> selectIPage(Page<ApartmentItemVo> apartmentItemVoPage, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.selectIpage(apartmentItemVoPage,queryVo);
    }

    @Override
    public ApartmentDetailVo selectByIdDiy(Long id) {
    //查询公寓基本信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
    //查询图片列表
       /* LambdaQueryWrapper<GraphInfo> graphVoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphVoLambdaQueryWrapper.eq(GraphInfo::getItemId,id);
        graphVoLambdaQueryWrapper.eq(GraphInfo::getItemType,ItemType.APARTMENT);
        List<GraphInfo> graphInfoList = graphInfoService.list(graphVoLambdaQueryWrapper);
            //ApartmentDetailVo 需要的是GraphVo对象，遍历集合转化对象,放入GraphVo集合
        List<GraphVo> graphVoList = new ArrayList<>();
        for (GraphInfo graphInfo : graphInfoList) {
            GraphVo graphVo = new GraphVo();
            graphVo.setName(graphInfo.getName());
            graphVo.setUrl(graphInfo.getUrl());
            graphVoList.add(graphVo);
        }*/
            //尝试使用Mapper自己写Sql语句的方法实现，会简介的很多

        List<GraphVo> graphVoList = graphInfoMapper.selectGraphVoByApartmentId(id);
    //查询标签列表
            /*LambdaQueryWrapper<LabelInfo> labelInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            labelInfoLambdaQueryWrapper.eq(LabelInfo::getType,1);
        labelInfoService.list(labelInfoLambdaQueryWrapper);*/
            //尝试使用Mapper自己写Sql语句的方法实现
        List<LabelInfo> labelInfoList = labelInfoMapper.selectLabelInfoByApartmentId(id);
    //查询配套列表
        /*LambdaQueryWrapper<FacilityInfo> facilityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        facilityInfoLambdaQueryWrapper.eq(FacilityInfo::getType,ItemType.APARTMENT);
        facilityInfoService.list(facilityInfoLambdaQueryWrapper);*/
        //尝试使用Mapper自己写Sql语句的方法实现
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectFacilityInfoByApartmentId(id);

        //查询杂费列表
        /*LambdaQueryWrapper<FeeKey> feeKeyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        feeKeyLambdaQueryWrapper.eq(FeeKey::getIsDeleted,0);
        feeKeyService.list(feeKeyLambdaQueryWrapper);

        LambdaQueryWrapper<FeeValue> feeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        feeValueLambdaQueryWrapper.eq(FeeValue::getIsDeleted,0);
        feeValueService.list(feeValueLambdaQueryWrapper);*/
        //尝试使用Mapper自己写Sql语句的方法实现
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectFeeValueVoByApartmentId(id);

        //将公寓基本信息复制到vo对象中
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);

        //将公寓图片列表复制到vo对象中
        apartmentDetailVo.setGraphVoList(graphVoList);
        //将标签列表复制到vo对象中
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        //将配套列表复制到vo对象中
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        //将杂费列表复制到vo对象中
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);




        return apartmentDetailVo;
    }

    //根据id删除公寓
    @Override
    public void apartmentRemoveById(Long id) {
        //自己写Sql语句，计算出id对应公寓下的房间数量
        int roomNumber = roomInfoMapper.selectRoomNumberByApartmentId(id);
    //方法二
        //使用MyBatisPlus自带的方法计算出id对应公寓下的房间数量
        LambdaQueryWrapper<RoomInfo> roomNumberQueryWrapper = new LambdaQueryWrapper<>();
        roomNumberQueryWrapper.eq(RoomInfo::getApartmentId,id);
       long roomNumber2 = roomInfoMapper.selectCount(roomNumberQueryWrapper);
        if(roomNumber2>0){
            //当触发这个异常时，全局异常处理器会调用我们写的getMessage方法获取message，将其printf
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ROOM_ERROR);
        }else {

            //删除图片列表
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //配置条件
            //首先判断图片类型是公寓类型，防止删除房间类型的图片，因为房间类型的图片id可能和公寓类型的图片Id一样
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT)
                    //其次判断图片的所有对象id是否和公寓的id相等，防止删除其他公寓的图片
                    .eq(GraphInfo::getItemId, id);
            //根据配置好的条件删除图片
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

            //删除标签列表
            LambdaQueryWrapper<ApartmentLabel> ApartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //设置条件
            ApartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId, id);
            //删除
            apartmentLabelService.remove(ApartmentLabelLambdaQueryWrapper);
            //删除杂费列表

            //创建条件对象配置条件
            LambdaQueryWrapper<ApartmentFeeValue> FeeKeyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            FeeKeyLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId, id);
            //删除
            apartmentFeeValueService.remove(FeeKeyLambdaQueryWrapper);


            //删除配套列表
            //创建条件对象配置调价
            LambdaQueryWrapper<ApartmentFacility> FacilityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            FacilityInfoLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId, id);
            apartmentFacilityService.remove(FacilityInfoLambdaQueryWrapper);
        }

    }








}




