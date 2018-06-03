package cn.mulanbay.business.handler;

public class HandlerResult {

	private int code;

	private String message = "操作成功";

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
