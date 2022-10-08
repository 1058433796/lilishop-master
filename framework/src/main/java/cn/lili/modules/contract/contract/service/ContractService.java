package cn.lili.modules.contract.contract.service;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.entity.ContractSearchParams;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.itemOrder.entity.ItemOrderSearchParams;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface ContractService extends IService<Contract> {

    IPage<Contract> queryByParams(ContractSearchParams contractSearchParams);


    void buyerSign(String id);

    IPage<Contract> queryAssociated(ContractSearchParams contractSearchParams);

}
