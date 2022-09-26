package cn.lili.modules.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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

    private String orderContent;

    private String buyerId;

}
