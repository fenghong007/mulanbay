package cn.mulanbay.persistent.common;

import org.apache.log4j.Logger;

/**
 * OPUtil
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class OPUtil {
	private static Logger logger = Logger.getLogger(OPUtil.class);
	
	public static BaseException handleException(Exception e) {
		logger.error("持久层异常", e);
		return new BaseException(e);
	}
}
