package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 资金流向
 */
public enum MoneyFlow {

    BUY(0,"买"),SALE(1,"卖");

    private int value;

    private String name;

    MoneyFlow(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}