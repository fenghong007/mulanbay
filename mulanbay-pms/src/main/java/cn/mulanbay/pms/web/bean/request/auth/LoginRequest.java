package cn.mulanbay.pms.web.bean.request.auth;

import org.hibernate.validator.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "{validate.user.username.notNull}")
	private String username;
	
	@NotBlank(message = "{validate.user.password.notNull}")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
