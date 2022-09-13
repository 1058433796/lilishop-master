package cn.lili.modules.company.entity.dos;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("company")
public class Company extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String companyType;
    private String companyName;
    private String businessNumber;
    private String businessAddress;
    private String businessDetailAddress;
    private Date businessValidBeg;
    private Date businessValidEnd;
    private Boolean businessLongPeriod;
    private String legalType;
    private String legalNumber;
    private String legalName;
    private Date legalValidBeg;
    private Date legalValidEnd;
    private Boolean legalLongPeriod;
    private String companyAddress;
    private String companyDetailAddress;
    private String companyEmergencyName;
    private String companyEmergencyPhoneNumber;
    private String code;
    private Date codeValidBeg;
    private Date codeValidEnd;
    private Boolean codeLongPeriod;
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

//    public Company(String companyType, String companyName, String businessNumber, String businessAddress,
//                   Boolean businessLongPeriod, String legalType, String legalNumber, String legalName,
//                   Boolean legalLongPeriod, String companyDetailAddress, String companyEmergencyName,
//                   String companyEmergencyPhoneNumber, String code, Boolean codeLongPeriod){
//        this.companyType = companyType;
//        this.companyName = companyName;
//        this.businessNumber = businessNumber;
//        this.businessAddress = businessAddress;
//        this.businessLongPeriod = businessLongPeriod;
//        this.legalType
////        PC端
//        this.clientEnum = ClientTypeEnum.PC.value();
//        this.face = "";
//        this.accountValid = true;
//    }
}
