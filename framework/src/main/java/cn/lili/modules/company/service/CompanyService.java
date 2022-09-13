package cn.lili.modules.company.service;

import cn.lili.common.security.token.Token;
import cn.lili.modules.company.entity.dos.Company;
import cn.lili.modules.member.entity.dos.Member;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CompanyService extends IService<Company> {


    /**
     * 测试方法
     * @return
     */
    List<Company> getAllCompany();

    /**
     * 登录：用户名、密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    Token usernameLogin(String username, String password);

    /**
     * 用户名
     *
     * @param userName 用户名
     * @return company信息
     */
     Company findCompany(String userName);

    /**
     * 注册：用户名、密码登录
     *
     * @param company
     * @return 是否成功
     */
    Token register(Company company);

    /**
     * 注册逻辑
     * @param company
     */
    public void registerHandler(Company company);
}
