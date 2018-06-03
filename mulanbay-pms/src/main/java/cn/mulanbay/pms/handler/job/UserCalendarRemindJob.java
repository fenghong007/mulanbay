package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.pms.common.CacheKey;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.domain.UserCalendar;
import cn.mulanbay.pms.persistent.service.UserCalendarService;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.job.AbstractBaseJob;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-03-13 21:12
 */
public class UserCalendarRemindJob extends AbstractBaseJob {

    private static Logger logger = Logger.getLogger(UserCalendarRemindJob.class);

    @Override
    public TaskResult doTask() {
        TaskResult taskResult = new TaskResult();
        UserCalendarService userCalendarService = BeanFactoryUtil.getBean(UserCalendarService.class);
        List<UserCalendar> list = userCalendarService.getCurrentUserCalendarList(null);
        if(list.isEmpty()){
            taskResult.setComment("没有用户日历数据");
        }else{
            taskResult.setExecuteResult(JobExecuteResult.SUCESS);
            PmsNotifyHandler pmsNotifyHandler=BeanFactoryUtil.getBean(PmsNotifyHandler.class);
            int n=list.size();
            long currentUserId = list.get(0).getUserId();
            StringBuffer sb = new StringBuffer();
            int aa=0;
            int tmpIndex=1;
            for(int i=0;i<n;i++){
                UserCalendar uc = list.get(i);
                if(uc.getUserId()!=currentUserId){
                    pmsNotifyHandler.addNotifyMessage("今日任务",sb.toString(),currentUserId,null);
                    setCacheCounts(currentUserId,tmpIndex);
                    aa++;
                    currentUserId = uc.getUserId();
                    tmpIndex=1;
                    sb = new StringBuffer();
                    sb.append((tmpIndex++)+"."+uc.getTitle()+"\n");
                }else {
                    if(i==(n-1)){
                        //最后一次
                        sb.append((tmpIndex++)+"."+uc.getTitle()+"\n");
                        pmsNotifyHandler.addNotifyMessage("今日任务",sb.toString(),currentUserId,null);
                        setCacheCounts(currentUserId,tmpIndex);
                        aa++;
                    }else{
                        sb.append((tmpIndex++)+"."+uc.getTitle()+"\n");
                    }
                }

            }
            taskResult.setComment("共发送"+aa+"条用户日历数据");
        }
        return taskResult;
    }

    /**
     * 设置缓存次数（供首页使用）
     * @param userId
     * @param counts
     */
    private void setCacheCounts(Long userId,int counts){
        try {
            CacheHandler cacheHandler = BeanFactoryUtil.getBean(CacheHandler.class);
            String key= MessageFormat.format(CacheKey.USER_TODAY_CALENDAR_COUNTS,userId);
            cacheHandler.set(key,counts+1,24*3600);
        } catch (Exception e) {
            logger.error("设置缓存次数异常",e);
        }
    }

    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
