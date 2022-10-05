package cn.lili.modules.itemOrder.mapper;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemOrderMapper extends BaseMapper<ItemOrder> {
    @Select("select g.* from order as g")
    IPage<ItemOrder> queryByParams(IPage<ItemOrder> page, @Param(Constants.WRAPPER) Wrapper<ItemOrder> queryWrapper);

    @Select("SELECT * FROM scheme_component WHERE scheme_id = " +
            "(SELECT scheme_id FROM item_order WHERE order_id=#{oid}) AND supplier_id=#{storeId}")
    List<SchemeComponent> queryOrderComponent(String oid, String storeId);
}