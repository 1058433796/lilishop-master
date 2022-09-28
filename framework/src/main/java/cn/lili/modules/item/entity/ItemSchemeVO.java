package cn.lili.modules.item.entity;

import cn.lili.modules.scheme.entity.Scheme;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ItemSchemeVO  extends ItemScheme{
    private List<Scheme> schemeList;
    public ItemSchemeVO(ItemScheme itemscheme) {
        BeanUtils.copyProperties(itemscheme, this);
    }
}
