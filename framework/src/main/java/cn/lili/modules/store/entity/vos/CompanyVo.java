package cn.lili.modules.store.entity.vos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CompanyVo {

    //所属行业
    @NotEmpty
    private String companyType;
    // 企业名称
    @NotEmpty
    private String companyName;
    // 营业执照注册号
    @NotEmpty
    private String businessNumber;
    // 营业执照所在地
    @NotEmpty
    private String businessAddress;
    // 营业执照详细地址
    @NotEmpty
    private String businessDetailAddress;
    // 营业期限
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date businessValidBeg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date businessValidEnd;
    // 营业期限 长期

    private Boolean businessLongPeriod;
    // 法人代表证件类型
    @NotEmpty
    private String legalType;
    // 法人代表证件号
    @NotEmpty
    private String legalNumber;
    // 法人代表名称
    @NotEmpty
    private String legalName;
    // 法人代表信息有效期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date legalValidBeg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date legalValidEnd;
    // 法人代表信息有效期 长期

    private Boolean legalLongPeriod;
    // 公司所在地
    @NotEmpty
    private String companyAddress;
    // 公司详细地址
    @NotEmpty
    private String companyDetailAddress;
    // 公司紧急联系人
    @NotEmpty
    private String companyEmergencyName;
    // 公司紧急联系人电话
    @NotEmpty
    private String companyEmergencyPhoneNumber;
    // 组织机构代码
    @NotEmpty
    private String code;
    // 组织机构代码有效期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date codeValidBeg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date codeValidEnd;
    // 组织机构代码有效期 长期

    private Boolean codeLongPeriod;
    @NotNull
    private String username;
    @NotNull
    private String password;

    public CompanyVo(String username, String password){
        this.username = username;
        this.password = password;

        companyType = "";
        companyName = "";
        businessNumber = "";
        businessAddress = "";
        businessDetailAddress = "";
        businessLongPeriod = true;
        legalType = "";
        legalNumber = "";
        legalName = "";
        legalLongPeriod = true;
        companyAddress = "";
        companyDetailAddress = "";
        companyEmergencyName = "";
        companyEmergencyPhoneNumber = "";
        code = "";
        codeLongPeriod = true;

    }

}

