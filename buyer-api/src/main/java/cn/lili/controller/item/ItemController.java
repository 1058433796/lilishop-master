package cn.lili.controller.item;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.enums.ResultUtilG;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.common.vo.ResultMessageG;
import cn.lili.modules.item.entity.*;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.item.service.ItemSchemeService;
import cn.lili.modules.item.service.ItemService;
import cn.lili.modules.scheme.entity.Scheme;
import cn.lili.modules.scheme.service.SchemeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.lili.modules.item.entity.ItemSearchParams;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/item/item")
public class ItemController {
    @Resource
    private ItemService itemService;
    @Resource
    private ItemGuarantyService itemGuarantyService;
    @Resource
    private ItemSchemeService itemSchemeService;
    @Resource
    private SchemeService schemeService;
    @Resource
    private ItemSchemeService itemschemeService;

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
//        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
//        item.setBuyerId(currentUser.getStoreId());
//        item.setBuyerName(currentUser.getStoreName());
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        item.setCreateTime(date);
        //创建项目
        if (itemService.saveItem(item)) {
            //创建项目之后,为项目创建对应方案
        }
            String itemId=item.getItemId();//添加之后的新项目ID
            List<Scheme> schemelist=schemeService.getSchemeList();
            int finish=0;
            for (int i=0;i<schemelist.size();i++){
                //按照每一条方案进行添加
                ItemSchemeVO itemScheme=new ItemSchemeVO();
                String schemeid= schemelist.get(i).getSchemeId();
                itemScheme.setSchemeId(schemeid);
                itemScheme.setItemId(itemId);
                itemScheme.setDoorId(schemelist.get(i).getDoorId());
                itemScheme.setOpenMethod(schemelist.get(i).getOpenMethod());
                itemScheme.setOpenDirection(schemelist.get(i).getOpenDirection());
                itemScheme.setHeight(schemelist.get(i).getHeight());
                itemScheme.setWidth(schemelist.get(i).getWidth());
                itemScheme.setThickness(schemelist.get(i).getThickness());
                itemScheme.setGuard(schemelist.get(i).getGuard());
                itemScheme.setTexture(schemelist.get(i).getTexture());
                itemScheme.setHandle(schemelist.get(i).getHandle());
                itemScheme.setFirerating(schemelist.get(i).getFirerating());
                itemScheme.setWjgroup(schemelist.get(i).getWjgroup());
                itemScheme.setDoorShape(schemelist.get(i).getDoorShape());
                itemScheme.setRoomFunction(schemelist.get(i).getRoomFunction());
                itemScheme.setWeight(schemelist.get(i).getWeight());
                itemScheme.setGroupType(schemelist.get(i).getGroupType());
                itemScheme.setQkType(schemelist.get(i).getQkType());
                if (itemschemeService.saveItemScheme(itemScheme)) {
                    finish++;
                    System.out.println(finish);
                    //保存一条项目方案后生成对应保证单
                    String itemName=item.getItemName();
//                    String primaryId=itemScheme.getPrimaryId();
//                    String schemeSum=schemelist.get(i).getSchemeSum();
//                    String orderName=itemName+"项目";
//                    String orderContent=itemName+"内容";
                    ItemGuaranty itemGuaranty=new ItemGuaranty();
                    itemGuaranty.setItemId(itemId);
                    itemGuaranty.setPrimaryId(itemScheme.getPrimaryId());
                    itemGuaranty.setBuyerId(item.getBuyerId());
                    itemGuaranty.setSchemeSum(schemelist.get(i).getSchemeSum());
                    itemGuaranty.setOrderContent(item.getItemName()+"内容");
                    itemGuaranty.setOrderName(item.getItemName()+"项目");
                    itemGuarantyService.createGuaranty(itemGuaranty);
                }
            }
        if (finish==schemelist.size()){
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        }
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);

    }
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "通过id获取")
    public ResultMessage<ItemVO> get(@PathVariable String id) {
        ItemVO item = itemService.getItemVO(id);
        return ResultUtil.data(item);
    }
    @ApiOperation(value = "要求的登录")
    @PostMapping(value = "/login")
    public ResultMessageG<LoginItem> login(@RequestParam String username,
                                           @RequestParam String password) throws NoSuchAlgorithmException {
        System.out.println("username"+username+password);

//        String Pass= DigestUtils.md5Hex(password);
        return ResultUtilG.data(itemService.queryLogin(username,password));


    }

}