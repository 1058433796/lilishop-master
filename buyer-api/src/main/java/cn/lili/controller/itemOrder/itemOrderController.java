package cn.lili.controller.itemOrder;

import cn.hutool.core.date.DateTime;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.contract.entity.ContractSearchParams;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParamsZy;
import cn.lili.modules.itemOrder.service.ItemOrderServiceZy;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/store/itemOrder/itemOrder")
public class itemOrderController {
    private static String prefix = "C";
    @Resource
    private ItemOrderServiceZy itemOrderServiceZy;
    @Autowired
    ContractService contractService;
    @ApiOperation(value = "分页获取订单列表")
    @GetMapping("/list")
    public ResultMessage<IPage<ItemOrder>> getByPage(ItemOrderSearchParamsZy itemOrderSearchParamsZy) {
        //获取当前登录商家账号
        return ResultUtil.data(itemOrderServiceZy.queryByParams(itemOrderSearchParamsZy));
    }

    @ApiOperation(value = "查询订单列表")
    @GetMapping
    public ResultMessage<IPage<ItemOrder>> queryMineOrder(ItemOrderSearchParamsZy itemOrderSearchParamsZy) {
        return ResultUtil.data(itemOrderServiceZy.queryByParams(itemOrderSearchParamsZy));
    }

    @ApiOperation(value = "获取供应商在此订单中提供的零件")
    @GetMapping("/provide/{oid}/{storeId}")
    public ResultMessage<List<SchemeComponent>> getSupplierComponent(@PathVariable("oid") String orderId, @PathVariable("storeId") String storeId) {
        List<SchemeComponent> items = itemOrderServiceZy.queryOrderComponent(orderId, storeId);
        return ResultUtil.data(items);
    }

    @ApiOperation("采购方响应订单")
    @PutMapping("/{oid}/response")
    public ResultMessage<Boolean> buyerResponse(@PathVariable("oid") String oid) {
        ItemOrder item = itemOrderServiceZy.getById(oid);
        item.setReplyStatus("已响应");
        item.setBuyerReply("已响应");
        itemOrderServiceZy.updateById(item);
        return ResultUtil.data(true);
    }

    @ApiOperation("根据项目id和方案ID寻找关联订单")
    @GetMapping("/associated/{itemid}/{schemeId}")
    public ResultMessage<List<ItemOrder>> associatedOrders(@PathVariable("itemid") String itemId,@PathVariable("schemeId") String schemeId) {
//        ItemOrder itemOrder = itemOrderServiceZy.getById(oid);
//        String schemePrimaryId = itemOrder.getSchemeId();
        List<ItemOrder> itemOrders =  itemOrderServiceZy.getAssociatedOrders(itemId,schemeId);
        return  ResultUtil.data(itemOrders);
    }


    @ApiOperation("根据项目编号查询所有合同")
    @GetMapping("/contract/{oid}/{schemeId}")
    public ResultMessage<Object> associatedContractOrders(@PathVariable("oid") String itemId,@PathVariable("schemeId") String schemeId) {
//        ItemOrder itemOrder = itemOrderServiceZy.getById(oid);
//        String schemePrimaryId = itemOrder.getSchemeId();
//        ItemOrderSearchParamsZy itemOrderSearchParamsZy = new ItemOrderSearchParamsZy();
//        itemOrderSearchParamsZy.setSchemeId(schemePrimaryId);
        System.out.println("根据项目编号查询所有合同");
        ContractSearchParams contractSearchParams = new ContractSearchParams();
        contractSearchParams.setItemId(itemId);
        contractSearchParams.setSchemeId(schemeId);
        return ResultUtil.data(contractService.queryAssociated(contractSearchParams));
    }
    @ApiOperation("根据项目编号查询所有签订合同的订单")
    @GetMapping("/signed/{oid}")
    public ResultMessage<Object> getSignedOrder(@PathVariable("oid") String itemId) {
//        ItemOrder itemOrder = itemOrderServiceZy.getById(oid);
//        String schemePrimaryId = itemOrder.getSchemeId();

        System.out.println("根据项目编号查询所有签订合同的订单");
        ItemOrderSearchParamsZy itemOrderSearchParamsZy = new ItemOrderSearchParamsZy();
        itemOrderSearchParamsZy.setContractStatus("已签署");
        itemOrderSearchParamsZy.setItemId(itemId);
        return ResultUtil.data(itemOrderServiceZy.queryByParams(itemOrderSearchParamsZy));
    }
    @ApiOperation("支付订单")
    @PutMapping("/pay/{oid}")
    public ResultMessage<Boolean> payOrder(@PathVariable("oid") String oid){
        itemOrderServiceZy.payOrder(oid, DateTime.now());
        return ResultUtil.data(true);
    }
    @ApiOperation("订单物流签署")
    @PutMapping("/logistic/{oid}")
    public ResultMessage<Boolean> logisticSignOrder(@PathVariable("oid") String oid){
        itemOrderServiceZy.buyerLogisticSign(oid, DateTime.now());
        return ResultUtil.data(true);
    }
}
