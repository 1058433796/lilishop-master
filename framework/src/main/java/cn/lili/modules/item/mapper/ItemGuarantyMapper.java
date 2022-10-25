package cn.lili.modules.item.mapper;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantyDetail;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ItemGuarantyMapper extends BaseMapper<ItemGuaranty> {
    @Update({"update item_guaranty set pay_flag=1 where id= #{id}"})
    void setPay(String id);

    @Select("SELECT count(*) FROM item_guaranty WHERE buyer_id=#{buyerId} AND pay_flag=0")
    Long waitToSignGuaranty(String buyerId);

    @Select("SELECT id , max(primary_id) primary_id,  max(scheme_sum) scheme_sum, max(pay_flag) pay_flag, max(order_name) order_name, max(create_time) create_time, max(order_content) order_content, count(order_id) order_num FROM " +
            "(" +
            "(SELECT * FROM item_guaranty) ig " +
            "NATURAL JOIN " +
            "(SELECT item_id, primary_id FROM item_scheme) isc " +
            "NATURAL JOIN " +
            "(SELECT order_id, scheme_id as primary_id FROM item_order) ior" +
            ") " +
            "${ew.customSqlSegment} " +
            "GROUP BY id "
            )
    IPage<ItemGuarantyDetail> queryByParams(IPage<ItemOrder> page, @Param(Constants.WRAPPER) Wrapper<ItemOrder> queryWrapper);
    @Select("SELECT * FROM " +
            "(SELECT * FROM item_order) ior " +
            "NATURAL JOIN " +
            "(SELECT item_id, primary_id as scheme_id FROM item_scheme) isc " +
            "${ew.customSqlSegment}")
    IPage<ItemOrderSimpleVO> queryOrderByParams(IPage<ItemOrder> page, @Param(Constants.WRAPPER) Wrapper<ItemOrder> queryWrapper);

    @Select("SELECT * FROM " +
            "(SELECT * FROM contract) co " +
            "NATURAL JOIN " +
            "(SELECT item_id, primary_id as scheme_id FROM item_scheme) isc " +
            "${ew.customSqlSegment}")
    IPage<Contract> queryContractByParams(IPage<ItemOrder> page, @Param(Constants.WRAPPER) Wrapper<ItemOrder> queryWrapper);

}
