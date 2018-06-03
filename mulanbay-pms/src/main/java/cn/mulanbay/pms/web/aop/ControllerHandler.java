package cn.mulanbay.pms.web.aop;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.BindUserLevel;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.IPAddressUtil;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.handler.LogHandler;
import cn.mulanbay.pms.handler.LoginCheckHandler;
import cn.mulanbay.pms.handler.SystemConfigHandler;
import cn.mulanbay.pms.persistent.domain.OperationLog;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.FunctionDataType;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import cn.mulanbay.pms.web.bean.LoginUser;
import cn.mulanbay.web.bean.request.PageSearch;
import cn.mulanbay.web.common.RequestContent;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by fenghong on 2017/8/31.
 * 1. 配置用户信息
 * 2. 记录日志(todo)
 */
@Component
@Aspect
public class ControllerHandler {

    private static Logger logger = Logger.getLogger(ControllerHandler.class);

    @Value("${system.need.operationLog}")
    boolean enableOperationLog;

    @Value("${system.pageSearch.maxPageSize}")
    int maxPageSize;

    @Value("${security.login.auth}")
    private boolean loginCheck;

    @Autowired
    SystemConfigHandler systemConfigHandler;

    @Autowired
    protected LoginCheckHandler loginCheckHandler;

    @Autowired
    LogHandler logHandler;

    @Autowired
    BaseService baseService;

    @Autowired
    CacheHandler cacheHandler;

    private ThreadLocal<OperationLog> logThreadLocal = new ThreadLocal<>();

