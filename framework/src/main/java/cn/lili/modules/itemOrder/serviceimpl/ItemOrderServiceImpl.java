package cn.lili.modules.itemOrder.serviceimpl;

import cn.hutool.core.date.DateTime;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dos.OrderGood;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderExportDTO;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.entity.vo.OrderGoodDetailVO;
import cn.lili.modules.itemOrder.mapper.ItemOrderMapper;
import cn.lili.modules.itemOrder.mapper.OrderGoodMapper;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import cn.lili.modules.order.order.aop.OrderLogPoint;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.OrderItem;
import cn.lili.modules.order.order.entity.dto.OrderMessage;
import cn.lili.modules.order.order.entity.enums.DeliverStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderComplaintStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderItemAfterSaleStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.mapper.SchemeComponentMapper;
import cn.lili.modules.schemeComponent.service.SchemeComponentService;
import cn.lili.modules.system.entity.dos.Logistics;
import cn.lili.modules.system.service.LogisticsService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ItemOrderServiceImpl extends ServiceImpl<ItemOrderMapper, ItemOrder> implements ItemOrderService {

    /**
     * ?????????????????????
     */
    @Resource
    private OrderGoodMapper orderGoodMapper;

    /**
     * ????????????
     */
    @Autowired
    private LogisticsService logisticsService;

    @Autowired
    private SchemeComponentMapper schemeComponentMapper;

    /**
     * ????????????
     *
     * @param orderId ????????????
     * @return ????????????
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
    public OrderGoodDetailVO  queryOrderComponent(String orderId) {
        ItemOrder itemOrder = this.getByOrderId(orderId);
        if (itemOrder == null) {
            throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
        }
        //?????????????????????
        List<SchemeComponent> schemeComponentList = this.baseMapper.queryOrderComponent(orderId, itemOrder.getStoreId());
        return new OrderGoodDetailVO(itemOrder, schemeComponentList);
    }

    @Override
    public List<ItemOrderExportDTO> queryExportOrder(ItemOrderSearchParams itemOrderSearchParams) {
        return this.baseMapper.queryExportOrder(itemOrderSearchParams.queryWrapper());
    }

    @Override
    @OrderLogPoint(description = "'??????['+#orderId+']?????????????????????['+#logisticsNo+']'", orderSn = "#ordeId")
    @Transactional(rollbackFor = Exception.class)
    public ItemOrder delivery(String orderId, String logisticsNo, String logisticsId) {
        ItemOrder order = OperationalJudgment.judgment(this.getByOrderId(orderId));
        //????????????????????????????????????????????????????????????
        if (order.getDistributionStatus().equals("?????????")) {
            //??????????????????
            Logistics logistics = logisticsService.getById(logisticsId);
            if (logistics == null) {
                throw new ServiceException(ResultCode.ORDER_LOGISTICS_ERROR);
            }
            //??????????????????
            order.setLogisticsCode(logistics.getId());
            order.setLogisticsName(logistics.getName());
            order.setLogisticsNo(logisticsNo);
            order.setLogisticsTime(DateTime.now());
            order.setDistributionStatus("?????????");
            order.setOrderStatus("?????????");
            this.updateById(order);
            //??????????????????????????????
//            OrderMessage orderMessage = new OrderMessage();
//            orderMessage.setNewStatus(OrderStatusEnum.DELIVERED);
//            orderMessage.setOrderSn(order.getSn());
//            this.sendUpdateStatusMessage(orderMessage);
        } else {
            throw new ServiceException(ResultCode.ORDER_DELIVER_ERROR);
        }
        return order;
    }
}
