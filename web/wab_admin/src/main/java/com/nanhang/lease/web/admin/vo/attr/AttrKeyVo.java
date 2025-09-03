package com.nanhang.lease.web.admin.vo.attr;

import com.nanhang.lease.model.entity.AttrKey;
import com.nanhang.lease.model.entity.AttrValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

//这个javaBean的意思是 ，一个对象代表一个房间的属性（比如占地），里面的集合存放的是属性的具体值（比如：32平方，36平方）

@Data
public class AttrKeyVo extends AttrKey {
//AttrKeyVo继承AttrKey，所以属性也有AttrKey中的name ,AttrKey继承BaseEntity，所以属性包含 id 创建时间，修改时间 isdelete等
//    所以AttKeyVo实际上包含的属性有 id name List creatTime updateTime isDelete
    @Schema(description = "属性value列表")
    private List<AttrValue> attrValueList;
}
