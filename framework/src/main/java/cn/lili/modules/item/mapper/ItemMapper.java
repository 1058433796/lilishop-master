package cn.lili.modules.item.mapper;


import cn.lili.modules.goods.entity.vos.GoodsVO;
import cn.lili.modules.item.entity.Item;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ItemMapper extends BaseMapper<Item> {
    @Select("SELECT * FROM item")
    List<Item> findData();

    @Select("select g.* from item as g ")
    IPage<Item> queryByParams(IPage<Item> page, @Param(Constants.WRAPPER) Wrapper<Item> queryWrapper);
}
