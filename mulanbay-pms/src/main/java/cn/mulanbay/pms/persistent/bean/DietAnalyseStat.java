package cn.mulanbay.pms.persistent.bean;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/2/5.
 * 购买记录的按日期统计
 */
public class DietAnalyseStat {

    private String name;

    private BigInteger totalCount;

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
}
