package cn.lili.modules.goods.serviceimpl;

import cn.lili.modules.goods.entity.dos.GoodsGallery;
import cn.lili.modules.goods.entity.dos.GoodsMaterial;
import cn.lili.modules.goods.entity.enums.GoodsMaterialEnum;
import cn.lili.modules.goods.mapper.GoodsMaterialMapper;
import cn.lili.modules.goods.service.GoodsMaterialService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsMaterialServiceImpl
        extends ServiceImpl<GoodsMaterialMapper, GoodsMaterial>
        implements GoodsMaterialService {

    //    将材料文件和goodId绑定
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(List<GoodsMaterial> materialList, String id, GoodsMaterialEnum type) {
//        删除旧的记录
        this.baseMapper.delete(new QueryWrapper<GoodsMaterial>()
                .eq("goods_id", id));
//                .eq("type", type));

            for(GoodsMaterial material: materialList){
                material.setGoodsId(id);
                this.baseMapper.insert(material);
                System.out.println(material);
            }
    }

    @Override
    public List<GoodsMaterial> getList(String goodsId, GoodsMaterialEnum materialEnum) {
        QueryWrapper<GoodsMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        if(materialEnum != null) {
            queryWrapper.eq("type", materialEnum.name());
        }

        System.out.println("------------------------------------");
        System.out.println(goodsId);
        System.out.println(queryWrapper);
        System.out.println("------------------------------------");

        return this.list(queryWrapper);
    }
}
