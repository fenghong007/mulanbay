package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.handler.RewardPointsHandler;
import cn.mulanbay.pms.persistent.bean.NotifyResult;
import cn.mulanbay.pms.persistent.domain.UserCalendar;
import cn.mulanbay.pms.persistent.domain.UserNotify;
import cn.mulanbay.pms.persistent.domain.UserNotifyRemind;
import cn.mulanbay.pms.persistent.enums.ResultType;
import cn.mulanbay.pms.persistent.enums.RewardSource;
import cn.mulanbay.pms.persistent.enums.UserCalendarFinishType;
import cn.mulanbay.pms.persistent.enums.UserCalendarSource;
import cn.mulanbay.pms.persistent.service.NotifyService;
import cn.mulanbay.pms.persistent.service.UserCalendarService;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.enums.TriggerType;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * 统计用户提醒的调度
 * 如果达到警告、告警值，往消息表写一条待发送记录
 * 一般为每天凌晨统计，根据用户配置的提醒时间设置expectSendTime值
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class UserNotifyRemindJob extends AbstractBaseRemindJob {

    private static Logger logger = Logger.getLogger(UserNotifyRemindJob.class);

    NotifyService notifyService;

    CacheHandler cacheHandler;

    PmsNotifyHandler pmsNotifyHandler=null;

    RewardPointsHandler rewardPointsHandler= null;

    UserCalendarService userCalendarService=null;

    @Override
    public TaskResult doTask() {
        TaskResult taskResult = new TaskResult();
        notifyService = BeanFactoryUtil.getBean(NotifyService.class);
        List<UserNotify> list = notifyService.getNeedRemindUserNotify();
        cacheHandler = BeanFactoryUtil.getBean(CacheHandler.class);
        pmsNotifyHandler = BeanFactoryUtil.getBean(PmsNotifyHandler.class);
        rewardPointsHandler = BeanFactoryUtil.getBean(RewardPointsHandler.class);
        userCalendarService = BeanFactoryUtil.getBean(UserCalendarService.class);
        if(list.isEmpty()){
            taskResult.setComment("没有需要提醒的用户提醒");
        }else{
            for(UserNotify userNotify : list){
                handleUserNotify(userNotify);
            }
            taskResult.setExecuteResult(JobExecuteResult.SUCESS);
        }
        return taskResult;
    }

    private void handleUserNotify(UserNotify userNotify){
        if(!userNotify.getRemind()){
            return;
        }
        Long userId = userNotify.getUserId();
        // 第一步先判断是否已经通知过
        String cs = cacheHandler.getForString("userNotify:"+userId+":"+userNotify.getId());
        if(cs!=null){
            logger.debug("用户ID="+userId+"的提醒["+userNotify.getTitle()+"],id="+userNotify.getId()+"已经提醒过了");
            return;
        }
        UserNotifyRemind unr = notifyService.getUserNotifyRemind(userNotify.getId(),userNotify.getUserId());
        NotifyResult notifyResult = notifyService.getNotifyResult(userNotify,userId);
        String title=null;
        String content=null;
        if(notifyResult.getOverAlertValue()>0&&unr.getOverAlertRemind()){
            // 达到报警值提醒
            title="["+userNotify.getTitle()+"]报警";
            if(userNotify.getNotifyConfig().getResultType()== ResultType.NAME_DATE||userNotify.getNotifyConfig().getResultType()== ResultType.NAME_NUMBER){
                content="["+userNotify.getTitle()+"]["+notifyResult.getName()+"]超过报警值["+userNotify.getAlertValue()+"],实际值为["
                        +notifyResult.getCompareValue()+"],计量单位:["+userNotify.getNotifyConfig().getValueTypeName()+"]\n";
            }else{
                content="["+userNotify.getTitle()+"]超过报警值["+userNotify.getAlertValue()+"],实际值为["
                        +notifyResult.getCompareValue()+"],计量单位:["+userNotify.getNotifyConfig().getValueTypeName()+"]\n";
            }
            notifyMessage(title,content,unr,userId,userNotify.getId());
        }else if(notifyResult.getOverWarningValue()>0&&unr.getOverWarningRemind()){
            // 达到警告值提醒
            title="["+userNotify.getTitle()+"]警告";
            if(userNotify.getNotifyConfig().getResultType()== ResultType.NAME_DATE||userNotify.getNotifyConfig().getResultType()== ResultType.NAME_NUMBER){
                content="["+userNotify.getTitle()+"]["+notifyResult.getName()+"]超过警告值["+userNotify.getWarningValue()+"],实际值为["
                        +notifyResult.getCompareValue()+"],计量单位:["+userNotify.getNotifyConfig().getValueTypeName()+"]\n";
            }else{
                content="["+userNotify.getTitle()+"]超过警告值["+userNotify.getWarningValue()+"],实际值为["
                        +notifyResult.getCompareValue()+"],计量单位:["+userNotify.getNotifyConfig().getValueTypeName()+"]\n";
            }
            notifyMessage(title,content,unr,userId,userNotify.getId());
        }else{
            logger.debug("用户ID="+userId+"的提醒["+userNotify.getTitle()+"],id="+userNotify.getId()+"不需要提醒");
            rewardPoint(userNotify,true,null);
        }

        //加入缓存(方便用户日历统计)
        //cacheHandler.set("userNotifyResult:"+userId+":"+DateUtil.getFormatDate(this.getBussDay(),DateUtil.FormatDay1),notifyResult,24*3600);
    }

    private void notifyMessage(String title, String content, UserNotifyRemind remind,Long userId,Long userNotifyId){
        content=content+"统计日期:"+DateUtil.getFormatDate(this.getBussDay(),DateUtil.FormatDay1);
        RemindTimeBean bean = this.calcRemindExpectTime(remind.getTriggerInterval(),remind.getTriggerType(),remind.getLastRemindTime(),remind.getRemindTime());
        Long messageId = pmsNotifyHandler.addNotifyMessage(title,content,userId,bean.getNextRemindTime());
        // 更新最后的提醒时间
        notifyService.updateLastRemindTime(remind.getId(),new Date());
        Date nextRemindTime = bean.getNextRemindTime();
        int rate=1;
        if(remind.getTriggerType()== TriggerType.MONTH){
            rate=30;
        }else if(remind.getTriggerType()== TriggerType.WEEK){
            rate=7;
        }else if(remind.getTriggerType()== TriggerType.YEAR){
            rate=365;
        }
        // 设置提醒的缓存
        long expiredSecond = (nextRemindTime.getTime()-System.currentTimeMillis())/1000+bean.getDays()*rate*24*3600;
        if(expiredSecond>0){
            cacheHandler.set("userNotify:"+userId+":"+userNotifyId,"123", (int) (expiredSecond));
        }else {
            logger.error("不合法的缓存失效时间:"+expiredSecond);
        }
        rewardPoint(remind.getUserNotify(),false,messageId);
        addToUserCalendar(remind,messageId);
    }

    /**
     * 更新积分，完成+，未达到要求减
     * @param userNotify
     */
    private void rewardPoint(UserNotify userNotify,boolean isComplete,Long messageId){
        try {
            int radio=1;
            int rewards=userNotify.getNotifyConfig().getRewardPoint()*radio;
            if(!isComplete){
                rewards=0-rewards;
            }
            String remark="用户提醒配置["+userNotify.getTitle()+"]触发警报惩罚";
            rewardPointsHandler.rewardPoints(userNotify.getUserId(),rewards,userNotify.getId(), RewardSource.NOTIFY,remark,messageId);
            if(isComplete){
                String bussKey = userNotify.getNotifyConfig().getBussKey();
                if(StringUtil.isEmpty(bussKey)){
                    logger.warn(userNotify.getNotifyConfig().getTitle()+"没有配置bussKey");
                    return;
                }
                String bussIdentityKey = bussKey;
                if(!StringUtil.isEmpty(userNotify.getBindValues())){
                    bussIdentityKey+="_"+userNotify.getBindValues();
                }
                userCalendarService.updateUserCalendarForFinish(userNotify.getUserId(),bussIdentityKey,new Date(), UserCalendarFinishType.AUTO);
            }
        } catch (Exception e) {
            logger.error("计划["+userNotify.getTitle()+"]积分奖励异常",e);
        }
    }

    /**
     * 更新到用户日历
     *
     * @param remind
     */
    private void addToUserCalendar(UserNotifyRemind remind,Long messageId){
        try {
            UserNotify userNotify = remind.getUserNotify();
            String bussKey = userNotify.getNotifyConfig().getBussKey();
            if(StringUtil.isEmpty(bussKey)){
                logger.warn(userNotify.getNotifyConfig().getTitle()+"没有配置bussKey");
                return;
            }
            String bussIdentityKey = bussKey;
            if(!StringUtil.isEmpty(userNotify.getBindValues())){
                bussIdentityKey+="_"+userNotify.getBindValues();
            }
            UserCalendar uc = userCalendarService.getUserCalendar(userNotify.getUserId(),bussIdentityKey,new Date());
            if(uc!=null){
                userCalendarService.updateUserCalendarToDate(uc,new Date(),messageId);
            }else{
                uc = new UserCalendar();
                uc.setUserId(userNotify.getUserId());
                uc.setTitle(userNotify.getCalendarTitle());
                uc.setContent(userNotify.getCalendarTitle());
                uc.setDelayCounts(0);
                uc.setBussDay(DateUtil.getDate(0));
                int rate=1;
                if(remind.getTriggerType()==TriggerType.MONTH){
                    rate=30;
                }else if(remind.getTriggerType()==TriggerType.WEEK){
                    rate=7;
                }
                uc.setExpireTime(DateUtil.getDate(remind.getTriggerInterval()*rate));
                uc.setBussIdentityKey(bussIdentityKey);
                uc.setSourceType(UserCalendarSource.NOTIFY);
                uc.setSourceId(userNotify.getId().toString());
                uc.setMessageId(messageId);
                userCalendarService.addUserCalendarToDate(uc);
            }
        } catch (Exception e) {
            logger.error("添加到用户日历异常",e);
        }

    }

    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
