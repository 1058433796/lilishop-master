package cn.lili.modules.item.entity.enums;

public enum SBState {
    SUCCESSED("successed", 0),
    FAILED("failed", 1);

    private String name;
    private int index;

    private SBState(String name, int index) {
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
