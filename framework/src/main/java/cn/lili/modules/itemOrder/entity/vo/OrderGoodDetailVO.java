package cn.lili.modules.itemOrder.entity.vo;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dos.OrderGood;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
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
    private List<SchemeComponent> schemeComponentList;

    public OrderGoodDetailVO(ItemOrder itemOrder, List<SchemeComponent> schemeComponentList) {
        this.itemOrder=itemOrder;
        this.schemeComponentList=schemeComponentList;
    }


}
