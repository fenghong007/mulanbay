package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;

/**
 * Created by fenghong on 2017/2/25.
 * 身体基本信息基于时间的统计
 */
public class BodyBasicInfoAvgStat {

    private BigDecimal avgWeight;
    private BigDecimal avgHeight;

    public BigDecimal getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(BigDecimal avgWeight) {
        this.avgWeight = avgWeight;
    }

    public BigDecimal getAvgHeight() {
        return avgHeight;
    }

    public void setAvgHeight(BigDecimal avgHeight) {
        this.avgHeight = avgHeight;
    }
}
