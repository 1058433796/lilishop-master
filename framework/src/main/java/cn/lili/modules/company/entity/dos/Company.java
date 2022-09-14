package cn.lili.modules.company.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("company")
public class Company extends BaseEntity {
    private static final long serialVersionUID = 1L;
//    营业执照信息
    private String companyType;
    private String companyName;
    private String businessNumber;
    private String businessAddress;
    private String businessDetailAddress;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date businessValidBeg;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date businessValidEnd;
    private Boolean businessLongPeriod;
//    法人代表信息
    private String legalType;
    private String legalNumber;
    private String legalName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date legalValidBeg;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date legalValidEnd;
    private Boolean legalLongPeriod;
    private String companyAddress;
    private String companyDetailAddress;
    private String companyEmergencyName;
    private String companyEmergencyPhoneNumber;
//    组织机构信息
    private String code;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date codeValidBeg;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date codeValidEnd;
    private Boolean codeLongPeriod;
//    账户信息
    private Boolean accountValid;
    private String username;
    private String password;

//    @ApiModelProperty(value = "客户端")
    private String clientEnum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(value = "最后一次登录时间")
    private Date lastLoginDate;
//    头像
    private String face;

}
