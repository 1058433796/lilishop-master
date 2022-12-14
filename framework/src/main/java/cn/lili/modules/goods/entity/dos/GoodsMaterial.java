package cn.lili.modules.goods.entity.dos;


import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@TableName("li_goods_material")
@Data
public class GoodsMaterial extends BaseEntity {
//    所属商品
    private String goodsId;
//    文件地址
    private String url;
/**
 * 文件类型：材料或者模型
 * @see cn.lili.modules.goods.entity.enums.GoodsMaterialEnum
  */
    private String type;

    public GoodsMaterial(String goodsId, String url, String type){
        this.goodsId = goodsId;
        this.url = url;
        this.type = type;
    }
}
