package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.bean.UserRewardPointsStat;
import cn.mulanbay.pms.persistent.service.AuthService;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.job.AbstractBaseJob;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 * 积分统计
 *
 * @author fenghong
 * @create 2018-01-23 22:36
 */
public class DailyRewardPointsStatJob extends AbstractBaseJob {

    AuthService authService;

    PmsNotifyHandler pmsNotifyHandler=null;

    String remindTime="08:30";

    @Override
    public TaskResult doTask() {
        TaskResult tr = new TaskResult();
        authService = BeanFactoryUtil.getBean(AuthService.class);
        pmsNotifyHandler = BeanFactoryUtil.getBean(PmsNotifyHandler.class);

        Date startTime = this.getBussDay();
        Date endTime = DateUtil.getTodayTillMiddleNightDate(startTime);
        List<UserRewardPointsStat> list = authService.statUserRewardPoints(startTime,endTime);
        if(list.isEmpty()){
            tr.setComment("今天没人获得积分");
        }else{
            for(UserRewardPointsStat urp : list){
                handleRewardPointsStat(startTime,urp);
            }
            tr.setExecuteResult(JobExecuteResult.SUCESS);
            tr.setComment("今天一共"+list.size()+"个人获得积分");
        }
        return tr;
    }

    private void handleRewardPointsStat(Date startTime ,UserRewardPointsStat urp){
        String title ="积分奖励统计";
        StringBuffer sb = new StringBuffer();
        sb.append("您在"+DateUtil.getFormatDate(startTime,DateUtil.FormatDay1+"获取"));
        int n = urp.getTotalCount().intValue();
        sb.append(n+"次积分奖励/惩罚");
        if(n>0){
            sb.append(",最终获得积分:"+urp.getTotalPoints().longValue()+".");
        }
        sb.append("当前总积分为:"+authService.getUserPiont(urp.getUserId().longValue()));
        Date nextRemindTime = DateUtil.getDate(DateUtil.getToday()+" "+remindTime+":00",DateUtil.Format24Datetime);
        pmsNotifyHandler.addNotifyMessage(title,sb.toString(),urp.getUserId().longValue(),nextRemindTime);

    }

    @Override
    public ParaCheckResult checkTriggerPara(String s) {
        if(!StringUtil.isEmpty(s)){
            remindTime=s;
        }
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
