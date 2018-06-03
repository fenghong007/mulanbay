package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2017/7/9.
 * 目前enum的映射采用默认的orinal（序列号），因此类中所列的枚举值不能改变顺序
 */
public enum SqlType{
    SQL(0,"sql"),
    HQL(1,"hql");
    private int value;
    private String name;

    SqlType(int value, String name) {
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
