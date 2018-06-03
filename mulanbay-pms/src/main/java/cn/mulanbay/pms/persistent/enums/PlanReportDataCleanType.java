package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum PlanReportDataCleanType {

    ALL(0,"所有"),BOTH_ZERO(1,"全部为零"),ONCE_ZERO(2,"其中有一个为零");

    private int value;

    private String name;

    PlanReportDataCleanType(int value, String name) {
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