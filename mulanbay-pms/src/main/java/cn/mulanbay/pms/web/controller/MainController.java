package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.ValidateError;
import cn.mulanbay.common.util.IPAddressUtil;
import cn.mulanbay.pms.common.CacheKey;
import cn.mulanbay.pms.common.ConfigKey;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.handler.LoginCheckHandler;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.domain.User;
import cn.mulanbay.pms.persistent.domain.UserSetting;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import cn.mulanbay.pms.persistent.enums.MonitorBussType;
import cn.mulanbay.pms.persistent.enums.UserStatus;
import cn.mulanbay.pms.persistent.service.AuthService;
import cn.mulanbay.pms.persistent.service.UserCalendarService;
import cn.mulanbay.pms.web.bean.LoginUser;
import cn.mulanbay.pms.web.bean.request.auth.LoginRequest;
import cn.mulanbay.pms.web.bean.request.auth.RegisterRequest;
import cn.mulanbay.pms.web.bean.response.MyInfoResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/main")
public class MainController extends BaseController {

    private static Logger logger = Logger.getLogger(MainController.class);

    @Value("${system.version}")
    private String version;

    @Value("${security.login.persist}")
    private boolean loginPersist;

    @Autowired
    LoginCheckHandler loginCheckHandler;

    @Autowired
    AuthService authService;

    @Autowired
    CacheHandler cacheHandler;

    @Autowired
    UserCalendarService userCalendarService;

    @Autowired
    PmsNotifyHandler pmsNotifyHandler;

    /**
     * 主页
     *
     * @return
     */
    @RequestMapping(value = "/main")
    public String main() {
        return "main";
    }

    /**
     * 登陆页面
     *
     * @return
     */
    @RequestMapping(value = "/login")
    public String login() {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
        } else {
            ValidateError ve = messageHandler.getErrorInfo("errorcode." + code);
            this.request.setAttribute("failInfo", "'" + ve.getErrorInfo() + "'");
        }
        return "login";
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/loginAuth", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean loginAuth(@Valid LoginRequest login, HttpServletResponse response) {
        String username = login.getUsername();

        User user = authService.getUserByUsernameOrPhone(username);
        if (user == null) {
            return callbackErrorCode(ErrorCode.USER_NOTFOUND);
        } else {
            if (user.getStatus() == UserStatus.DISABLE) {
                return callbackErrorCode(ErrorCode.USER_IS_STOP);
            }
            if (user.getExpireTime() != null && user.getExpireTime().before(new Date())) {
                return callbackErrorCode(ErrorCode.USER_EXPIRED);
            }
            // 检测密码
            String rp = user.getPassword();
            String encodePassword = loginCheckHandler.encodePassword(login.getPassword());
            if (!rp.equalsIgnoreCase(encodePassword)) {
                return callbackErrorCode(ErrorCode.USER_PASSWORD_ERROR);
            }
            doLogin(user,response);
            addLoginNotifyMsg(login.getUsername());
            return callback(null);
        }
    }

    private void addLoginNotifyMsg(String username){
        try {
            // 发送系统通知
            String content="用户["+username+"]登录系统";
            pmsNotifyHandler.addMessageToNotifier("用户登录系统",content,new Date(), LogLevel.WARNING,null, MonitorBussType.SECURITY);
        } catch (Exception e) {
            logger.error("增加登录提醒日志异常",e);
        }

    }

    /**
     * 登录
     * @param user
     * @param response
     */
    private void doLogin(User user, HttpServletResponse response){
        LoginUser lu = new LoginUser();
        lu.setUser(user);
        setCookie(lu, response);
        if(loginPersist){
            cacheHandler.set("userLogin:" + lu.getLoginToken(), lu, 3600 * 24 * 7);
        }
        user.setLastLoginIp(IPAddressUtil.getIpAddress(request));
        user.setLastLoginTime(new Date());
        user.setLastLoginToken(lu.getLoginToken());
        baseService.updateObject(user);
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletResponse response) {
        this.request.setAttribute("failInfo", "null");
        long uid = this.getCurrentUserId();
        LoginUser lu = loginCheckHandler.getLoginUser(request);
        if (lu == null) {
            return "main/login";
        }
        Cookie cookies[] = request.getCookies();
        for (int i = 0, len = cookies.length; i < len; i++) {
            Cookie cookie = new Cookie(cookies[i].getName(), null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // 消除session
        HttpSession session = request.getSession();
        session.invalidate();
        session = null;

        return "login";
    }

    /**
     * 设置Cookie
     *
     * @param lu
     * @param response
     */
    private void setCookie(LoginUser lu, HttpServletResponse response) {

        lu.setLoginToken(UUID.randomUUID().toString());
        // uidcookie.setDomain(cookieDomain);
        Cookie tockencookie = new Cookie(ConfigKey.COOKIE_TOKEN,
                lu.getLoginToken());
        tockencookie.setMaxAge(Integer.MAX_VALUE);
        tockencookie.setPath("/");
        // tockencookie.setDomain(cookieDomain);
        response.addCookie(tockencookie);

        //保存登陆信息
        request.getSession().setAttribute(lu.getLoginToken(), lu);
    }

    /**
     * 我的信息
     *
     * @return
     */
    @RequestMapping(value = "/myInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean myInfo() {
        Long userId = this.getCurrentUserId();
        User user = baseService.getObject(User.class, userId);
        MyInfoResponse res = new MyInfoResponse();
        res.setUsername(user.getUsername());
        res.setNickname(user.getNickname());
        res.setVersion(version);
        String key = MessageFormat.format(CacheKey.USER_TODAY_CALENDAR_COUNTS, userId);
        Integer cc = cacheHandler.get(key, Integer.class);
        if (cc == null) {
            cc = userCalendarService.getTodayUserCalendarCount(userId).intValue();
            cacheHandler.set(key, cc, 30);
        }
        res.setTodayCalendars(cc);
        return callback(res);
    }

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean userRegister(@Valid RegisterRequest rr, HttpServletResponse response) {
        User user = new User();
        user.setCreatedTime(new Date());
        user.setUsername(rr.getUsername());
        user.setPassword(loginCheckHandler.encodePassword(rr.getPassword()));
        user.setLevel(3);
        user.setNickname(rr.getUsername());
        user.setPoints(0);
        UserSetting us = new UserSetting();
        us.setCreatedTime(new Date());
        us.setSendEmail(false);
        us.setSendWxMessage(true);
        authService.userRegister(user,us);
        //自动登录
        doLogin(user,response);
        return callback(null);
    }

    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }
}