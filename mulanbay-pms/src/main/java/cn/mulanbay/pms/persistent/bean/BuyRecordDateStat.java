package cn.mulanbay.pms.persistent.bean;

import cn.mulanbay.pms.persistent.bean.common.CalendarDateStat;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/2/5.
 * 购买记录的按日期统计
 */
public class BuyRecordDateStat implements DateStat ,CalendarDateStat {

    // 可以为天(20100101),周(1-36),月(1-12),年(2014)
    private Integer indexValue;

    private BigInteger totalCount;

    // 价格，精确到元，可以为运费、总价
    private BigDecimal totalPrice;

    public Integer getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public double getCalendarStatValue() {
        return totalCount.doubleValue();
    }

    @Override
    public int getDateIndexValue() {
        return indexValue.intValue();
    }
}
