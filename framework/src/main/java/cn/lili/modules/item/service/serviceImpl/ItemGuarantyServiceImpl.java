package cn.lili.modules.item.service.serviceImpl;

import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantySearchParams;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSchemeSearchParams;
import cn.lili.modules.item.mapper.ItemGuarantyMapper;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ItemGuarantyServiceImpl extends ServiceImpl<ItemGuarantyMapper, ItemGuaranty> implements ItemGuarantyService {

    @Resource
    ItemGuarantyMapper itemGuarantyMapper;
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean createGuaranty(ItemGuaranty itemGuaranty) {
        boolean save = this.save(itemGuaranty);
        return save;
    }

    @Override
    public void setPayFlag(String id){
        this.setPay(id);
    }

    private void setPay(String id) {
        this.baseMapper.setPay(id);
    }

    public IPage<ItemGuaranty> queryByParams(ItemGuarantySearchParams itemGuarantySchemeSearchParams) {
        return this.page(PageUtil.initPage(itemGuarantySchemeSearchParams), itemGuarantySchemeSearchParams.queryWrapper());
    }
}
