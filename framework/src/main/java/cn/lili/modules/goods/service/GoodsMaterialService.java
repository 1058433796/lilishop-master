package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.GoodsMaterial;
import cn.lili.modules.goods.entity.enums.GoodsMaterialEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GoodsMaterialService extends IService<GoodsMaterial> {
    public void add(List<String> materialList, String id, GoodsMaterialEnum type);
}
