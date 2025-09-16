package com.nanhang.lease.web.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.model.entity.RoomInfo;
import com.nanhang.lease.web.admin.vo.room.RoomItemVo;
import com.nanhang.lease.web.admin.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.nanhang.lease.model.RoomInfo
*/
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    int selectRoomNumberByApartmentId(Long id);


    IPage<RoomItemVo> selectIPage(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo);
}




