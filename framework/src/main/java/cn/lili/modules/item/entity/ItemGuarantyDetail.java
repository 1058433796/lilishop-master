package cn.lili.modules.item.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ItemGuarantyDetail {
    private String id;

    private String primaryId;

    private String schemeSum;

    private Integer payFlag;

    private String orderName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String orderContent;

    private String buyerId;
    private int orderNum;
}
