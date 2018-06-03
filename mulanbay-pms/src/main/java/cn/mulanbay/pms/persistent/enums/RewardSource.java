package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum RewardSource {

    OPERATION(0,"操作"),NOTIFY(1,"提醒"),PLAN(2,"计划");

    private int value;

    private String name;

    RewardSource(int value, String name) {
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

    public static RewardSource getRewardSource(int value){
        for(RewardSource rs : RewardSource.values()){
            if(rs.getValue()==value){
                return rs;
            }
        }
        return null;
    }
}