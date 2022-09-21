package cn.lili.modules.order.order.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@EqualsAndHashCode(callSuper = true)
@Data
@TableName("order_good")
@ApiModel(value = "订单商品信息")
@NoArgsConstructor
public class OrderGood
        //extends BaseEntity
{

    private static final long serialVersionUID = 2108971190191410182L;

    @ApiModelProperty("订单编号")
    private String orderId;

    @ApiModelProperty(value = "品名")
    private String goodName;

    @ApiModelProperty(value = "参数要求")
    private String goodRequire;

    @ApiModelProperty(value = "饰面颜色")
    private String goodColor;

    @ApiModelProperty(value = "型号")
    private String goodType;

    @ApiModelProperty(value = "品牌")
    private String goodBrand;

    @ApiModelProperty(value = "数量")
    private Integer goodNumber;

    @ApiModelProperty(value = "单位")
    private String goodUnit;

    @ApiModelProperty(value = "单价")
    private String goodUnitprice;

    @ApiModelProperty(value = "总价")
    private String goodTotalprice;


   }
