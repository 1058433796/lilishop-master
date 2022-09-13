package cn.lili.controller.passport;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.company.entity.dos.Company;
import cn.lili.modules.company.entity.vos.CompanyVo;
import cn.lili.modules.company.service.CompanyService;
import cn.lili.modules.verification.entity.enums.VerificationEnums;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.xml.transform.Result;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/company/passport")
public class CompanyPassportController {

    private String defaultPassword = "123456";
    @Autowired
    private CompanyService companyService;

    @GetMapping("/companyList")
    public ResultMessage<Object> getCompanyList(){
        List<Company> companyList = this.companyService.getAllCompany();
        return ResultUtil.data(companyList);
    }

    @PostMapping("/login")
    public ResultMessage<Object> login(@NotNull(message = "用户名不能为空") @RequestParam String username,
                                           @NotNull(message = "密码不能为空") @RequestParam String password) {
        return ResultUtil.data(this.companyService.usernameLogin(username, password));
    }

    @PostMapping(value="/register")
    public ResultMessage<Object> register(CompanyVo companyVo) {
        System.out.println(companyVo);

        Company company = new Company(companyVo.getCompanyType(), companyVo.getCompanyName(), companyVo.getLicenseRegisterNumber(),
                "", companyVo.getLicenseDetailAddress(), null, null, companyVo.getLicenseValidLongPeriod(),
                companyVo.getLegalRepresentLicenseType(), companyVo.getLegalRepresentLicenseNumber(), companyVo.getLegalRepresentName(),
                null, null, companyVo.getLegalRepresentLicenseLongPeriod(), "", companyVo.getCompanyDetailAddress(),
                companyVo.getCompanyEmergencyName(), companyVo.getCompanyEmergencyPhoneNumber(), companyVo.getOrgCode(),
                null, null, companyVo.getOrgCodeValidLongPeriod(), true, companyVo.getCompanyEmergencyPhoneNumber(), this.defaultPassword, ClientTypeEnum.PC.value(), new Date(), "face");
//      如果选择长期 不设置期限
        if(companyVo.getLicenseValidLongPeriod()){
            company.setBusinessLongPeriod(true);
        }else{
            company.setBusinessValidBeg(companyVo.getLicenseValidBeg());
            company.setBusinessValidEnd(companyVo.getLicenseValidEnd());
        }

        if(companyVo.getLegalRepresentLicenseLongPeriod()){
            company.setLegalLongPeriod(true);
        }else{
            company.setLegalValidBeg(companyVo.getLegalRepresentLicenseValidBeg());
            company.setLegalValidEnd(companyVo.getLegalRepresentLicenseValidEnd());
        }

        if(companyVo.getOrgCodeValidLongPeriod()){
            company.setCodeLongPeriod(true);
        }else{
            company.setCodeValidBeg(companyVo.getOrgCodeValidBeg());
            company.setCodeValidEnd(companyVo.getOrgCodeValidEnd());
        }
        return ResultUtil.data(companyService.register(company));
//        return ResultUtil.data(companyService.register(username, password, mobilePhone));
//        if (smsUtil.verifyCode(mobilePhone, VerificationEnums.REGISTER, uuid, code)) {
//            return ResultUtil.data(memberService.register(username, password, mobilePhone));
//        } else {
//            throw new ServiceException(ResultCode.VERIFICATION_SMS_CHECKED_ERROR);
//        }
    }
}
