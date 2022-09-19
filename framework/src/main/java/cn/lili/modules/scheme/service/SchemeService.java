package cn.lili.modules.scheme.service;
import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.scheme.entity.Scheme;
import cn.lili.modules.scheme.entity.SchemeSearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SchemeService extends IService<Scheme> {
    /**
     * 所有方案
     *
     * @param schemeSearchParams 查询参数
     * @return 方案分页
     */
    IPage<Scheme> queryByParams(SchemeSearchParams schemeSearchParams);

    /**
     * 所有方案
     *
     *
     * @return 所有方案列表
     */
    List<Scheme> getSchemeList();


}
