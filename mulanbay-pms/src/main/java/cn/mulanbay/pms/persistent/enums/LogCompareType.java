package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 比对类型
 */
public enum LogCompareType {

    EARLY(0,"早于"),LATER(1,"晚于");

    private int value;

    private String name;

    LogCompareType(int value, String name) {
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