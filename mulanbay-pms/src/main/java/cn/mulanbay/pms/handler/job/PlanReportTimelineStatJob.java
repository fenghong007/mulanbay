package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.persistent.domain.PlanReport;
import cn.mulanbay.pms.persistent.domain.PlanReportTimeline;
import cn.mulanbay.pms.persistent.domain.UserPlan;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.PlanReportDataStatFilterType;
import cn.mulanbay.pms.persistent.service.PlanService;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanSearch;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.job.AbstractBaseJob;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;

import java.util.Date;
import java.util.List;

/**
 * 用户计划时间线统计job,一般为每天统计一次,统计昨天的
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class PlanReportTimelineStatJob extends AbstractBaseJob {

    private static Logger logger = Logger.getLogger(PlanReportTimelineStatJob.class);

    PlanService planService = null;

    BaseService baseService=null;

    @Override
    public TaskResult doTask() {
        TaskResult result =new TaskResult();
        planService = BeanFactoryUtil.getBean(PlanService.class);
        baseService = BeanFactoryUtil.getBean(BaseService.class);
        UserPlanSearch sf = new UserPlanSearch();
        sf.setStatus(CommonStatus.ENABLE);
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
            PlanReport planReport = planService.statPlanReport(userPlan,date,userPlan.getUserId(), PlanReportDataStatFilterType.ORIGINAL);
            if(planReport==null){
                logger.warn("用户计划["+userPlan.getTitle()+"],id="+userPlan.getId()+"在["+date+"]没有数据");
                return true;
            }
            PlanReportTimeline timeline =new PlanReportTimeline();
            BeanCopy.copyProperties(planReport,timeline);
            //需要设置为该天的
            timeline.setBussStatDate(date);
            baseService.saveObject(timeline);
            return true;
        } catch (BeansException e) {
            logger.error("处理用户计划统计一次",e);
            return false;
        }

    }

    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
