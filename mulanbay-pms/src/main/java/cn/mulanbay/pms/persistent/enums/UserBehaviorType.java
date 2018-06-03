package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum UserBehaviorType {

    MUSIC(0,"音乐"),SPORT(1,"运动"),BUY(2,"消费"),HEALTH(3,"健康"),READ(4,"阅读"),LIFE(5,"人生经历"),LOG(6,"日志"),WORK(7,"工作"),COMMON(8,"通用");

    private int value;

    private String name;

    UserBehaviorType(int value, String name) {
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