package cn.lili.modules.item.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data

public class ItemGuarantySearchParam extends PageVO {
    @ApiModelProperty(value = "项目ID")
    String itemId;
    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(itemId)) {
            queryWrapper.eq("isc.item_id", itemId);
        }
        return queryWrapper;
    }
}
