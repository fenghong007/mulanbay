package cn.mulanbay.web.common;

import cn.mulanbay.business.handler.MessageHandler;
import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.exception.ValidateError;
import cn.mulanbay.web.bean.response.ResultBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller层异常处理类
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
@ControllerAdvice
public class ApiExceptionHandler {

	private static Logger logger = Logger.getLogger(ApiExceptionHandler.class);

	@Autowired
	protected MessageHandler messageHandler;

	protected boolean doSystemLog(){
		return false;
	}

	/**
	 * 程序异常
	 * 
	 * @param ae
	 * @return
	 */
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ResultBean handleApplicationExceptionError(ApplicationException ae,HttpServletRequest request) {
		if (ae.getMyException() == null) {
			logger.error("handleApplicationExceptionError:"+ae.getMessage());
		} else {
			logger.error("handleApplicationExceptionError:"+ae.getMessage(), ae.getMyException());
		}

		ResultBean rb = new ResultBean();
		rb.setCode(ae.getErrorCode());
		ValidateError ve = messageHandler.getErrorCodeInfo(ae.getErrorCode());
		rb.setMessage(ve.getErrorInfo());
		if(doSystemLog()){
			this.addSystemLog(request,ae.getClass(),"程序运行异常",rb.getMessage(),ae.getErrorCode());
		}
		return rb;
	}

	/**
	 * 处理controller的验证错误处理
	 * 
	 * @param be
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ResultBean handleBindExceptionError(BindException be,HttpServletRequest request) {
		logger.error("handleBindExceptionError");
		ResultBean rb = new ResultBean();
		//ValidateError ee = messageHandler.getErrorCodeInfo(ErrorCode.FIELD_VALIDATE_ERROR);
		//rb.setCode(ee.getCode());
		//rb.setMessage(ee.getErrorInfo());
		Errors errors = be.getBindingResult();
		List<ValidateError> es = messageHandler.getErrorInfo(errors);
		//默认选择第一个
		rb.setCode(es.get(0).getCode());
		rb.setMessage(es.get(0).getErrorInfo());
		//rb.setData(es);
		if(doSystemLog()){
			this.addSystemLog(request,be.getClass(),"请求参数验证异常",rb.getMessage()+",代码:("+es.get(0).getField()+")",rb.getCode());
		}
		return rb;
	}

	/**
	 * 处理PersistentException持久层异常
	 * 
	 * @param pe
	 * @return
	 */
	@ExceptionHandler(PersistentException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ResultBean handlePersistentExceptionError(PersistentException pe,HttpServletRequest request) {
		logger.error("handlePersistentExceptionError:"+pe.getMessage(), pe.getMyException());
		ResultBean rb = new ResultBean();
		rb.setCode(pe.getErrorCode());
		rb.setMessage(pe.getMessage());
		if(doSystemLog()){
			String message = pe.getMyException().getMessage();
			this.addSystemLog(request,pe.getClass(),"持久层处理异常",message,rb.getCode());
		}
		return rb;
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ResultBean handleUnexpectedServerError(RuntimeException ex,HttpServletRequest request) {
		logger.error("RuntimeException", ex);
		ResultBean rb = new ResultBean();
		rb.setCode(ErrorCode.SYSTEM_ERROR);
		rb.setMessage("系统异常：" + ex.getMessage());
		if(doSystemLog()){
			this.addSystemLog(request,ex.getClass(),"未能捕获的运行期异常",rb.getMessage(),rb.getCode());
		}
		return rb;
	}

	/**
	 * 程序异常
	 *
	 * @param ae
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ResultBean handleExceptionError(Exception ae,HttpServletRequest request) {
		logger.error("handleExceptionError:"+ae.getMessage());
		ResultBean rb = new ResultBean();
		rb.setCode(ErrorCode.UNKHOWM_ERROR);
		ValidateError ve = messageHandler.getErrorCodeInfo(ErrorCode.UNKHOWM_ERROR);
		rb.setMessage(ve.getErrorInfo()+":"+ae.getMessage());
		if(doSystemLog()){
			this.addSystemLog(request,ae.getClass(),"系统异常",rb.getMessage(),rb.getCode());
		}
		return rb;
	}

	protected void addSystemLog(HttpServletRequest request,Class exceptionClass,String title,String msg,int errorCode){

	}
	
}