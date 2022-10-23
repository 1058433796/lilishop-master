package cn.lili.controller.itemOrder;

import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderExportDTO;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.entity.vo.OrderGoodDetailVO;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import cn.lili.modules.itemOrder.service.ItemOrderServiceZy;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 店铺端,订单接口
 *
 * @author Chopper
 * @since 2020/11/17 4:35 下午
 **/
@Slf4j
@RestController
@RequestMapping("/store/itemOrder/itemOrder")
@Api(tags = "店铺端,订单接口")
public class ItemOrderStoreController {

    /**
     * 订单
     */
    @Autowired
    private ItemOrderService itemOrderService;


    @ApiOperation(value = "查询订单列表")
    @GetMapping
    public ResultMessage<IPage<ItemOrderSimpleVO>> queryMineOrder(ItemOrderSearchParams itemOrderSearchParams) {
        return ResultUtil.data(itemOrderService.queryByParams(itemOrderSearchParams));
    }
    @ApiOperation(value = "获取供应商在此订单中提供的零件")
    @GetMapping("/provide/{oid}")
    public ResultMessage<OrderGoodDetailVO> getSupplierComponent(@PathVariable("oid") String orderId) {
        OrderGoodDetailVO items = itemOrderService.queryOrderComponent(orderId);
        return ResultUtil.data(items);
    }


    @ApiOperation(value = "查询订单导出列表")
    @GetMapping("/queryExportOrder")
    public ResultMessage<List<ItemOrderExportDTO>> queryExportOrder(ItemOrderSearchParams itemOrderSearchParams) {
        return ResultUtil.data(itemOrderService.queryExportOrder(itemOrderSearchParams));
    }

    @ApiOperation("供应商响应订单")
    @PutMapping("/{oid}/response")
    public ResultMessage<Boolean> storeResponse(@PathVariable("oid") String oid) {
        System.out.println("供应商响应订单：" + oid);
        ItemOrder item = itemOrderService.getById(oid);
        item.setStoreReply("已响应");
        item.setReplyStatus("已响应");
        itemOrderService.updateById(item);
        return ResultUtil.data(true);
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "订单发货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "logisticsNo", value = "发货单号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "logisticsId", value = "物流公司", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/{orderId}/delivery")
    public ResultMessage<Object> delivery(@NotNull(message = "参数非法") @PathVariable String orderId,
                                          @NotNull(message = "发货单号不能为空") String logisticsNo,
                                          @NotNull(message = "请选择物流公司") String logisticsId) {
        return ResultUtil.data(itemOrderService.delivery(orderId, logisticsNo, logisticsId));
    }

}