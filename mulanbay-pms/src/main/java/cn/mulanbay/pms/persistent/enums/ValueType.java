package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2017/7/9.
 * 目前enum的映射采用默认的orinal（序列号），因此类中所列的枚举值不能改变顺序
 */
public enum ValueType{
    DAY(0,"天"),
    HOUR(1,"小时"),
    MINUTE(2,"分钟"),
    NUMBER(3,"个"),
    PERCENT(4,"%"),
    KILOMETRES(5,"公里"),
    TIMES(6,"次"),
    MONEY(7,"元"),
    BOOK(8,"本"),
    KIND(9,"种");
    private int value;
    private String name;

    ValueType(int value, String name) {
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