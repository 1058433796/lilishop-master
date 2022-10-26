package cn.lili.modules.store.entity.vos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompanySecondVo {
//    营业执照
    private List<String> busLicPhotoList;
//    法人证件
    private List<String> legalLicPhotoList;
//    银行开户许可证
    private List<String> bankLicPhotoList;
//    组织机构代码证
    private List<String> orgCodeLicPhotosList;

    @NotNull
    private String username;
    @NotNull
    private String password;

    public CompanySecondVo(String username, String password){
        this.username = username;
        this.password = password;
        busLicPhotoList = new ArrayList<>();
        legalLicPhotoList = new ArrayList<>();
        bankLicPhotoList = new ArrayList<>();
        orgCodeLicPhotosList = new ArrayList<>();
    }

}
