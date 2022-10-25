package cn.lili.controller.item;

import cn.hutool.core.date.DateTime;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantyDetail;
import cn.lili.modules.item.entity.ItemGuarantySearchParam;
import cn.lili.modules.item.entity.ItemGuarantyVO;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Contact;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/store/itemGuaranty/itemGuaranty")
public class ItemGuarantyController {
    @Resource
    private ItemGuarantyService itemGuarantyService;

    @PostMapping(value = "/set",consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加履约保证单")
    public ResultMessage<String> saveGuaranty(@RequestBody @Validated ItemGuarantyVO itemGuarantyVO) {
        System.out.println(">>>>>");
        itemGuarantyVO.setCreateTime(DateTime.now());
        if (itemGuarantyService.getById(itemGuarantyVO.getId())!=null) {
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        };
        if (itemGuarantyService.createGuaranty(itemGuarantyVO)) {
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_ADD_ERROR);
    }


    @GetMapping("/item")
    public ResultMessage<IPage<ItemGuarantyDetail>> getGuaranty(ItemGuarantySearchParam itemGuarantySearchParam)  {
        return ResultUtil.data(itemGuarantyService.queryByParams(itemGuarantySearchParam));
    }

    @GetMapping("/order")
    public ResultMessage<IPage<ItemOrderSimpleVO>> getOrderGuaranty(ItemGuarantySearchParam itemGuarantySearchParam)  {
        return ResultUtil.data(itemGuarantyService.queryOrderByParams(itemGuarantySearchParam));
    }
    @GetMapping("/contract")
    public ResultMessage<IPage<Contract>> getContractGuaranty(ItemGuarantySearchParam itemGuarantySearchParam)  {
        return ResultUtil.data(itemGuarantyService.queryContractByParams(itemGuarantySearchParam));
    }
}
