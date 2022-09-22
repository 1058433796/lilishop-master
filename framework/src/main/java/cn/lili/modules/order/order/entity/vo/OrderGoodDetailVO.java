package cn.lili.modules.order.order.entity.vo;

import cn.lili.modules.order.order.entity.dos.*;
import cn.lili.modules.order.trade.entity.dos.OrderLog;
import io.minio.messages.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderGoodDetailVO implements Serializable {
    private static final long serialVersionUID = -6293102172184734928L;

    /**
     * 订单
     */
    private ItemOrder itemOrder;

    /**
     * 子订单信息
     */
    private List<OrderGood> orderGoods;

    public OrderGoodDetailVO(ItemOrder itemOrder, List<OrderGood> orderGoods) {
        this.itemOrder=itemOrder;
        this.orderGoods=orderGoods;
    }


}
