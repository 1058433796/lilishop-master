package cn.lili.modules.item.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemGuarantySearchParams extends PageVO {
    //只需要查询项目是否生成履约保证单即可
    private String itemId;
    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(itemId)) {
                queryWrapper.like("item_Id", itemId);
        }
        return queryWrapper;
    }
}
