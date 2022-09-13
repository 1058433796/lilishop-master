package cn.lili.modules.item.service;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ItemService extends IService<Item> {
//    public List<Item> findAll();
    /**
     * 商品查询
     *
     * @param itemSearchParams 查询参数
     * @return 商品分页
     */
    IPage<Item> queryByParams(ItemSearchParams itemSearchParams);

}
