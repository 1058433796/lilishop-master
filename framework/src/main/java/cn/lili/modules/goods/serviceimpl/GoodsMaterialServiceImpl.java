package cn.lili.modules.goods.serviceimpl;

import cn.lili.modules.goods.entity.dos.GoodsMaterial;
import cn.lili.modules.goods.entity.enums.GoodsMaterialEnum;
import cn.lili.modules.goods.mapper.GoodsMaterialMapper;
import cn.lili.modules.goods.service.GoodsMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsMaterialServiceImpl
        extends ServiceImpl<GoodsMaterialMapper, GoodsMaterial>
        implements GoodsMaterialService {

    //    将材料文件和goodId绑定
    @Override
    public void add(List<String> materialList, String id, GoodsMaterialEnum type) {
            for(String url: materialList){
                GoodsMaterial material = new GoodsMaterial(id, url, type.name());
                this.save(material);
            }
    }
}
