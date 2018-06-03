package cn.mulanbay.common.http;

import java.util.Date;

/**
 * ${DESCRIPTION}
 * 第三方API日志处理
 * @author fenghong
 * @create 2017-10-12 10:11
 **/
public interface HttpApiLogProcessor {

    public void doHttpApiLog(String url, String method, Object request, HttpResult response, Date startTime, Date endTime);
}
