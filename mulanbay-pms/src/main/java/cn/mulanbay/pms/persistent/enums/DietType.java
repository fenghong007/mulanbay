package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum DietType {

    BREAKFAST(0,"早餐"),LUNCH(1,"午餐"),DINNER(2,"晚餐"),OTHER(3,"其他");

    private int value;

    private String name;

    DietType(int value, String name) {
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