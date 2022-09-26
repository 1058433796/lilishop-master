package cn.lili.modules.itemOrder.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dos.OrderGood;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderExportDTO;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.entity.vo.OrderGoodDetailVO;
import cn.lili.modules.itemOrder.mapper.ItemOrderMapper;
import cn.lili.modules.itemOrder.mapper.OrderGoodMapper;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import cn.lili.modules.order.order.entity.dto.OrderExportDTO;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ItemOrderServiceImpl extends ServiceImpl<ItemOrderMapper, ItemOrder> implements ItemOrderService {

    /**
     * 订单货物数据层
     */
    @Resource
    private OrderGoodMapper orderGoodMapper;


    /**
     * 获取订单
     *
     * @param orderId 订单编号
     * @return 订单详情
     */
    @Override
    public ItemOrder getByOrderId(String orderId) {
        return this.getOne(new LambdaQueryWrapper<ItemOrder>().eq(ItemOrder::getOrderId, orderId));
    }

    @Override
    public IPage<ItemOrderSimpleVO> queryByParams(ItemOrderSearchParams itemOrderSearchParams) {
        QueryWrapper queryWrapper = itemOrderSearchParams.queryWrapper();
        queryWrapper.groupBy("o.order_id");
        queryWrapper.orderByDesc("o.order_id");
        return this.baseMapper.queryByParams(PageUtil.initPage(itemOrderSearchParams), queryWrapper);
    }

    @Override
    public OrderGoodDetailVO  queryDetail(String orderId) {
        ItemOrder itemOrder = this.getByOrderId(orderId);
        //System.out.println(itemOrder.toString());
        if (itemOrder == null) {
            throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
        }
        QueryWrapper<OrderGood> orderGoodWrapper = new QueryWrapper<>();
        orderGoodWrapper.eq("order_id", orderId);
        //查询订单项信息
        List<OrderGood> orderGoods = orderGoodMapper.selectList(orderGoodWrapper);
        //System.out.println(orderGoods.toString());
        //查询订单日志信息
        //List<OrderLog> orderLogs = orderLogService.getOrderLog(orderSn);
        //查询发票信息
        //Receipt receipt = receiptService.getByOrderSn(orderSn);
        //查询订单和自订单，然后写入vo返回
        OrderGoodDetailVO orderGoodDetailVO=new OrderGoodDetailVO(itemOrder, orderGoods);
        System.out.println(orderGoodDetailVO);
        return  orderGoodDetailVO;
    }

    @Override
    public List<ItemOrderExportDTO> queryExportOrder(ItemOrderSearchParams itemOrderSearchParams) {
        return this.baseMapper.queryExportOrder(itemOrderSearchParams.queryWrapper());
    }

}
