package cn.lili.modules.item.entity;

import lombok.Data;

import java.util.List;
@Data
public class LoginItem {
    private String name;

    private String role;

    private List<ShortItem> projectInfo;

}
