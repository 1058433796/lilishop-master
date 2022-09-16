package cn.lili.controller.passport;
import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.token.Token;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.company.entity.dos.Company;
import cn.lili.modules.company.entity.vos.CompanyVo;
import cn.lili.modules.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/company/passport")
public class CompanyPassportController {

    private final static String defaultPassword = "123456";
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
        Company company = new Company(companyVo.getCompanyType(), companyVo.getCompanyName(), companyVo.getLicenseRegisterNumber(),
                companyVo.getLicenseAddress(), companyVo.getLicenseDetailAddress(), null, null, companyVo.getLicenseValidLongPeriod(),
                companyVo.getLegalRepresentLicenseType(), companyVo.getLegalRepresentLicenseNumber(), companyVo.getLegalRepresentName(),
                null, null, companyVo.getLegalRepresentLicenseLongPeriod(), companyVo.getCompanyAddress(), companyVo.getCompanyDetailAddress(),
                companyVo.getCompanyEmergencyName(), companyVo.getCompanyEmergencyPhoneNumber(), companyVo.getOrgCode(),
                null, null, companyVo.getOrgCodeValidLongPeriod(), true, companyVo.getCompanyEmergencyPhoneNumber(), this.defaultPassword, ClientTypeEnum.PC.name(), new Date(), "face");
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
        Token token = companyService.register(company);
        if(token == null)return ResultUtil.error(ResultCode.USER_EXIST);
        else return ResultUtil.data(token);
    }
}
