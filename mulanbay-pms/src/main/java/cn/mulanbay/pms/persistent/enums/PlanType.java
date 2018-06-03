package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum PlanType {

    DAY(0,"天"),WEEK(1,"周"),MONTH(2,"月"),SEASON(3,"季度"),YEAR(4,"年");

    private int value;

    private String name;

    PlanType(int value, String name) {
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