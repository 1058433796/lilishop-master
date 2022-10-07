package cn.lili.modules.schemeComponent.mapper;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface SchemeComponentMapper extends BaseMapper<SchemeComponent> {
    @Select("select g.* from scheme_component as g ")
    IPage<SchemeComponent> queryByParams(IPage<SchemeComponent> page, @Param(Constants.WRAPPER) Wrapper<SchemeComponent> queryWrapper);

    @Select("SELECT supplier_id FROM scheme_component WHERE scheme_id=#{schemeId} GROUP BY supplier_id;")
    List<String> getSchemeSuppliers(@Param("schemeId") String schemeId);
    @Select("SELECT * FROM scheme_component WHERE scheme_id=#{schemeId} AND storeId=#{storeId};")
    List<SchemeComponent> getSchemeComponentBySchemeIdAndStore(@Param("schemeId") String schemeId, @Param("storeId") String storeId);

    // 获取方案中包含每个供应商的id及其对应的总价
    @Select("SELECT supplier_id id, sum(component_unit_price*component_number) total FROM scheme_component WHERE scheme_id=#{schemeId} GROUP BY supplier_id")
    List<IdTotal> getOrderBy(@Param("schemeId") String schemeId);
}

