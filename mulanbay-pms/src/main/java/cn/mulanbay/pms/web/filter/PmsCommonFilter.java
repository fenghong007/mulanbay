package cn.mulanbay.pms.web.filter;

import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.web.filter.CommonFilter;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by fenghong on 2017/5/9.
 */
public class PmsCommonFilter extends CommonFilter {

    private static Logger logger = Logger.getLogger(PmsCommonFilter.class);

    @Override
    protected void logRequest(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();
        if(url.startsWith("/static")){
            //静态资源不打日志
            return;
        }
        logger.debug("收到请求:"+url);
        logger.debug("请求参数："+ JsonUtil.beanToJson(httpServletRequest.getParameterMap()));
    }
}
