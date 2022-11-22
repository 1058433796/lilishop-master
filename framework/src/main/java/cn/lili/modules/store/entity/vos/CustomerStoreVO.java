package cn.lili.modules.store.entity.vos;

import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
@Data
public class CustomerStoreVO {

    @ApiModelProperty("客户编号")
    private String id;

    @ApiModelProperty(value = "客户名称")
    private String buyerName;

    @ApiModelProperty(value = "客户电话")
    private String buyerPhone;

    @ApiModelProperty(value = "交易金额")
    private Double tradeAmount;

    @ApiModelProperty(value = "最近交易时间")
    private String latestTime;

    @ApiModelProperty(value = "供应商Id")
    private String storeId;

    @ApiModelProperty(value = "地址名称， '，'分割")
    private String storeAddressPath;

    @ApiModelProperty(value = "地址id，'，'分割 ")
    private String storeAddressIdPath;

    @ApiModelProperty(value = "详细地址")
    private String storeAddressDetail;




}
