package cn.lili.modules.schemeComponent.service;

import cn.lili.modules.item.entity.Item;
import cn.lili.modules.item.entity.ItemSearchParams;
import cn.lili.modules.schemeComponent.entity.SchemeComponent;
import cn.lili.modules.schemeComponent.entity.SchemeComponentSearchParams;
import cn.lili.modules.schemeComponent.mapper.IdTotal;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SchemeComponentService extends IService<SchemeComponent> {
    /**
     * 方案零件查询
     *
     * @param schemeComponentSearchParams 查询参数
     * @return 分页
     */
    IPage<SchemeComponent> queryByParams(SchemeComponentSearchParams schemeComponentSearchParams);
    @Select("SELECT supplier_id FROM scheme_component WHERE scheme_id=#{schemeId} GROUP BY supplier_id;")
    List<String> getSchemeSuppliers(@Param("schemeId") String schemeId);
    @Select("SELECT * FROM scheme_component WHERE scheme_id=#{schemeId} AND storeId=#{storeId};")
    List<SchemeComponent> getSchemeComponentBySchemeIdAndStore(@Param("schemeId") String schemeId, @Param("storeId") String storeId);

    // 获取方案中包含每个供应商的id及其对应的总价
    @Select("SELECT supplier_id id, sum(component_unit_price*component_number) total FROM scheme_component WHERE scheme_id=#{schemeId} GROUP BY supplier_id")
    List<IdTotal> getOrderBy(@Param("schemeId") String schemeId);

    String getSchemeSumById(String schemeId);
}
