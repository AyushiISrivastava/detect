package com.vehicle.detection.util;

/**
 * Created by Ayushi on 21/01/19.
 */
public enum StatusCode {

	SERVER_ERROR(5000), RECORD_FETCHED(3000), RECORD_SAVED(4000), RECORD_DELETED(4004), INVALID_INPUT(3001)
	,RECORDS_NOT_SAVED(4004), RECORD_NOT_FETCHED(3001);
	Integer value;

	StatusCode(Integer i) {
		this.value = i;
	}

	public Integer getValue() {
		return value;
	}

}
