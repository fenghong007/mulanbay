package cn.mulanbay.pms.web.interceptor;

import cn.mulanbay.business.handler.MessageHandler;
import cn.mulanbay.common.exception.ValidateError;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.pms.handler.LoginCheckHandler;
import cn.mulanbay.web.bean.response.ResultBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseInterceptor implements HandlerInterceptor {

	private static Logger logger = Logger.getLogger(BaseInterceptor.class);

	private String interceptorName;

	@Autowired
	MessageHandler messageHandler;
	
	public String getInterceptorName() {
		return interceptorName;
	}

	public void setInterceptorName(String interceptorName) {
		this.interceptorName = interceptorName;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exp)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	void handleFail(HttpServletRequest request, HttpServletResponse response,
			String redirectPath,int code,String failInfo, String reqUrl) {
		try {
			ValidateError ve = messageHandler.getErrorCodeInfo(code);
			String errorInfo = "";
			if(ve!=null){
				errorInfo=ve.getErrorInfo();
			}else{
				errorInfo="未找到相关错误信息";
			}
			if (reqUrl.startsWith("/main")||reqUrl.endsWith("list")) {
				// 普通界面,直接界面，不走controller
				response.sendRedirect(request.getContextPath() + redirectPath
						+ "?code="+code);
			} else {
				// ajax调用
				ResultBean rb = new ResultBean();
				rb.setCode(code);
				rb.setMessage(errorInfo);
				// Ajax调用
				response.getWriter().print(JsonUtil.beanToJson(rb));
			}
		} catch (Exception e) {
			logger.error("处理拦截器拦截验证失败异常", e);
		}
	}
}
