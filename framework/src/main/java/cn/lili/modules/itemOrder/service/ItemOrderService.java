package cn.lili.modules.itemOrder.service;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderExportDTO;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.entity.vo.OrderGoodDetailVO;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    OrderGoodDetailVO queryOrderComponent(String oid);

    /**
     * 查询导出订单列表
     *
     * @param itemOrderSearchParams 查询参数
     * @return 导出订单列表
     */
    List<ItemOrderExportDTO> queryExportOrder(ItemOrderSearchParams itemOrderSearchParams);

    /**
     * 订单发货
     *
     * @param orderId      订单编号
     * @param logisticsNo 发货单号
     * @param logisticsId   物流公司
     * @return 订单
     */
    ItemOrder delivery(String orderId, String logisticsNo, String logisticsId);
}
