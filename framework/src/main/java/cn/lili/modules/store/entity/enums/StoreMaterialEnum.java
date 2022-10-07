package cn.lili.modules.store.entity.enums;

public enum StoreMaterialEnum {

    BUSINESS_LICENSE("营业执照"),
    LEGAL_LICENSE("法人证件"),
    BANK_OPEN_LICENSE("银行开户许可证"),
    ORG_CODE_LICENSE("组织机构代码证");
    private final String description;

    StoreMaterialEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
