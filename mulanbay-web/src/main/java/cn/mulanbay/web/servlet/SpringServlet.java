package cn.mulanbay.web.servlet;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.business.handler.HandlerManager;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.thread.ThreadManager;
import cn.mulanbay.common.util.BeanFactoryUtil;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import java.io.FileNotFoundException;

/**
 * 系统的一些初始化操作，并获取web容器中的Bean管理器，方便其他模块使用
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class SpringServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger s_log = Logger.getLogger(SpringServlet.class);

	/**
	 * WebApplicationContext
	 */
	private static WebApplicationContext s_wac;

	/**
	 * destroy
	 */
	@Override
	public void destroy() {
		s_log.info("SpringContext begin to destroy....................");
		HandlerManager hm = BeanFactoryUtil.getBean(HandlerManager.class);
		for (BaseHandler bh : hm.getHandlerList()) {
			s_log.info(bh.getHandlerName()+" Handler begin to destroy...");
			bh.destroy();
			if(bh.isDoSystemLog()){
				doLog(null,bh.getHandlerName()+"关闭",bh.getHandlerName()+"关闭成功");
			}
			s_log.error(bh.getHandlerName() + " Handler destroyed。");
		}
		s_log.info("关闭了"+hm.getHandlerList().size()+"个Handler");
		ThreadManager.getInstance().stopAll();
		s_log.info("SpringContext Destroyed....................");
	}

	/**
	 * init
	 * 
	 * @throws FileNotFoundException
	 */
	@Override
	public void init() {
		try {
			s_log.info("SpringContext init....................");
			s_wac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext());
			BeanFactoryUtil.setApplicationContext(s_wac);
			initSystem();
			s_log.info("SpringContext end.....................");
		} catch (Exception e) {
			s_log.error("SpringServlet初始化异常", e);
			System.exit(-1);
		}
	}

	/**
	 * 获取WebApplicationContext
	 * 
	 * @return WebApplicationContext WebApplicationContext
	 */
	public static WebApplicationContext getBeanFactory() {
		return s_wac;
	}

	/**
	 * 初始化信息
	 */
	private void initSystem() {
		HandlerManager hm = BeanFactoryUtil.getBean(HandlerManager.class);
		for (BaseHandler bh : hm.getHandlerList()) {
			s_log.info(bh.getHandlerName()+" Handler begin to init...");
			bh.init();
			if (!bh.selfCheck()) {
				s_log.error(bh.getHandlerName() + "自检失败。");
				if (bh.isScfShutdown()) {
					s_log.error("因" + bh.getHandlerName() + "自检失败，关闭系统。");
					System.exit(-1);
				}
			}
			if(bh.isDoSystemLog()){
				doLog(null,bh.getHandlerName()+"初始化",bh.getHandlerName()+"初始化成功");
			}
			s_log.info(bh.getHandlerName()+" Handler init end");
		}
		s_log.info("初始化了"+hm.getHandlerList().size()+"个Handler");
		doLog(ErrorCode.SYSTEM_STARTED,"系统启动","系统启动成功");

	}

	/**
	 * 子类实现
	 * @param title
	 * @param msg
	 */
	protected void doLog(Integer errorCode,String title,String msg){

	}

}
