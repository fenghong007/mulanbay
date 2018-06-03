package cn.mulanbay.schedule.job;


import cn.mulanbay.common.util.CommandUtil;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.thread.CommandExecuteThread;
import org.apache.log4j.Logger;

/**
 *
 * 执行操作系统命令
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class CommandJob extends AbstractBaseJob {

	private static Logger logger = Logger.getLogger(CommandJob.class);
	
	private String cmd;

	private int osType;

	private boolean isAsyn = false;

	@Override
	public TaskResult doTask() {
		TaskResult tr = new TaskResult();
		String res = "";
		if (isAsyn) {
			CommandExecuteThread thread = new CommandExecuteThread(cmd, osType);
			res = "将在" + thread.getAsynTime() + "秒后执行命令:" + cmd;
			thread.start();
			logger.debug(res);
		} else {
			res = CommandUtil.executeCmd(osType, cmd);
			logger.debug("执行了命令:" + cmd + ",命令结果：" + res);
		}
		if (res != null && res.length() > 200) {
			// 避免过长
			res = res.substring(0, 200);
		}
		tr.setComment(res);
		tr.setExecuteResult(JobExecuteResult.SUCESS);
		return tr;
	}

	/**
	 * 提醒通知
	 * @param cmd
	 */
	public void notifyLog(String cmd){

	}

	@Override
	public ParaCheckResult checkTriggerPara(String triggerDetail) {
		ParaCheckResult rb = new ParaCheckResult();
		rb.setMessage("参数格式为：1. 命令,2.操作系统类型（-1由程序判断 0LINUX 1WINDOWS）,3. 是否异步（true|false）");
		try {
			if (triggerDetail == null || triggerDetail.isEmpty()) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_NULL);
				return rb;
			}

			/**
			 * 配置参数 <br>
			 * 1. 命令 <br>
			 * 2. 操作系统类型（0LINUX 1WINDOWS）<br>
			 */
			String[] para = triggerDetail.split(",");
			if (para.length < 2) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_LENGTH_ERROR);
				return rb;
			}
			cmd = para[0];
			osType = Integer.valueOf(para[1]);
			isAsyn = Boolean.parseBoolean(para[2]);
		} catch (Exception e) {
			logger.error("检查参数异常", e);
			rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_FORMAT_ERROR);
			rb.setMessage(rb.getMessage() + "," + e.getMessage());
		}
		return rb;
	}

}
