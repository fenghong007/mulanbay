package cn.mulanbay.pms.web.bean;

import cn.mulanbay.pms.persistent.domain.User;
import org.apache.log4j.Logger;

/**
 * 登陆用户信息
 * @author fenghong
 *
 */
public class LoginUser implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6230252639677382337L;
	
	private static Logger logger = Logger.getLogger(LoginUser.class);
	// 是否授权
	private boolean isSystemAuthed = false;
	
	private String loginToken;

	private User user;

	//权限角色编号
	private Long roleId;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return user.getId();
	}

	public String getUsername() {
		if (user == null) {
			return "用户不存在";
		}
		return user.getUsername();
	}

	/**
	 * 获取用户级别
	 * @return
	 */
	public Integer getLevel(){
		return user.getLevel();
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
