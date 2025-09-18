package com.nanhang.lease.web.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.model.entity.RoomInfo;
import com.nanhang.lease.web.admin.vo.room.RoomDetailVo;
import com.nanhang.lease.web.admin.vo.room.RoomItemVo;
import com.nanhang.lease.web.admin.vo.room.RoomQueryVo;
import com.nanhang.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface RoomInfoService extends IService<RoomInfo> {

    void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo);

    IPage<RoomItemVo> selectIPage(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo);

    RoomDetailVo selectByIdDiy(Long id);

    void removeByIdDiy(Long id);
}
