package cn.lili.modules.item.entity.enums;

public enum CState {
    NA("na", 0),
    WAITING("waiting", 1),
    SUCCESSED("successed", 2),
    FAILED("failed", 3);

    private String name;
    private int index;

    private CState(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
