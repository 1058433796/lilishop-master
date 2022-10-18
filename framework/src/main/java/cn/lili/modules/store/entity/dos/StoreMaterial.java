package cn.lili.modules.store.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储store相关的文件如电子证件
 */
@Data
@TableName("li_store_material")
@AllArgsConstructor
@NoArgsConstructor
public class StoreMaterial extends BaseEntity {
    //    所属商品
    private String storeId;
    //    文件地址
    private String url;
    /**
     * 文件类型：材料或者模型
     * @see cn.lili.modules.store.entity.enums.StoreMaterialEnum
     */
    private String type;
}
