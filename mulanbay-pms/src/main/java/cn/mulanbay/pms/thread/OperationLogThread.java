package cn.mulanbay.pms.thread;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.handler.LogHandler;
import cn.mulanbay.pms.handler.RewardPointsHandler;
import cn.mulanbay.pms.handler.SystemConfigHandler;
import cn.mulanbay.pms.persistent.domain.OperationLog;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.domain.SystemLog;
import cn.mulanbay.pms.persistent.enums.FunctionType;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import cn.mulanbay.pms.persistent.enums.RewardSource;
import org.apache.log4j.Logger;

import java.util.Date;

public class OperationLogThread  extends BaseLogThread {

	private static Logger logger = Logger.getLogger(OperationLogThread.class);

	private OperationLog log;

	public OperationLogThread(OperationLog log) {
		super("操作日志");
		this.log=log;
	}

	@Override
	public void run() {
		handleLog(log);
	}

	/**
	 *
	 * 增加操作日志
	 *
	 * @param log
	 */
	private void handleLog(OperationLog log) {
		try {
			SystemConfigHandler systemConfigHandler = getSystemConfigHandler();
			SystemFunction sf = log.getSystemFunction();
			int errorCode=0;
			String msgContent="";
			if(log.getUrlAddress()!=null){
				msgContent = log.getUrlAddress();
			}
			if(sf==null){
				logger.warn("找不到请求地址["+log.getUrlAddress()+"],method["+log.getMethod()+"]功能点配置信息");
			}else {
				errorCode=sf.getErrorCode();
				if(!sf.getDoLog()){
					logger.warn("请求地址["+log.getUrlAddress()+"],method["+log.getMethod()+"]功能点配置不记录日志");
					return;
				}
				msgContent+="("+sf.getName()+")";
				log.setSystemFunction(sf);
				log.setIdValue(this.getParaIdValue(sf,log.getParaMap()));
				if(sf.getFunctionType()== FunctionType.CREATE||sf.getFunctionType()==FunctionType.EDIT){
					if(log.getParaMap()==null||log.getParaMap().isEmpty()){
						//新增和修改类型获取不到参数说明有问题
						this.addParaNotFoundSystemLog(log);
					}
				}
			}
			Date now = new Date();
			log.setStoreTime(now);
			//会比较慢
			log.setHostIpAddress(systemConfigHandler.getHostIpAddress());
			log.setCreatedTime(now);
			//序列化比较耗时间
			log.setParas(JsonUtil.beanToJson(changeToNormalMap(log.getParaMap())));
			log.setHandleDuration(log.getOccurEndTime().getTime()-log.getOccurStartTime().getTime());
			log.setStoreDuration(log.getStoreTime().getTime()-log.getOccurEndTime().getTime());
			if(log.getUserId()==null){
				log.setUserId(0L);
				log.setUserName("未知");
			}
			BaseService baseService = BeanFactoryUtil.getBean(BaseService.class);
			baseService.saveObject(log);
			if(sf!=null&&sf.getRewardPoint()!=0&&log.getUserId()>0){
				//积分奖励(操作类的积分记录管理的messageId为操作记录的编号)
				RewardPointsHandler rewardPointsHandler = BeanFactoryUtil.getBean(RewardPointsHandler.class);
				rewardPointsHandler.rewardPoints(log.getUserId(),sf.getRewardPoint(),sf.getId(), RewardSource.OPERATION,"功能操作奖励",log.getId());
			}
			this.notifyError(log.getUserId(),errorCode,msgContent);
		} catch (Exception e) {
			logger.error("增加操作日志异常", e);
		}
	}

	private void addParaNotFoundSystemLog(OperationLog log){
		//有可能在request的InputStream里面
		SystemLog systemLog = new SystemLog();
		BeanCopy.copyProperties(log,systemLog);
		systemLog.setLogLevel(LogLevel.WARNING);
		systemLog.setTitle("获取不到请求参数信息");
		systemLog.setContent("获取不到请求参数信息");
		systemLog.setErrorCode(PmsErrorCode.OPERATION_LOG_PARA_IS_NULL);
		BeanFactoryUtil.getBean(LogHandler.class).addSystemLog(systemLog);
	}

	private SystemConfigHandler getSystemConfigHandler(){
		return BeanFactoryUtil.getBean(SystemConfigHandler.class);
	}
	
}
