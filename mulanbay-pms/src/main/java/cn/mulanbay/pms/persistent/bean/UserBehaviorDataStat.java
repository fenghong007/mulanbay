package cn.mulanbay.pms.persistent.bean;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/9/4.
 */
public class UserBehaviorDataStat {

    //格式：2017-01-01
    private String date;

    private String name;

    private Object value;

    //todo 不同的类型比较以后可以采用数量
    private BigInteger totalCount;

    private Boolean dateRegion;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getDateRegion() {
        return dateRegion;
    }

    public void setDateRegion(Boolean dateRegion) {
        this.dateRegion = dateRegion;
    }
}
