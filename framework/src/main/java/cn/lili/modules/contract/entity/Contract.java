package cn.lili.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@TableName("contract")
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @TableId
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    private String storeId;

    private String storeName;

    private String providerState = "已签署";

    private String buyerId;

    private String buyerState = "未签署";

    private Double amount;

    private String orderId;

    private String buyerName;

}
