package cn.lili.modules.company.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.event.TransactionCommitSendMQEvent;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.properties.RocketmqCustomProperties;
import cn.lili.common.security.token.Token;
import cn.lili.common.utils.SnowFlake;
import cn.lili.modules.company.entity.dos.Company;
import cn.lili.modules.company.entity.token.CompanyTokenGenerate;
import cn.lili.modules.company.mapper.CompanyMapper;
import cn.lili.modules.company.service.CompanyService;
import cn.lili.rocketmq.tags.MemberTagsEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Autowired
    private CompanyTokenGenerate companyTokenGenerate;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<Company> getAllCompany() {
        List<Company> companyList = this.baseMapper.getAllCompany();
        return companyList;
    }

    @Override
    public Token usernameLogin(String username, String password) {
        Company company = this.findCompany(username);
        if(company == null || !company.getAccountValid()){
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
//        //判断密码是否输入正确
        if (!new BCryptPasswordEncoder().matches(password, company.getPassword())) {
            throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
        }
        return companyTokenGenerate.createToken(company, false);
    }

    /**
     * 根据用户名寻找是否已存在公司
     * @param username 手机号
     * @return 查找到的company
     */
    @Override
    public Company findCompany(String username) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
//        queryWrapper.eq("username", userName).or().eq("mobile", userName);
        return this.baseMapper.selectOne(queryWrapper);
    }

    public Long getCompanyCount(String username){
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
//        queryWrapper.eq("username", userName).or().eq("mobile", userName);
        return this.baseMapper.selectCount(queryWrapper);
    }

    @Override
    public void registerHandler(Company company) {
        company.setId(SnowFlake.getIdStr());
        this.save(company);
        applicationEventPublisher.publishEvent(new TransactionCommitSendMQEvent("new company register", rocketmqCustomProperties.getMemberTopic(), MemberTagsEnum.COMPANY_REGISTER.name(), company));
    }

    @Override
    public Token register(Company company) {
//        检查是否合法
        if(!checkCompany(company.getUsername())){
            return null;
        }
//        注册后自动登录
        registerHandler(company);
//        生成token
        return companyTokenGenerate.createToken(company, false);
    }

    /**
     * 根据username检查是否合规
     * @param username 用户名
     * @return 是否合法
     */

    private Boolean checkCompany(String username) {
        return this.getCompanyCount(username) <= 0;
    }
}
