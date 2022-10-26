package cn.lili.controller.itemScheme;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Snowflake;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.service.ItemSchemeService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
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

    @PostMapping(value = "/{id}/supplier")
    @ApiOperation(value = "通过方案编号创建订单")
    public  ResultMessage<Object> getSchemeSupplier(@PathVariable("id") String schemePrimaryId) {
        System.out.println("建立订单");
        ItemScheme itemScheme  = itemSchemeService.getById(schemePrimaryId);
        String schemeId = itemScheme.getSchemeId();
        List<IdTotal> supplierList = SchemeComponentService.getOrderBy(schemeId);
        StoreVO buyerStore = storeServiceZy.getStoreDetail();
        StoreAfterSaleAddressDTO storep = storeDetailService.getStoreAfterSaleAddressDTO();
        Snowflake snowflake = new Snowflake();
        ArrayList<Object> arrayList = new ArrayList<>();
        Member loggingBuyer = memberService.getUserInfo();
        for(IdTotal idTotal : supplierList) {
            try {
                ItemOrder itemOrder = new ItemOrder();
                itemOrder.setOrderId(snowflake.nextId()+"");
                itemOrder.setOrderAmount(idTotal.getTotal());
                Store supplierStore = storeServiceZy.getById(idTotal.getId());
                itemOrder.setStoreName(supplierStore.getStoreName());
                itemOrder.setStoreId(idTotal.getId());
                itemOrder.setCreateTime(DateTime.now());
                itemOrder.setSchemeId(schemePrimaryId);
                itemOrder.setConsigneeName(loggingBuyer.getNickName());
                itemOrder.setConsigneePhone(loggingBuyer.getMobile());
                itemOrder.setBuyerPhone(storep.getSalesConsigneeMobile());
                itemOrder.setBuyerName(buyerStore.getStoreName());
                itemOrder.setConsigneeAddress(buyerStore.getStoreAddressPath() + buyerStore.getStoreAddressDetail());
                itemOrder.setBuyerId(buyerStore.getId());
                itemOrderServiceZy.save(itemOrder);
                arrayList.add(itemOrder);
            }catch (Exception e) {
                logger.error("Order create error: schemeID:" + schemeId);
                throw new RuntimeException(e);
            }
        }
       return ResultUtil.data(arrayList);
    }
}
