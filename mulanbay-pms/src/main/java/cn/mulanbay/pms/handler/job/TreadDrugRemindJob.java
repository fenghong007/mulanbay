package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.domain.TreatDrug;
import cn.mulanbay.pms.persistent.service.TreatService;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * 用户用药提醒job
 * 根据药的使用说明再根据当前时间判断是否要用药了
 * 目前还未真正使用
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class TreadDrugRemindJob extends AbstractBaseRemindJob {

    private static Logger logger = Logger.getLogger(TreadDrugRemindJob.class);

    CacheHandler cacheHandler;

    TreatService treatService=null;

    PmsNotifyHandler pmsNotifyHandler=null;

    String remindTime ="08:30";

    @Override
    public TaskResult doTask() {
        TaskResult taskResult = new TaskResult();
        cacheHandler = BeanFactoryUtil.getBean(CacheHandler.class);
        treatService = BeanFactoryUtil.getBean(TreatService.class);
        pmsNotifyHandler = BeanFactoryUtil.getBean(PmsNotifyHandler.class);
        Date bussDay = this.getBussDay();
        Date date = DateUtil.getTodayTillMiddleNightDate(bussDay);
        List<TreatDrug> list = treatService.getNeedRemindDrug(date);
        if(list.isEmpty()){
            taskResult.setComment("没有需要提醒的用药");
        }else{
            //目前提醒时间统一由调度器设置
            Date remindNotifyTime = DateUtil.getDate(DateUtil.getFormatDate(new Date(),DateUtil.FormatDay1)+" "+remindTime+":00",DateUtil.Format24Datetime);
            int n=0;
            for(TreatDrug treatDrug : list){
                handleTreatDrug(treatDrug,bussDay,remindNotifyTime);
                n++;
            }
            taskResult.setExecuteResult(JobExecuteResult.SUCESS);
            taskResult.setComment("检查药品共"+n+"个");
        }
        return taskResult;
    }

    private void handleTreatDrug(TreatDrug treatDrug,Date bussDay ,Date remindNotifyTime){
        if(treatDrug.getBeginDate().before(bussDay)){
            //统计昨天的用药记录
            long yesterdayCount = treatService.getDrugDetailCount(treatDrug.getId(),bussDay);
            if(yesterdayCount<treatDrug.getPerTimes().longValue()){
                String title="["+DateUtil.getFormatDate(bussDay,DateUtil.FormatDay1+"]用药次数没达到要求");
                String content="药品["+treatDrug.getName()+"]在["+DateUtil.getFormatDate(bussDay,DateUtil.FormatDay1+"]用药次数没达到要求," +
                        "应该要["+treatDrug.getPerTimes().longValue()+"]次,实际只有["+yesterdayCount+"]次");
                pmsNotifyHandler.addNotifyMessage(title,content,treatDrug.getUserId(),remindNotifyTime);
            }
        }
        //今天需要用药
        String title="药品["+treatDrug.getName()+"]用药提醒";
        String content="药品["+treatDrug.getName()+"]今天需要用药["+treatDrug.getPerTimes().longValue()+"]次";
        pmsNotifyHandler.addNotifyMessage(title,content,treatDrug.getUserId(),remindNotifyTime);

    }


    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        if(!StringUtil.isEmpty(triggerDetail)){
            remindTime = triggerDetail;
        }
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
