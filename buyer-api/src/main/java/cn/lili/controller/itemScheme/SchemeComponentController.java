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

    @ApiOperation(value = "分页获取方案零件列表")
    @GetMapping("/list")
    public ResultMessage<IPage<SchemeComponent>> getByPage(SchemeComponentSearchParams schemeComponentSearchParams) {
        return ResultUtil.data(SchemeComponentService.queryByParams(schemeComponentSearchParams));
    }

    @PostMapping(value = "/{id}/{itemid}/supplier")
    @ApiOperation(value = "通过方案编号创建订单,id是方案ID,再传一个itemid")
    public  ResultMessage<Object> getSchemeSupplier(@PathVariable("id") String schemeId,@PathVariable("itemid") String itemId) throws IOException {
        Member loggingBuyer = memberService.getUserInfo();
        ArrayList<Object> arrayList = new ArrayList<>();
        System.out.println("创建订单"+itemId);
        Snowflake snowflake = new Snowflake();
        List<IdTotal> supplierList = SchemeComponentService.getOrderBy(schemeId);
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        System.out.println("建立订单");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url="http://112.230.202.198:8008//doorandhardware/v1/order?userName=冯平&projectCode=43";//"http://112.230.202.198:8008//doorandhardware/v1/order?userName="+currentUser.getNickName()+"&projectCode="+itemId
        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        ItemVO item = itemService.getItemVO(itemId);
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println("长度：" + entity.getContentLength());
                JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                System.out.println("data" + jsonObject.get("data"));
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    //每一个品牌生成一条订单
                    JSONObject order = data.getJSONObject(i);//订单
                    JSONArray items = order.getJSONArray("items");//订单中包含的商品，是一个数组
                    String title = order.getString("title");//标题
                    String total = order.getString("total");//总价
                    ItemOrder itemOrder = new ItemOrder();
                    itemOrder.setItemId(itemId);
                    itemOrder.setItemName(item.getItemName());
                    itemOrder.setOrderId(snowflake.nextId() + "");//创建一个
                    itemOrder.setOrderAmount(Double.parseDouble(total));//可用total代替
                    itemOrder.setStoreName(goodsService.getSupplyerNameByBrandName(title.replace("品牌订单", "")));
                    String storeId=goodsService.getSupplyerByBrandName(title.replace("品牌订单", ""));//输入的是品牌名
                    itemOrder.setStoreId(storeId);
                    itemOrder.setCreateTime(DateTime.now());
                    itemOrder.setSchemeId(schemeId);
                    itemOrder.setConsigneeName(loggingBuyer.getNickName());
                    itemOrder.setConsigneePhone(loggingBuyer.getMobile());
                    itemOrder.setBuyerName(loggingBuyer.getNickName());
                    itemOrder.setConsigneeAddress("默认收货地址");
                    itemOrder.setBuyerId(currentUser.getId());
                    itemOrder.setBuyerName(currentUser.getNickName());
                    itemOrderServiceZy.save(itemOrder);
                    //在scheme_component里插入相关的订单内包含的商品
                    for (int j = 0; j < items.size(); j++) {
                        JSONObject component = items.getJSONObject(j);//这是一条商品数据
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
                        System.out.println("新建了component" + component.getString("brand"));
                    }
                    System.out.println("开始创建合同");
                    Contract contract = contractService.getById(prefix + itemOrder.getOrderId());//有无生成合同
                    if (contract == null) {//没有合同
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

    @ApiOperation(value = "根据方案ID获取到总包价格")
    @GetMapping("/sum/{id}")
    public String getSchemeSumById(@PathVariable("id") String schemeId) {
        System.out.println(SchemeComponentService.getSchemeSumById(schemeId));
        return SchemeComponentService.getSchemeSumById(schemeId);
    }

}
