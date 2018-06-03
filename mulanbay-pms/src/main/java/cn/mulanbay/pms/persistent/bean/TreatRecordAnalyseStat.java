package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/1/31.
 * 看病记录分析统计结果
 */
public class TreatRecordAnalyseStat {

    private String name;

    private BigInteger totalCount;

    private BigDecimal totalFee;

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

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }
}
