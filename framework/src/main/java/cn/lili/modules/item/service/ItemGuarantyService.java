package cn.lili.modules.item.service;


import cn.lili.modules.contract.entity.Contract;
import cn.lili.modules.item.entity.ItemGuaranty;
import cn.lili.modules.item.entity.ItemGuarantyDetail;
import cn.lili.modules.item.entity.ItemGuarantySearchParam;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface ItemGuarantyService extends IService<ItemGuaranty> {

    /**
     * 新增保证单
     *
     * @param itemGuaranty 插入的保证单实例
     * @return 结果
     */
    boolean createGuaranty(ItemGuaranty itemGuaranty);

    //更新履约保证金支付状态
    void setPayFlag(String id);

    Long waitToSignGuaranty(String buyerId);

    IPage<ItemGuarantyDetail> queryByParams(ItemGuarantySearchParam itemGuarantySearchParam);

    IPage<ItemOrderSimpleVO> queryOrderByParams(ItemGuarantySearchParam itemGuarantySearchParam);
    IPage<Contract> queryContractByParams(ItemGuarantySearchParam itemGuarantySearchParam);


}
