package cn.lili.modules.item.service.serviceImpl;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.item.mapper.ItemMapper;
import cn.lili.modules.item.service.ItemService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
//    @Resource
//    ItemMapper itemMapper;
//    @Override
//    public List<Item> findAll(){
//        List<Item> itemList=itemMapper.findData();
//        return itemList;
//    }
    @Override
    public IPage<Item> queryByParams(ItemSearchParams itemSearchParams) {
        return this.page(PageUtil.initPage(itemSearchParams), itemSearchParams.queryWrapper());
    }
}
