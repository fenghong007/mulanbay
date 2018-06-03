package cn.mulanbay.schedule.handler;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.schedule.QuartzServer;
import cn.mulanbay.schedule.QuartzSource;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.domain.TaskLog;
import cn.mulanbay.schedule.domain.TaskTrigger;
import cn.mulanbay.schedule.enums.RedoType;
import cn.mulanbay.schedule.thread.QuartzMonitorThread;
import cn.mulanbay.schedule.thread.RedoThread;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Date;
import java.util.concurrent.*;

/**
 * ${DESCRIPTION}
 * 调度维护总入口
 * 如果自动发布的程序中需要关闭web容器（如tomcat），在启动前先要等待destroy方法中的时长，这样保证程序可以正常关闭、启动
 * 如果直接采用kill -9模式关闭程序，可能会导致正在执行的job异常。
 * 调度服务最好能独立出服务器，因为涉及到部署节点ScheduleHandler问题。
 * @author fenghong
 * @create 2017-10-19 9:43
 **/
public class ScheduleHandler extends BaseHandler {

    private static Logger logger = Logger.getLogger(ScheduleHandler.class);

    private static QuartzMonitorThread quartzMonitorThread;

    private static QuartzServer quartzServer;

    private static ExecutorService scheduledThreadPool;

    private boolean enableSchedule =false;

    private QuartzSource quartzSource;

    int corePoolSize;

    public ScheduleHandler() {
        super("调度处理");
    }

    @Override
    public void init() {
        super.init();
        if(this.isEnableSchedule()){
            quartzServer = new QuartzServer();
            quartzServer.setQuartzSource(quartzSource);
            logger.debug("初始化调度服务");
            quartzMonitorThread = new QuartzMonitorThread(quartzServer);
            quartzMonitorThread.start();
            logger.debug("启动调度监控服务");
            //调度线程的线程池采用： 丢弃任务并抛出RejectedExecutionException异常。 (默认)
            ThreadFactory threadFactory = new CustomizableThreadFactory("scheduleHandler");
            scheduledThreadPool = new ThreadPoolExecutor(corePoolSize,100,10L,
                    TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(200),
                    threadFactory,new ThreadPoolExecutor.AbortPolicy());
        }else {
            logger.debug("该应用设置为不启动调度服务");
        }

    }

    @Override
    public Boolean selfCheck() {
        return super.selfCheck();
    }

    @Override
    public void destroy() {
        if(this.isEnableSchedule()){
            int activeJobs = quartzServer.getScheduleJobsCount();
            logger.warn("目前活跃的调度任务数:"+activeJobs);
            quartzServer.shutdown(shutDownWaitForJobsToComplete());
            quartzMonitorThread.stopThread();
            scheduledThreadPool.shutdown();
            try {
                long waitSeconds = this.getShutDownWaitSeconds();
                if(waitSeconds>0&&activeJobs>0){
                    Thread.sleep(getShutDownWaitSeconds()*1000);
                }
            } catch (InterruptedException e) {
                logger.error("destroy error:"+e.getMessage());
            }
        }
        super.destroy();
    }

    /**
     * 设置调度检查
     * @param check
     */
    public void setMonitorCheck(boolean check){
        quartzMonitorThread.setCheck(check);
    }

    public void setQuartzSource(QuartzSource quartzSource) {
        this.quartzSource = quartzSource;
    }

    /**
     * 根据 Job 调度日志来自动重做
     * @param logId
     * @param isSync 是否同步
     */
    public void manualRedo(long logId,boolean isSync) {
        if(!this.isEnableSchedule()){
            throw new ApplicationException(ScheduleErrorCode.SCHEDULE_NOT_ENABLED);
        }
        TaskLog taskLog=quartzSource.getSchedulePersistentProcessor().selectTaskLog(logId);
        if(taskLog.getTaskTrigger().getRedoType()== RedoType.CANNOT){
            throw new ApplicationException(ScheduleErrorCode.TRIGGER_CANNOT_REDO);
        }
        startRedoJob(taskLog,isSync);
    }

    /**
     * 手动执行一个配置的任务
     * @param triggerId
     * @param bussDay
     * @param isSync 是否同步
     */
    public void manualNew(long triggerId,Date bussDay,boolean isSync) {
        if(!this.isEnableSchedule()){
            throw new ApplicationException(ScheduleErrorCode.SCHEDULE_NOT_ENABLED);
        }
        TaskTrigger taskTrigger = quartzSource.getSchedulePersistentProcessor().selectTaskTrigger(triggerId);
        if(taskTrigger.getCheckUnique()){
            boolean b =quartzSource.getSchedulePersistentProcessor().isTaskLogExit(triggerId,bussDay);
            if(b){
                throw new ApplicationException(ScheduleErrorCode.SCHEDULE_ALREADY_EXECED);
            }
        }
        TaskLog taskLog = new TaskLog();
        taskLog.setBussDate(bussDay);
        taskLog.setTaskTrigger(taskTrigger);
        startRedoJob(taskLog,isSync);
    }

    /**
     * 开启重做任务
     *
     * @param taskLog
     * @param isSync
     */
    private void startRedoJob(TaskLog taskLog,boolean isSync){
        RedoThread redoThread = new RedoThread(taskLog,true);
        redoThread.setQuartzSource(quartzSource);
        if(!isSync){
            scheduledThreadPool.execute(redoThread);
            //redoThread.start();
            logger.debug("启动一个调度日志重做线程任务");
        }else{
            redoThread.run();
            logger.debug("执行一个调度日志重做线程任务");
        }
    }

    /**
     * 关闭时是否等待任务完成
     * 默认是需要等待
     * @return
     */
    public boolean shutDownWaitForJobsToComplete(){
        return true;
    }

    /**
     * 停止等待时间（有些正在运行的任务可能需要等待一会）
     * 默认5秒
     * @return
     */
    public long getShutDownWaitSeconds(){
        return 5;
    }

    public boolean isEnableSchedule() {
        return enableSchedule;
    }

    public void setEnableSchedule(boolean enableSchedule) {
        this.enableSchedule = enableSchedule;
    }

    /**
     * 当前正在运行的job数
     * @return
     */
    public int getCurrentlyExecutingJobsCount(){
        if(enableSchedule){
            return quartzServer.getCurrentlyExecutingJobsCount();
        }else {
            return 0;
        }
    }

    public int getScheduleJobsCount(){
        if(enableSchedule){
            return quartzServer.getScheduleJobsCount();
        }else {
            return 0;
        }
    }

    /**
     * 设置调度状态
     * @param b
     */
    public void setScheduleStatus(boolean b){
        if(!this.isEnableSchedule()){
            throw new ApplicationException(ScheduleErrorCode.SCHEDULE_NOT_ENABLED);
        }
        quartzServer.setSchedule(b);
    }
}
