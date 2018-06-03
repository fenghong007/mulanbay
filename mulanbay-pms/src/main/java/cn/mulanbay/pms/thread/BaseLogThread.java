package cn.mulanbay.pms.thread;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.MapUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.handler.SystemConfigHandler;
import cn.mulanbay.pms.persistent.domain.ErrorCodeDefine;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.domain.User;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;

public class BaseLogThread extends Thread {

	private static Logger logger = Logger.getLogger(BaseLogThread.class);

	public BaseLogThread(String name){
		super(name);
	}

	/**
	 * request默认拿到的是数组类型，这里转换为通用的原始参数
	 * @param paraMap
	 * @return
	 */
	protected Map changeToNormalMap(Map paraMap){

		return MapUtil.changeRequestMapToNormalMap(paraMap);
	}

	/**
	 * 获取参数ID值
	 * @param sf
	 * @param paraMap
	 * @return
	 */
	protected String  getParaIdValue(SystemFunction sf, Map paraMap){
		if(!StringUtil.isEmpty(sf.getIdField())){
			//设置key的值，方便后期查找比对使用,目前只对修改类有效
			Object oo = paraMap.get(sf.getIdField());
			if(oo!=null){
				return  oo.toString();
			}
		}
		return null;
	}

	protected void notifyError(Long userId,int code,String message){
		try {
			SystemConfigHandler systemConfigHandler = BeanFactoryUtil.getBean(SystemConfigHandler.class);
			ErrorCodeDefine ec = systemConfigHandler.getErrorCodeDefine(code);
			if(ec==null){
				return;
			}else{
				if(ec.getNotifiable()){
					Date notifyTime = null;
					if(ec.getRealtimeNotify()){
						notifyTime=new Date();
					}
					//通知
					PmsNotifyHandler pmsNotifyHandler=BeanFactoryUtil.getBean(PmsNotifyHandler.class);
					String title = "错误代码通知,code:"+code+"("+ec.getName()+")";
					pmsNotifyHandler.addMessageToNotifier(title,message+","+getUserInfo(userId),notifyTime, LogLevel.WARNING,ec.getCauses(), ec.getBussType());

				}else{
					return;
				}
			}
		} catch (Exception e) {
			logger.error("处理错误代码异常",e);
		}
	}

	private String getUserInfo(Long userId){
		if(userId==null){
			return "";
		}else{
			String s="操作人UserId:"+userId;
			BaseService baseService = BeanFactoryUtil.getBean(BaseService.class);
			User user = baseService.getObject(User.class,userId);
			if(user!=null){
				s+=",手机号:"+user.getPhone();
			}
			return s;
		}
	}

}
