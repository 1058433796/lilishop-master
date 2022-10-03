package cn.lili.modules.goods.entity.enums;

public enum GoodsMaterialEnum {
    Model("模型文件"),
    MATERIAL("材料文件"),

    REPORT("报告文件"),

    INSTRUCTION("说明文件");

    private String description;
    GoodsMaterialEnum(String desc){
        description = desc;
    }
    public String description() {
        return description;
    }
}
