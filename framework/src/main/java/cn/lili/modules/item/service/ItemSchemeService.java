package cn.lili.modules.item.service;

import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.ItemScheme;
import cn.lili.modules.item.entity.ItemSchemeSearchParams;
import cn.lili.modules.scheme.entity.Scheme;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ItemSchemeService extends IService<ItemScheme> {
    /**
     * 为项目添加方案
     */
    boolean saveItemScheme(ItemScheme itemScheme);

    IPage<ItemScheme> queryByParams(ItemSchemeSearchParams itemschemeSearchParams);

    /**
     * 确认项目方案
     * @param id    项目方案编号 primary_id
     */
    void checkItemScheme(String id);
}
