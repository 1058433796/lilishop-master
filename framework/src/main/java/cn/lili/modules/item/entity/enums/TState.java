package cn.lili.modules.item.entity.enums;

public enum TState {
    NA("na", 0),
    SUCCESSED("successed", 2),
    FAILED("failed", 3);

    private String name;
    private int index;

    private TState(String name, int index) {
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
