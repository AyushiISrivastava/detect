package com.vehicle.detection.model;

import java.util.List;
/**
 * Created by Ayushi on 21/01/19.
 */
public class Vehicle {

	public String vehicleName;
	public String id;
	public String frame;
	public String powertrain;
	public List<Wheel> wheels;
	public long timestampGeneration;
	public long timestampUpload;
	public String fileName;
	public String fileContent;

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getPowertrain() {
		return powertrain;
	}

	public void setPowertrain(String powertrain) {
		this.powertrain = powertrain;
	}

	public List<Wheel> getWheels() {
		return wheels;
	}

	public void setWheels(List<Wheel> wheels) {
		this.wheels = wheels;
	}

	public long getTimestampGeneration() {
		return timestampGeneration;
	}

	public void setTimestampGeneration(long timestampGeneration) {
		this.timestampGeneration = timestampGeneration;
	}

	public long getTimestampUpload() {
		return timestampUpload;
	}

	public void setTimestampUpload(long timestampUpload) {
		this.timestampUpload = timestampUpload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
}
