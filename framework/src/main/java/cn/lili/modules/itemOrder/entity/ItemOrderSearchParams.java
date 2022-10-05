package cn.lili.modules.itemOrder.entity;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemOrderSearchParams extends PageVO {

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
        if (CharSequenceUtil.isNotEmpty(replyStatus)) {
            queryWrapper.like("reply_status", replyStatus);
        }
        return queryWrapper;
    }

}
