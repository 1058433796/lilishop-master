package cn.lili.modules.item.entity.enums;

public enum OState {
    CREATED("created", 0),
    INITIALIZED("initialized", 1),
    REBIRTHED("rebirthed", 3),
    ABORTED("aborted", 4),
    NETPAYSTARTED("netpaystarted", 5);

    private String name;
    private int index;

    private OState(String name, int index) {
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
