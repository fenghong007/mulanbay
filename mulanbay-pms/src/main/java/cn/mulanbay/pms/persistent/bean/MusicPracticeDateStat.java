package cn.mulanbay.pms.persistent.bean;

import cn.mulanbay.pms.persistent.bean.common.CalendarDateStat;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/1/31.
 * 口琴练习统计
 */
public class MusicPracticeDateStat implements DateStat ,CalendarDateStat {
    // 月份
    private Integer indexValue;
    private BigDecimal totalMinutes;
    private BigInteger totalCount;

    public Integer getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
    }

    public BigDecimal getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(BigDecimal totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public double getCalendarStatValue() {
        return totalMinutes.doubleValue();
    }

    @Override
    public int getDateIndexValue() {
        return indexValue.intValue();
    }
}
