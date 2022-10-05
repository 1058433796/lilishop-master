package cn.lili.controller.itemScheme;

import cn.hutool.core.date.DateTime;
import cn.lili.Snowflake;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.item.entity.ItemVO;
import cn.lili.modules.item.service.ItemService;
import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.entity.SchemeComponentSearchParams;
import cn.lili.modules.schemeComponent.mapper.IdTotal;
import cn.lili.modules.schemeComponent.service.SchemeComponentService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.entity.dto.StoreAfterSaleAddressDTO;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreService;
import com.alipay.api.domain.ItemOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store/schemeComponent/schemeComponent")
public class SchemeComponentController {
    @Resource
    private SchemeComponentService SchemeComponentService;

    @Autowired
    StoreService storeService;

    @Autowired
    ItemOrderService itemOrderService;

    @Autowired
    StoreDetailService storeDetailService;

    Logger logger = LoggerFactory.getLogger(SchemeComponentController.class);

    @ApiOperation(value = "分页获取方案零件列表")
    @GetMapping("/list")
    public ResultMessage<IPage<SchemeComponent>> getByPage(SchemeComponentSearchParams schemeComponentSearchParams) {
        return ResultUtil.data(SchemeComponentService.queryByParams(schemeComponentSearchParams));
    }

    @PostMapping(value = "/{id}/supplier")
    @ApiOperation(value = "通过方案编号创建订单")
    public  ResultMessage<Object> getSchemeSupplier(@PathVariable("id") String schemeId) {
        System.out.println("建立订单");
        List<IdTotal> supplierList = SchemeComponentService.getOrderBy(schemeId);
        StoreVO buyerStore = storeService.getStoreDetail();
        StoreAfterSaleAddressDTO storep = storeDetailService.getStoreAfterSaleAddressDTO();
        Snowflake snowflake = Snowflake.getInstanceSnowflake();
        ArrayList<Object> arrayList = new ArrayList<>();
        for(IdTotal idTotal : supplierList) {
            try {
                ItemOrder itemOrder = new ItemOrder();
                itemOrder.setOrderId(snowflake.nextId() + "");
                itemOrder.setOrderAmount(idTotal.getTotal()+"");
                Store supplierStore = storeService.getById(idTotal.getId());
                itemOrder.setStoreName(supplierStore.getStoreName());
                itemOrder.setStoreId(idTotal.getId());
                itemOrder.setCreateTime(DateTime.now());
                itemOrder.setSchemeId(schemeId);
                itemOrder.setBuyerPhone(storep.getSalesConsigneeMobile());
                itemOrder.setBuyerName(storep.getSalesConsigneeName());
                itemOrder.setBuyerAddress(buyerStore.getStoreAddressPath() + buyerStore.getStoreAddressDetail());
                itemOrder.setBuyerId(buyerStore.getId());
                itemOrderService.save(itemOrder);
                arrayList.add(itemOrder);
            }catch (Exception e) {
                logger.error("Order create error: schemeID:" + schemeId);
                throw new RuntimeException(e);
            }
        }
//        SchemeComponentSearchParams  schemeComponentSearchParams = new SchemeComponentSearchParams();
//        schemeComponentSearchParams.setSchemeId(schemeId);
//        IPage<SchemeComponent> iPage = SchemeComponentService.queryByParams(schemeComponentSearchParams);
//        List<SchemeComponent> schemeComponents = iPage.getRecords();
//        ArrayList<Object> arrayList = new ArrayList<>();
//        Snowflake snowflake = Snowflake.getInstanceSnowflake();
//        // 获取采购方店铺信息
//
//        for (int i=0; i < schemeComponents.size(); ++i) {
//            SchemeComponent schemeComponent = schemeComponents.get(i);
//            String ids = schemeComponent.getSupplierId();
//            Store supplierStore = storeService.getById(ids);
//            DateTime dateTime = DateTime.now();
//            ItemOrder itemOrder = new ItemOrder();
//            itemOrder.setOrderAmount(Double.parseDouble(schemeComponent.getComponentUnitPrice())*Double.parseDouble(schemeComponent.getComponentNumber())+"");
//            itemOrder.setStoreName(supplierStore.getStoreName());
//            // 设置订单中与采购方相关的信息
//            itemOrder.setBuyerPhone(storep.getSalesConsigneeMobile());
//            itemOrder.setBuyerName(storep.getSalesConsigneeName());
//            itemOrder.setBuyerAddress(buyerStore.getStoreAddressPath() + buyerStore.getStoreAddressDetail());
//            itemOrder.setBuyerId(buyerStore.getId());
//            try {
//                // 生成订单号、订单产生时间并设置订单所对应的供应商店铺的id
//                itemOrder.setOrderId(snowflake.nextId()+"");
//                itemOrder.setCreateTime(dateTime.now());
//                itemOrder.setStoreId(ids);
//                itemOrderService.save(itemOrder);
//                arrayList.add(itemOrder);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
       return ResultUtil.data(arrayList);
    }


}
