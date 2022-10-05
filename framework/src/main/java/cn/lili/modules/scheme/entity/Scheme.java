package cn.lili.modules.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("all_scheme")
@ApiModel(value = "方案")
public class Scheme {
    private String schemeId;

    private String schemeName;

    private String doorId;

    private String location;

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

    private String updateTime;

}
