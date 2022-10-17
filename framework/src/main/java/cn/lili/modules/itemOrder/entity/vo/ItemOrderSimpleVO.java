package cn.lili.modules.itemOrder.entity.vo;

import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.order.entity.enums.DeliverStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ItemOrderSimpleVO {


    @ApiModelProperty("订单编号")
    private String orderId;

    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    /**
     * @see DeliveryMethodEnum
     */
    @ApiModelProperty(value = "配送方式")
    private String distributionMode;

    /**
     * @see PaymentMethodEnum
     */
    @ApiModelProperty(value = "支付方式")
    private String payMode;

    @ApiModelProperty(value = "支付时间")
    private String payTime;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "收货人电话")
    private String buyerPhone;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;

    @ApiModelProperty(value = "订单金额")
    private Double orderAmount;

//    @ApiModelProperty(value = "收货人地址")
//    private String buyerAddress;

    @ApiModelProperty(value = "供应商响应状态")
    private String storeReply;

    @ApiModelProperty(value = "采购方响应状态")
    private String buyerReply;

    @ApiModelProperty(value = "合同合同签署状态")
    private String contractStatus;

    /**
     * @see PayStatusEnum
     */
    @ApiModelProperty(value = "付款状态")
    private String payStatus;
    /**
     * @see DeliverStatusEnum
     */
    @ApiModelProperty(value = "发货状态")
    private String distributionStatus;

    @ApiModelProperty(value = "响应状态")
    private String replyStatus;

    @ApiModelProperty(value = "下单人电话")
    private String consigneePhone;

    @ApiModelProperty(value = "采购方ID")
    private String buyerId;

    @ApiModelProperty(value = "采购商名称")
    private String buyerName;

    @ApiModelProperty(value = "供应商ID")
    private String storeId;

    @ApiModelProperty(value = "供应商名称")
    private String storeName;
}
