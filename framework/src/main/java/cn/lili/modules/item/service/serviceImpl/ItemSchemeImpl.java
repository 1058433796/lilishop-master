package cn.lili.modules.item.service.serviceImpl;

import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSchemeSearchParams;
import cn.lili.modules.item.mapper.ItemSchemeMapper;
import cn.lili.modules.item.service.ItemSchemeService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemSchemeImpl extends ServiceImpl<ItemSchemeMapper, ItemScheme> implements ItemSchemeService {
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveItemScheme(ItemScheme item) {
        boolean save = this.save(item);
        return save;
    }
    @Override
    public IPage<ItemScheme> queryByParams(ItemSchemeSearchParams itemSchemeSearchParams) {
        return this.page(PageUtil.initPage(itemSchemeSearchParams), itemSchemeSearchParams.queryWrapper());
    }

    @Override
    public void checkItemScheme(String id){
        this.updateByPrimaryId(id);
    }
    private void updateByPrimaryId(String id){
        this.baseMapper.updateByPrimaryId(id);
    }
}
