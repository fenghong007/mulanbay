package cn.mulanbay.schedule.job;

import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的多个存储过程调用<br>
 * 如果存储过程带参数，默认支持的参数：业务日期。
 * 
 * @author fenghong
 * 
 */
public class CallMutiProcedureJob extends CallProcedureJob {

	private static Logger logger = Logger.getLogger(CallMutiProcedureJob.class);

	// 是否链式（前面的执行失败与否将影响下一步的执行）
	private boolean iSChain = true;

	List<ProcedureConfig> procedures;

	@Override
	public TaskResult doTask() {
		TaskResult tr = new TaskResult();
		if (procedures == null || procedures.isEmpty()) {
			tr.setComment("存储过程配置为空，不执行");
			return tr;
		}
		String subTaskExecuteResults = "";
		JobExecuteResult executeResult = JobExecuteResult.SUCESS;
		StringBuffer sb = new StringBuffer();
		sb.append("");
		for (ProcedureConfig pc : procedures) {
			TaskResult tt = doSingleProcedure(pc.getProcedureName(),
					pc.getDateParaType());
			subTaskExecuteResults += tt.getExecuteResult() + ",";
			sb.append(tt.getComment() + ",");
			if (iSChain
					&& tt.getExecuteResult() == JobExecuteResult.FAIL) {
				executeResult = JobExecuteResult.FAIL;
				break;
			}
		}
		tr.setExecuteResult(executeResult);
		tr.setComment(sb.toString());
		if (subTaskExecuteResults.length() > 0) {
			subTaskExecuteResults = subTaskExecuteResults.substring(0,
					subTaskExecuteResults.length() - 1);
		}
		tr.setSubTaskExecuteResults(subTaskExecuteResults);
		return tr;
	}

	private TaskResult doSingleProcedure(String procedureName, int dateParaType) {
		try {
			return this.doProcedure(procedureName, dateParaType);
		} catch (Exception e) {
			logger.error("执行存储过程[" + procedureName + "],dateParaType["
					+ dateParaType + "]异常", e);
			TaskResult tr = new TaskResult();
			tr.setExecuteResult(JobExecuteResult.FAIL);
			tr.setComment("执行存储过程[" + procedureName + "],dateParaType["
					+ dateParaType + "]异常：" + e.getMessage());
			return tr;
		}
	}
	@Override
	public ParaCheckResult checkTriggerPara(String triggerDetail) {
		ParaCheckResult rb = new ParaCheckResult();
		rb.setMessage("参数格式为：1. 存储过程名称=2. 时间参数类型（0不需要,1只需要单天,2一天的时间段,3一个月的时间段）.多个存储过程则以|分隔,最后一个为链式参数(true|false)");
		try {
			if (triggerDetail == null || triggerDetail.isEmpty()) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_NULL);
				return rb;
			}
			procedures = new ArrayList<ProcedureConfig>();
			String[] configs = triggerDetail.split(",");
			for (String c : configs) {
				if (c.equalsIgnoreCase("true") || c.equalsIgnoreCase("false")) {
					iSChain = Boolean.parseBoolean(c);
					break;
				}
				/**
				 * 配置参数 <br>
				 * 1. 存储过程名称 <br>
				 * 2. 时间参数类型（0,1,2,3）<br>
				 */
				String[] para = c.split("=");
				if (para.length < 2) {
					rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_LENGTH_ERROR);
					procedures.clear();
					logger.warn("错误的参数：" + c);
					return rb;
				} else {
					ProcedureConfig pc = new ProcedureConfig();
					pc.setProcedureName(para[0]);
					pc.setDateParaType(Integer.valueOf(para[1]));
					procedures.add(pc);
				}
			}

		} catch (Exception e) {
			logger.error("检查参数异常", e);
			rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_FORMAT_ERROR);
			rb.setMessage(rb.getMessage() + "," + e.getMessage());
		}
		return rb;
	}

	/**
	 * 存储过程配置
	 * 
	 * @author fenghong
	 * 
	 */
	class ProcedureConfig {

		private String procedureName;

		private int dateParaType;

		public String getProcedureName() {
			return procedureName;
		}

		public void setProcedureName(String procedureName) {
			this.procedureName = procedureName;
		}

		public int getDateParaType() {
			return dateParaType;
		}

		public void setDateParaType(int dateParaType) {
			this.dateParaType = dateParaType;
		}

	}

}
