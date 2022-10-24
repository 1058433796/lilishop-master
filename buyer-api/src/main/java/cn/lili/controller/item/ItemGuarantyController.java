package cn.lili.controller.item;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantySearchParams;
import cn.lili.modules.item.entity.ItemGuarantyVO;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.item.service.ItemGuarantyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/store/itemGuaranty/itemGuaranty")
public class ItemGuarantyController {
    @Resource
    private ItemGuarantyService itemGuarantyService;

    @PostMapping(value = "/set",consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加履约保证单")
    public ResultMessage<String> saveGuaranty(@RequestBody @Validated ItemGuarantyVO itemGuarantyVO) {
        System.out.println(">>>>>");
        if (itemGuarantyService.createGuaranty(itemGuarantyVO)) {
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_ADD_ERROR);
    }
    @ApiOperation(value = "查询履约保证单是否存在")
    @GetMapping("/list")
    public ResultMessage<IPage<ItemGuaranty>> getByPage(ItemGuarantySearchParams itemGuarantySearchParams) {
        //获取当前登录商家账号
//        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
//        itemSearchParams.setBuyerId(storeId);
        return ResultUtil.data(itemGuarantyService.queryByParams(itemGuarantySearchParams));
    }

}
