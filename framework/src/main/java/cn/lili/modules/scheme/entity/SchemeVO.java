package cn.lili.modules.scheme.entity;

import cn.lili.modules.item.entity.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SchemeVO extends Scheme{
    private List<Scheme> schemeList;
    public SchemeVO(Scheme scheme) {
        BeanUtils.copyProperties(scheme, this);
    }
}
