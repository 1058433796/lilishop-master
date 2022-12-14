package cn.lili.modules.contract.service.serviceImpl;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.entity.ContractSearchParams;
import cn.lili.modules.contract.mapper.ContractMapper;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    @Override
    public IPage<Contract> queryByParams(ContractSearchParams contractSearchParams) {
        System.out.println("查询合同");
        return this.page(PageUtil.initPage(contractSearchParams), contractSearchParams.queryWrapper());
    }

    @Override
    public void buyerSign(String id, Date date) {
        this.baseMapper.buyerSign(id,date);
        this.baseMapper.buyerSignOrder(id.substring(1));
    }

    @Override
    public void providerSign(String id) {
        this.baseMapper.providerSign(id);
    }

    @Override
    public IPage<Contract> queryAssociated(ContractSearchParams contractSearchParams) {
        QueryWrapper queryWrapper = contractSearchParams.queryWrapper();
        return this.baseMapper.queryAssociated(PageUtil.initPage(contractSearchParams), queryWrapper);

    }

    @Override
    public Long waitToSignContract(String buyerId) {
        return this.baseMapper.waitToSignContract(buyerId);
    }

    @Override
    public boolean save(Contract entity) {
        return super.save(entity);
    }
}
