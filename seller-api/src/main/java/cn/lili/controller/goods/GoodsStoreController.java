package cn.lili.controller.goods;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.dto.GoodsOperationDTO;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.goods.entity.dto.GoodsSkuStockDTO;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.modules.goods.entity.vos.GoodsSkuVO;
import cn.lili.modules.goods.entity.vos.GoodsVO;
import cn.lili.modules.goods.entity.vos.StockWarningVO;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.service.StoreDetailService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.lili.common.enums.ResultCode.GOODS_ERROR;

/**
 * 店铺端,商品接口
 *
 * @author pikachu
 * @since 2020-02-23 15:18:56
 */
@RestController
@Api(tags = "店铺端,商品接口")
@RequestMapping("/store/goods/goods")
public class GoodsStoreController {

    /**
     * 商品
     */
    @Autowired
    private GoodsService goodsService;
    /**
     * 商品sku
     */
    @Autowired
    private GoodsSkuService goodsSkuService;
    /**
     * 店铺详情
     */
    @Autowired
    private StoreDetailService storeDetailService;

    @ApiOperation(value = "分页获取商品列表")
    @GetMapping(value = "/list")
    public ResultMessage<IPage<Goods>> getByPage(GoodsSearchParams goodsSearchParams) {
        //获取当前登录商家账号
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        goodsSearchParams.setStoreId(storeId);
        return ResultUtil.data(goodsService.queryByParams(goodsSearchParams));
    }

    @ApiOperation(value = "分页获取商品Sku列表")
    @GetMapping(value = "/sku/list")
    public ResultMessage<IPage<GoodsSku>> getSkuByPage(GoodsSearchParams goodsSearchParams) {
        //获取当前登录商家账号
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        goodsSearchParams.setStoreId(storeId);
        return ResultUtil.data(goodsSkuService.getGoodsSkuByPage(goodsSearchParams));
    }

    @ApiOperation(value = "分页获取库存告警商品列表")
    @GetMapping(value = "/list/stock")
    public ResultMessage<StockWarningVO> getWarningStockByPage(GoodsSearchParams goodsSearchParams) {
        //获取当前登录商家账号
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        StoreDetail storeDetail = OperationalJudgment.judgment(storeDetailService.getStoreDetail(storeId));
        Integer stockWarnNum = storeDetail.getStockWarning();
        goodsSearchParams.setStoreId(storeId);
        goodsSearchParams.setLeQuantity(stockWarnNum);
        goodsSearchParams.setMarketEnable(GoodsStatusEnum.UPPER.name());
        IPage<GoodsSku> goodsSku = goodsSkuService.getGoodsSkuByPage(goodsSearchParams);
        StockWarningVO stockWarning = new StockWarningVO(stockWarnNum, goodsSku);
        return ResultUtil.data(stockWarning);
    }


