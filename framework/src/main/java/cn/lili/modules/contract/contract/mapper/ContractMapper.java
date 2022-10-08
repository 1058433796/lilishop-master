package cn.lili.modules.contract.contract.mapper;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ContractMapper extends BaseMapper<Contract> {

    @Update("UPDATE contract SET buyer_state='已签署' WHERE id=#{id}" )
    void buyerSign(String id);

    @Select("select g.* from order as g")
    IPage<Contract> queryByParams(IPage<Contract> page, @Param(Constants.WRAPPER) Wrapper<Contract> queryWrapper);

    @Select("SELECT * FROM " +
            "(SELECT * from contract) AS s " +
            "NATURAL JOIN " +
            "(SELECT order_id , scheme_id FROM item_order) AS o ${ew.customSqlSegment}")
    IPage<Contract> queryAssociated(IPage<Contract> page, @Param(Constants.WRAPPER) Wrapper<Contract> queryWrapper);

}
