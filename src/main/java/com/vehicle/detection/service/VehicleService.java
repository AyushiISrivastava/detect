package com.vehicle.detection.service;

import com.vehicle.detection.util.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Ayushi on 21/01/19.
 */
public interface VehicleService {

	public ApiResponse processXml(MultipartFile xmlFile, Long currentTS);

	public ApiResponse getXmlData();

	public ApiResponse getPastDataDetails(String filename);
}
