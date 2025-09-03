package com.nanhang.lease.web.admin.service.impl;

import com.nanhang.lease.model.entity.AttrKey;
import com.nanhang.lease.web.admin.mapper.AttrKeyMapper;
import com.nanhang.lease.web.admin.service.AttrKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanhang.lease.web.admin.vo.attr.AttrKeyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liubo
* @description 针对表【attr_key(房间基本属性表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class AttrKeyServiceImpl extends ServiceImpl<AttrKeyMapper, AttrKey>
    implements AttrKeyService{

    @Autowired
    //我们在MyBatisPlus配置类中已经使用了MapperScan配置了Mapper包，
    // 所以Spring是可以扫描到下面这个Mapper类的，这里报错显示找不到是因为idea不支持MapperScan，
    // 但是咋运行的过程中实际上是可以找到的
    //可以在所有Mapper类上方添加Mapper注解，作用和MapperScan是一样的，二者选一
    private AttrKeyMapper attrKeyMapper;

    @Override
    public List<AttrKeyVo> AttrInfoList() {

        return attrKeyMapper.listAttrInfo();
    }
}




