package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.persistent.domain.PlanReport;
import cn.mulanbay.pms.persistent.domain.PlanReportTimeline;
import cn.mulanbay.pms.persistent.domain.UserPlan;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.PlanReportDataStatFilterType;
import cn.mulanbay.pms.persistent.enums.PlanType;
import cn.mulanbay.pms.persistent.service.PlanService;
import cn.mulanbay.pms.persistent.service.UserPlanService;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanSearch;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.job.AbstractBaseJob;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;

import java.util.Date;
import java.util.List;

/**
 * 用户计划统计job
 * 月计划每月一次，年计划每年一次（不同类型的计划需要开不同的调度任务，配置不同的调度周期）
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class PlanReportStatJob extends AbstractBaseJob {

    private static Logger logger = Logger.getLogger(PlanReportStatJob.class);

    PlanType planType;

    UserPlanService userPlanService=null;

    PlanService planService = null;

    BaseService baseService=null;

    @Override
    public TaskResult doTask() {
        TaskResult result =new TaskResult();
        userPlanService = BeanFactoryUtil.getBean(UserPlanService.class);
        planService = BeanFactoryUtil.getBean(PlanService.class);
        baseService = BeanFactoryUtil.getBean(BaseService.class);
        UserPlanSearch sf = new UserPlanSearch();
        sf.setStatus(CommonStatus.ENABLE);
        sf.setPlanType(planType);
        sf.setPage(-1);
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(UserPlan.class);
        List<UserPlan> list = baseService.getBeanList(pr);
        if(list.isEmpty()){
            result.setComment("可用用户计划为空");
        }else{
            Date date = this.getBussDay();
            int success=0;
            int fail=0;
            for(UserPlan userPlan:list){
                boolean b =statAndSaveUserPlan(userPlan,date);
                if(b){
                    success++;
                }else {
                    fail++;
                }
            }
            result.setExecuteResult(JobExecuteResult.SUCESS);
            result.setComment("一共统计了"+list.size()+"个用户计划,成功:"+success+"个,失败"+fail+"个");
        }
        return result;
    }

    private boolean statAndSaveUserPlan(UserPlan userPlan,Date date ){
        try {
            PlanReport planReport = null;
            //先去用户计划时间线查询，如果有的话，直接复制拷贝
            PlanReportTimeline timeline = planService.getPlanReportTimeline(userPlan.getId(),date);
            if(timeline!=null){
                planReport = new PlanReport();
                //report里面存的是第一天的日期，而timeline存的是当天的日期
                Date bussStatDate = planService.getFirstStatDayByType(userPlan.getPlanConfig().getPlanType(),date);
                BeanCopy.copyProperties(timeline,planReport);
                planReport.setBussStatDate(bussStatDate);
            }else{
                planReport = planService.statPlanReport(userPlan,date,userPlan.getUserId(), PlanReportDataStatFilterType.ORIGINAL);
            }
            if(planReport!=null){
                baseService.saveObject(planReport);
            }else{
                logger.warn("用户计划["+userPlan.getTitle()+"],id="+userPlan.getId()+"在["+date+"]没有数据");
            }
            return true;
        } catch (BeansException e) {
            logger.error("处理用户计划统计一次",e);
            return false;
        }

    }

    /**
     * 参数为调度周期类型
     * @param triggerDetail
     * @return
     */
    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        ParaCheckResult result = new ParaCheckResult();
        if(StringUtil.isEmpty(triggerDetail)){
            result.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_NULL);
            result.setMessage("调度参数检查失败，参数为空");
        }else{
            planType = PlanType.valueOf(triggerDetail);
        }
        return result;
    }

}
