package cn.lili.modules.schemeComponent.mapper;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SchemeComponentMapper extends BaseMapper<SchemeComponent> {
    @Select("select g.* from scheme_component as g ")
    IPage<SchemeComponent> queryByParams(IPage<SchemeComponent> page, @Param(Constants.WRAPPER) Wrapper<SchemeComponent> queryWrapper);
}
