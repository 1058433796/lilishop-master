package cn.lili.modules.contract.service;


import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.entity.ContractSearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ContractService extends IService<Contract> {

    IPage<Contract> queryByParams(ContractSearchParams contractSearchParams);


    void providerSign(String id);


    void buyerSign(String id);

    IPage<Contract> queryAssociated(ContractSearchParams contractSearchParams);
    Long waitToSignContract(String buyerId);

}
