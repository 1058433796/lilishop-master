package cn.lili.modules.itemOrder.service.serviceImpl;

import cn.lili.modules.itemOrder.entity.ItemOrder;
import cn.lili.modules.itemOrder.entity.ItemOrderSearchParams;
import cn.lili.modules.itemOrder.mapper.ItemOrderMapper;
import cn.lili.modules.itemOrder.service.ItemOrderService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ItemOrderServiceImpl extends ServiceImpl<ItemOrderMapper, ItemOrder> implements ItemOrderService {
    @Override
    public IPage<ItemOrder> queryByParams(ItemOrderSearchParams itemOrderSearchParams) {
        return this.page(PageUtil.initPage(itemOrderSearchParams), itemOrderSearchParams.queryWrapper());
    }
}
