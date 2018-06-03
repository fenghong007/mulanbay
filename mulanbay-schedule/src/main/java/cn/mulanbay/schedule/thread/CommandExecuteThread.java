package cn.mulanbay.schedule.thread;

import cn.mulanbay.common.thread.EnhanceThread;
import cn.mulanbay.common.util.CommandUtil;
import org.apache.log4j.Logger;

/**
 * 操作系统命令执行线程
 * 
 * @author fenghong
 * 
 */
public class CommandExecuteThread extends EnhanceThread {

	private static Logger logger = Logger.getLogger(CommandExecuteThread.class);

	// 异步时间（秒）
	private long asynTime = 5;

	private String cmd;

	private int osType;

	public CommandExecuteThread(String cmd, int osType) {
		super("启动执行操作系统命令");
		this.cmd = cmd;
		this.osType = osType;
	}

	public long getAsynTime() {
		return asynTime;
	}

	public void setAsynTime(long asynTime) {
		this.asynTime = asynTime;
	}

	@Override
	public void doTask() {
		try {
			logger.warn("启动执行操作系统类型为[" + osType + "]的命令:" + cmd + "的线程");
			sleep(asynTime * 1000);
			logger.warn("开始执行操作系统类型为[" + osType + "]的命令:" + cmd);
			String res = CommandUtil.executeCmd(osType, cmd);
			logger.warn("执行操作系统类型为[" + osType + "]的命令:" + cmd + "结果为:" + res);
		} catch (Exception e) {
			logger.debug("执行了操作系统类型为[" + osType + "]的命令:" + cmd + "异常", e);
		}
	}

}
