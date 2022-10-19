package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.GoodsGallery;
import cn.lili.modules.goods.entity.dos.GoodsMaterial;
import cn.lili.modules.goods.entity.enums.GoodsMaterialEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GoodsMaterialService extends IService<GoodsMaterial> {
    public void add(List<GoodsMaterial> materialList, String id, GoodsMaterialEnum type);

    List<GoodsMaterial> getList(String goodsId, GoodsMaterialEnum materialEnum);
}
