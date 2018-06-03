package cn.mulanbay.common.http;

import org.apache.commons.httpclient.HttpStatus;

/**
 * ${DESCRIPTION}
 * http请求返回类
 * @author fenghong
 * @create 2017-10-11 16:45
 **/
public class HttpResult {

    private int statusCode = HttpStatus.SC_OK;

    private String responseData;

    private String errorInfo;

    private Class exceptionClass;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Class getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(Class exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionClassName(){
        if(exceptionClass==null){
            return null;
        }else {
            return exceptionClass.getName();
        }
    }
}
