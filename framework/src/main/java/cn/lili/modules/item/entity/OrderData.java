package cn.lili.modules.item.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;StringNode
import lombok.Data;

@Data
@ApiModel(value = "前端输入的订单相关信息")
public class OrderData {

    @ApiModelProperty(value = "业务系统APPID")
    private String appid;

    @ApiModelProperty(value = "收费机构ID")
    private String jgid;

    @ApiModelProperty(value = "业务类型ID")
    private String ywlxid;

    @ApiModelProperty(value = "缴费金额")
    private String jfje;

    @ApiModelProperty
    private String dsforderid="";

    @ApiModelProperty(value = "客户类型")
    private String customtype;

    @ApiModelProperty(value = "客户ID")
    private String customid;

    @ApiModelProperty
    private String customname="";

    @ApiModelProperty
    private String description="";

    @ApiModelProperty
    private String extension="";








}
