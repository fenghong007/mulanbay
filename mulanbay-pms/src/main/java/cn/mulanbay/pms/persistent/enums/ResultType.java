package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2017/7/9.
 * 目前enum的映射采用默认的orinal（序列号），因此类中所列的枚举值不能改变顺序
 */
public enum ResultType{
    DATE(0,"日期"),
    NUMBER(1,"数字"),
    NAME_DATE(2,"名称-日期"),
    NAME_NUMBER(3,"名称-数字");
    private int value;
    private String name;

    ResultType(int value, String name) {
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

