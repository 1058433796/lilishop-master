package cn.lili.modules.statistics.mapper;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.statistics.entity.vo.OrderStatisticsDataVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单统计数据处理层
 *
 * @author Bulbasaur
 * @since 2020/11/17 7:34 下午
 */
public interface ItemOrderStatisticsMapper extends BaseMapper<ItemOrder> {

    /**
     * 获取订单统计数据
     *
     * @param queryWrapper 查询条件
     * @return 订单统计列表
     */
    @Select("SELECT DATE_FORMAT(create_time,'%Y-%m-%d') AS create_time,SUM(order_amount) AS price FROM item_order " +
            " ${ew.customSqlSegment}")
    List<OrderStatisticsDataVO> getOrderStatisticsData(@Param(Constants.WRAPPER) Wrapper queryWrapper);

    /**
     * 订单数量
     *
     * @param queryWrapper 查询条件
     * @return 订单数量
     */
    @Select("SELECT count(0) FROM li_order ${ew.customSqlSegment}")
    Integer count(@Param(Constants.WRAPPER) Wrapper queryWrapper);

    /**
     * 查询订单简短信息分页
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 简短订单分页
     */
    @Select("select  o.* from item_order as o ${ew.customSqlSegment}  ")
    IPage<ItemOrderSimpleVO> queryByParams(IPage<OrderSimpleVO> page, @Param(Constants.WRAPPER) Wrapper<OrderSimpleVO> queryWrapper);

}