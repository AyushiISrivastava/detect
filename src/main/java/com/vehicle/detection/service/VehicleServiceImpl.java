package com.vehicle.detection.service;

import com.vehicle.detection.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vehicle.detection.manager.VehicleManager;
import org.springframework.web.multipart.MultipartFile;


/**
 * Created by Ayushi on 22/01/19.
 */

@Service
public class VehicleServiceImpl implements VehicleService{

	@Autowired
	VehicleManager vehicleManager;

	@Override
	public ApiResponse processXml(MultipartFile xmlFile, Long currentTS){
		return vehicleManager.processXml(xmlFile, currentTS);
	}

	@Override
	public ApiResponse getXmlData(){
		return vehicleManager.getXmlData();
	}

	@Override
	public ApiResponse getPastDataDetails(String filename){
		return vehicleManager.getPastDataDetails(filename);
	}
}
