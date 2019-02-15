package com.vehicle.detection.controller;

import com.vehicle.detection.service.VehicleService;
import com.vehicle.detection.util.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
/**
 * Created by Ayushi on 21/01/19.
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);

	@Autowired
	VehicleService vehicleService;

	@PostMapping(value = "/readxml")
	@ResponseBody ApiResponse readXml(@RequestParam("xmlFile") MultipartFile xmlFile) {
		Long currentTS = new Date().getTime();
		return vehicleService.processXml(xmlFile, currentTS);
	}

	@GetMapping(value = "/getXmlData")
	@ResponseBody ApiResponse getXmlData(){
		return vehicleService.getXmlData();
	}

	@PostMapping(value = "/getPastDataDetails")
	@ResponseBody ApiResponse getPastDataDetails(@RequestParam("filename") String filename) {
		return vehicleService.getPastDataDetails(filename);
	}
}
