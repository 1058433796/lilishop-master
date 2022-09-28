package cn.lili.modules.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("item_scheme")
@ApiModel(value = "项目方案")
public class ItemScheme {
    @TableId(type = IdType.ASSIGN_ID)
    private  String primaryId;

    private String schemeId;

    private String itemId;

    private Integer checkFlag;

    private String schemeSum;
}
