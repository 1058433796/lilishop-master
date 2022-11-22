package cn.lili.modules.statistics.mapper;

import cn.lili.modules.order.order.entity.dos.StoreFlow;
import cn.lili.modules.statistics.entity.vo.CategoryStatisticsDataVO;
import cn.lili.modules.statistics.entity.vo.GoodsStatisticsDataVO;
import cn.lili.modules.statistics.entity.vo.StoreStatisticsDataVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品统计数据处理层
 *
 * @author Bulbasaur
 * @since 2020/11/17 7:34 下午
 */
public interface StoreFlowStatisticsMapper extends BaseMapper<StoreFlow> {
    /**
     * 商品统计
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 商品统计列表
     */
    @Select("select pm as goods_name, SUM(component_unit_price * component_number) as price, SUM(component_number) as num\n" +
            "from item_order io inner join scheme_component sc  on io.order_id = sc.order_id ${ew.customSqlSegment}")
    List<GoodsStatisticsDataVO> getGoodsStatisticsData(IPage<GoodsStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsStatisticsDataVO> queryWrapper);
    /**
     * 产品统计
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 商品统计列表
     */
    @Select("SELECT component_id as goods_id,pm as goods_name,SUM(component_unit_price) AS price,SUM(component_number) AS num FROM scheme_component ${ew.customSqlSegment}")
    List<GoodsStatisticsDataVO> getGoodsStatisticsDataTop(IPage<GoodsStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsStatisticsDataVO> queryWrapper);
    /**
     * 分类统计
     *
     * @param queryWrapper 查询条件
     * @return 分类统计列表
     */
    @Select("SELECT category_id,category_name,SUM(price) AS price,SUM(num) AS num FROM li_store_flow ${ew.customSqlSegment}")
    List<CategoryStatisticsDataVO> getCateGoryStatisticsData(@Param(Constants.WRAPPER) Wrapper<CategoryStatisticsDataVO> queryWrapper);



    /**
     * 店铺统计列表
     *
     * @param page         分页
     * @param queryWrapper 查询参数
     * @return 店铺统计列表
     */
    @Select("SELECT store_id AS storeId,store_name AS storeName,SUM(final_price) AS price,SUM(num) AS num FROM li_store_flow ${ew.customSqlSegment}")
    List<StoreStatisticsDataVO> getStoreStatisticsData(IPage<GoodsStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsStatisticsDataVO> queryWrapper);

}