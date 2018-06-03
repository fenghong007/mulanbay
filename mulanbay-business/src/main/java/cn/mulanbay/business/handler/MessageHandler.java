package cn.mulanbay.business.handler;

import cn.mulanbay.common.exception.ValidateError;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 资源文件配置的处理
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class MessageHandler extends BaseHandler {

	private static Logger logger = Logger.getLogger(MessageHandler.class);
	
	@Autowired
	ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	public MessageHandler() {
		super("消息处理");
	}

	/**

	 * 表单字段验证异常及错误代码的异常信息
	 * 
	 * @param key
	 * @return
	 */
	public ValidateError getErrorInfo(String key) {
		ValidateError ve = new ValidateError();
		ve.setField(key);
		if (key == null || key.isEmpty()) {
			ve.setErrorInfo("错误信息未定义");
		} else {
			if (key.indexOf("{") >= 0) {
				// key加大挂号为了可以直接页面使用
				key = key.substring(1, key.length() - 1);
			}
			ve.setField(key);
			String ms = getConfigMessage(key);
			if (ms == null || ms.isEmpty()) {
				ve.setErrorInfo("未定义");
				ve.setCode(-1);
			} else {
				String s[] = ms.split(",");
				ve.setErrorInfo(s[1]);
				int errorCode = Integer.valueOf(s[0]);
				ve.setCode(errorCode);
			}

		}
		return ve;
	}

	/**
	 * 获取错误信息
	 * 
	 * @param errors
	 * @return
	 */
	public List<ValidateError> getErrorInfo(Errors errors) {
		List<ValidateError> es = new ArrayList<ValidateError>();
		for (ObjectError oe : errors.getAllErrors()) {
			String key = oe.getDefaultMessage();
			ValidateError ve = getErrorInfo(key);
			es.add(ve);
		}
		return es;
	}

	/**
	 * 后期可以根据Accept-Language来选择资源文件类型，实现国际化
	 * 
	 * @param key
	 * @return
	 */
	public String getConfigMessage(String key) {
		try {
			return reloadableResourceBundleMessageSource.getMessage(key, null,
					Locale.ROOT);
		} catch (NoSuchMessageException e) {
			logger.warn("未找到" + key + "的配置项");
		} catch (Exception e) {
			logger.error("GetConfigMessage error", e);
		}
		return null;
	}

	public ValidateError getErrorCodeInfo(int code) {
		return getErrorInfo("errorcode." + code);
	}
}
