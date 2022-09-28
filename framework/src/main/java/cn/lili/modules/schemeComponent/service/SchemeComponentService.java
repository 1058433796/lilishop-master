package cn.lili.modules.schemeComponent.service;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.entity.SchemeComponentSearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface SchemeComponentService extends IService<SchemeComponent> {
    /**
     * 方案零件查询
     *
     * @param schemeComponentSearchParams 查询参数
     * @return 分页
     */
    IPage<SchemeComponent> queryByParams(SchemeComponentSearchParams schemeComponentSearchParams);
}
