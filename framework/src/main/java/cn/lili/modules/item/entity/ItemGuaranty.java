package cn.lili.modules.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("item_guaranty")
@ApiModel(value = "履约保证单")
public class ItemGuaranty {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String primaryId;

    private String schemeSum;

    private Integer payFlag;

    private String orderName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String orderContent;

    private String buyerId;

    private String itemId;
}
