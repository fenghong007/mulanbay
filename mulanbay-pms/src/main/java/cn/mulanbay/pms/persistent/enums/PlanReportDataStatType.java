package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 *
 */
public enum PlanReportDataStatType {

    ORIGINAL(0,"原始数据"),PERCENT(1,"百分比");

    private int value;

    private String name;

    PlanReportDataStatType(int value, String name) {
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