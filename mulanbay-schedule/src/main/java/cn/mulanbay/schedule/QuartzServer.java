package cn.mulanbay.schedule;

import cn.mulanbay.schedule.domain.TaskTrigger;
import cn.mulanbay.schedule.job.AbstractBaseJob;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;

/**
 * 调度服务器
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class QuartzServer {

	private static Logger logger = Logger.getLogger(QuartzServer.class);

	private StdSchedulerFactory factory;

	private Scheduler scheduler;

	private List<TaskTrigger> triggerList;

	private boolean isSchedule = true;

	private QuartzSource quartzSource;

	public QuartzServer() {
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		try {
			logger.debug("开始初始化调度工厂");
			factory = new StdSchedulerFactory();
			scheduler = factory.getScheduler();
			scheduler.start();
			logger.debug("初始化调度工厂结束");

		} catch (Exception e) {
			logger.error("初始化调度工厂异常", e);
		}

	}

	public QuartzSource getQuartzSource() {
		return quartzSource;
	}

	public void setQuartzSource(QuartzSource quartzSource) {
		this.quartzSource = quartzSource;
	}

	public List<TaskTrigger> getTriggerList() {
		return triggerList;
	}

	public void setTriggerList(List<TaskTrigger> triggerList) {
		this.triggerList = triggerList;
	}

	public boolean isSchedule() {
		return isSchedule;
	}

	public void setSchedule(boolean isSchedule) {
		this.isSchedule = isSchedule;
	}

	/**
	 * 获取调度
	 * 
	 * @param taskTriggerId
	 * @return
	 */
	public TaskTrigger getScheduledTaskTrigger(long taskTriggerId) {
		if (triggerList == null || triggerList.isEmpty()) {
			return null;
		} else {
			for (TaskTrigger tt : triggerList) {
				if (tt.getId() == taskTriggerId) {
					return tt;
				}
			}
			return null;
		}
	}

	/**
	 * 是否已经被调度
	 * 
	 * @param taskTriggerId
	 * @return
	 */
	public boolean isTaskTriggerScheduled(long taskTriggerId) {
		if (triggerList == null || triggerList.isEmpty()) {
			return false;
		} else {
			for (TaskTrigger tt : triggerList) {
				if (tt.getId() == taskTriggerId) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 是否已经被调度
	 * 
	 * @param taskTriggerId
	 * @return
	 */
	public boolean isTaskTriggerExecuting(long taskTriggerId) {
		try {
			List<JobExecutionContext> list = scheduler
					.getCurrentlyExecutingJobs();
			if (list == null || list.isEmpty()) {
				return false;
			} else {
				for (JobExecutionContext jc : list) {
					JobDetail job = jc.getJobDetail();
					TaskTrigger tt = (TaskTrigger) job.getJobDataMap().get(
							QuartzConstant.SHCEDULE_TASK_TRIGGER);
					if (tt.getId() == taskTriggerId) {
						return true;
					}
				}
				return false;
			}
		} catch (Exception e) {
			logger.error("获取调度任务是否正在执行异常", e);
			return false;
		}
	}

	/**
	 * 返回正在调度执行的job数
	 * 
	 * @return
	 */
	public int getCurrentlyExecutingJobsCount() {
		try {
			return scheduler.getCurrentlyExecutingJobs().size();
		} catch (Exception e) {
			logger.error("获取正在调度执行的job数异常", e);
			return -1;
		}
	}
	
	/**
	 * 返回正在调度执行的job
	 * 
	 * @return
	 */
	public List<TaskTrigger> getCurrentlyExecutingJobs() {
		List<TaskTrigger> list = new ArrayList<TaskTrigger>();
		try {
			List<JobExecutionContext> jecs = scheduler
					.getCurrentlyExecutingJobs();
			for (JobExecutionContext jec : jecs) {
				TaskTrigger tt = (TaskTrigger) jec.getJobDetail()
						.getJobDataMap().get(QuartzConstant.SHCEDULE_TASK_TRIGGER);
				list.add(tt);
			}
		} catch (Exception e) {
			logger.error("获取正在调度执行的job异常", e);
		}
		return list;
	}

	/**
	 * 获取调度器的job数
	 * @return
	 */
	public int getScheduleJobsCount() {
		if (triggerList == null) {
			return 0;
		} else {
			return triggerList.size();
		}
	}

	/**
	 * 刷新调度
	 * 
	 * @param isForce
	 *            1.非强制刷新：只有变化的才会去刷新<br>
	 *            2.强制刷新：不考虑是否变化，全部刷新<br>
	 */
	public void refleshSchedule(boolean isForce) {
		scheduleTask(isForce);
	}

	/**
	 * 调度任务
	 * 
	 * @param isForce
	 *            1.非强制刷新：只有变化的才会去刷新<br>
	 *            2.强制刷新：不考虑是否变化，全部刷新<br>
	 */
	private void scheduleTask(boolean isForce) {
		try {
			logger.debug("任务调度开始");
			if (!isSchedule()) {
				scheduler.clear();
				triggerList = null;
				logger.warn("调度已经停止，清空所有调度");
				return;
			}
			if (isForce) {
				scheduler.clear();
				logger.warn("进行调度强制刷新，清空所有调度");
			}
			int changeCount = 0;
			if (triggerList == null || triggerList.isEmpty()) {
				scheduler.clear();
				logger.warn("触发器列表为空，清空所有调度");
				return;
			}
			// 更新调度
			for (TaskTrigger ts : triggerList) {
				JobKey key = JobKey.jobKey(ts.getId().toString(),
						ts.getGroupName());
				JobDetail job = scheduler.getJobDetail(key);
				if (job == null) {
					logger.info("任务：" + ts.getName() + "["
							+ ts.getId() + "]不存在，新增一个任务");
					addTask(ts);
					changeCount++;
				} else {
					TaskTrigger oldTs = (TaskTrigger) job.getJobDataMap().get(
							QuartzConstant.SHCEDULE_TASK_TRIGGER);
					if (ts.getModifyTime() == null) {
						// 说明没有修改，不做处理
						logger.debug("任务：" + ts.getName() + "["
								+ ts.getId() + "]没有变化");
					} else {
						if (oldTs.getModifyTime() == null
								|| ts.getModifyTime().getTime() > oldTs
										.getModifyTime().getTime()) {
							// 修改过
							scheduler.deleteJob(key);
							logger.info("移除一个过时的任务：" + ts.getName()
									+ "[" + ts.getId() + "]，重新调度");
							addTask(ts);
							changeCount++;
						}
					}

				}
			}
			// 没有的要移除
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey key : jobKeys) {
				String name = key.getName();
				String group = key.getGroup();
				logger.debug("[key]name：" + name + ",group:" + group);
				boolean isExit = isInActiveTrigger(name, group);
				if (!isExit) {
					scheduler.deleteJob(key);
					logger.info("移除一个废弃的任务：" + name);
					changeCount++;
				}
			}
			logger.debug("任务调度结束. 任务修改数量：" + changeCount);
		} catch (Exception e) {
			logger.error("调度任务异常", e);
		}
	}

	/**
	 * 关闭调度服务器
	 * 如果应用不是被强行关闭，那么需要手动关闭调度服务
	 *
	 * @param waitForJobsToComplete
	 */
	public void shutdown(boolean waitForJobsToComplete) {
		try {
			logger.warn("开始关闭调度服务器");
			scheduler.clear();
			scheduler.shutdown(waitForJobsToComplete);
			logger.warn("关闭调度服务器结束");
		} catch (Exception e) {
			logger.error("关闭调度服务器异常", e);
		}
	}

	private boolean isInActiveTrigger(String name, String group) {
		for (TaskTrigger ts : triggerList) {
			if (ts.getId().toString().equals(name)
					&& ts.getGroupName().equals(group)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 添加一个调度
	 *
	 * @param ts
	 */
	private void addTask(TaskTrigger ts) {
		try {
			String className = ts.getTaskClass();
			Class<AbstractBaseJob> jobClass = (Class<AbstractBaseJob>) Class.forName(className);

			JobDetail jobDetail = newJob(jobClass).withIdentity(
					ts.getId().toString(), ts.getGroupName())
					.build();
			// 设置参数
			jobDetail.getJobDataMap()
					.put(QuartzConstant.SHCEDULE_TASK_TRIGGER, ts);
			jobDetail.getJobDataMap()
					.put(QuartzConstant.SCHEDULE_QUARTZ_SOURCE, quartzSource);

			Trigger trigger = TriggerFactory.createTrigger(ts);

			scheduler.scheduleJob(jobDetail, trigger);
			logger.info("添加一个任务：" + ts.getName() + "["
					+ ts.getId() + "]");

		} catch (Exception e) {
			logger.error("增加任务异常", e);
		}
	}

}
