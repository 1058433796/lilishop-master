package cn.lili.modules.company.entity.token;

import cn.lili.common.context.ThreadContextHolder;
import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.properties.RocketmqCustomProperties;
import cn.lili.common.security.AuthCompany;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.security.token.Token;
import cn.lili.common.security.token.TokenUtil;
import cn.lili.common.security.token.base.AbstractTokenGenerate;
import cn.lili.modules.company.entity.dos.Company;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.rocketmq.RocketmqSendCallbackBuilder;
import cn.lili.rocketmq.tags.MemberTagsEnum;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CompanyTokenGenerate extends AbstractTokenGenerate<Company> {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Token createToken(Company company, Boolean longTerm) {
//        return null;
        //获取客户端类型
        String clientType = ThreadContextHolder.getHttpRequest().getHeader("clientType");
        ClientTypeEnum clientTypeEnum;
        try {
            //如果客户端为空，则缺省值为PC，pc第三方登录时不会传递此参数
            if (clientType == null) {
                clientTypeEnum = ClientTypeEnum.PC;
            } else {
                clientTypeEnum = ClientTypeEnum.valueOf(clientType);
            }
        } catch (IllegalArgumentException e) {
            clientTypeEnum = ClientTypeEnum.UNKNOWN;
        }
        //记录最后登录时间，客户端类型
        company.setLastLoginDate(new Date());
        company.setClientEnum(clientTypeEnum.name());
        String destination = rocketmqCustomProperties.getMemberTopic() + ":" + MemberTagsEnum.MEMBER_LOGIN.name();
        rocketMQTemplate.asyncSend(destination, company, RocketmqSendCallbackBuilder.commonCallback());

        AuthCompany authCompany = new AuthCompany(company.getUsername(), company.getId(), company.getFace(), UserEnums.COMPANY);
        //登陆成功生成token
        return tokenUtil.createToken(company.getUsername(), authCompany, longTerm, UserEnums.COMPANY);
    }

    @Override
    public Token refreshToken(String refreshToken) {
        return tokenUtil.refreshToken(refreshToken, UserEnums.COMPANY);
    }
}
