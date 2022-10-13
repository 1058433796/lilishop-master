package cn.lili.modules.item.mapper;



import cn.lili.modules.item.entity.OrderBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;
import java.util.List;

public interface IbankMapper extends BaseMapper<OrderBean> {



    @Select("SELECT * FROM bcp.ordertableonlyforbean where orderid=#{orderid}")
    OrderBean findData(String orderid);

}
