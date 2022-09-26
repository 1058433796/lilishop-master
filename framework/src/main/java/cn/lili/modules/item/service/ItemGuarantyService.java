package cn.lili.modules.item.service;


import cn.lili.modules.item.entity.ItemGuaranty;
import com.baomidou.mybatisplus.extension.service.IService;
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

}
