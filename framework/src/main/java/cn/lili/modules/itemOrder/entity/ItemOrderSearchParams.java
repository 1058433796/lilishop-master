package cn.lili.modules.itemOrder.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemOrderSearchParams extends PageVO {

    @ApiModelProperty(value = "采购方ID")
    private String buyerId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(buyerId)) {
            queryWrapper.like("buyer_id", buyerId);
        }
        return queryWrapper;
    }

}
