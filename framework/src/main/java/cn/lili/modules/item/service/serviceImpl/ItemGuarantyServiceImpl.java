package cn.lili.modules.item.service.serviceImpl;

import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantyDetail;
import cn.lili.modules.item.mapper.ItemGuarantyMapper;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.item.entity.ItemGuarantySearchParam;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
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
