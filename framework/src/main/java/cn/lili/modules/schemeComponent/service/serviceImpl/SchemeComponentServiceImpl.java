package cn.lili.modules.schemeComponent.service.serviceImpl;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.entity.SchemeComponentSearchParams;
import cn.lili.modules.schemeComponent.mapper.IdTotal;
import cn.lili.modules.schemeComponent.mapper.SchemeComponentMapper;
import cn.lili.modules.schemeComponent.service.SchemeComponentService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class SchemeComponentServiceImpl extends ServiceImpl<SchemeComponentMapper, SchemeComponent> implements SchemeComponentService {
    @Autowired
    private  SchemeComponentService schemeComponentService;

    @Override
    public IPage<SchemeComponent> queryByParams(SchemeComponentSearchParams schemeComponentSearchParams) {
        return this.page(PageUtil.initPage(schemeComponentSearchParams), schemeComponentSearchParams.queryWrapper());
    }

    @Override
    public List<String> getSchemeSuppliers(String schemeId) {
        return this.baseMapper.getSchemeSuppliers(schemeId);
    }

    @Override
    public List<SchemeComponent> getSchemeComponentBySchemeIdAndStore(String schemeId, String storeId) {
        return this.baseMapper.getSchemeComponentBySchemeIdAndStore(schemeId, storeId);
    }

    @Override
    public List<IdTotal> getOrderBy(String schemeId) {
        return this.baseMapper.getOrderBy(schemeId);
    }


}
