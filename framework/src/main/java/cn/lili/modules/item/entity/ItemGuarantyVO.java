package cn.lili.modules.item.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ItemGuarantyVO extends ItemGuaranty{
    private List<ItemGuaranty> itemGuarantyList;
    public ItemGuarantyVO(ItemGuaranty itemGuaranty){
        BeanUtils.copyProperties(itemGuaranty, this);}
}
