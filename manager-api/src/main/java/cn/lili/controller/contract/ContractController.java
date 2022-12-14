package cn.lili.controller.contract;

import cn.hutool.core.date.DateTime;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.entity.ContractSearchParams;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.service.ItemOrderServiceZy;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.service.StoreServiceZy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/manager/contract/contract")
public class ContractController {
    // 合同编号前缀，合同编号由前缀拼接订单编号而成
    private static String prefix = "C";
    @Autowired
    ContractService contractService;

    @Autowired
    ItemOrderServiceZy itemOrderServiceZy;

    @Autowired
    StoreServiceZy storeServiceZy;

    @ApiOperation(value = "分页获取合同列表")
    @GetMapping("/list")
    public ResultMessage<IPage<Contract>> getByPage(ContractSearchParams contractSearchParams) {
        System.out.println("获取关联合同:" + contractSearchParams.getStoreId());
//        StoreVO buyerStore = storeServiceZy.getStoreDetail();
//        contractSearchParams.setBuyerId(buyerStore.getId());
        return ResultUtil.data(contractService.queryByParams(contractSearchParams));
    }

    @ApiOperation(value = "根据订单号生成合同，如果此订单存在对应的合同则返回已生成的合同")
    @PutMapping("/{orderId}/create")
    public ResultMessage<Object> createContract(@PathVariable("orderId") String id) {
        System.out.println("create contract:"  + id);
        Contract contract = contractService.getById(prefix+id);
        ItemOrder order = itemOrderServiceZy.getById(id);
        if(contract!=null) {
            ContractSearchParams contractSearchParams = new ContractSearchParams();
            contractSearchParams.setSchemeId(order.getSchemeId());
            return ResultUtil.data(contractService.queryAssociated(contractSearchParams));
        }
        Contract newContract = new Contract();
        newContract.setId(prefix+id);
        newContract.setOrderId(id);
        newContract.setCreateTime(DateTime.now());
        ItemOrder itemOrder = itemOrderServiceZy.getById(id);
        Store store = storeServiceZy.getById(itemOrder.getBuyerId());
        newContract.setAmount(itemOrder.getOrderAmount());
        newContract.setBuyerId(itemOrder.getBuyerId());
        newContract.setStoreName(itemOrder.getStoreName());
        newContract.setStoreId(itemOrder.getStoreId());
        newContract.setBuyerName(store.getStoreName());
        contractService.save(newContract);
        ContractSearchParams contractSearchParams = new ContractSearchParams();
        contractSearchParams.setSchemeId(order.getSchemeId());
        return ResultUtil.data(contractService.queryAssociated(contractSearchParams));
    }

    @ApiOperation(value = "采购方签署合同")
    @PutMapping("/{contractId}/sign")
    public ResultMessage<Object> signContract(@PathVariable("contractId") String id) {
        Date date = DateTime.now();
        contractService.buyerSign(id, date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return ResultUtil.data(sdf.format(date));
    }
}
