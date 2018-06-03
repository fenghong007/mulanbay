package cn.mulanbay.schedule;

public class ScheduleInfo {

	private String deployId;
	
	private boolean isCheck;
	
	private boolean isSchedule;
	
	private long interval;
	
	private int scheduleJobsCount;
	
	private int currentlyExecutingJobsCount;

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public boolean isSchedule() {
		return isSchedule;
	}

	public void setSchedule(boolean isSchedule) {
		this.isSchedule = isSchedule;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public int getScheduleJobsCount() {
		return scheduleJobsCount;
	}

	public void setScheduleJobsCount(int scheduleJobsCount) {
		this.scheduleJobsCount = scheduleJobsCount;
	}

	public int getCurrentlyExecutingJobsCount() {
		return currentlyExecutingJobsCount;
	}

	public void setCurrentlyExecutingJobsCount(int currentlyExecutingJobsCount) {
		this.currentlyExecutingJobsCount = currentlyExecutingJobsCount;
	}
	
	
}
