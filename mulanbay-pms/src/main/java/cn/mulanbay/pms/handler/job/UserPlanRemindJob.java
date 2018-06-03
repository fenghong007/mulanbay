package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.handler.RewardPointsHandler;
import cn.mulanbay.pms.persistent.domain.PlanReport;
import cn.mulanbay.pms.persistent.domain.UserCalendar;
import cn.mulanbay.pms.persistent.domain.UserPlan;
import cn.mulanbay.pms.persistent.domain.UserPlanRemind;
import cn.mulanbay.pms.persistent.enums.*;
import cn.mulanbay.pms.persistent.service.PlanService;
import cn.mulanbay.pms.persistent.service.UserCalendarService;
import cn.mulanbay.pms.persistent.service.UserPlanService;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * 统计用户计划完成进度的调度
 * 如果进度达不到要求（和时间的进度想比），往消息表写一条待发送记录
 * 一般为每天凌晨统计，根据用户配置的提醒时间设置expectSendTime值
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class UserPlanRemindJob extends AbstractBaseRemindJob {

    private static Logger logger = Logger.getLogger(UserPlanRemindJob.class);

    PlanType planType;

    UserPlanService userPlanService=null;

    PlanService planService = null;

    PmsNotifyHandler pmsNotifyHandler=null;

    CacheHandler cacheHandler= null;

    RewardPointsHandler rewardPointsHandler= null;

    UserCalendarService userCalendarService=null;

    @Override
    public TaskResult doTask() {
        TaskResult result =new TaskResult();
        if(planType==null){
            result.setExecuteResult(JobExecuteResult.SKIP);
            result.setComment("计划类型为空，无法执行调度");
        }
        userPlanService = BeanFactoryUtil.getBean(UserPlanService.class);
        planService = BeanFactoryUtil.getBean(PlanService.class);
        pmsNotifyHandler = BeanFactoryUtil.getBean(PmsNotifyHandler.class);
        cacheHandler = BeanFactoryUtil.getBean(CacheHandler.class);
        rewardPointsHandler = BeanFactoryUtil.getBean(RewardPointsHandler.class);
        userCalendarService = BeanFactoryUtil.getBean(UserCalendarService.class);
        List<UserPlan> list =userPlanService.getNeedRemindUserPlan(planType);
        if(list.isEmpty()){
            result.setComment("没有需要提醒的用户计划");
        }else {
            for(UserPlan userPlan:list){
                handleUserPlanRemind(userPlan);
            }
            result.setExecuteResult(JobExecuteResult.SUCESS);
        }
        return result;
    }

    private void handleUserPlanRemind(UserPlan userPlan){
        try {
            if(!userPlan.getRemind()){
                return;
            }
            Long userId = userPlan.getUserId();
            // 第一步先判断是否已经通知过
            String cs = cacheHandler.getForString("userPlanNotify:"+userId+":"+userPlan.getId());
            if(cs!=null){
                logger.debug("用户ID="+userId+"的计划["+userPlan.getTitle()+"],id="+userPlan.getId()+"已经提醒过了");
                return;
            }
            CompareType compareType =userPlan.getPlanConfig().getCompareType();
            UserPlanRemind remind = userPlanService.getRemindByUserPlan(userPlan.getId(),userPlan.getUserId());
            // 通过缓存查询上一次提醒时间
            //需要用运营日计算，比如2017-12-01号调度的，应该是用2017-11-30号计算
            Date date = this.getBussDay();
            int totalDays = this.getTotalDaysPlan(date);
            int dayIndex = this.getDayOfPlan(date);
            //已经过去几天
            double rate = NumberUtil.getPercentValue(dayIndex,totalDays,0);
            if(rate>=remind.getFormTimePassedRate().doubleValue()){

                //统计
                PlanReport planReport = planService.statPlanReport(userPlan,date,userId, PlanReportDataStatFilterType.ORIGINAL);
                double planCountRate = NumberUtil.getPercentValue(planReport.getReportCountValue(),planReport.getPlanCountValue(),2);
                double planValueRate = NumberUtil.getPercentValue(planReport.getReportValue(),planReport.getPlanValue(),2);
                if(compareType==CompareType.MORE){
                    //大于类型(即完成的值必须要大于这个)
                    if(planCountRate<rate||planValueRate<rate){
                        //进度落后
                        //提醒
                        String title="计划["+userPlan.getTitle()+"]提醒";
                        StringBuffer content = new StringBuffer();
                        String unit= userPlan.getPlanConfig().getUnit();
                        content.append("你的计划["+userPlan.getTitle()+"]进度落后了,时间已经过去"+rate+"%,但是进度没有赶上。\n");
                        content.append("计划的次数已经完成["+planReport.getReportCountValue()+"]次,期望["+planReport.getPlanCountValue()+"]次,完成进度："+planCountRate+"%\n");
                        content.append("计划的值已经完成["+planReport.getReportValue()+"]"+unit+",期望["+planReport.getPlanValue()+"]"+unit+",完成进度："+planValueRate+"%\n");
                        content.append(getStatDayRangInfo(planReport,date));
                        this.notifyMessage(title,content.toString(),remind,false);
                    }else{
                        //计划完成
                        String title="计划["+userPlan.getTitle()+"]完成";
                        StringBuffer content = new StringBuffer();
                        String unit= userPlan.getPlanConfig().getUnit();
                        content.append("你的计划["+userPlan.getTitle()+"]已经完成\n");
                        content.append("计划次数已经完成["+planReport.getReportCountValue()+"]次,期望["+planReport.getPlanCountValue()+"]次,完成进度："+planCountRate+"%\n");
                        content.append("计划值已经完成["+planReport.getReportValue()+"]"+unit+",期望["+planReport.getPlanValue()+"]"+unit+",完成进度："+planValueRate+"%\n");
                        content.append(getStatDayRangInfo(planReport,date));
                        this.notifyMessage(title,content.toString(),remind,true);
                    }
                }else{
                    //小于类型
                    //超标
                    if(planReport.getReportCountValue()>planReport.getPlanCountValue()||planReport.getReportValue()>planReport.getPlanValue()){
                        String title="计划["+userPlan.getTitle()+"]提醒";
                        StringBuffer content = new StringBuffer();
                        String unit= userPlan.getPlanConfig().getUnit();
                        content.append("你的计划["+userPlan.getTitle()+"]已经超出预期\n");
                        content.append("计划的次数已经达到["+planReport.getReportCountValue()+"]次,期望["+planReport.getPlanCountValue()+"]次\n");
                        content.append("计划的值已经达到["+planReport.getReportValue()+"]"+unit+",期望["+planReport.getPlanValue()+"]"+unit+"\n");
                        content.append(getStatDayRangInfo(planReport,date));
                        this.notifyMessage(title,content.toString(),remind,true);
                    }else if(dayIndex==totalDays){
                        // 完成目标
                        String title="计划["+userPlan.getTitle()+"]完成";
                        StringBuffer content = new StringBuffer();
                        String unit= userPlan.getPlanConfig().getUnit();
                        content.append("你的计划["+userPlan.getTitle()+"]已经满足要求\n");
                        content.append("计划的次数["+planReport.getReportCountValue()+"]次,期望["+planReport.getPlanCountValue()+"]次\n");
                        content.append("计划的值["+planReport.getReportValue()+"]"+unit+",期望["+planReport.getPlanValue()+"]"+unit+"\n");
                        content.append(getStatDayRangInfo(planReport,date));
                        this.notifyMessage(title,content.toString(),remind,true);
                    }
                }
                //加入缓存(方便用户日历统计)
                //cacheHandler.set("userPlanReport:"+userId+":"+DateUtil.getFormatDate(date,DateUtil.FormatDay1),planReport,24*3600);
            }else{
                logger.debug("当前时间进度是"+rate+",配置的最小提醒时间进度为"+remind.getFormTimePassedRate()+",不提醒");
            }


        } catch (Exception e) {
            logger.error("处理用户计划["+userPlan.getTitle()+"]异常",e);
        }
    }

    /**
     * 更新积分，完成+，未达到要求减
     * @param userPlan
     * @param isComplete
     */
    private void rewardPoint(UserPlan userPlan,boolean isComplete,Long messageId){
        try {
            int radio=0;
            if(userPlan.getPlanConfig().getPlanType()==PlanType.YEAR){
                radio=30;
            }else if(userPlan.getPlanConfig().getPlanType()==PlanType.MONTH){
                radio=10;
            }
            int rewards=0;
            String remark=null;
            if(isComplete){
                rewards = userPlan.getPlanConfig().getRewardPoint()*radio;
                remark="计划["+userPlan.getTitle()+"]完成奖励";
            }else{
                //未完成减去
                rewards = userPlan.getPlanConfig().getRewardPoint()*(-1);
                remark="计划["+userPlan.getTitle()+"]进度未达到要求惩罚";
            }
            rewardPointsHandler.rewardPoints(userPlan.getUserId(),rewards,userPlan.getId(), RewardSource.PLAN,remark,messageId);
            if(isComplete){
                //删除日历
                String bussKey = userPlan.getPlanConfig().getBussKey();
                if(StringUtil.isEmpty(bussKey)){
                    logger.warn(userPlan.getPlanConfig().getTitle()+"没有配置bussKey");
                    return;
                }
                String bussIdentityKey = bussKey;
                if(!StringUtil.isEmpty(userPlan.getBindValues())){
                    bussIdentityKey+="_"+userPlan.getBindValues();
                    userCalendarService.updateUserCalendarForFinish(userPlan.getUserId(),bussIdentityKey,new Date(),UserCalendarFinishType.AUTO);
                }
            }else{
                //添加到用户日历
                addToUserCalendar(userPlan,messageId);
            }
        } catch (Exception e) {
            logger.error("计划["+userPlan.getTitle()+"]积分奖励异常",e);
        }
    }

    /**
     * 更新到用户日历
     *
     * @param userPlan
     */
    private void addToUserCalendar(UserPlan userPlan,Long messageId){
        try {
            String bussKey = userPlan.getPlanConfig().getBussKey();
            if(StringUtil.isEmpty(bussKey)){
                logger.warn(userPlan.getPlanConfig().getTitle()+"没有配置bussKey");
                return;
            }
            String bussIdentityKey = bussKey;
            if(!StringUtil.isEmpty(userPlan.getBindValues())){
                bussIdentityKey+="_"+userPlan.getBindValues();
            }
            UserCalendar uc = userCalendarService.getUserCalendar(userPlan.getUserId(),bussIdentityKey,new Date());
            if(uc!=null){
                userCalendarService.updateUserCalendarToDate(uc,new Date(),messageId);
            }else{
                uc = new UserCalendar();
                uc.setUserId(userPlan.getUserId());
                uc.setTitle(userPlan.getCalendarTitle());
                uc.setContent(userPlan.getCalendarTitle());
                uc.setDelayCounts(0);
                uc.setBussDay(DateUtil.getDate(0));
                if(userPlan.getPlanConfig().getPlanType()==PlanType.MONTH){
                    uc.setExpireTime(DateUtil.getLastDayOfMonth(uc.getBussDay()));
                }else{
                    uc.setExpireTime(DateUtil.getLastDayOfYear(DateUtil.getYear(uc.getBussDay())));
                }
                uc.setBussIdentityKey(bussIdentityKey);
                uc.setSourceType(UserCalendarSource.PLAN);
                uc.setSourceId(userPlan.getId().toString());
                uc.setMessageId(messageId);
                userCalendarService.addUserCalendarToDate(uc);
            }
        } catch (Exception e) {
            logger.error("添加到用户日历异常",e);
        }

    }

    private String getStatDayRangInfo(PlanReport planReport,Date date){
        String s = "期望数据参考年份:"+planReport.getPlanConfigYear()+"\n";
        Date firstDate = getFirstDate(date);
        s+="统计日期:"+DateUtil.getFormatDate(firstDate,DateUtil.FormatDay1)+"~"+DateUtil.getFormatDate(date,DateUtil.FormatDay1);
        return s;
    }

    private Date getFirstDate(Date date){
        if(planType==PlanType.YEAR){
            return DateUtil.getYearFirst(date);
        }else if(planType==PlanType.MONTH){
            return DateUtil.getFirstDayOfMonth(date);
        }else {
            return date;
        }
    }

    /**
     * 计划所在总的天数
     * @param date
     * @return
     */
    private int getTotalDaysPlan(Date date){
        if(planType==PlanType.MONTH){
            return DateUtil.getMonthDays(date);
        }else if(planType==PlanType.YEAR){
            return DateUtil.getYearDays(date);
        }else {
            return 0;
        }
    }

    /**
     * 计划所在总的天数
     * @param date
     * @return
     */
    private int getDayOfPlan(Date date){
        if(planType==PlanType.MONTH){
            return DateUtil.getDayOfMonth(date);
        }else if(planType==PlanType.YEAR){
            return DateUtil.getDayOfYear(date);
        }else {
            return 0;
        }
    }

    /**
     * 消息提醒
     * @param title
     * @param content
     * @param remind
     * @param isComplete
     */
    private void notifyMessage(String title,String content,UserPlanRemind remind,boolean isComplete){
        UserPlan userPlan =remind.getUserPlan();
        RemindTimeBean bean = this.calcRemindExpectTime(remind.getTriggerInterval(),remind.getTriggerType(),remind.getLastRemindTime(),remind.getRemindTime());
        Long messageId = pmsNotifyHandler.addNotifyMessage(title,content,userPlan.getUserId(),bean.getNextRemindTime());
        // 更新最后的提醒时间
        userPlanService.updateLastRemindTime(remind.getId(),new Date());
        Date nextRemindTime = bean.getNextRemindTime();
        if(isComplete){
            //完成后，设置为最后一天
            PlanType planType = userPlan.getPlanConfig().getPlanType();
            if(planType==PlanType.MONTH){
                nextRemindTime = DateUtil.getLastDayOfMonth(new Date());
            }else if(planType==PlanType.YEAR){
                nextRemindTime = DateUtil.getLastDayOfCurrYear();
            }
        }
        // 设置提醒的缓存
        long expiredSecond = (nextRemindTime.getTime()-System.currentTimeMillis())/1000+bean.getDays()*24*3600;
        if(expiredSecond>0){
            cacheHandler.set("userPlanNotify:"+userPlan.getUserId()+":"+userPlan.getId(),"123", (int) (expiredSecond));
        }else {
            logger.error("不合法的缓存失效时间:"+expiredSecond);
        }
        //积分奖励
        rewardPoint(userPlan,isComplete,messageId);
    }

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