    @ApiOperation(value = "通过id获取")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<GoodsVO> get(@PathVariable String id) {
        GoodsVO goods = OperationalJudgment.judgment(goodsService.getGoodsVO(id));
        return ResultUtil.data(goods);
    }

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResultMessage<GoodsOperationDTO> save(@Valid @RequestBody GoodsOperationDTO goodsOperationDTO) {
        System.out.println("开始调用");
        goodsService.addGoods(goodsOperationDTO);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = "http://112.230.202.198:8008/commodity/v1/add";
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = new JSONObject();;
        jsonObject.put("id",goodsOperationDTO.getGoodsCode());
        jsonObject.put("name",goodsOperationDTO.getGoodsName());
        jsonObject.put("ansi",goodsOperationDTO.getAnsiCert());
        if(goodsOperationDTO.getEnCert().equals("有")){
            jsonObject.put("en",true);
        }
        else {
            jsonObject.put("en",false);
        }
        if(goodsOperationDTO.getGbCert().equals("有")){
            jsonObject.put("gb",true);
        }
        else {
            jsonObject.put("gb",false);
        }
        if(goodsOperationDTO.getFireProofCert().equals("有")){
            jsonObject.put("fire_certification",true);
        }
        else {
            jsonObject.put("fire_certification",false);
        }
        jsonObject.put("material",goodsOperationDTO.getMaterial());
        jsonObject.put("dimensions",goodsOperationDTO.getSize());
        jsonObject.put("load_bearing",goodsOperationDTO.getLoadBearing());
        jsonObject.put("high_level",goodsOperationDTO.getForceLevel());
        jsonObject.put("adjustable_parameter",goodsOperationDTO.getAdjustParam());
        jsonObject.put("auxiliary_certification",goodsOperationDTO.getAuxCert());
        jsonObject.put("model",goodsOperationDTO.getGoodsCode());//未知
        jsonObject.put("brand",goodsOperationDTO.getGoodsBrand());
        jsonObject.put("unitPrice",goodsOperationDTO.getGoodsDisplayPrice());
        jsonObject.put("finishingFace",goodsOperationDTO.getDecoration());
        System.out.println("json"+jsonObject.toString());
        post.addHeader("content-type", "application/json");
        post.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            System.out.println(response);
            if (entity != null) {
                System.out.println("响应内容：");
                System.out.println(EntityUtils.toString(entity));
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResultUtil.success();}


    @ApiOperation(value = "修改商品")
    @PutMapping(value = "/update/{goodsId}", consumes = "application/json", produces = "application/json")
    public ResultMessage<GoodsOperationDTO> update(@RequestBody GoodsOperationDTO goodsOperationDTO, @PathVariable String goodsId) {
        goodsService.editGoods(goodsOperationDTO, goodsId);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = "http://112.230.202.198:8008/commodity/v1/update";
        HttpPut post = new HttpPut(url);
        JSONObject jsonObject = new JSONObject();;
        jsonObject.put("id",goodsOperationDTO.getGoodsCode());
        jsonObject.put("name",goodsOperationDTO.getGoodsName());
        jsonObject.put("ansi",goodsOperationDTO.getAnsiCert());
        if(goodsOperationDTO.getEnCert().equals("有")){
            jsonObject.put("en",true);
        }
        else {
            jsonObject.put("en",false);
        }
        if(goodsOperationDTO.getGbCert().equals("有")){
            jsonObject.put("gb",true);
        }
        else {
            jsonObject.put("gb",false);
        }
        if(goodsOperationDTO.getFireProofCert().equals("有")){
            jsonObject.put("fire_certification",true);
        }
        else {
            jsonObject.put("fire_certification",false);
        }
        jsonObject.put("material",goodsOperationDTO.getMaterial());
        jsonObject.put("dimensions",goodsOperationDTO.getSize());
        jsonObject.put("load_bearing",goodsOperationDTO.getLoadBearing());
        jsonObject.put("high_level",goodsOperationDTO.getForceLevel());
        jsonObject.put("adjustable_parameter",goodsOperationDTO.getAdjustParam());
        jsonObject.put("auxiliary_certification",goodsOperationDTO.getAuxCert());
        jsonObject.put("model",goodsOperationDTO.getGoodsCode());//未知
        jsonObject.put("brand",goodsOperationDTO.getGoodsBrand());
        jsonObject.put("unitPrice",goodsOperationDTO.getGoodsDisplayPrice());
        jsonObject.put("finishingFace",goodsOperationDTO.getDecoration());
        post.addHeader("content-type", "application/json");
        post.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println("响应内容：");
                System.out.println(EntityUtils.toString(entity));
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResultUtil.success();
    }

    @ApiOperation(value = "下架商品", notes = "下架商品时使用")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "query", allowMultiple = true)
    @PutMapping(value = "/under")
    public ResultMessage<Object> underGoods(@RequestParam List<String> goodsId) {

        goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.DOWN, "商家下架");
        return ResultUtil.success();
    }

    @ApiOperation(value = "上架商品", notes = "上架商品时使用")
    @PutMapping(value = "/up")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "query", allowMultiple = true)
    public ResultMessage<Object> unpGoods(@RequestParam List<String> goodsId) {
        goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, "");
        return ResultUtil.success();
    }

    @ApiOperation(value = "删除商品")
    @PutMapping(value = "/delete")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "query", allowMultiple = true)
    public ResultMessage<Object> deleteGoods(@RequestParam List<String> goodsId) {
        Goods goods=new Goods();
        for (String id:goodsId){
            goods=goodsService.getByGoodsId(id);
        }
        goodsService.deleteGoods(goodsId);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = "http://112.230.202.198:8008/commodity/v1/delete";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",goods.getGoodsCode());
            HttpPut post = new HttpPut(url);
            post.addHeader("content-type", "application/json");
            post.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
            try {
                CloseableHttpResponse response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("响应内容：");
                    System.out.println(jsonObject.toString());
                    System.out.println(EntityUtils.toString(entity));
                }
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        return ResultUtil.success();
    }


    @ApiOperation(value = "设置商品运费模板")
    @PutMapping(value = "/freight")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "query", allowMultiple = true),
            @ApiImplicitParam(name = "templateId", value = "运费模板ID", required = true, paramType = "query")
    })
    public ResultMessage<Object> freight(@RequestParam List<String> goodsId, @RequestParam String templateId) {
        goodsService.freight(goodsId, templateId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "根据goodsId分页获取商品规格列表")
    @GetMapping(value = "/sku/{goodsId}/list")
    public ResultMessage<List<GoodsSkuVO>> getSkuByList(@PathVariable String goodsId) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return ResultUtil.data(goodsSkuService.getGoodsSkuVOList(goodsSkuService.list(new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getGoodsId, goodsId).eq(GoodsSku::getStoreId, storeId))));
    }

    @ApiOperation(value = "修改商品库存")
    @PutMapping(value = "/update/stocks", consumes = "application/json")
    public ResultMessage<Object> updateStocks(@RequestBody List<GoodsSkuStockDTO> updateStockList) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        // 获取商品skuId集合
        List<String> goodsSkuIds = updateStockList.stream().map(GoodsSkuStockDTO::getSkuId).collect(Collectors.toList());
        // 根据skuId集合查询商品信息
        List<GoodsSku> goodsSkuList = goodsSkuService.list(new LambdaQueryWrapper<GoodsSku>().in(GoodsSku::getId, goodsSkuIds).eq(GoodsSku::getStoreId, storeId));
        // 过滤不符合当前店铺的商品
        List<String> filterGoodsSkuIds = goodsSkuList.stream().map(GoodsSku::getId).collect(Collectors.toList());
        List<GoodsSkuStockDTO> collect = updateStockList.stream().filter(i -> filterGoodsSkuIds.contains(i.getSkuId())).collect(Collectors.toList());
        goodsSkuService.updateStocks(collect);
        return ResultUtil.success();
    }

}
