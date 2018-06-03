package cn.mulanbay.business.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;

import java.util.Locale;

/**
 * 基础Handler，定义通用的方法及流程
 * 项目中涉及到第三方或者在service-controller之间的调用的可以集成及实现
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class BaseHandler {

	@Autowired
	AbstractMessageSource messageSource;

	// 自检失败关闭系统
	boolean scfShutdown = false;

	protected String handlerName;

	int cmdCode;

	public BaseHandler() {
	}

	public BaseHandler(String handlerName) {
		this.handlerName = handlerName;
	}

	/**
	 * 后期命令发送
	 * @return
	 */
	public int getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(int cmdCode) {
		this.cmdCode = cmdCode;
	}

	/**
	 * 命令处理
	 * 
	 * @param para
	 * @param cmdValue
	 * @return
	 */
	public HandlerResult handle(Object para,int cmdValue) {
		HandlerResult hr =new HandlerResult();
		return hr;
	}

	/**
	 * 初始化，一般为系统启动时调用
	 */
	public void init() {

	}

	/**
	 * 重新加载，在系统运行时操作，需要线程同步
	 */
	public void reload() {

	}
	
	/**
	 * 容器destroy时调用
	 */
	public void destroy() {

	}

	/**
	 * 自检，一般为系统启动时调用
	 * 
	 * @return
	 */
	public Boolean selfCheck() {
		return true;
	}

	public boolean isScfShutdown() {
		return scfShutdown;
	}

	public void setScfShutdown(boolean scfShutdown) {
		this.scfShutdown = scfShutdown;
	}

	/**
	 * Handler名称
	 * 
	 * @return
	 */
	public String getHandlerName() {
		return handlerName;
	}

	String getConfig(String key) {
		try {
			return messageSource.getMessage(key, null, Locale.ROOT);
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

	/**
	 * 读取配置信息
	 *
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	String getConfig(String key, String defaultValue) {
		String s = getConfig(key);
		if (s == null || s.isEmpty()) {
			return defaultValue;
		} else {
			return s;
		}
	}

	int getIntegerConfig(String key, int defaultValue) {
		String s = getConfig(key);
		if (s == null || s.isEmpty()) {
			return defaultValue;
		} else {
			return Integer.valueOf(s);
		}
	}


	/**
	 * 处理信息
	 * @return
	 */
	public HandlerInfo getHanderInfo(){
		return new HandlerInfo(this.handlerName);
	}

	public boolean isDoSystemLog(){
		return false;
	}
}
