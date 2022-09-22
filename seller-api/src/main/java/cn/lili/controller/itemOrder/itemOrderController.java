package cn.lili.controller.itemOrder;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/store/itemOrder/itemOrder")
public class itemOrderController {
    @Resource
    private ItemOrderService itemOrderService;

    @ApiOperation(value = "分页获取项目列表")
    @GetMapping("/list")
    public ResultMessage<IPage<ItemOrderSimpleVO>> getByPage(ItemOrderSearchParams itemOrderSearchParams) {
        //获取当前登录商家账号
//        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
//        itemOrderSearchParams.setBuyerId(storeId);
        return ResultUtil.data(itemOrderService.queryByParams(itemOrderSearchParams));
    }
}
