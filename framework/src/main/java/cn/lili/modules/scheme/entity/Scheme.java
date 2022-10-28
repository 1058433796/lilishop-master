package cn.lili.modules.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("all_scheme")
@ApiModel(value = "方案")
public class Scheme {
    private String schemeId;

//    private String schemeName;

    private String doorId;

//    private String location;

    private String openMethod="平开";

    private String openDirection="外开";

    private String height="2500";

    private String width="900";

    private String thickness="55";

    private String texture="木";

    private String handle;

    private String guard="无";

    private String firerating="甲";

    private String wjgroup="HWE-4";

//    private String updateTime="一小时前";

    private String schemeSum;

    private String roomFunction;

    private String weight;

    private String doorShape;

    private String groupType;

    private String qkType;


}
