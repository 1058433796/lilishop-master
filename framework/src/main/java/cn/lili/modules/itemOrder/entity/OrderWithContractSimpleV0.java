package cn.lili.modules.itemOrder.entity;

import lombok.Data;

@Data
public class OrderWithContractSimpleV0 {
    private String orderId;
    private String name; //商品名称
    private String storeName;
    private String number;//数量
    private String orderAmount;
    private String buyerState;
    private String createTime;
    private String payStatus;
}
