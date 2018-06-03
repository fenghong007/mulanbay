package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/2/25.
 * 身体基本信息基于时间的统计
 */
public class BodyBasicInfoDateStat implements DateStat {

    private Integer indexValue;
    private BigInteger totalCount;
    private BigDecimal totalWeight;
    private BigDecimal totalHeight;
    private BigDecimal totalBmi;


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

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(BigDecimal totalHeight) {
        this.totalHeight = totalHeight;
    }

    public BigDecimal getTotalBmi() {
        return totalBmi;
    }

    public void setTotalBmi(BigDecimal totalBmi) {
        this.totalBmi = totalBmi;
    }
}
