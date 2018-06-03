package cn.mulanbay.schedule.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ProcedureProcessor;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import org.apache.log4j.Logger;

import java.util.Date;


/**
 * 通用的存储过程调用<br>
 * 如果存储过程带参数，默认支持的参数：业务日期。
 * 
 * @author fenghong
 * 
 */
public class CallProcedureJob extends AbstractBaseJob {

	private static Logger logger = Logger.getLogger(CallProcedureJob.class);

	// 存储过程名称
	private String procedureName;

	private int dateParaType;

	@Override
	public TaskResult doTask() {
		return doProcedure(procedureName, dateParaType);
	}

	protected TaskResult doProcedure(String procedureName, int dateParaType) {
		TaskResult tr = new TaskResult();
		ProcedureProcessor processor = BeanFactoryUtil.getBean(ProcedureProcessor.class);
		Date start = this.getBussDay();
		Date end = null;
		String comment = "存储过程[" + procedureName + "]";
		logger.debug("存储过程[" + procedureName + "]的时间参数类型：" + dateParaType);
		switch (dateParaType) {
			case 0 : {
				// 不需要
				comment += "参数：空";
				processor.executeProcedure(procedureName);
				break;
			}
			case 1 : {
				// 只需要一天
				logger.debug("存储过程[" + procedureName + "]的执行时间参数：" + start);
				comment += "参数：开始日期["
						+ DateUtil.getFormatDate(start,
								DateUtil.Format24Datetime) + "]";
				processor.executeProcedure(procedureName,start);
				break;
			}
			case 2 : {
				// 时间段
				end = DateUtil.getDate(1, start);
				logger.debug("存储过程[" + procedureName + "]的执行时间参数：" + start
						+ "," + end);
				comment += "参数：开始日期["
						+ DateUtil.getFormatDate(start,
								DateUtil.Format24Datetime)
						+ "],结束日期["
						+ DateUtil
								.getFormatDate(end, DateUtil.Format24Datetime)
						+ "]";
				processor.executeProcedure(procedureName,start,end);
				break;
			}
			case 3 : {
				// 月
				end = DateUtil.getDate(0);
				start = DateUtil.getDateMonth(-1, end);
				logger.debug("存储过程[" + procedureName + "]的执行时间参数：" + start
						+ "," + end);
				comment += "参数：开始日期["
						+ DateUtil.getFormatDate(start,
								DateUtil.Format24Datetime)
						+ "],结束日期["
						+ DateUtil
								.getFormatDate(end, DateUtil.Format24Datetime)
						+ "]";
				processor.executeProcedure(procedureName,start,end);
				break;
			}
			default:
		}
		logger.debug("开始执行存储过程:" + procedureName);
		tr.setExecuteResult(JobExecuteResult.SUCESS);
		tr.setComment(comment + ",时间参数类型：" + dateParaType);
		logger.debug("执行存储过程:" + procedureName + "结束");
		return tr;
	}


	@Override
	public ParaCheckResult checkTriggerPara(String triggerDetail) {
		ParaCheckResult rb = new ParaCheckResult();
		rb.setMessage("参数格式为：1. 存储过程名称 ,2. 时间参数类型（0不需要,1只需要单天,2一天的时间段,3一个月的时间段）");
		try {
			if (triggerDetail == null || triggerDetail.isEmpty()) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_NULL);
				return rb;
			}
			/**
			 * 配置参数 <br>
			 * 1. 存储过程名称 <br>
			 * 2. 时间参数类型（0,1,2,3）<br>
			 */
			String[] para = triggerDetail.split(",");
			if (para.length < 2) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_LENGTH_ERROR);
				return rb;
			}
			procedureName = para[0];
			dateParaType = Integer.valueOf(para[1]);
		} catch (Exception e) {
			logger.error("检查参数异常", e);
			rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_FORMAT_ERROR);
			rb.setMessage(rb.getMessage() + "," + e.getMessage());
		}
		return rb;
	}

}
