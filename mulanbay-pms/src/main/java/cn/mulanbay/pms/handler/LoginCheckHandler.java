package cn.mulanbay.pms.handler;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.business.handler.MessageHandler;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.Md5Util;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.common.ConfigKey;
import cn.mulanbay.pms.persistent.domain.User;
import cn.mulanbay.pms.persistent.service.AuthService;
import cn.mulanbay.pms.web.bean.LoginUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录验证
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
@Component
public class LoginCheckHandler extends BaseHandler {

	private static Logger logger = Logger.getLogger(LoginCheckHandler.class);

	@Value("${security.login.auth}")
	boolean loginCheck;

	@Value("${security.login.persist}")
	boolean loginPersist;

	@Value("${security.login.timeout}")
	int loginTimeout;

	@Value("${security.password.salt}")
	String md5Salt;

	@Autowired
	MessageHandler messageHandler;

	@Autowired
    AuthService authService;

	@Autowired
	CacheHandler cacheHandler;

	public LoginCheckHandler() {
		super("登陆验证处理");
	}

	@Override
	public void init() {
		logger.info("开始初始化登录验证配置");
		try {
			// TODO 初始化功能点列表
		} catch (Exception e) {
			logger.error("读取登录配置文件异常", e);
		} finally {

		}
		logger.info("初始化登录验证配置成功");
	}

	/**
	 * 登录验证
	 * 
	 * @param re
	 * @return
	 */
	public int checkLogin(HttpServletRequest re) {
		if (!loginCheck) {
			return ErrorCode.SUCCESS;
		}
		String url = re.getServletPath();
		logger.debug("验证" + url + "的登录信息");
		return checkLoginInfo(re);
	}

	private int checkLoginInfo(HttpServletRequest re) {
		LoginUser lu = this.getLoginUser(re);
		if (lu == null) {
			return ErrorCode.USER_NOT_LOGIN;
		} else {
			return ErrorCode.SUCCESS;
		}
	}

	/**
	 * 增加为空、是否启动登陆验证判断
	 * @param request
	 * @return
	 */
	public long getLoginUserIdEnhanced(HttpServletRequest request){
		if(!isLoginCheck()){
			//不做登陆验证，返回用户1
			return 1L;
		}
		LoginUser loginUser = getLoginUser(request);
		if(loginUser==null){
			return  0L;
		}else{
			return loginUser.getUserId();
		}
	}

	/**
	 * 获取登陆用户
	 * @param re
	 * @return
	 */
	public LoginUser getLoginUser(HttpServletRequest re) {
		LoginUser loginUser = (LoginUser) re.getSession().getAttribute(ConfigKey.SESSION_USER_INFO);
		if(loginUser==null&&loginPersist){
			String loginTocken = this.getLoginToken(re);
			if(!StringUtil.isEmpty(loginTocken)){
				loginUser = cacheHandler.get("userLogin:"+loginTocken,LoginUser.class);
				if(loginUser!=null){
					re.getSession().setAttribute(ConfigKey.SESSION_USER_INFO, loginUser);
				}else{
					logger.error("客户端有loginToken，服务器缓存中找不到用户数据");
					loginUser = this.findFromDatabase(loginTocken);
					if(loginUser!=null){
						re.getSession().setAttribute(ConfigKey.SESSION_USER_INFO, loginUser);
					}
				}
			}else {
				logger.error("客户端找不到loginToken");
			}
		}
		return loginUser;
	}

	private String getLoginToken(HttpServletRequest request){
		// 验证
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if (ck.getName().equals(ConfigKey.COOKIE_TOKEN)) {
					return ck.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 从数据库中获取
	 * @param token
	 * @return
	 */
	private LoginUser findFromDatabase(String token){
		User user = authService.getUserByLastLoginTocken(token);
		if(user!=null){
			LoginUser loginUser = new LoginUser();
			loginUser.setLoginToken(token);
			loginUser.setUser(user);
			return loginUser;
		}else{
			return null;
		}
	}

	public boolean isLoginCheck() {
		return loginCheck;
	}

	public void setLoginCheck(boolean loginCheck) {
		this.loginCheck = loginCheck;
	}

	/**
	 * 计算密码值
	 *
	 * @param originalPwd
	 * @return
	 */
	public String encodePassword(String originalPwd){
		if(md5Salt==null){
			return Md5Util.encodeByMD5(originalPwd);
		}else{
			return Md5Util.encodeByMD5(originalPwd+ md5Salt);
		}
	}
}
