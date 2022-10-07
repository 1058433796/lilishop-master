package cn.lili.modules.item.mapper;

import cn.lili.modules.item.entity.ItemGuaranty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

public interface ItemGuarantyMapper extends BaseMapper<ItemGuaranty> {
    @Update({"update item_guaranty set pay_flag=1 where id= #{id}"})
    void setPay(String id);
}
