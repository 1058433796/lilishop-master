package cn.lili.modules.itemOrder.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.utils.DateUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.order.entity.enums.*;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemOrderSearchParams extends PageVO {

    private static final long serialVersionUID = -6380573339089959194L;

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

    //old
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "下单开始时间")
    private Date startDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "下单结束时间")
    private Date endDate;


    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "关键字 商品名称/买家名称/店铺名称")
    private String keywords;


    /**
     * @see CommentStatusEnum
     */
    @ApiModelProperty(value = "评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，")
    private String commentStatus;

    @ApiModelProperty(value = "是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空")
    private String parentOrderSn;

    @ApiModelProperty(value = "是否为某订单类型的订单，如果是则为订单类型的id，否则为空")
    private String promotionId;

    @ApiModelProperty(value = "总价格,可以为范围，如10_1000")
    private String flowPrice;

    /**
     * @see OrderPromotionTypeEnum
     */
    @ApiModelProperty(value = "订单促销类型")
    private String orderPromotionType;

    public <T> QueryWrapper<T> queryWrapper() {
        AuthUser currentUser = UserContext.getCurrentUser();
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        //关键字查询
//        if (CharSequenceUtil.isNotEmpty(keywords)) {
//            wrapper.and(keyWrapper -> keyWrapper.like("o.order_id", keywords).or().like("oi.goods_name", keywords));
//        }
        if (currentUser != null) {
            //按卖家查询
            wrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.STORE.name()), "o.store_id", currentUser.getStoreId());

            //店铺查询
            wrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MANAGER.name())
                    && CharSequenceUtil.isNotEmpty(storeId), "o.store_id", storeId);

            //按买家查询
            wrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MEMBER.name()) && buyerId == null, "o.member_id", currentUser.getId());

        }
        //按照买家查询
        wrapper.like(CharSequenceUtil.isNotEmpty(buyerId), "o.member_id", buyerId);

        //按订单编号查询
        wrapper.like(CharSequenceUtil.isNotEmpty(orderId), "o.sn", orderId);

        //按时间查询
        wrapper.ge(startDate != null, "o.create_time", startDate);

        wrapper.le(endDate != null, "o.create_time", DateUtil.endOfDate(endDate));
        //按购买人用户名
        wrapper.like(CharSequenceUtil.isNotEmpty(buyerName), "o.member_name", buyerName);

        //按订单类型
        //wrapper.eq(CharSequenceUtil.isNotEmpty(orderType), "o.order_type", orderType);

        //物流查询
        //wrapper.like(CharSequenceUtil.isNotEmpty(shipName), "o.consignee_name", shipName);

        //按商品名称查询
        wrapper.like(CharSequenceUtil.isNotEmpty(goodsName), "oi.goods_name", goodsName);

        //付款方式
        wrapper.like(CharSequenceUtil.isNotEmpty(payMode), "o.pay_mode", payMode);

        //按支付方式
       //wrapper.eq(CharSequenceUtil.isNotEmpty(paymentMethod), "o.payment_method", paymentMethod);

        //订单状态
        wrapper.eq(CharSequenceUtil.isNotEmpty(orderStatus), "o.order_status", orderStatus);

        //付款状态
        wrapper.eq(CharSequenceUtil.isNotEmpty(payStatus), "o.pay_status", payStatus);

        //订单来源
        //wrapper.like(CharSequenceUtil.isNotEmpty(clientType), "o.client_type", clientType);

        //按评价状态
        wrapper.eq(CharSequenceUtil.isNotEmpty(commentStatus), "oi.comment_status", commentStatus);

        //按标签查询
//        if (CharSequenceUtil.isNotEmpty(tag)) {
//            String orderStatusColumn = "o.order_status";
//            OrderTagEnum tagEnum = OrderTagEnum.valueOf(tag);
//            switch (tagEnum) {
//                //待付款
//                case WAIT_PAY:
//                    wrapper.eq(orderStatusColumn, OrderStatusEnum.UNPAID.name());
//                    break;
//                //待发货
//                case WAIT_SHIP:
//                    wrapper.eq(orderStatusColumn, OrderStatusEnum.UNDELIVERED.name());
//                    break;
//                //待收货
//                case WAIT_ROG:
//                    wrapper.eq(orderStatusColumn, OrderStatusEnum.DELIVERED.name());
//                    break;
//                //已取消
//                case CANCELLED:
//                    wrapper.eq(orderStatusColumn, OrderStatusEnum.CANCELLED.name());
//                    break;
//                //已完成
//                case COMPLETE:
//                    wrapper.eq(orderStatusColumn, OrderStatusEnum.COMPLETED.name());
//                    break;
//                default:
//                    break;
//            }
//        }

        // 依赖订单
        wrapper.eq(parentOrderSn != null, "o.parent_order_sn", parentOrderSn);
        // 促销活动id
        wrapper.eq(CharSequenceUtil.isNotEmpty(promotionId), "o.promotion_id", promotionId);

        wrapper.eq(CharSequenceUtil.isNotEmpty(orderPromotionType), "o.order_promotion_type", orderPromotionType);

        if (CharSequenceUtil.isNotEmpty(flowPrice)) {
            String[] s = flowPrice.split("_");
            if (s.length > 1) {
                wrapper.between("o.flow_price", s[0], s[1]);
            } else {
                wrapper.ge("o.flow_price", s[0]);
            }
        }
        //wrapper.eq("o.delete_flag", false);
        return wrapper;
    }

}
