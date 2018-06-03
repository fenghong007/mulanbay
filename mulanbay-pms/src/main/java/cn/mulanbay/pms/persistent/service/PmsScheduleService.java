package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.schedule.domain.TaskLog;
import cn.mulanbay.schedule.domain.TaskTrigger;
import cn.mulanbay.schedule.enums.TriggerStatus;
import cn.mulanbay.schedule.impl.HibernatePersistentProcessor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 需要全部复写父类的方法，否则会报：Could not obtain transaction-synchronized Session for current thread
 */
@Service
public class PmsScheduleService extends HibernatePersistentProcessor {

    @Override
    public List<TaskTrigger> getActiveTaskTrigger(String deployId, boolean supportDistri) {
        return super.getActiveTaskTrigger(deployId, supportDistri);
    }

    @Override
    public void updateTaskTriggerForNewJob(TaskTrigger taskTrigger) {
        super.updateTaskTriggerForNewJob(taskTrigger);
    }

    @Override
    public void saveTaskLog(TaskLog taskLog) {
        super.saveTaskLog(taskLog);
    }

    @Override
    public TaskLog selectTaskLog(Long logId) {
        return super.selectTaskLog(logId);
    }

    @Override
    public TaskTrigger selectTaskTrigger(Long triggerId) {
        return super.selectTaskTrigger(triggerId);
    }

    @Override
    public void updateTaskLog(TaskLog taskLog) {
        super.updateTaskLog(taskLog);
    }

    @Override
    public void updateTaskTriggerForRedoJob(TaskTrigger taskTrigger) {
        super.updateTaskTriggerForRedoJob(taskTrigger);
    }

    @Override
    public void updateTaskTriggerStatus(Long triggerId, TriggerStatus status) {
        super.updateTaskTriggerStatus(triggerId, status);
    }

    @Override
    public boolean isTaskLogExit(Long taskTriggerId, Date bussDate) {
        return super.isTaskLogExit(taskTriggerId, bussDate);
    }

    @Override
    public boolean isTaskLogExit(String scheduleIdentityId) {
        return super.isTaskLogExit(scheduleIdentityId);
    }

    @Override
    public List<TaskLog> getAutoRedoTaskLogs(String deployId, boolean supportDistri, Date minDate) {
        return super.getAutoRedoTaskLogs(deployId, supportDistri, minDate);
    }
}
