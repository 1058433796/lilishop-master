package cn.lili.modules.statistics.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HomeStatisticDataVO {
    @ApiModelProperty(value = "待响应订单数")
    private Long waitToSignOrder;
    @ApiModelProperty(value = "待响应合同数")
    private Long waitToSignContract;
    @ApiModelProperty(value = "待签署履约保证单")
    private Long waitToSignGuaranty;
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
