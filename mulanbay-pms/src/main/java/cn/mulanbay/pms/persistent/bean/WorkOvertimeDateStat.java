package cn.mulanbay.pms.persistent.bean;

import cn.mulanbay.pms.persistent.bean.common.CalendarDateStat;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/1/31.
 * 加班统计
 */
public class WorkOvertimeDateStat implements DateStat ,CalendarDateStat {
    // 月份
    private Integer indexValue;
    private BigDecimal totalHours;
    private BigInteger totalCount;

    public Integer getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public double getCalendarStatValue() {
        return totalHours.doubleValue();
    }

    @Override
    public int getDateIndexValue() {
        return indexValue.intValue();
    }
}
