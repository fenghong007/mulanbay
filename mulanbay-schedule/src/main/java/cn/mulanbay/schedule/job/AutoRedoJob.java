package cn.mulanbay.schedule.job;

import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.domain.TaskLog;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.thread.RedoThread;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 *
 * 自动错做的调度
 * 有些调度执行失败后，可能当时因为网络原因或资源原因
 * 自动重做调度自动化重做这些调度
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class AutoRedoJob extends AbstractBaseJob {

    private static Logger logger = Logger.getLogger(AutoRedoJob.class);

    private static TaskResult SUCESS =new TaskResult(JobExecuteResult.SUCESS);

    @Override
    public TaskResult doTask() {
        Date minDate = DateUtil.getDate(-7,new Date());
        List<TaskLog> list = this.quartzSource.getSchedulePersistentProcessor().
                getAutoRedoTaskLogs(this.quartzSource.getDeployId(),this.quartzSource.isSupportDistri(),minDate);
        if(StringUtil.isEmpty(list)){
            logger.debug("没有需要自动重做的任务");
        }else{
            for(TaskLog taskLog:list){
                autoRedo(taskLog);
            }
        }
        return SUCESS;
    }

    private void autoRedo(TaskLog taskLog){
        try {
            RedoThread redoThread = new RedoThread(taskLog);
            redoThread.setQuartzSource(quartzSource);
            //同步方式执行，否则会导致重复执行
            redoThread.run();
            logger.debug("执行一个调度日志重做线程任务");
        } catch (Exception e) {
            logger.error("自动重做任务，taskLogId="+taskLog.getId()+"异常",e);
        }
    }

    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
