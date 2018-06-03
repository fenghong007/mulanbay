package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum FoodType {

    RICE(0,"米饭"),NOODLE(1,"面食"),OTHER(2,"其他");

    private int value;

    private String name;

    FoodType(int value, String name) {
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