package cn.lili.controller.item;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.item.service.ItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/item/itemList")
public class ItemController {
    @Resource
    private ItemService itemService;

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
}
