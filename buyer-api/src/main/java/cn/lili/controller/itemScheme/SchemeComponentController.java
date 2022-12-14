package cn.lili.controller.itemScheme;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Snowflake;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSchemeSearchParams;
import cn.lili.modules.item.entity.ItemVO;
import cn.lili.modules.item.service.ItemSchemeService;
import cn.lili.modules.item.service.ItemService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParamsZy;
import cn.lili.modules.itemOrder.service.ItemOrderServiceZy;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.scheme.entity.Scheme;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.entity.SchemeComponentSearchParams;
import cn.lili.modules.schemeComponent.mapper.IdTotal;
import cn.lili.modules.schemeComponent.service.SchemeComponentService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dto.StoreAfterSaleAddressDTO;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreServiceZy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/schemeComponent/schemeComponent")
public class SchemeComponentController {
    private static String prefix = "C";
    @Autowired
    ContractService contractService;
    @Resource
    private SchemeComponentService SchemeComponentService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    StoreServiceZy storeServiceZy;
    @Resource
    private ItemService itemService;
    @Autowired
    ItemOrderServiceZy itemOrderServiceZy;
    @Autowired
    SchemeComponentService schemeComponentService;
    @Autowired
    StoreDetailService storeDetailService;
    @Autowired
    ItemSchemeService itemSchemeService;
    @Autowired
    MemberService memberService;
    Logger logger = LoggerFactory.getLogger(SchemeComponentController.class);

    @ApiOperation(value = "??????????????????????????????")
    @GetMapping("/list")
    public ResultMessage<IPage<SchemeComponent>> getByPage(SchemeComponentSearchParams schemeComponentSearchParams) {
        return ResultUtil.data(SchemeComponentService.queryByParams(schemeComponentSearchParams));
    }

    @PostMapping(value = "/{id}/{itemid}/supplier")
    @ApiOperation(value = "??????????????????????????????,id?????????ID,????????????itemid")
    public  ResultMessage<Object> getSchemeSupplier(@PathVariable("id") String schemeId,@PathVariable("itemid") String itemId) throws IOException {
        Member loggingBuyer = memberService.getUserInfo();
        ArrayList<Object> arrayList = new ArrayList<>();
        System.out.println("????????????"+itemId);
        Snowflake snowflake = new Snowflake();
        List<IdTotal> supplierList = SchemeComponentService.getOrderBy(schemeId);
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        System.out.println("????????????");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url="http://112.230.202.198:8008//doorandhardware/v1/order?projectCode="+itemId;//"http://112.230.202.198:8008//doorandhardware/v1/order?userName="+currentUser.getNickName()+"&projectCode="+itemId "http://112.230.202.198:8008//doorandhardware/v1/order?userName=??????&projectCode=43"
        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        ItemVO item = itemService.getItemVO(itemId);
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println("?????????" + entity.getContentLength());
                JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                System.out.println("data" + jsonObject.get("data"));
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    //?????????????????????????????????
                    JSONObject order = data.getJSONObject(i);//??????
                    JSONArray items = order.getJSONArray("items");//??????????????????????????????????????????
                    String title = order.getString("title");//??????
                    String total = order.getString("total");//??????
                    ItemOrder itemOrder = new ItemOrder();
                    itemOrder.setItemId(itemId);
                    itemOrder.setItemName(item.getItemName());
                    itemOrder.setOrderId(snowflake.nextId() + "");//????????????
                    itemOrder.setOrderAmount(Double.parseDouble(total));//??????total??????
                    itemOrder.setStoreName(goodsService.getSupplyerNameByBrandName(title.replace("????????????", "")));
                    String storeId=goodsService.getSupplyerByBrandName(title.replace("????????????", ""));//?????????????????????
                    itemOrder.setStoreId(storeId);
                    itemOrder.setCreateTime(DateTime.now());
                    itemOrder.setSchemeId(schemeId);
                    itemOrder.setConsigneeName(loggingBuyer.getNickName());
                    itemOrder.setConsigneePhone(loggingBuyer.getMobile());
                    itemOrder.setBuyerName(loggingBuyer.getNickName());
                    itemOrder.setConsigneeAddress("??????????????????");
                    itemOrder.setBuyerId(currentUser.getId());
                    itemOrder.setBuyerName(currentUser.getNickName());
                    itemOrderServiceZy.save(itemOrder);
                    //???scheme_component??????????????????????????????????????????
                    for (int j = 0; j < items.size(); j++) {
                        JSONObject component = items.getJSONObject(j);//????????????????????????
                        SchemeComponent schemeComponent = new SchemeComponent();
                        schemeComponent.setSupplierId(storeId);
                        schemeComponent.setComponentId(component.getString("id"));
                        schemeComponent.setSchemeId(schemeId);
                        schemeComponent.setOrderId(itemOrder.getOrderId());
                        schemeComponent.setComponentUnitPrice(component.getString("unitprice"));
                        schemeComponent.setPp(component.getString("brand"));
                        schemeComponent.setXh(component.getString("model"));
                        schemeComponent.setDw(component.getString("company"));
                        schemeComponent.setSm(component.getString("finishingFace"));
                        schemeComponent.setComponentNumber(component.getInteger("totalPrice")/component.getInteger("unitprice"));
                        schemeComponent.setPm(component.getString("categoryName"));
                        schemeComponent.setParameter(component.getString("parameter"));
                        schemeComponentService.save(schemeComponent);
                        System.out.println("?????????component" + component.getString("brand"));
                    }
                    System.out.println("??????????????????");
                    Contract contract = contractService.getById(prefix + itemOrder.getOrderId());//??????????????????
                    if (contract == null) {//????????????
                        Contract newContract = new Contract();
                        newContract.setId(prefix + itemOrder.getOrderId());
                        newContract.setOrderId(itemOrder.getOrderId());
                        newContract.setCreateTime(DateTime.now());
                        Store store = storeServiceZy.getById(itemOrder.getStoreId());
                        newContract.setAmount(itemOrder.getOrderAmount());
                        newContract.setBuyerId(itemOrder.getBuyerId());
                        newContract.setStoreName(itemOrder.getStoreName());
                        newContract.setStoreId(itemOrder.getStoreId());
                        newContract.setBuyerName(loggingBuyer.getNickName());
                        newContract.setSchemeId(itemOrder.getSchemeId());
                        newContract.setItemId(itemId);
                        newContract.setItemName(item.getItemName());
                        contractService.save(newContract);
                        System.out.println("create contract:" + prefix + itemOrder.getOrderId());
                    }
                    arrayList.add(itemOrder);
                }
            }
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResultUtil.data(arrayList);

    }

    @ApiOperation(value = "????????????ID?????????????????????")
    @GetMapping("/sum/{id}")
    public String getSchemeSumById(@PathVariable("id") String schemeId) {
        System.out.println(SchemeComponentService.getSchemeSumById(schemeId));
        return SchemeComponentService.getSchemeSumById(schemeId);
    }

}
