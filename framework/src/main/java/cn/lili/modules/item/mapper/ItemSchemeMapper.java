package cn.lili.modules.item.mapper;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemScheme;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ItemSchemeMapper extends BaseMapper<ItemScheme> {
    @Select("select g.* from item_scheme as g ")
    IPage<ItemScheme> queryByParams(IPage<ItemScheme> page, @Param(Constants.WRAPPER) Wrapper<ItemScheme> queryWrapper);

    @Update({"update item_scheme set check_flag=1 where primary_id= #{id}"})
    void updateByPrimaryId(String id);
}
