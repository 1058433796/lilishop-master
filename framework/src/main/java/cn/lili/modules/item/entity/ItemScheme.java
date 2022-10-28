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
    private String doorId;

//    private String location;

    private String openMethod;

    private String openDirection;

    private String height;

    private String width;

    private String thickness;

    private String texture;

    private String handle;

    private String guard;

    private String firerating;

    private String wjgroup;

    private String roomFunction;

    private String weight;

    private String doorShape;

    private String groupType;

    private String qkType;




//    private String updateTime;

}
