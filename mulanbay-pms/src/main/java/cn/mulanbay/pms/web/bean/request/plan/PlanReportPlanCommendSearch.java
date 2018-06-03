package cn.mulanbay.pms.web.bean.request.plan;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/10.
 */
public class PlanReportPlanCommendSearch extends QueryBulider implements BindUser ,FullEndDateTime {

    @Query(fieldName = "bussStatDate", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "bussStatDate", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "planConfigId", op = Parameter.Operator.EQ)
    private Long planConfigId;

    @Query(fieldName = "userId", op = Parameter.Operator.EQ)
    public Long userId;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getPlanConfigId() {
        return planConfigId;
    }

    public void setPlanConfigId(Long planConfigId) {
        this.planConfigId = planConfigId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