    private HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request;
    }

    //controller包的子包里面任何方法
    @Pointcut("execution(public * cn.mulanbay.pms.web.controller.*.*(..))")
    public void setUserInfo(){
    }

    //controller包的子包里面任何方法
    @Pointcut("execution(public * cn.mulanbay.pms.web.controller.*.*(..))")
    public void doLog(){
    }

    @Before("setUserInfo()")
    public void beforeBuss(JoinPoint joinPoint){
        try {
            if(!loginCheck){
                //未开启登录验证的，直接返回
                return;
            }
            Date date = new Date();

            //检查功能点是否启用
            HttpServletRequest request = this.getRequest();
            LoginUser loginUser = loginCheckHandler.getLoginUser(request);
            Long userId=1L;
            Integer level=null;
            if(loginUser!=null){
                userId=loginUser.getUserId();
                level=loginUser.getLevel();
            }
            String url = request.getRequestURI();
            String method = request.getMethod();
            SystemFunction sf = systemConfigHandler.getFunction(url,method);
            if(sf!=null&&sf.getStatus()== CommonStatus.DISABLE){
                throw new ApplicationException(PmsErrorCode.SYSTEM_FUNCTION_DISABLED);
            }
            if (sf == null) {
                //后期直接抛功能点未设置
                //throw new ApplicationException(ErrorCode.FUNCTION_UN_DEFINE);
                logger.warn("url:" + url + ",method:" + method + "未配置功能定义");
            } else if (sf.getLoginAuth()) {
                if (loginUser == null) {
                    throw new ApplicationException(ErrorCode.USER_NOT_LOGIN);
                }
                if (sf.getRequestLimit()) {
                    String key="request_limit:" + loginUser.getUserId() + ":" + url;
                    //请求限制
                    String s = cacheHandler.getForString(key);
                    if (s != null) {
                        throw new ApplicationException(ErrorCode.USER_REQUEST_TOO_FREQ);
                    } else {
                        cacheHandler.set(key, "123", sf.getRequestLimitPeriod());
                    }
                }
                if(sf.getDayLimit()>0){
                    String key ="request_limit_day:"+DateUtil.getToday(DateUtil.FormatDay1) +":"+ loginUser.getUserId() + ":" + url;
                    //请求限制
                    Integer s = cacheHandler.get(key,Integer.class);
                    if (s != null) {
                        if(s.intValue()<sf.getDayLimit()){
                            s=s+1;
                            cacheHandler.set(key, s, 24*3600);
                        }else{
                            throw new ApplicationException(ErrorCode.USER_FUNCTION_TOO_FREQ);
                        }
                    } else {
                        cacheHandler.set(key, 1, 24*3600);
                    }
                }
                if (sf.getPermissionAuth()) {
                    // 权限认证
                    Long roleId = loginUser.getRoleId();
                    if(roleId==null){
                        throw new ApplicationException(ErrorCode.USER_NOT_AUTH);
                    }
                    boolean b = systemConfigHandler.isRoleAuth(roleId,sf.getId());
                    if(!b){
                        throw new ApplicationException(ErrorCode.USER_NOT_AUTH);
                    }
                }

                //todo IP拦截验证
            }

            //设置用户等信息
            handleRequestInfoSet(joinPoint, userId,level);
            //记录日志
            if(enableOperationLog){
                this.addOperationLog(date, request, url, method, loginUser,sf);
            }
        }catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("do beforeBuss error",e);
        }
    }

    private void addOperationLog(Date date, HttpServletRequest request, String url, String method, LoginUser loginUser,SystemFunction sf) {
        try {
            // 记录操作日志
            OperationLog log = new OperationLog();
            log.setSystemFunction(sf);
            log.setOccurStartTime(date);
            RequestContent content = new RequestContent(request);
            Map paraMap = content.getParameterMap();
            // json 序列化换为异步执行
            log.setParaMap(paraMap);
            log.setUrlAddress(url);
            log.setMethod(method);
            log.setIpAddress(IPAddressUtil.getIpAddress(request));
            if (logger.isDebugEnabled()) {
                logger.debug("收到请求：" + url+",请求方法："+method);
                logger.debug("请求参数：" + JsonUtil.beanToJson(paraMap));
                logger.debug("content-type:" + request.getContentType());
                //logger.debug("cookie:" + JsonUtil.beanToJson(request.getCookies()));
            }
            if (loginUser != null) {
                log.setUserId(loginUser.getUserId());
                log.setUserName(loginUser.getUsername());
            }
            logThreadLocal.set(log);
        } catch (ApplicationException e) {
            logger.error("do before addOperationLog error", e);
        } catch (Exception e) {
            logger.error("do before addOperationLog error", e);
        }
    }

    @AfterReturning(value="doLog()",returning="resultMap")
    public void afterBuss(JoinPoint joinpoint,Object resultMap){
        try {
            if(!enableOperationLog){
               return;
            }
            OperationLog log = logThreadLocal.get();
            if(log==null){
                logHandler.addSystemLog(LogLevel.WARNING,"获取不到当前线程的日志信息",
                        "获取不到当前线程的日志信息",PmsErrorCode.OPERATION_LOG_CANNOT_GET);
                //logger.warn("获取不到当前线程的日志信息");
                return;
            }
            //记录返回数据
            SystemFunction sf = log.getSystemFunction();
            if(sf!=null&&sf.getRecordReturnData()&&sf.getFunctionDataType()!= FunctionDataType.PAGE){
                String ss = JsonUtil.beanToJson(resultMap);
                if(ss==null){
                    log.setReturnData("{\"msg\":\"返回数据为空\"}");
                } else if(ss.length()<5000){
                    log.setReturnData(ss);
                }else {
                    log.setReturnData("{\"msg\":\"返回数据内容过长\"}");
                }
            }
            log.setOccurEndTime(new Date());
            logHandler.addOperationLog(log);
            logThreadLocal.remove();
            logger.debug("调用方法后记录日志");
        } catch (Exception e) {
            logger.error("do afterBuss error",e);
        }

    }

    //抛出异常时才调用
    @AfterThrowing("doLog()")
    public void afterThrowing()
    {
        //System.out.println("校验token出现异常了......");
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public void handleRequestInfoSet(JoinPoint joinPoint, Long userId,Integer level) {
        Object[] arguments = joinPoint.getArgs();
        for (Object arg : arguments) {
            if (arg != null) {
                //如果是包含用户对象
                if (userId != null && arg instanceof BindUser) {
                    BindUser bu = (BindUser) arg;
                    bu.setUserId(userId);
                }
                if(level!=null && arg instanceof BindUserLevel){
                    BindUserLevel bu = (BindUserLevel) arg;
                    bu.setLevel(level);
                }
                // 判断分页数据中的最大数
                if (arg instanceof PageSearch) {
                    PageSearch bu = (PageSearch) arg;
                    if (bu.getPageSize() > maxPageSize) {
                        throw new ApplicationException(ErrorCode.PAGE_SIZE_OVER_MAX);
                    }
                }
                //时间查询类添加23:59:59
                if(arg instanceof FullEndDateTime){
                    FullEndDateTime qwu = (FullEndDateTime) arg;
                    Date endDate = qwu.getEndDate();
                    if(endDate!=null){
                        qwu.setEndDate(DateUtil.getTodayTillMiddleNightDate(endDate));
                    }
                }
            }

        }
    }
}
