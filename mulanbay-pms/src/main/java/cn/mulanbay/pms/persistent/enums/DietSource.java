package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum DietSource {

    SELF_MADE(0,"自己做"),RESTAURANT(1,"餐馆"),TAKE_OUT(2,"外卖"),OTHER(3,"其他");

    private int value;

    private String name;

    DietSource(int value, String name) {
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