package cn.lili.modules.company.entity.vos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CompanyVo {

    //所属行业
    private String companyType;
    // 企业名称
    private String companyName;
    // 营业执照注册号
    private String licenseRegisterNumber;
    // 营业执照所在地
    private String licenseAddress;
    // 营业执照详细地址
    private String licenseDetailAddress;
    // 营业期限
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date licenseValidBeg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date licenseValidEnd;
    // 营业期限 长期
    private Boolean licenseValidLongPeriod;
    // 法人代表证件类型
    private String legalRepresentLicenseType;
    // 法人代表证件号
    private String legalRepresentLicenseNumber;
    // 法人代表名称
    private String legalRepresentName;
    // 法人代表信息有效期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date legalRepresentLicenseValidBeg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date legalRepresentLicenseValidEnd;
    // 法人代表信息有效期 长期
    private Boolean legalRepresentLicenseLongPeriod;
    // 公司所在地
    private String companyAddress;
    // 公司详细地址
    private String companyDetailAddress;
    // 公司紧急联系人
    private String companyEmergencyName;
    // 公司紧急联系人电话
    private String companyEmergencyPhoneNumber;
    // 组织机构代码
    private String orgCode;
    // 组织机构代码有效期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orgCodeValidBeg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orgCodeValidEnd;
    // 组织机构代码有效期 长期
    private Boolean orgCodeValidLongPeriod;
}

