package com.vehicle.detection.util;

/**
 * Created by Ayushi on 21/01/19.
 */
public class ApiResponse {

	private Object data;
	private Boolean success;
	private Integer status;
	private String message;

	public ApiResponse() {
		this.success = Boolean.FALSE;
		this.status = StatusCode.SERVER_ERROR.getValue();
		this.message = "Oops, something went wrong!";
	}

	public ApiResponse(Object data, Integer status, Boolean success, String message) {
		this.data = data;
		this.status = status;
		this.success = success;
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
