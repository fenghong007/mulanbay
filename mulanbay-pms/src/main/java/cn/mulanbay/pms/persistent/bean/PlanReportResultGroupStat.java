package cn.mulanbay.pms.persistent.bean;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/1/31.
 *  总的概要统计
 */
public class PlanReportResultGroupStat {

    private BigInteger totalCount;

    private Short resultType;

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public Short getResultType() {
        return resultType;
    }

    public void setResultType(Short resultType) {
        this.resultType = resultType;
    }
}
