package cn.lili.modules.item.service.serviceImpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.item.entity.ItemVO;
import cn.lili.modules.item.mapper.ItemMapper;
import cn.lili.modules.item.service.ItemService;
import cn.lili.modules.promotion.entity.dos.Pintuan;
import cn.lili.modules.promotion.entity.dto.search.PromotionGoodsSearchParams;
import cn.lili.modules.promotion.entity.enums.PromotionsScopeTypeEnum;
import cn.lili.modules.promotion.entity.vos.PintuanVO;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Resource
    ItemMapper itemMapper;

    @Autowired
    private  ItemService itemService;
    @Override
    public List<Item> findAll(){
        List<Item> itemList=itemMapper.findData();
        return itemList;
    }
    @Override
    public IPage<Item> queryByParams(ItemSearchParams itemSearchParams) {
        return this.page(PageUtil.initPage(itemSearchParams), itemSearchParams.queryWrapper());
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveItem(Item item) {
        boolean save = this.save(item);
        return save;
    }

    @Override
    public Long getStoreProductNum(String id) {
        return this.baseMapper.getStoreProductNum(id);
    }

    @Override
    public ItemVO getItemVO(String id) {
        Item item = this.getById(id);
        if (item == null) {
            log.error("项目id[" + id + "]不存在！");
            throw new ServiceException(ResultCode.PINTUAN_NOT_EXIST_ERROR);
        }
        ItemVO itemVO = new ItemVO(item);
        ItemSearchParams searchParams = new ItemSearchParams();
        searchParams.setItemId(item.getItemId());
        itemVO.setItemList(itemService.listFindAll(searchParams));
        return itemVO;
    }
    @Override
    public List<Item> listFindAll(ItemSearchParams searchParams){
        return this.list(searchParams.queryWrapper());
    }
}
