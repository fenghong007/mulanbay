package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;

/**
 * Created by fenghong on 2017/1/31.
 *  总的概要统计
 */
public class PlanReportPlanCommend {

    private BigDecimal reportCountValue;

    private BigDecimal reportValue;

    public BigDecimal getReportCountValue() {
        return reportCountValue;
    }

    public void setReportCountValue(BigDecimal reportCountValue) {
        this.reportCountValue = reportCountValue;
    }

    public BigDecimal getReportValue() {
        return reportValue;
    }

    public void setReportValue(BigDecimal reportValue) {
        this.reportValue = reportValue;
    }
}
