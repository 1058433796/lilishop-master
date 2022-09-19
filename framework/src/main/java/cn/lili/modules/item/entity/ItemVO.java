package cn.lili.modules.item.entity;

import cn.lili.modules.promotion.entity.dos.Pintuan;
import cn.lili.modules.promotion.entity.dos.PromotionGoods;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ItemVO extends Item{
    private List<Item> itemList;
    public ItemVO(Item item) {
        BeanUtils.copyProperties(item, this);
    }
}

