package com.nanhang.lease.web.admin.mapper;

import com.nanhang.lease.model.entity.ApartmentInfo;
import com.nanhang.lease.model.enums.LeaseStatus;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentSubmitVo;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.nanhang.lease.model.ApartmentInfo
*/
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);
}




