package cn.lili.modules.itemOrder.serviceimpl;

import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.dto.ItemOrderSearchParamsZy;
import cn.lili.modules.itemOrder.entity.vo.OrderWithContractSimpleV0;
import cn.lili.modules.itemOrder.mapper.ItemOrderMapperZy;
import cn.lili.modules.itemOrder.service.ItemOrderServiceZy;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemOrderServiceZyImpl extends ServiceImpl<ItemOrderMapperZy, ItemOrder> implements ItemOrderServiceZy {
    @Override
    public IPage<ItemOrder> queryByParams(ItemOrderSearchParamsZy itemOrderSearchParams) {
        return this.page(PageUtil.initPage(itemOrderSearchParams), itemOrderSearchParams.queryWrapper());
    }

    @Override
    public List<SchemeComponent> queryOrderComponent(String oid, String storeId) {
        return this.baseMapper.queryOrderComponent(oid, storeId);
    }
    @Override
    public List<ItemOrder> getAssociatedOrders(String itemId,String schemeId){
        return this.baseMapper.getAssociatedOrders(itemId,schemeId);

    }

    @Override
    public IPage<ItemOrder> queryAssociatedContractOrders(ItemOrderSearchParamsZy itemOrderSearchParams) {
        return this.baseMapper.queryAssociatedContractOrders(PageUtil.initPage(itemOrderSearchParams), itemOrderSearchParams.queryWrapper());
    }

    @Override
    public void payOrder(String oid, Date time) {
        this.baseMapper.payOrder(oid, time);
    }

    @Override
    public void buyerLogisticSign(String oid, Date date) {
        this.baseMapper.buyerLogisticSign(oid, date);
    }

}
