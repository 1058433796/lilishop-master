package cn.lili.modules.itemOrder.mapper;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderExportDTO;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemOrderMapper extends BaseMapper<ItemOrder> {


    /**
     * 查询订单简短信息分页
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 简短订单分页
     */
    @Select("select  o.* from item_order as o ${ew.customSqlSegment} ")
    IPage<ItemOrderSimpleVO> queryByParams(IPage<ItemOrderSimpleVO> page, @Param(Constants.WRAPPER) Wrapper<ItemOrderSimpleVO> queryWrapper);

    /**
     * 查询订单信息
     *
     * @param queryWrapper 查询条件
     * @return 简短订单分页
     */
    @Select("select o.* " +
            " FROM li_order o INNER JOIN li_order_item AS oi on o.sn = oi.order_sn ${ew.customSqlSegment} ")
    List<Order> queryListByParams(@Param(Constants.WRAPPER) Wrapper<Order> queryWrapper);

    @Select("SELECT * FROM scheme_component WHERE scheme_id = " +
            "(SELECT scheme_id FROM item_order WHERE order_id=#{oid}) AND supplier_id=#{storeId}")
    List<SchemeComponent> queryOrderComponent(String oid, String storeId);
    /**
     * 查询导出订单DTO列表
     *
     * @param queryWrapper 查询条件
     * @return 导出订单DTO列表
     */
    @Select("SELECT o.* from item_order o ${ew.customSqlSegment}")
    List<ItemOrderExportDTO> queryExportOrder(@Param(Constants.WRAPPER) Wrapper<ItemOrderSimpleVO> queryWrapper);
}
