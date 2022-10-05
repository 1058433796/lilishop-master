package cn.lili.modules.contract.service.serviceImpl;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.entity.ContractSearchParams;
import cn.lili.modules.contract.mapper.ContractMapper;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    @Override
    public IPage<Contract> queryByParams(ContractSearchParams contractSearchParams) {
        System.out.println("查询合同");
        return this.page(PageUtil.initPage(contractSearchParams), contractSearchParams.queryWrapper());
    }
    public void buyerSign(String id) {
        this.baseMapper.buyerSign(id);
    }
}
