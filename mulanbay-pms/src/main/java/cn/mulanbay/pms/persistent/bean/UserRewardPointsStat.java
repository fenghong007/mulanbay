package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-01-23 22:42
 */
public class UserRewardPointsStat {

    private BigInteger userId;

    //总次数
    private BigInteger totalCount;

    // 总得分
    private BigDecimal totalPoints;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(BigDecimal totalPoints) {
        this.totalPoints = totalPoints;
    }
}
