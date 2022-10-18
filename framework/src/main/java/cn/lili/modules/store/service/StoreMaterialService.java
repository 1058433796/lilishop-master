package cn.lili.modules.store.service;

import cn.lili.modules.store.entity.dos.StoreMaterial;
import cn.lili.modules.store.entity.enums.StoreMaterialEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StoreMaterialService extends IService<StoreMaterial> {
    void add(List<String> busLicPhotoList, String id, StoreMaterialEnum businessLicense);
}
