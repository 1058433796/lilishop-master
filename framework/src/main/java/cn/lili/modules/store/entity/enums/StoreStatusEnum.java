package cn.lili.modules.store.entity.enums;

/**
 * 店铺状态枚举
 *
 * @author pikachu
 * @since 2020年3月07日 上午11:04:25
 */
public enum StoreStatusEnum {
    /**
     * 开启中
     */
    OPEN("开启中"),
    /**
     * 店铺关闭
     */
    CLOSED("店铺关闭"),
    /**
     * 尚未完成第一步申请
     */
    APPLY_FIRST_STEP("正在申请第一步"),
    /**
     * 完成第一步申请，尚未完成第二步申请
     */
    APPLY_SECOND_STEP("正在申请第二步"),
    /**
     * 审核拒绝
     */
    REFUSED("审核拒绝"),
    /**
     * 已经完成申请，资料审核中
     */
    APPLYING("已申请，正在提交审核");

    private final String description;

    StoreStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
