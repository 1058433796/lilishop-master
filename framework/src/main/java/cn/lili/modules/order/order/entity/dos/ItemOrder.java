package cn.lili.modules.order.order.entity.dos;

import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.order.entity.enums.*;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import cn.lili.mybatis.BaseEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//@EqualsAndHashCode(callSuper = true)
@Data
@TableName("item_order")
@ApiModel(value = "订单")
@NoArgsConstructor
public class ItemOrder
        //extends BaseEntity
{
    private static final long serialVersionUID = 2233811628066468683L;

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

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "收货人电话")
    private String buyerPhone;

    @ApiModelProperty(value = "收货人姓名")
    private String buyerName;

    @ApiModelProperty(value = "订单金额")
    private Double orderAmount;

    @ApiModelProperty(value = "收货人地址")
    private String buyerAddress;

    @ApiModelProperty(value = "供应商响应状态")
    private String storeReply;

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

    @ApiModelProperty(value = "采购方ID")
    private String buyerId;

    @ApiModelProperty(value = "供应商ID")
    private String storeId;





}