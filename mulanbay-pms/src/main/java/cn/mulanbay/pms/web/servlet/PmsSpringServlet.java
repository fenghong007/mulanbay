package cn.mulanbay.pms.web.servlet;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.pms.handler.LogHandler;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import cn.mulanbay.web.servlet.SpringServlet;
import org.apache.log4j.Logger;

public class PmsSpringServlet extends SpringServlet {

    private static Logger logger = Logger.getLogger(PmsSpringServlet.class);


    @Override
    protected void doLog(Integer errorCode, String title, String msg) {
        try {
            if(errorCode==null){
                errorCode = ErrorCode.SUCCESS;
            }
            LogHandler logHandler = BeanFactoryUtil.getBean(LogHandler.class);
            if(logHandler!=null){
                logHandler.addSystemLog(LogLevel.WARNING,title,msg,errorCode);
            }
        } catch (Exception e) {
            logger.error("doLog 异常",e);
        }

    }
}
