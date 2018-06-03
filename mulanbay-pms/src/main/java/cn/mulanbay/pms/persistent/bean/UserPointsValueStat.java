package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-16 11:05
 */
public class UserPointsValueStat {

    private BigInteger sourceId;

    private Integer rewards;

    private BigDecimal totalRewardPoints;
    private BigInteger totalCount;

    public BigInteger getSourceId() {
        return sourceId;
    }

    public void setSourceId(BigInteger sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getRewards() {
        return rewards;
    }

    public void setRewards(Integer rewards) {
        this.rewards = rewards;
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
}
