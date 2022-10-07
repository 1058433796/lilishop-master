package cn.lili.modules.itemOrder.mapper;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.item.entity.Item;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.itemOrder.entity.OrderWithContractSimpleV0;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ItemOrderMapper extends BaseMapper<ItemOrder> {
    @Select("select g.* from order as g")
    IPage<ItemOrder> queryByParams(IPage<ItemOrder> page, @Param(Constants.WRAPPER) Wrapper<ItemOrder> queryWrapper);

    @Select("SELECT * FROM scheme_component WHERE scheme_id = " +
            "(SELECT scheme_id FROM item_scheme WHERE primary_id = " +
            "(SELECT scheme_id FROM item_order WHERE order_id=#{oid})) AND supplier_id=#{storeId}")
    List<SchemeComponent> queryOrderComponent(String oid, String storeId);
    @Select("SELECT * FROM item_order WHERE scheme_id=#{oid}")
    List<ItemOrder> getAssociatedOrders(String oid);
    @Update("UPDATE item_order SET pay_status='已付款' WHERE order_id=#{oid}")
    void payOrder(String oid);

    @Select("SELECT * FROM " +
            "(SELECT * FROM item_order) AS o " +
            "NATURAL JOIN " +
            "(SELECT order_id, buyer_state FROM contract) AS c ${ew.customSqlSegment}")
    IPage<OrderWithContractSimpleV0> queryAssociatedContractOrders(IPage<Contract> page, @Param(Constants.WRAPPER) Wrapper<Contract> queryWrapper);

}