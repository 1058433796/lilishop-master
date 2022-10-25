package cn.lili.modules.member.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreUserUpdateDTO {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "姓名")
    private String nickName;

    @ApiModelProperty(value = "会员地址ID")
    private String regionId;

    @ApiModelProperty(value = "会员地址")
    private String region;


    @ApiModelProperty(value = "会员微信")
    private String wechat;

    @ApiModelProperty(value = "会员邮箱")
    private String email;
}
