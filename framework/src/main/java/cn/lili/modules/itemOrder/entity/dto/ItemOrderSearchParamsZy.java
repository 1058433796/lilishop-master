package cn.lili.modules.itemOrder.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemOrderSearchParamsZy extends PageVO {

    @ApiModelProperty(value = "采购方ID")
    private String buyerId;

    private String orderId;
    private String storeName;
    private String storeReply;
    private String distributionStatus;
    private String startDate;
    private String endDate;
    private String payStatus;
    private String replyStatus;

    private String schemeId;
    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(buyerId)) {
            queryWrapper.like("buyer_id", buyerId);
        }

        if (CharSequenceUtil.isNotEmpty(orderId)) {
            queryWrapper.like("order_id", orderId);
        }

        if (CharSequenceUtil.isNotEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }

        if (CharSequenceUtil.isNotEmpty(storeReply)) {
            queryWrapper.like("store_reply", storeReply);
        }

        if (CharSequenceUtil.isNotEmpty(distributionStatus)) {
            queryWrapper.like("distribution_status", distributionStatus);
        }

        if (CharSequenceUtil.isNotEmpty(startDate) && CharSequenceUtil.isNotEmpty(endDate)) {
            queryWrapper.ge("create_time", startDate);
            endDate += " 23:59:59";
            queryWrapper.le("create_time", endDate);
        }
        if (CharSequenceUtil.isNotEmpty(payStatus)) {
            queryWrapper.like("pay_status", payStatus);
        }
//        if (CharSequenceUtil.isNotEmpty(replyStatus)) {
//            queryWrapper.like("reply_status", replyStatus);
//        }
        if (CharSequenceUtil.isNotEmpty(replyStatus)) {
            queryWrapper.like("buyer_reply", replyStatus);
        }

        if (CharSequenceUtil.isNotEmpty(schemeId)) {
            queryWrapper.like("scheme_id", schemeId);
        }

        return queryWrapper;
    }

}
