package cn.lili.controller.itemOrder;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.Item;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.itemOrder.entity.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/itemOrder/itemOrder")
public class itemOrderController {
    @Resource
    private ItemOrderService itemOrderService;

    @ApiOperation(value = "分页获取项目列表")
    @GetMapping("/list")
    public ResultMessage<IPage<ItemOrder>> getByPage(ItemOrderSearchParams itemOrderSearchParams) {
        //获取当前登录商家账号
        return ResultUtil.data(itemOrderService.queryByParams(itemOrderSearchParams));
    }

    @ApiOperation(value = "查询订单列表")
    @GetMapping
    public ResultMessage<IPage<ItemOrder>> queryMineOrder(ItemOrderSearchParams itemOrderSearchParams) {
        return ResultUtil.data(itemOrderService.queryByParams(itemOrderSearchParams));
    }

    @ApiOperation(value = "获取供应商在此订单中提供的零件")
    @GetMapping("/provide/{oid}/{storeId}")
    public ResultMessage<List<SchemeComponent>> getSupplierComponent(@PathVariable("oid") String orderId, @PathVariable("storeId") String storeId) {
        List<SchemeComponent> items = itemOrderService.queryOrderComponent(orderId, storeId);
        return ResultUtil.data(items);
    }
    @ApiOperation("采购方响应订单")
    @PutMapping("/{oid}/response")
    public ResultMessage<Boolean> buyerResponse(@PathVariable("oid") String oid) {
        System.out.println("采购方响应订单：" + oid);
        ItemOrder item = itemOrderService.getById(oid);
        item.setReplyStatus("已响应");
        itemOrderService.updateById(item);
        return ResultUtil.data(true);
    }
}
