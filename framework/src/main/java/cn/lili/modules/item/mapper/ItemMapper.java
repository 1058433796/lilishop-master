package cn.lili.modules.item.mapper;


import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ShortItem;
import cn.lili.modules.item.entity.LoginItem;
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

    // 获取采购方总产品数，即总的项目数目
    @Select("SELECT count(*) FROM item WHERE buyer_id=#{id}")
    Long getStoreProductNum(String id);
    @Select("select g.* from item as g ")
    IPage<Item> queryByParams(IPage<Item> page, @Param(Constants.WRAPPER) Wrapper<Item> queryWrapper);

    @Select("SELECT item_name,item_id FROM item WHERE buyer_name=#{name}")
    List<ShortItem> queryBuyer(String name);

    @Select("SELECT  item_name,item_id FROM item WHERE designer_name=#{name} and designer_pass=#{pass}")
    List<ShortItem> queryDesigner(String name,String pass);
    @Select("select designer_pass from item where designer_name=#{name}")
    String queryDesignerPass(String name);
}
