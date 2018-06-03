package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2017/1/14.
 */
public enum ExperienceType {

    LIVE(0,"生活"),WORK(1,"工作"),TRAVEL(2,"旅行"),STUDY(3,"读书");
    private int value;

    private String name;

    ExperienceType(int value, String name) {
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
