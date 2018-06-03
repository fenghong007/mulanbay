package cn.mulanbay.pms.persistent.bean;

import cn.mulanbay.pms.persistent.bean.common.CalendarDateStat;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/1/31.
 * 用户积分统计
 */
public class UserPointsDateStat implements DateStat ,CalendarDateStat {
    // 月份
    private Integer indexValue;
    private BigDecimal totalRewardPoints;
    private BigInteger totalCount;

    public Integer getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
    }

    public BigDecimal getTotalRewardPoints() {
        return totalRewardPoints;
    }

    public void setTotalRewardPoints(BigDecimal totalRewardPoints) {
        this.totalRewardPoints = totalRewardPoints;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public double getCalendarStatValue() {
        return totalRewardPoints.intValue();
    }

    @Override
    public int getDateIndexValue() {
        return indexValue.intValue();
    }
}
