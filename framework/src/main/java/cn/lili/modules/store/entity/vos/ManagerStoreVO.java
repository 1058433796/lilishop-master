package cn.lili.modules.store.entity.vos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ManagerStoreVO {

    @ApiModelProperty("客户编号")
    private String Id;

    @ApiModelProperty(value = "客户名称")
    private String buyerName;

    @ApiModelProperty(value = "客户电话")
    private String buyerPhone;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期", hidden = true)
    private Date createTime;

    private String grade="甲";

    private Integer point=10;




}
