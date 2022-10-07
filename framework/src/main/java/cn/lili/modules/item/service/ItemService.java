package cn.lili.modules.item.service;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.item.entity.ItemVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService extends IService<Item> {
    public List<Item> findAll();
    /**
     * 商品查询
     *
     * @param itemSearchParams 查询参数
     * @return 商品分页
     */
    IPage<Item> queryByParams(ItemSearchParams itemSearchParams);
    /**
     * 新增项目
     *
     * @param item 插入的项目实例
     * @return 商品分页
     */
    boolean saveItem(Item item);

    /**
     * 项目查询
     *
     * @param searchParams 查询参数
     * @return 商品分页
     */
    List<Item> listFindAll(ItemSearchParams searchParams);

    /**
     * 项目查询
     *
     * @param id 项目ID
     * @return 商品分页
     */
    ItemVO getItemVO(String id);
}
