package cn.lili.modules.scheme.mapper;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.scheme.entity.Scheme;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SchemeMapper extends BaseMapper<Scheme> {
    @Select("select g.* from all_scheme as g ")
    IPage<Item> queryByParams(IPage<Scheme> page, @Param(Constants.WRAPPER) Wrapper<Scheme> queryWrapper);

    @Select("SELECT * FROM all_scheme")
    List<Scheme> findAllScheme();
}
