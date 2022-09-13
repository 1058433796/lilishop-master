package cn.lili.common.security;

import cn.lili.common.security.enums.UserEnums;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthCompany implements Serializable {

    private static final long serialVersionUID = 582441893336003319L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String face;

    /**
     * id
     */
    private String id;

    /**
     * 长期有效（用于手机app登录场景或者信任场景等）
     */
    private Boolean longTerm = false;

    /**
     * @see UserEnums
     * 角色
     */
    private UserEnums role;

    /**
     * 是否是超级管理员
     */
    private Boolean isSuper = false;

    public AuthCompany(String username, String id, String face, UserEnums role) {
        this.username = username;
        this.face = face;
        this.id = id;
        this.role = role;
    }
}
