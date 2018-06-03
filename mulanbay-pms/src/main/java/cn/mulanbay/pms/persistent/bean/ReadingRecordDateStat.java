package cn.mulanbay.pms.persistent.bean;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/2/25.
 * 身体不适基于时间的统计
 */
public class ReadingRecordDateStat implements DateStat{

    private Integer indexValue;
    private BigInteger totalCount;

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

}
