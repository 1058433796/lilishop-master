package cn.lili.controller.itemScheme;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Snowflake;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSchemeSearchParams;
import cn.lili.modules.item.service.ItemSchemeService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParamsZy;
import cn.lili.modules.itemOrder.service.ItemOrderServiceZy;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.entity.SchemeComponentSearchParams;
import cn.lili.modules.schemeComponent.mapper.IdTotal;
import cn.lili.modules.schemeComponent.service.SchemeComponentService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dto.StoreAfterSaleAddressDTO;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreServiceZy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/schemeComponent/schemeComponent")
public class SchemeComponentController {
    @Resource
    private SchemeComponentService SchemeComponentService;

    @Autowired
    StoreServiceZy storeServiceZy;

    @Autowired
    ItemOrderServiceZy itemOrderServiceZy;

    @Autowired
    StoreDetailService storeDetailService;
    @Autowired
    ItemSchemeService itemSchemeService;
    @Autowired
    MemberService memberService;
    Logger logger = LoggerFactory.getLogger(SchemeComponentController.class);

    @ApiOperation(value = "分页获取方案零件列表")
    @GetMapping("/list")
    public ResultMessage<IPage<SchemeComponent>> getByPage(SchemeComponentSearchParams schemeComponentSearchParams) {
        return ResultUtil.data(SchemeComponentService.queryByParams(schemeComponentSearchParams));
    }

    @PostMapping(value = "/{id}/{itemid}/supplier")
    @ApiOperation(value = "通过方案编号创建订单,id是方案ID,再传一个itemid")
    public  ResultMessage<Object> getSchemeSupplier(@PathVariable("id") String schemeId,@PathVariable("itemid") String itemId) {
            System.out.println("itemid"+itemId);
//        ItemScheme itemScheme  = itemSchemeService.getById(schemePrimaryId);
//        String schemeId = itemScheme.getSchemeId();
        List<IdTotal> supplierList = SchemeComponentService.getOrderBy(schemeId);
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
//        StoreVO buyerStore = storeServiceZy.getStoreDetail();
        System.out.println("建立订单");
//        StoreAfterSaleAddressDTO storep = storeDetailService.getStoreAfterSaleAddressDTO();
        Snowflake snowflake = new Snowflake();
        ArrayList<Object> arrayList = new ArrayList<>();
        Member loggingBuyer = memberService.getUserInfo();
        for (IdTotal idTotal : supplierList) {
            try {
                ItemOrder itemOrder = new ItemOrder();
                itemOrder.setItemId(itemId);
                itemOrder.setOrderId(snowflake.nextId() + "");
                itemOrder.setOrderAmount(idTotal.getTotal());
                Store supplierStore = storeServiceZy.getById(idTotal.getId());
                itemOrder.setStoreName(supplierStore.getStoreName());
                itemOrder.setStoreId(idTotal.getId());
                itemOrder.setCreateTime(DateTime.now());
                itemOrder.setSchemeId(schemeId);
                itemOrder.setConsigneeName(loggingBuyer.getNickName());
                itemOrder.setConsigneePhone(loggingBuyer.getMobile());
                //                System.out.println("1");
                //                System.out.println(storep.getSalesConsigneeMobile());
                //                itemOrder.setBuyerPhone(storep.getSalesConsigneeMobile());
                itemOrder.setBuyerName(currentUser.getNickName());
                itemOrder.setConsigneeAddress("默认收货地址");
                itemOrder.setBuyerId(currentUser.getId());
                itemOrderServiceZy.save(itemOrder);
                arrayList.add(itemOrder);
            } catch (Exception e) {
                logger.error("Order create error: schemeID:" + schemeId);
                throw new RuntimeException(e);
            }
        }
        return ResultUtil.data(arrayList);

    }

    @ApiOperation(value = "根据方案ID获取到总包价格")
    @GetMapping("/sum/{id}")
    public String getSchemeSumById(@PathVariable("id") String schemeId) {
        System.out.println(SchemeComponentService.getSchemeSumById(schemeId));
        return SchemeComponentService.getSchemeSumById(schemeId);
    }

}
