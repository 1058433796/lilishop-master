package cn.lili.modules.item.service.serviceImpl;

import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantyDetail;
import cn.lili.modules.item.mapper.ItemGuarantyMapper;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.item.entity.ItemGuarantySearchParam;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.mybatis.util.PageUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@Service
public class ItemGuarantyServiceImpl extends ServiceImpl<ItemGuarantyMapper, ItemGuaranty> implements ItemGuarantyService {

    @Resource
    ItemGuarantyMapper itemGuarantyMapper;
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean createGuaranty(ItemGuaranty itemGuaranty) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url="http://112.230.202.198:8008//doorandhardware/v1/order?projectCode="+itemGuaranty.getItemId();//"http://112.230.202.198:8008//doorandhardware/v1/order?userName="+currentUser.getNickName()+"&projectCode="+itemGuaranty.getItemId() "http://112.230.202.198:8008//doorandhardware/v1/order?userName=冯平&projectCode=43"
        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            Integer sum=0;
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println("长度：" + entity.getContentLength());
                JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                System.out.println("data" + jsonObject.get("data"));
                JSONArray data = jsonObject.getJSONArray("data");

                for (int i = 0; i < data.size(); i++) {
                    JSONObject order = data.getJSONObject(i);//订单
                    Integer total = order.getInteger("total");//总价
                    sum=sum+total;
                }
            }
            itemGuaranty.setSchemeSum(String.valueOf(sum));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean save = this.save(itemGuaranty);
        return save;
    }

    @Override
    public IPage<ItemGuarantyDetail> queryByParams(ItemGuarantySearchParam itemGuarantySearchParam) {
        return this.baseMapper.queryByParams(PageUtil.initPage(itemGuarantySearchParam), itemGuarantySearchParam.queryWrapper());
    }

    @Override
    public IPage<ItemOrderSimpleVO> queryOrderByParams(ItemGuarantySearchParam itemGuarantySearchParam) {
        return this.baseMapper.queryOrderByParams(PageUtil.initPage(itemGuarantySearchParam), itemGuarantySearchParam.queryWrapper());
    }

    @Override
    public IPage<Contract> queryContractByParams(ItemGuarantySearchParam itemGuarantySearchParam) {
        return this.baseMapper.queryContractByParams(PageUtil.initPage(itemGuarantySearchParam), itemGuarantySearchParam.queryWrapper());
    }


    @Override
    public void setPayFlag(String id){
        this.setPay(id);
    }

    @Override
    public Long waitToSignGuaranty(String buyerId) {
        return this.baseMapper.waitToSignGuaranty(buyerId);
    }

    private void setPay(String id) {
        this.baseMapper.setPay(id);
    }

    @Override
    public ItemGuaranty queryByItemId(String itemId){
        return this.baseMapper.searchByItemId(itemId);
    }
}
