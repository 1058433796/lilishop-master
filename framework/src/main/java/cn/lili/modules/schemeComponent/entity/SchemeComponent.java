package cn.lili.modules.schemeComponent.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("scheme_component")
@ApiModel(value = "方案零件")
public class SchemeComponent {
    private String componentId;

    private String componentName;

    private String componentUnitPrice;

    private String componentNumber;

    private String schemeId;

    private String supplierId;

}
