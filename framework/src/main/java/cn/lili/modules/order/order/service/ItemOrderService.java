package cn.lili.modules.order.order.service;

import cn.lili.modules.order.order.entity.dos.ItemOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.OrderGood;
import cn.lili.modules.order.order.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.OrderDetailVO;
import cn.lili.modules.order.order.entity.vo.OrderGoodDetailVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ItemOrderService extends IService<ItemOrder> {


    /**
     * 根据orderId查询
     *
     * @param orderId 订单编号
     * @return 订单信息
     */
    ItemOrder getByOrderId(String orderId);
    /**
     * 订单查询
     *
     * @param itemorderSearchParams 查询参数
     * @return 简短订单分页
     */
    IPage<ItemOrderSimpleVO> queryByParams(ItemOrderSearchParams itemorderSearchParams);
    /**
     * 订单详细
     *
     * @param orderId 订单id
     * @return 订单详细
     */
    OrderGoodDetailVO queryDetail(String orderId);

}
