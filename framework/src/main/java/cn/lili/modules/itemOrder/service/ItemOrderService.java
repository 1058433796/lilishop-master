package cn.lili.modules.itemOrder.service;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.entity.vo.OrderGoodDetailVO;
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
