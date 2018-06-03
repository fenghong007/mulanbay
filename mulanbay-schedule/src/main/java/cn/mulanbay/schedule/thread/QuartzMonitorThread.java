package cn.mulanbay.schedule.thread;

import cn.mulanbay.common.thread.EnhanceThread;
import cn.mulanbay.schedule.QuartzServer;
import cn.mulanbay.schedule.ScheduleInfo;
import cn.mulanbay.schedule.domain.TaskTrigger;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 调度监控线程（总入口）
 * 
 * @author fh
 * 
 */
public class QuartzMonitorThread extends EnhanceThread {

	private static Logger logger = Logger.getLogger(QuartzMonitorThread.class);

	QuartzServer quartzServer;

	private boolean isCheck = true;

	public QuartzMonitorThread(QuartzServer quartzServer) {
		super("调度监控线程");
		this.quartzServer =quartzServer;
		// 默认60秒
		this.setInterval(60);
	}

	@Override
	public void doTask() {
		if (isCheck) {
			checkSchedule(false);
		} else {
			logger.debug("当前设置为不检查调度");
		}
	}

	/**
	 * 检查调度
	 * @param isForce
	 */
	public void checkSchedule(boolean isForce) {
		try {
			boolean b = quartzServer.isSchedule();
			if (b) {
                // 加载触发器列表
                List<TaskTrigger> list = getActiveScheduleList();
                synchronized (quartzServer) {
                    quartzServer.setTriggerList(list);
                    quartzServer.refleshSchedule(isForce);
                }
            } else {
                logger.warn("调度没有开启或者已经停止");
                synchronized (quartzServer) {
                    quartzServer.refleshSchedule(isForce);
                }
            }
		} catch (Exception e) {
			logger.error("检查调度异常",e);
		}

	}

	/**
	 * 获取正常的调度列表
	 * 
	 * @return
	 */
	private List<TaskTrigger> getActiveScheduleList() {
		return quartzServer.getQuartzSource().getSchedulePersistentProcessor().getActiveTaskTrigger(
				quartzServer.getQuartzSource().getDeployId(),quartzServer.getQuartzSource().isSupportDistri());
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	/**
	 * 获取调度信息列表
	 * @return
	 */
	public ScheduleInfo getScheduleInfo() {
		ScheduleInfo si = new ScheduleInfo();
		si.setDeployId(quartzServer.getQuartzSource().getDeployId());
		si.setCheck(isCheck);
		si.setInterval(interval);
		si.setSchedule(quartzServer.isSchedule());
		si.setScheduleJobsCount(quartzServer.getScheduleJobsCount());
		si.setCurrentlyExecutingJobsCount(quartzServer
				.getCurrentlyExecutingJobsCount());
		return si;
	}

	@Override
	public void stopThread() {
		try {
			this.isStop = true;
			this.interrupt();
		} catch (Exception e) {
			logger.error("调度检查线程停止异常", e);
		}
	}
}
