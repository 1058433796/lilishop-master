package cn.lili.modules.scheme.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SchemeSearchParams extends PageVO {
    @ApiModelProperty(value = "方案ID")
    private String schemeId;

    @ApiModelProperty(value = "项目ID")
    private String itemId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(itemId)) {
            queryWrapper.like("item_id", itemId);
        }
        return queryWrapper;
    }
}
