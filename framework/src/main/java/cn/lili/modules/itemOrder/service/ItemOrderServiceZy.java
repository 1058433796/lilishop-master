package cn.lili.modules.itemOrder.service;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.OrderWithContractSimpleV0;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemOrderServiceZy extends IService<ItemOrder> {
    /**
     * 项目订单查询
     *
     * @param itemOrderSearchParams 查询参数
     * @return 订单分页
     */
    IPage<ItemOrder> queryByParams(ItemOrderSearchParams itemOrderSearchParams);
    List<SchemeComponent> queryOrderComponent(String oid, String storeId);
    List<ItemOrder> getAssociatedOrders(String oid);

    IPage<OrderWithContractSimpleV0> queryAssociatedContractOrders(ItemOrderSearchParams ItemOrderSearchParams);

    void payOrder(String oid);
}
