package cn.lili.modules.schemeComponent.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchemeComponentSearchParams extends PageVO {
    @ApiModelProperty(value = "方案ID")
    private String schemeId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(schemeId)) {
            queryWrapper.like("scheme_id", schemeId);
        }
        return queryWrapper;
    }
}
