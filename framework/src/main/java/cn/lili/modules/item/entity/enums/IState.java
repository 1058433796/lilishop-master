package cn.lili.modules.item.entity.enums;

public enum IState {
    NA("na", 0),
    TOTRANSFER("totransfer", 1),
    ROLLOUT("rollout", 2),
    ROLLIN("rollin", 3),
    CASHREFUND("cashrefund", 4),
    NETREFUNDING("netrefunding", 5),
    NETREFUNDED("netrefunded", 6);

    private String name;
    private int index;

    private IState(String name, int index) {
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

    public String toString() {
        String all = ",";

        for(int i = 0; i < values().length; ++i) {
            all = all + values()[i].getName() + ",";
        }

        return all;
    }
}
