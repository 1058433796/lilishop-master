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

}
