package cn.mulanbay.schedule.job;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.IPAddressUtil;
import cn.mulanbay.schedule.*;
import cn.mulanbay.schedule.domain.TaskLog;
import cn.mulanbay.schedule.domain.TaskTrigger;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.enums.TaskUniqueType;
import cn.mulanbay.schedule.enums.TriggerStatus;
import cn.mulanbay.schedule.enums.TriggerType;
import cn.mulanbay.schedule.lock.LockStatus;
import cn.mulanbay.schedule.lock.ScheduleLocker;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 所有调度任务的基类，定义调度的流程
 * 同时支持新建的调度和重做的调度
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public abstract class AbstractBaseJob implements Job {

	private static Logger logger = Logger.getLogger(AbstractBaseJob.class);

	protected final static ParaCheckResult DEFAULT_SUCCESS_PARA_CHECK =new ParaCheckResult();

	// 调度资源
	protected QuartzSource quartzSource;

	//调度触发器（无论新建还是重做，都必须有该对象）
	private TaskTrigger taskTrigger;

	private boolean isRedo = false;

	// 只有重做的情况下才会有该对象
	private TaskLog taskLog;

	// 是否正在做
	private boolean isDoing = false;

	// 是否检查过参数
	private boolean isParaChecked = false;

	// 是否要更新调度（重做情况下有效）
	private boolean isUpdateTrigger;

	//调度的出发时间
	private Date scheduledFireTime;

	public TaskTrigger getTaskTrigger() {
		if (isRedo) {
			return taskLog.getTaskTrigger();
		}
		return taskTrigger;
	}

	public void setQuartzSource(QuartzSource quartzSource) {
		this.quartzSource = quartzSource;
	}

	public TaskLog getTaskLog() {
		return taskLog;
	}

	public void setTaskLog(TaskLog taskLog) {
		this.taskLog = taskLog;
	}

	public boolean isRedo() {
		return isRedo;
	}

	public void setRedo(boolean isRedo) {
		this.isRedo = isRedo;
	}

	public void setUpdateTrigger(boolean isUpdateTrigger) {
		this.isUpdateTrigger = isUpdateTrigger;
	}

	public AbstractBaseJob() {
		super();
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(arg0!=null){
			scheduledFireTime=arg0.getScheduledFireTime();
		}
		if (isRedo) {
			redoJob(arg0);
		} else {
			newJob(arg0);
		}
	}

	/**
	 * 新的任务调度
	 * 
	 * @param arg0
	 * @throws JobExecutionException
	 */
	private void newJob(JobExecutionContext arg0) throws JobExecutionException {
		LockStatus lockStatus = null;
        long costTime=0L;
		try {
			// 获取调度器
			taskTrigger = (TaskTrigger) arg0.getMergedJobDataMap().get(
					QuartzConstant.SHCEDULE_TASK_TRIGGER);
			quartzSource = (QuartzSource) arg0.getMergedJobDataMap().get(
					QuartzConstant.SCHEDULE_QUARTZ_SOURCE);
			boolean checkUnique = this.checkScheduleExecUnique();
			if(!checkUnique){
				logger.error("调度任务id="+taskTrigger.getId()+"重复执行");
				return;
			}
			lockStatus = this.lock();
			if(lockStatus!=LockStatus.SUCESS){
				return;
			}
			logger.debug("开始执行[" + taskTrigger.getName() + "]");
			taskLog = new TaskLog();
			// TODO线程同步
			if (isDoing) {
				logger.debug("任务[" + taskTrigger.getName()
						+ "]已经在执行");
				taskLog.setLogComment("任务[" + taskTrigger.getName()
						+ "]已经在执行，跳过本次执行。");
				taskTrigger.setLastExecuteResult(JobExecuteResult.SKIP);
				return;
			}
			isDoing = true;
			Date startTime = new Date();
			// 新的
			logger.debug("新的JOB[" + taskTrigger.getName() + "]");
			taskLog.setStartTime(startTime);
			taskLog.setTaskTrigger(taskTrigger);
			//taskTrigger.setLastExecuteTime(startTime);

			// 执行任务
			TaskResult ar = checkParaAndDoTask();

			taskLog.setExecuteResult(ar.getExecuteResult());
			taskLog.setSubTaskExecuteResults(ar.getSubTaskExecuteResults());
			taskLog.setLogComment(ar.getComment());

			// TODO 需要重新加载taskTrigger
			taskTrigger.setLastExecuteResult(ar.getExecuteResult());
			logger.debug("[" + taskTrigger.getName() + "]执行结束");
		} catch (Exception e) {
			logger.error("[" + taskTrigger.getName() + "]执行异常", e);
			taskTrigger.setLastExecuteResult(JobExecuteResult.FAIL);
			taskLog.setExecuteResult(JobExecuteResult.FAIL);
			taskLog.setLogComment(e.getMessage());
		} finally {
			if(lockStatus ==LockStatus.SUCESS){
				try {
					isDoing = false;
					Date endTime = new Date();
                    costTime = endTime.getTime()-taskLog
                            .getStartTime().getTime();
					SchedulePersistentProcessor processor = this.getPersistentProcessor();

					taskTrigger.setNextExecuteTime(arg0.getNextFireTime());
					taskTrigger.setTotalCount(taskTrigger.getTotalCount() + 1);
					if (taskTrigger.getLastExecuteResult() == JobExecuteResult.FAIL) {
						taskTrigger.setFailCount(taskTrigger.getFailCount() + 1);
					}
					taskTrigger.setLastExecuteTime(new Date());
					if(taskTrigger.getTriggerType()== TriggerType.NOW){
						//一次类型的设为无效
						taskTrigger.setTriggerStatus(TriggerStatus.DISABLE);
					}
					// 更新执行调度
					processor.updateTaskTriggerForNewJob(taskTrigger);
					if(taskTrigger.getLoggable()){
						// 新的
						taskLog.setEndTime(endTime);
						taskLog.setBussDate(getBussDay());
						taskLog.setCostTime(costTime);
						taskLog.setRedoTimes((short) 0);
						// 保存执行日志
						taskLog.setIpAddress(getIpAddress());
						taskLog.setLogComment(cutComment(taskLog.getLogComment()));
						taskLog.setDeployId(quartzSource.getDeployId());
						String scheduleIdentityId = this.generateScheduleIdentityId();
						taskLog.setScheduleIdentityId(scheduleIdentityId);
						processor.saveTaskLog(taskLog);
					}
					notifyMessage();
				} catch (Exception e) {
					logger.error("保存执行记录异常", e);
				}
				// 解锁
				unlock(costTime/1000);
			}else{
				return;
			}

		}
	}

	/**
	 * 重做任务调度
	 * 
	 * @param arg0
	 * @throws JobExecutionException
	 */
	private void redoJob(JobExecutionContext arg0) throws JobExecutionException {
		LockStatus lockStatus = null;
		try {
			taskTrigger = taskLog.getTaskTrigger();
			lockStatus = this.lock();
			if(lockStatus!=LockStatus.SUCESS){
				return;
			}
			logger.debug("开始重做任务[" + taskTrigger.getName() + "]");
			Date startTime = new Date();
			if (taskLog.getId() == null) {
				taskLog.setStartTime(startTime);
			} else {
				taskLog.setLastStartTime(startTime);
			}
			// 执行任务
			TaskResult ar = checkParaAndDoTask();

			taskLog.setExecuteResult(ar.getExecuteResult());
			taskLog.setSubTaskExecuteResults(ar.getSubTaskExecuteResults());
			taskLog.setLogComment(ar.getComment());
			logger.debug("[" + taskLog.getTaskTrigger().getName()
					+ "]执行结束");
		} catch (Exception e) {
			logger.error("[" + taskLog.getTaskTrigger().getName()
					+ "]执行异常", e);
			taskLog.setExecuteResult(JobExecuteResult.FAIL);
			taskLog.setLogComment(e.getMessage());
		} finally {
			if(lockStatus ==LockStatus.SUCESS){
				try {
					Date endTime = new Date();
					SchedulePersistentProcessor processor = this.getPersistentProcessor();
					taskLog.setIpAddress(getIpAddress());
					taskLog.setDeployId(quartzSource.getDeployId());
					if (taskLog.getId() == null) {
						String lc = taskLog.getLogComment() == null ? "" : taskLog
								.getLogComment();
						taskLog.setLogComment(lc + "[未执行，手动重做]");
						taskLog.setEndTime(endTime);
						taskLog.setRedoTimes((short) 0);
						taskLog.setCostTime((taskLog.getEndTime().getTime() - taskLog
								.getStartTime().getTime()));
						taskLog.setLogComment(cutComment(taskLog.getLogComment()));
						String scheduleIdentityId = this.generateScheduleIdentityId();
						taskLog.setScheduleIdentityId(scheduleIdentityId);
						processor.saveTaskLog(taskLog);
					} else {
						taskLog.setLastEndTime(endTime);
						short redoTime = (taskLog.getRedoTimes() == null ? 0
								: taskLog.getRedoTimes().shortValue());
						taskLog.setRedoTimes((short) (redoTime + 1));
						taskLog.setCostTime((taskLog.getLastEndTime().getTime() - taskLog
								.getLastStartTime().getTime()));
						taskLog.setLogComment(cutComment(taskLog.getLogComment()));
						processor.updateTaskLog(taskLog);
					}
					if (isUpdateTrigger) {
						taskTrigger.setTotalCount(taskTrigger.getTotalCount() + 1);
						if (taskLog.getExecuteResult() == JobExecuteResult.FAIL) {
							taskTrigger
									.setFailCount(taskTrigger.getFailCount() + 1);
						}
						taskTrigger
								.setLastExecuteResult(taskLog.getExecuteResult());
						taskTrigger.setLastExecuteTime(endTime);
						// 通知调度器更新
						//taskTrigger.setModifyTime(new Date());
						processor.updateTaskTriggerForRedoJob(taskTrigger);
					}
				} catch (Exception e) {
					logger.error("保存执行记录异常", e);
				}
				notifyMessage();
				unlock(taskLog.getCostTime()/1000);
			}else{
				return;
			}

		}
	}

	/**
	 * 生成唯一的调度编号
	 * @return
	 */
	private String generateScheduleIdentityId(){
		String dateTimeString = DateUtil.getFormatDate(scheduledFireTime,DateUtil.Format24Datetime2);
		return taskTrigger.getId()+"_"+dateTimeString;
	}

	/**
	 * 上锁，只针对分布式调度的有效
	 * @return
	 */
	private LockStatus lock(){
		if(!taskTrigger.getDistriable()){
			//不支持分布式的不需要上锁
			return LockStatus.SUCESS;
		}
		ScheduleLocker scheduleLocker = quartzSource.getScheduleLocker();
		if(scheduleLocker==null){
			logger.debug("没有调度锁配置，无法进行锁");
			return LockStatus.SUCESS;
		}else{
			String identityKey = getIdentityKey();
			LockStatus lockStatus= scheduleLocker.lock(identityKey,taskTrigger.getTimeout());
			logger.debug("调度锁key="+identityKey+",上锁结果:"+lockStatus);
			return lockStatus;
		}

	}

	/**
	 * 解锁，只针对分布式调度的有效
	 * (1)这里其实会涉及到一个时钟同步问题，不同的主机时钟差别很大，那么时间很难控制。
	 * 支持分布式的任务如果其任务执行时间很快最好在同一台主机不同的应用上跑
	 * (2)另外一种identifyKey通过JobExecutionContext的ScheduleFireTime来确定唯一性(精确到秒)
	 * 可以解决时钟不同步的问题
	 * (3)目前已经添加scheduleIdentityId检查，及时时钟不同步也无问题
	 * @param costSeconds
	 * @return
	 */
	private LockStatus unlock(long costSeconds){
		if(!taskTrigger.getDistriable()){
			//不支持分布式的不需要上锁
			return LockStatus.SUCESS;
		}
		if(costSeconds<quartzSource.getDistriTaskMinCost()){
			try {
				Thread.sleep((quartzSource.getDistriTaskMinCost()-costSeconds)*1000L);
			} catch (Exception e) {
				logger.error("unlock sleep error",e);
			}
		}
		ScheduleLocker scheduleLocker = quartzSource.getScheduleLocker();
		if(scheduleLocker==null){
			return LockStatus.SUCESS;
		}else{
			String identityKey = getIdentityKey();
			LockStatus lockStatus =  scheduleLocker.unlock(getIdentityKey());
			logger.debug("调度锁key="+identityKey+",解锁结果:"+lockStatus);
			return lockStatus;
		}

	}

	/**
	 * 每次调度任务执行的唯一编号
	 * @return
	 */
	private String getIdentityKey(){
		String prefix= (isRedo ? "redo":"new");
		return prefix+"_"+taskTrigger.getGroupName()+"_"+taskTrigger.getId();
	}

	/**
	 * 消息通知
	 * 针对执行失败操作
	 */
	private void notifyMessage(){
		if(quartzSource.getNotifiableProcessor()!=null){
			JobExecuteResult jer = taskLog.getExecuteResult();
			if(jer==JobExecuteResult.FAIL||jer==JobExecuteResult.DUPLICATE){
				Long taskTriggerId = taskTrigger.getId();
				String title="调度器["+taskTrigger.getName()+"]调度执行异常";
				String content="调度器["+taskTrigger.getName()+"]调度在"+
						DateUtil.getFormatDate(new Date(),DateUtil.Format24Datetime)+"执行异常,执行结果:"+jer+"，错误信息:"+
						taskLog.getLogComment();
				quartzSource.getNotifiableProcessor().notifyMessage(taskTriggerId,title,content);
			}
		}
	}

	/**
	 * 业务操作，具体的业务逻辑
	 * 由子类来实现
	 * @return
	 */
	public abstract TaskResult doTask();

	/**
	 * 检查调度执行是否唯一
	 * @return
	 */
	private boolean checkScheduleExecUnique(){
		if(taskTrigger.getCheckUnique()){
			if(taskTrigger.getUniqueType()== TaskUniqueType.IDENTITY){
				String scheduleIdentityId = this.generateScheduleIdentityId();
				boolean isExit = this.getPersistentProcessor().isTaskLogExit(scheduleIdentityId);
				if(isExit){
					return false;
				}
			}else{
				boolean isExit = this.getPersistentProcessor().isTaskLogExit(taskTrigger.getId(),this.getBussDay());
				if(isExit){
					return false;
				}
			}
		}
		return true;
	}


	private TaskResult checkParaAndDoTask() {
		if (isParaChecked) {
			if(!checkNeedExec()){
				return new TaskResult(JobExecuteResult.SKIP);
			}
			// 已经检查过
			return doTask();
		} else {
			logger.info("检查[" + getTaskTrigger().getName() + "]的参数:"
					+ getTaskTrigger().getTriggerParas());
			ParaCheckResult pcr = checkTriggerPara(getTaskTrigger()
					.getTriggerParas());
			if (pcr.getErrorCode() != ErrorCode.SUCCESS) {
				TaskResult tr = new TaskResult();
				tr.setExecuteResult(JobExecuteResult.FAIL);
				tr.setComment("检查参数异常，错误代码：" + pcr.getErrorCode() + ","
						+ pcr.getMessage());
				return tr;
			} else {
				isParaChecked = true;
				return doTask();
			}
		}
	}

	/**
	 * 获取业务统计的日期,只精确到天
	 *
	 * @return
	 */
	public Date getBussDay() {
		if (isRedo) {
			return taskLog.getBussDate();
		} else {
			int offsetDays = taskTrigger.getOffsetDays().intValue();
			//需要根据调度时间来计算，因为有可能很多年没有执行，quartz会从最近的一次开始调度
			Date d = DateUtil.getDate(offsetDays,scheduledFireTime);
			return d;
		}
	}

	private String getIpAddress() {
		return IPAddressUtil.getLocalIpAddress();
	}

	private SchedulePersistentProcessor getPersistentProcessor() {
		return quartzSource.getSchedulePersistentProcessor();
	}

	/**
	 * 检查是否需要执行
	 * 参数格式：01:00-02:20,03:30-17:40
	 * @return
	 */
	private boolean checkNeedExec(){
		String execTimePeriods = taskTrigger.getExecTimePeriods();
		boolean b = DateUtil.checkTimeAllow(execTimePeriods);
		if(!b){
			logger.debug("ID["+taskTrigger.getId()+"],name["+taskTrigger.getName()+"]的调度在当前时间配置为不执行");
		}
		return b;
	}

	/**
	 * 避免备注过长
	 * 
	 * @param s
	 * @return
	 */
	private String cutComment(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		} else {
			if (s.length() > 255) {
				// 避免过长
				s = s.substring(0, 255);
			}
			return s;
		}
	}

	/**
	 * 检查调度的参数
	 * 
	 * @return
	 */
	public abstract ParaCheckResult checkTriggerPara(String triggerDetail);
}
