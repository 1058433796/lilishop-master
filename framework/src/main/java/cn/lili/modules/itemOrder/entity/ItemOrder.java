package cn.lili.modules.itemOrder.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.classgraph.json.Id;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.context.annotation.Primary;

import java.util.Date;

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

    private String orderAmount;

    private String buyerAddress;

    private String storeReply = "未响应";

    private String contractStatus = "未签署";

    private String payStatus = "未付款";

    private String distributionStatus = "未发货";

    private String replyStatus = "未响应";

    private String buyerId;

    private String storeId;

    private String schemeId;

    private String storeName;
}

