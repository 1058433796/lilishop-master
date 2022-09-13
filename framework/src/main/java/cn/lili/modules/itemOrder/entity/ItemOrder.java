package cn.lili.modules.itemOrder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("item_order")
@ApiModel(value = "订单")
public class ItemOrder {

    private String orderId;

    private String orderStatus;

    private String distributionMode;

    private String payMode;

    private Date createTime;

    private String buyerPhone;

    private String buyerName;

    private String orderAmount;

    private String buyerAddress;

    private String storeReply;

    private String contractStatus;

    private String payStatus;

    private String distributionStatus;

    private String replyStatus;

    private String buyerId;

    private String storeId;
}

