package cn.mulanbay.schedule;

import cn.mulanbay.schedule.domain.TaskLog;
import cn.mulanbay.schedule.domain.TaskTrigger;
import cn.mulanbay.schedule.enums.TriggerStatus;

import java.util.Date;
import java.util.List;

/**
 * 调度持久层操作接口定义
 *
 * @author fenghong
 * @create 2017-10-19 9:26
 **/
public interface SchedulePersistentProcessor {

    void updateTaskTriggerForNewJob(TaskTrigger taskTrigger);

    void saveTaskLog(TaskLog taskLog);

    TaskLog selectTaskLog(Long logId);

    TaskTrigger selectTaskTrigger(Long triggerId);

    void updateTaskLog(TaskLog taskLog);

    void updateTaskTriggerForRedoJob(TaskTrigger taskTrigger);

    void updateTaskTriggerStatus(Long triggerId, TriggerStatus status);

    /**
     * 获取当前有效的调度列表
     * @param deployId
     * @supportDistri 必须要有锁机制
     * @return
     */
    List<TaskTrigger> getActiveTaskTrigger(String deployId, boolean supportDistri);

    /**
     * 判断调度日志是否存在了，针对周期为日类型以上的
     * @param taskTriggerId
     * @param bussDate
     * @return
     */
    boolean isTaskLogExit(Long taskTriggerId, Date bussDate);

    /**
     * 判断调度日志是否存在了
     * @param scheduleIdentityId
     * @return
     */
    boolean isTaskLogExit(String scheduleIdentityId);

    /**
     * 获取自动重做的任务列表
     * @param deployId
     * @param supportDistri
     * @param minDate 最小开始时间
     * @return
     */
    List<TaskLog> getAutoRedoTaskLogs(String deployId, boolean supportDistri, Date minDate);

}
