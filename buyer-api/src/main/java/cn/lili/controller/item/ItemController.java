package cn.lili.controller.item;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.*;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.item.service.ItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.lili.modules.item.entity.ItemSearchParams;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/item/item")
public class ItemController {
    @Resource
    private ItemService itemService;
    @Resource
    private ItemGuarantyService itemGuarantyService;
    //    @ApiOperation(value = "测试获取项目列表")
//    @GetMapping("/test")
//    public List<Item> findAll(){
//        System.out.println("test");
//        return itemService.findAll();
//    }
    @ApiOperation(value = "分页获取项目列表")
    @GetMapping("/list")
    public ResultMessage<IPage<Item>> getByPage(ItemSearchParams itemSearchParams) {
        //获取当前登录商家账号
//        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
//        itemSearchParams.setBuyerId(storeId);
        return ResultUtil.data(itemService.queryByParams(itemSearchParams));
    }
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加项目")
    public ResultMessage<String> saveItem(@RequestBody @Validated ItemVO item) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        item.setBuyerId(currentUser.getStoreId());
        item.setBuyerName(currentUser.getStoreName());
        if (itemService.saveItem(item)) {
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_ADD_ERROR);
    }
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "通过id获取")
    public ResultMessage<ItemVO> get(@PathVariable String id) {
        ItemVO item = itemService.getItemVO(id);
        return ResultUtil.data(item);
    }


}