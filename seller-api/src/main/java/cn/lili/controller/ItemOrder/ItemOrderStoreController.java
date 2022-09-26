package cn.lili.controller.itemOrder;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderExportDTO;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.entity.vo.OrderGoodDetailVO;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "订单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{orderId}")
    public ResultMessage<OrderGoodDetailVO> detail(@NotNull @PathVariable String orderId) {
        System.out.println(itemOrderService.getByOrderId(orderId));
        OperationalJudgment.judgment(itemOrderService.getByOrderId(orderId));
        System.out.println(itemOrderService.queryDetail(orderId).toString());
        return ResultUtil.data(itemOrderService.queryDetail(orderId));
    }


    @ApiOperation(value = "查询订单导出列表")
    @GetMapping("/queryExportOrder")
    public ResultMessage<List<ItemOrderExportDTO>> queryExportOrder(ItemOrderSearchParams itemOrderSearchParams) {
        return ResultUtil.data(itemOrderService.queryExportOrder(itemOrderSearchParams));
    }


}