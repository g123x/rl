package com.sg.rl.common.entity.base;

 
public class HttpResult {
    private int errorCode;
    private String errorMsg;
    private Object data;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
 
	public String getErrorMsg() {
		return errorMsg; 
	} 
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg; 
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}