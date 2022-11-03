package cn.lili.modules.itemOrder.entity.dos;

import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.order.entity.enums.*;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//@EqualsAndHashCode(callSuper = true)
//@Data
//@TableName("item_order")
//@ApiModel(value = "订单")
//@NoArgsConstructor
//public class ItemOrder
//        //extends BaseEntity
//{
//    private static final long serialVersionUID = 2233811628066468683L;
//    @TableId
//    @ApiModelProperty("订单编号")
//    private String orderId;
//
//    /**
//     * @see OrderStatusEnum
//     */
//    @ApiModelProperty(value = "订单状态")
//    private String orderStatus;
//
//    /**
//     * @see DeliveryMethodEnum
//     */
//    @ApiModelProperty(value = "配送方式")
//    private String distributionMode;
//
//    /**
//     * @see PaymentMethodEnum
//     */
//    @ApiModelProperty(value = "支付方式")
//    private String payMode;
//
//    @ApiModelProperty(value = "创建时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date createTime;
//
//    @ApiModelProperty(value = "收货人电话")
//    private String buyerPhone;
//
//    @ApiModelProperty(value = "收货人姓名")
//    private String consigneeName;
//
//    @ApiModelProperty(value = "收货人地址")
//    private String buyerAddress;
//
//    @ApiModelProperty(value = "订单金额")
//    private Double orderAmount;
//
//    @ApiModelProperty(value = "供应商响应状态")
//    private String storeReply;
//
//    @ApiModelProperty(value = "采购方响应状态")
//    private String buyerReply;
//
//    @ApiModelProperty(value = "合同合同签署状态")
//    private String contractStatus;
//
//    /**
//     * @see PayStatusEnum
//     */
//    @ApiModelProperty(value = "付款状态")
//    private String payStatus;
//    /**
//     * @see DeliverStatusEnum
//     */
//    @ApiModelProperty(value = "发货状态")
//    private String distributionStatus;
//
//    @ApiModelProperty(value = "响应状态")
//    private String replyStatus;
//
//    @ApiModelProperty(value = "采购方ID")
//    private String buyerId;
//
//    @ApiModelProperty(value = "采购商名称")
//    private String buyerName;
//
//    @ApiModelProperty(value = "供应商ID")
//    private String storeId;
//
//    @ApiModelProperty(value = "供应商名称")
//    private String storeName;
//
//    @ApiModelProperty(value = "所属方案号")
//    private String schemeId;
//
//
//
//}
//



@Data
@TableName("item_order")
@ApiModel(value = "订单")
public class ItemOrder {
    @TableId
    private String orderId;

    private String orderStatus = "未发货";

    private String distributionMode = "快递";

    private String payMode = "支付宝支付";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String buyerPhone;

    private String buyerName;

    private double orderAmount;

    private String storeReply = "已响应";

    private String contractStatus = "未签署";

    private String payStatus = "未付款";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private String distributionStatus = "未发货";

    private String replyStatus = "未响应";

    private String buyerId;

    private String buyerReply;

    private String storeId;

    private String schemeId;

    private String storeName;
    @ApiModelProperty(value = "订单类型")
    private String type;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    private String consigneePhone;

    private String consigneeAddress;

    private String consigneeAddressId;

    private String logisticsCode;

    private String logisticsName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logisticsTime;

    private String logisticsNo;

    @ApiModelProperty(value = "采购方物流签署时间")
    private Date buyerLogisticSignTime;
}

