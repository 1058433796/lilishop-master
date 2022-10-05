package cn.lili.modules.store.serviceimpl;

import cn.lili.modules.goods.entity.dos.GoodsMaterial;
import cn.lili.modules.store.entity.dos.StoreMaterial;
import cn.lili.modules.store.entity.enums.StoreMaterialEnum;
import cn.lili.modules.store.mapper.StoreMaterialMapper;
import cn.lili.modules.store.service.StoreMaterialService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreMaterialServiceImpl extends ServiceImpl<StoreMaterialMapper, StoreMaterial> implements StoreMaterialService {
    @Override
    public void add(List<String> busLicPhotoList, String id, StoreMaterialEnum materialEnum) {
        //        删除旧的记录
        this.remove(new QueryWrapper<StoreMaterial>().eq("store_id", id));
        for(String url:busLicPhotoList){
            StoreMaterial storeMaterial = new StoreMaterial(id, url, materialEnum.name());
            this.save(storeMaterial);
        }
    }
}
