package cn.mulanbay.pms.web.bean.response.plan;

import cn.mulanbay.pms.persistent.domain.PlanReport;
import cn.mulanbay.pms.persistent.domain.UserPlan;

/**
 * Created by fenghong on 2017/8/28.
 */
public class UserPlanResponse extends UserPlan {

    //用于最新统计
    private PlanReport planReport;

    public PlanReport getPlanReport() {
        return planReport;
    }

    public void setPlanReport(PlanReport planReport) {
        this.planReport = planReport;
    }

}
