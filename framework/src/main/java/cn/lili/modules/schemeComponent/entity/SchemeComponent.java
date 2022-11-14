package cn.lili.modules.schemeComponent.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("scheme_component")
@ApiModel(value = "方案零件")
public class SchemeComponent {
    private String componentId;

    private String pm;

    private String componentUnitPrice;

    private Integer componentNumber;

    private String schemeId;

    private String supplierId;

    private String cs;

    private String sm;

    private String dw;

    private String pp;

    private String xh;

    private String orderId;
    private String parameter;
}
