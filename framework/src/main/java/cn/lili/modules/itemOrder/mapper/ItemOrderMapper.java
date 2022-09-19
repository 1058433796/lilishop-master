package cn.lili.modules.itemOrder.mapper;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ItemOrderMapper extends BaseMapper<ItemOrder> {
    @Select("select g.* from order as g ")
    IPage<ItemOrder> queryByParams(IPage<ItemOrder> page, @Param(Constants.WRAPPER) Wrapper<ItemOrder> queryWrapper);
}
