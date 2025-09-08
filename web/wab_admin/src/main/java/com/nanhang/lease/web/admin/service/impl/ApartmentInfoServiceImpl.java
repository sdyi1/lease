package com.nanhang.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nanhang.lease.model.entity.*;
import com.nanhang.lease.model.enums.ItemType;
import com.nanhang.lease.web.admin.mapper.ApartmentInfoMapper;
import com.nanhang.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {


    //    针对表【graph_info(公寓&图片关联表)】的数据库操作Service
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Autowired
    private GraphInfoService graphInfoService;
    //    针对表【apartment_label(公寓标签关联表)】的数据库操作Service
    @Autowired
    private ApartmentLabelService apartmentLabelService;

    //    针对表【apartment_facility(公寓&配套关联表)】的数据库操作Service
    @Autowired
    private ApartmentFacilityService apartmentFacilityService;





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
            ApartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId,ItemType.APARTMENT);
                //删除
            apartmentLabelService.remove(ApartmentLabelLambdaQueryWrapper);
        //删除杂费列表

                //创建条件对象配置条件
            LambdaQueryWrapper<ApartmentFeeValue> FeeKeyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            FeeKeyLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId,ItemType.APARTMENT);
                //删除
                apartmentFeeValueService.remove(FeeKeyLambdaQueryWrapper);


        //删除配套列表
            //创建条件对象配置调价
            LambdaQueryWrapper<ApartmentFacility> FacilityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            FacilityInfoLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId,ItemType.APARTMENT);
        apartmentFacilityService.remove(FacilityInfoLambdaQueryWrapper);

        }

        //1:添加剩下的属性
        //2:添加剩下的属性

        //添加图片列表

        //添加配套列表
        //添加标签列表
        //添加杂费列表
        //添加图片列表





    }
}




