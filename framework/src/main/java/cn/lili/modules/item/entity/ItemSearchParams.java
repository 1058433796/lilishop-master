package cn.lili.modules.item.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemSearchParams extends PageVO {

    @ApiModelProperty(value = "项目名称")
    private String itemName;
    @ApiModelProperty(value = "采购方ID")
    private String buyerId;

    @ApiModelProperty(value = "项目ID")
    private String itemId;

    @ApiModelProperty(value = "地点")
    private String createLocation;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(itemName)) {
            queryWrapper.like("item_name", itemName);
        }
        if (CharSequenceUtil.isNotEmpty(createLocation)) {
            queryWrapper.like("create_location", createLocation);
        }
        if (CharSequenceUtil.isNotEmpty(buyerId)) {
            queryWrapper.like("buyer_id", buyerId);
        }
        if (CharSequenceUtil.isNotEmpty(itemId)) {
            queryWrapper.like("item_id", itemId);
        }
        if (createTime != null) {
            queryWrapper.le("create_time", createTime);
        }

        return queryWrapper;
    }

}
