package cn.lili.controller.contract;

import cn.hutool.core.date.DateTime;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.entity.ContractSearchParams;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/store/contract/contract")
public class ContractController {
    // 合同编号前缀，合同编号由前缀拼接订单编号而成
    private static String prefix = "C";
    @Autowired
    ContractService contractService;

    @Autowired
    ItemOrderService itemOrderService;


    @ApiOperation(value = "分页获取合同列表")
    @GetMapping("/list")
    public ResultMessage<IPage<Contract>> getByPage(ContractSearchParams contractSearchParams) {
        return ResultUtil.data(contractService.queryByParams(contractSearchParams));
    }

    @ApiOperation(value = "根据订单号生成合同，如果此订单存在对应的合同则返回已生成的合同")
    @PutMapping("/{orderId}/create")
    public ResultMessage<Object> createContract(@PathVariable("orderId") String id) {
        System.out.println("create contract:"  + id);
        Contract contract = contractService.getById(prefix+id);
        if(contract!=null) {
            ItemOrder itemOrder = itemOrderService.getById(id);
            ContractBrief contractBrief = new ContractBrief(contract.getId(), itemOrder.getStoreName(), contract.getTimeStart(), contract.getBuyerState(), contract.getProviderState());
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.add(contractBrief);
            return ResultUtil.data(arrayList);
        }
        Contract newContract = new Contract();
        newContract.setId(prefix+id);
        newContract.setTimeStart(DateTime.now());
        ItemOrder itemOrder = itemOrderService.getById(id);

        newContract.setBuyerId(itemOrder.getBuyerId());
        newContract.setStoreName(itemOrder.getStoreName());
        newContract.setStoreId(itemOrder.getStoreId());
        ContractBrief contractBrief = new ContractBrief(newContract.getId(), itemOrder.getStoreName(), newContract.getTimeStart(), newContract.getBuyerState(), newContract.getProviderState());
        contractService.save(newContract);
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(contractBrief);
        return ResultUtil.data(arrayList);
    }

    @ApiOperation(value = "采购方签署合同")
    @PutMapping("/{contractId}/sign")
    public ResultMessage<Object> signContract(@PathVariable("contractId") String id) {
        System.out.println("签署合同");
        contractService.buyerSign(id);
        return ResultUtil.data(true);
    }

    @AllArgsConstructor
    public class ContractBrief {
        public String contractId;
        public String providerName;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        public Date createTime;
        public String buyerState;
        public String providerState;
    }
}
