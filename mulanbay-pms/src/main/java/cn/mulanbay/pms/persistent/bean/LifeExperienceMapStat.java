package cn.mulanbay.pms.persistent.bean;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/1/14.
 */
public class LifeExperienceMapStat {

    private String name;

    //次数
    private BigInteger totalCount;

    //天数
    private BigInteger totalDays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public BigInteger getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(BigInteger totalDays) {
        this.totalDays = totalDays;
    }
}
