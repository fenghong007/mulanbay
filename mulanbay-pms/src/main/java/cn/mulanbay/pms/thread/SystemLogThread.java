package cn.mulanbay.pms.thread;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.handler.SystemConfigHandler;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.domain.SystemLog;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;

public class SystemLogThread extends BaseLogThread {

	private static Logger logger = Logger.getLogger(SystemLogThread.class);

	private SystemLog log;

	public SystemLogThread(SystemLog log) {
		super("系统日志");
		this.log =log;
	}

	@Override
	public void run() {
		handleLog(log);
	}

	/**
	 *
	 * 增加系统日志
	 * 
	 * @param log
	 */
	private void handleLog(SystemLog log) {
		try {
			SystemConfigHandler systemConfigHandler = getSystemConfigHandler();
			SystemFunction sf = log.getSystemFunction();
			if(sf!=null){
				log.setSystemFunction(sf);
				log.setIdValue(this.getParaIdValue(sf,log.getParaMap()));
			}

			Date now = new Date();
			log.setStoreTime(now);
			//会比较慢
			log.setHostIpAddress(systemConfigHandler.getHostIpAddress());
			log.setCreatedTime(now);
			Map map = log.getParaMap();
			if(map!=null&&!map.isEmpty()){
                //序列化比较耗时间
                log.setParas(JsonUtil.beanToJson(changeToNormalMap(map)));
            }
			log.setStoreDuration(log.getStoreTime().getTime()-log.getOccurTime().getTime());
			BaseService baseService = BeanFactoryUtil.getBean(BaseService.class);
			baseService.saveObject(log);
			this.notifyError(log.getUserId(),log.getErrorCode(),log.getContent());
		} catch (Exception e) {
			logger.error("增加系统日志异常", e);
		}
	}

	private SystemConfigHandler getSystemConfigHandler(){
		return BeanFactoryUtil.getBean(SystemConfigHandler.class);
	}
	
}
