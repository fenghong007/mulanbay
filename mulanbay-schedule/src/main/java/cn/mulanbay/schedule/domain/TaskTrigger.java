package cn.mulanbay.schedule.domain;


import cn.mulanbay.schedule.enums.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_trigger")
@DynamicInsert
@DynamicUpdate
public class TaskTrigger implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7821258290163948795L;

	private Long id;
	private String name;
	private Long userId;
	private String deployId;
	private String taskClass;
	private Boolean distriable;
	private RedoType redoType;
	private String subTaskCodes;
	private String subTaskNames;
	private Integer allowedRedoTimes;
	private Long timeout;
	private String groupName;
	private TriggerType triggerType;
	private Integer triggerInterval;
	private String triggerParas;
	private String cronExpression;
	private Integer offsetDays;
	private Date firstExecuteTime;
	private Date nextExecuteTime;
	private TriggerStatus triggerStatus;
	private JobExecuteResult lastExecuteResult;
	private Date lastExecuteTime;
	private Long totalCount;
	private Long failCount;
	private Boolean checkUnique;
	private TaskUniqueType uniqueType;
	private Boolean loggable;
	private Boolean notifiable;
	private String execTimePeriods;
	private Date createdTime;
	private Date modifyTime;
	private String comment;


	//由内存和数据库共同计算出，不保存在数据库
	private Integer scheduledStatus;

	// Constructors

	/** default constructor */
	public TaskTrigger() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "user_id", nullable = false, length = 20)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "deploy_id", nullable = false, length = 32)
	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	@Column(name = "task_class", nullable = false, length = 100)
	public String getTaskClass() {
		return taskClass;
	}

	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}

	@Column(name = "distriable", nullable = false)
	public Boolean getDistriable() {
		return distriable;
	}

	public void setDistriable(Boolean distriable) {
		this.distriable = distriable;
	}

	@Column(name = "redo_type", nullable = false)
	public RedoType getRedoType() {
		return redoType;
	}

	public void setRedoType(RedoType redoType) {
		this.redoType = redoType;
	}

	@Column(name = "sub_task_codes")
	public String getSubTaskCodes() {
		return subTaskCodes;
	}

	public void setSubTaskCodes(String subTaskCodes) {
		this.subTaskCodes = subTaskCodes;
	}

	@Column(name = "sub_task_names")
	public String getSubTaskNames() {
		return subTaskNames;
	}

	public void setSubTaskNames(String subTaskNames) {
		this.subTaskNames = subTaskNames;
	}

	@Column(name = "allowed_redo_times")
	public Integer getAllowedRedoTimes() {
		return allowedRedoTimes;
	}

	public void setAllowedRedoTimes(Integer allowedRedoTimes) {
		this.allowedRedoTimes = allowedRedoTimes;
	}

	@Column(name = "timeout")
	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	@Column(name = "group_name")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "trigger_type")
	public TriggerType getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(TriggerType triggerType) {
		this.triggerType = triggerType;
	}

	@Column(name = "trigger_interval")
	public Integer getTriggerInterval() {
		return triggerInterval;
	}

	public void setTriggerInterval(Integer triggerInterval) {
		this.triggerInterval = triggerInterval;
	}

	@Column(name = "trigger_paras")
	public String getTriggerParas() {
		return triggerParas;
	}

	public void setTriggerParas(String triggerParas) {
		this.triggerParas = triggerParas;
	}


    @Column(name = "cron_expression")
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

	@Column(name = "offset_days")
	public Integer getOffsetDays() {
		return offsetDays;
	}

	public void setOffsetDays(Integer offsetDays) {
		this.offsetDays = offsetDays;
	}

	@Column(name = "first_execute_time")
	public Date getFirstExecuteTime() {
		return firstExecuteTime;
	}

	public void setFirstExecuteTime(Date firstExecuteTime) {
		this.firstExecuteTime = firstExecuteTime;
	}

	@Column(name = "next_execute_time")
	public Date getNextExecuteTime() {
		return nextExecuteTime;
	}

	public void setNextExecuteTime(Date nextExecuteTime) {
		this.nextExecuteTime = nextExecuteTime;
	}

	@Column(name = "trigger_status")
	public TriggerStatus getTriggerStatus() {
		return triggerStatus;
	}

	public void setTriggerStatus(TriggerStatus triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	@Column(name = "last_execute_result")
	public JobExecuteResult getLastExecuteResult() {
		return lastExecuteResult;
	}

	public void setLastExecuteResult(JobExecuteResult lastExecuteResult) {
		this.lastExecuteResult = lastExecuteResult;
	}

	@Column(name = "last_execute_time")
	public Date getLastExecuteTime() {
		return lastExecuteTime;
	}

	public void setLastExecuteTime(Date lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	@Column(name = "total_count")
	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	@Column(name = "fail_count")
	public Long getFailCount() {
		return failCount;
	}

	public void setFailCount(Long failCount) {
		this.failCount = failCount;
	}

	@Column(name = "check_unique")
	public Boolean getCheckUnique() {
		return checkUnique;
	}

	public void setCheckUnique(Boolean checkUnique) {
		this.checkUnique = checkUnique;
	}

	@Column(name = "unique_type")
	public TaskUniqueType getUniqueType() {
		return uniqueType;
	}

	public void setUniqueType(TaskUniqueType uniqueType) {
		this.uniqueType = uniqueType;
	}

	@Column(name = "loggable")
	public Boolean getLoggable() {
		return loggable;
	}

	public void setLoggable(Boolean loggable) {
		this.loggable = loggable;
	}

	@Column(name = "notifiable")
	public Boolean getNotifiable() {
		return notifiable;
	}

	public void setNotifiable(Boolean notifiable) {
		this.notifiable = notifiable;
	}

	@Column(name = "exec_time_periods")
	public String getExecTimePeriods() {
		return execTimePeriods;
	}

	public void setExecTimePeriods(String execTimePeriods) {
		this.execTimePeriods = execTimePeriods;
	}

	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "modify_time")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "TaskTrigger [taskTriggerId=" + id
				+ ", taskTriggerName=" + name + "]";
	}

	@Transient
	public Long getTillNextExecuteTime() {
		Date dd=nextExecuteTime;
		if(dd==null){
			dd= firstExecuteTime;
		}
		return (dd.getTime() - System.currentTimeMillis()) / 1000;
	}

	@Transient
	public Integer getScheduledStatus() {
		return scheduledStatus;
	}

	public void setScheduledStatus(Integer scheduledStatus) {
		this.scheduledStatus = scheduledStatus;
	}

	@Transient
	public String getRedoTypeName(){
		return redoType==null ? null : redoType.getName();
	}

	@Transient
	public String getTriggerTypeName(){
		return triggerType==null ? null : triggerType.getName();
	}

	@Transient
	public String getLastExecuteResultName(){
		return lastExecuteResult==null ? null : lastExecuteResult.getName();
	}

	@Transient
	public String getUniqueTypeName(){
		return uniqueType==null ? null : uniqueType.getName();
	}

}