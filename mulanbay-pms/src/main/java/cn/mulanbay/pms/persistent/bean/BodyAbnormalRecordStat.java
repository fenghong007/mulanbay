package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by fenghong on 2017/2/12.
 */
public class BodyAbnormalRecordStat {

    private String name;

    private BigInteger totalCount;

    private BigDecimal totalLastDays;

    private Date maxOccurDate;

    private Date minOccurDate;

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

    public BigDecimal getTotalLastDays() {
        return totalLastDays;
    }

    public void setTotalLastDays(BigDecimal totalLastDays) {
        this.totalLastDays = totalLastDays;
    }

    public Date getMaxOccurDate() {
        return maxOccurDate;
    }

    public void setMaxOccurDate(Date maxOccurDate) {
        this.maxOccurDate = maxOccurDate;
    }

    public Date getMinOccurDate() {
        return minOccurDate;
    }

    public void setMinOccurDate(Date minOccurDate) {
        this.minOccurDate = minOccurDate;
    }
}
