package cn.lili.modules.statistics.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HomeStatisticDataVO {
    @ApiModelProperty(value = "待付款订单数")
    private Long waitToPay;
    @ApiModelProperty(value = "待发货订单数")
    private Long waitToDis;
    @ApiModelProperty(value = "总订单数")
    private Long totalOrderNum;
    @ApiModelProperty(value = "总订单金额")
    private Double totalOrderAmount;
    @ApiModelProperty(value = "已支付订单数")
    private Long payedOrderNum;
    @ApiModelProperty(value = "已支付订单的总金额")
    private Double payedOrderAmount;
    @ApiModelProperty(value = "总产品数")
    private Long productNum;

}
