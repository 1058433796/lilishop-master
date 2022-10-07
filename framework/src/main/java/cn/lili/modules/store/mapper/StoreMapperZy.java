package cn.lili.modules.store.mapper;

import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.vos.CustomerStoreVO;
import cn.lili.modules.store.entity.vos.StoreVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 店铺数据处理层
 *
 * @author pikachu
 * @since2020-03-07 09:18:56
 */
public interface StoreMapperZy extends BaseMapper<Store> {

    /**
     * 获取店铺详细
     *
     * @param id 店铺ID
     * @return 店铺VO
     */
    @Select("select s.*,d.* from li_store s inner join li_store_detail d on s.id=d.store_id where s.id=#{id} ")
    StoreVO getStoreDetail(String id);

    /**
     * 获取店铺分页列表
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 店铺VO分页列表
     */
    @Select("select s.* from li_store as s ${ew.customSqlSegment}")
    IPage<StoreVO> getStoreList(IPage<StoreVO> page, @Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);


    /**
     * 修改店铺收藏数据
     *
     * @param storeId 店铺id
     * @param num     收藏数量
     */
    @Update("update li_store set collection_num = collection_num + #{num} where id = #{storeId}")
    void updateCollection(String storeId, Integer num);

//    @Select("SELECT * FROM " +
//            "(SELECT id,store_name as buyer_name,store_address_detail,store_address_id_path,store_address_path FROM li_store)AS s " +
//            "NATURAL JOIN " +
//            "(SELECT buyer_id AS id,store_id,buyer_phone,MAX(create_time) AS latestTime,SUM(order_amount) AS tradeAmount FROM item_order GROUP BY buyer_id,store_id,buyer_phone) AS o ${ew.customSqlSegment}"
//    )

//    @Select("SELECT * FROM " +
//            "(SELECT id, store_name as buyer_name, store_address_detail,store_address_id_path, store_address_path FROM li_store)AS s " +
//            "NATURAL JOIN " +
//            "(SELECT store_id AS id, buyer_id, MAX(create_time) AS latestTime,SUM(order_amount) AS tradeAmount FROM item_order GROUP BY buyer_id, store_id) AS o ${ew.customSqlSegment}"
//    )
@Select("SELECT * FROM " +
        "(SELECT id, store_name as buyer_name, store_address_detail,store_address_id_path, store_address_path FROM li_store)AS s " +
        "NATURAL JOIN " +
        "(SELECT store_id AS id, buyer_id, MAX(create_time) AS latestTime,SUM(order_amount) AS tradeAmount FROM item_order GROUP BY buyer_id, store_id) AS o " +
        " NATURAL JOIN " +
        "(SELECT store_id AS id, sales_consignee_mobile as buyer_phone FROM li_store_detail) AS sd "+
        "${ew.customSqlSegment}"
)
    IPage<CustomerStoreVO> queryByParams(IPage<CustomerStoreVO> page, @Param(Constants.WRAPPER) Wrapper<CustomerStoreVO> queryWrapper);
//    @Select("SELECT * FROM " +
//            "((SELECT id, store_name as buyer_name, store_address_detail ,store_address_id_path, store_address_path FROM li_store)AS s " +
//            "NATURAL JOIN " +
//            "(SELECT store_id AS id, buyer_id, MAX(create_time) AS latestTime,SUM(order_amount) AS tradeAmount FROM item_order GROUP BY buyer_id, store_id) As o)" +
//            "NATURAL JOIN (SELECT id, sales_consignee_mobile as buyer_phone FROM li_store_detail) AS sd"  +
//            " AS o ${ew.customSqlSegment}"
//    )
}