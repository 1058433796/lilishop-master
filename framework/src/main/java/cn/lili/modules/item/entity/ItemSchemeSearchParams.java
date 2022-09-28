package cn.lili.modules.item.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemSchemeSearchParams extends PageVO {
    @ApiModelProperty(value = "项目ID")
    private String itemId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (itemId != null) {
            queryWrapper.eq("item_id", itemId);
        }
        return queryWrapper;
    }
}
