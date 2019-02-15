package com.vehicle.detection.manager;

import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.vehicle.detection.model.Material;
import com.vehicle.detection.model.Position;
import com.vehicle.detection.model.Vehicle;
import com.vehicle.detection.model.Wheel;
import com.vehicle.detection.repository.MongoUtility;
import com.vehicle.detection.util.ApiResponse;
import com.vehicle.detection.util.Messages;
import com.vehicle.detection.util.StatusCode;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.MongoClient;
import com.mongodb.DBCursor;
import java.io.IOException;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

/**
 * Created by Ayushi on 22/01/19.
 */

@Component
public class VehicleManager {

	private static final String ENTITY = "vehicle";

	public ApiResponse processXml(MultipartFile xmlFile, Long currentTS) {
		try {
			boolean saveData = false, saveFile = false;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File convFile = new File(xmlFile.getOriginalFilename());
			Document doc = dBuilder.parse(convFile);
			doc.getDocumentElement().normalize();
			String rootElement = doc.getDocumentElement().getNodeName();
			System.out.println(rootElement);
			String fileContent = getFileContent(xmlFile);
			NodeList nList = doc.getElementsByTagName("vehicle");
			List<Vehicle> vehicleList = new ArrayList<>();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				String currentElement = nNode.getNodeName();

				System.out.println(currentElement);
				Vehicle vehicle = new Vehicle();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					vehicle.setId(element.getElementsByTagName("id").item(0).getTextContent());
					NodeList frame = element.getElementsByTagName("frame");
					for (int ite = 0; ite < frame.getLength(); ite++) {
						Node nNodeFrame = frame.item(ite);
						if (nNodeFrame.getNodeType() == Node.ELEMENT_NODE) {
							Element elementFrame = (Element) nNodeFrame;
							vehicle.setFrame(elementFrame.getElementsByTagName("material").item(0).getTextContent());
						}
					}
					List<Wheel> wheelList = new ArrayList<>();
					Wheel wheel = new Wheel();
					NodeList wheelsNodeList = doc.getElementsByTagName("wheels");
					System.out.println(wheelsNodeList);
					String wheels = "";
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element elemenWheels = (Element) nNode;
						NodeList wheelNodeList = elemenWheels.getElementsByTagName("wheel");
						List<Position> positionList = new ArrayList<>();
						List<Material> materialList = new ArrayList<>();
						wheels += wheelNodeList.getLength()+ " ";
						Node nNode1 = wheelNodeList.item(0);
						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element elementWheel = (Element) nNode1;
							Material material = new Material();
							material.setMaterial(elementWheel.getElementsByTagName("material").item(0).getTextContent());
							materialList.add(material);
							wheels += material.getMaterial()+" ";
						}
						for (int tem = 0; tem < wheelNodeList.getLength(); tem++) {
							Node nNode2 = wheelNodeList.item(tem);
							if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
								Element elementWheel = (Element) nNode2;
								Position position = new Position();
								position.setPosition(elementWheel.getElementsByTagName("position").item(0).getTextContent());
								positionList.add(position);
								wheels += position.getPosition() + " ";
							}
						}
						wheel.setMaterialList(materialList);
						wheel.setPositionList(positionList);
						wheelList.add(wheel);
						System.out.println(
							"jgv" + wheel.getPositionList().get(0).getPosition() + "  " + wheel.getMaterialList().get(0).getMaterial());
					}
					System.out.println(wheels);
					vehicle.setWheels(wheelList);
					vehicle.setPowertrain(element.getElementsByTagName("powertrain").item(0).getTextContent());
					vehicle.setTimestampUpload(currentTS);
					vehicle.setFileContent(fileContent);
					System.out.println(vehicle.getId());
					System.out.println(vehicle.getFrame());
					System.out.println(vehicle.getWheels());
					System.out.println(vehicle.getPowertrain());
					System.out.println(vehicle.getVehicleName());
					vehicle.setVehicleName(vehicleCharacteristics(vehicle).getVehicleName());
					vehicle.setFileName(convFile.getName());
					long reportTime = new Date().getTime();
					Map<String, Object> mapData = new HashMap<>();
					mapData.put("vehicleName", vehicle.getVehicleName());
					mapData.put("id", vehicle.getId());
					mapData.put("frame", vehicle.getFrame());
					mapData.put("powertrain", vehicle.getPowertrain());
					mapData.put("wheels", wheels);
					mapData.put("timestampUpload", currentTS);
					mapData.put("timestampGeneration", reportTime);
					mapData.put("fileName", convFile.getName());
					mapData.put("fileContent", vehicle.getFileContent());
					vehicle.setTimestampGeneration(reportTime);
					saveData = saveXmlDataToDB(mapData);
					saveFile = saveFileIntoMongoDB(convFile);
				}
				if (saveData && saveFile)
					vehicleList.add(vehicle);
			}
			if (!CollectionUtils.isEmpty(vehicleList))
				return new ApiResponse(vehicleList, StatusCode.RECORD_SAVED.getValue(), true, Messages.RECORD_SAVED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(null, StatusCode.RECORDS_NOT_SAVED.getValue(), true, Messages.NO_DATA);
	}

	private boolean saveXmlDataToDB(Map<String, Object> map) {
		MongoUtility.saveDocument(ENTITY, new org.bson.Document(map));
		return true;
	}

	private boolean saveFileIntoMongoDB(File filename) throws IOException {
		String dbFileName = filename.getName();
		File xmlFile = new File("Xml.xml");
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("vehicle_db");
		GridFS gfs = new GridFS(db, "xml");
		GridFSInputFile gfsFile = gfs.createFile(xmlFile);
		gfsFile.setFilename(dbFileName);
		gfsFile.save();
		return true;
	}

	private static void getFile() throws IOException {
		String newFileName = "File";
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("vehicle_db");
		GridFS gfsPhoto = new GridFS(db, "xml");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
		imageForOutput.writeTo("untitled1.xml");
	}

	/*
	* Determine the type of vehicle
	* by its characteristics
	* */
	public Vehicle vehicleCharacteristics(Vehicle vehicle) {
		if ((vehicle.getFrame().equals("plastic")) && (vehicle.getPowertrain().equalsIgnoreCase("human"))) {
			vehicle.getWheels().stream().forEach(wheel -> {
				if (wheel.getPositionList().size() == 3) {
					wheel.getMaterialList().stream().forEach(material -> {
						if (material.getMaterial().equals("plastic")) {
							wheel.getPositionList().stream().forEach(position -> {
								if ((position.getPosition().equals("front")) || (position.getPosition().equals("rear left")) || (position.getPosition().equals("rear right")) || (position.getPosition().equals("right rear"))
									|| (position.getPosition().equals("left rear"))) {
									vehicle.setVehicleName("Big Wheel");
								}
							});
						}
					});
				}
			});
		} else if ((vehicle.getFrame().equals("metal")) && (vehicle.getPowertrain().equalsIgnoreCase("human"))) {
			vehicle.getWheels().stream().forEach(wheel -> {
				if (wheel.getPositionList().size() == 2) {
					wheel.getMaterialList().stream().forEach(material -> {
						if (material.getMaterial().equals("metal")) {
							wheel.getPositionList().stream().forEach(position -> {
								if ((position.getPosition().equals("front")) || (position.getPosition().equals("rear"))) {
									vehicle.setVehicleName("Bicycle");
								}
							});
						}
					});
				}
			});
		} else if ((vehicle.getFrame().equals("metal")) && (vehicle.getPowertrain()
			.equalsIgnoreCase("internal combustion"))) {
			vehicle.getWheels().stream().forEach(wheel -> {
				if (wheel.getPositionList().size() == 2) {
					wheel.getMaterialList().stream().forEach(material -> {
						if (material.getMaterial().equals("metal")) {
							wheel.getPositionList().stream().forEach(position -> {
								if ((position.getPosition().equals("front")) || (position.getPosition().equals("rear"))) {
									vehicle.setVehicleName("Motorcycle");
								}
							});
						}
					});
				}
			});
		} else if ((vehicle.getFrame().equals("plastic")) && (vehicle.getPowertrain().equalsIgnoreCase("Bernoulli"))) {
			vehicle.getWheels().stream().forEach(wheel -> {
				if (wheel.getPositionList().size() == 0) {
					vehicle.setVehicleName("Hang Glider");
				}
			});
		} else if ((vehicle.getFrame().equals("metal")) && (vehicle.getPowertrain()
			.equalsIgnoreCase("Internal Combustion"))) {
			vehicle.getWheels().stream().forEach(wheel -> {
				if (wheel.getPositionList().size() == 4) {
					wheel.getPositionList().stream().forEach(position -> {
						if ((position.getPosition().equals("front right")) || (position.getPosition().equals("rear right")) || (position.getPosition().equals("front left")) || (position.getPosition().equals("rear left"))
							|| (position.getPosition().equals("right front")) || (position.getPosition().equals("right rear")) || (position.getPosition().equals("left front")) || (position.getPosition().equals("left rear"))) {
							vehicle.setVehicleName("Car");
						}
					});
				}
			});
		}
		return vehicle;
	}

	public ApiResponse getXmlData() {
		LinkedHashSet<Map<String, Object>> fileList = new LinkedHashSet<>();
		FindIterable<org.bson.Document> findIterable = MongoUtility.findAll("xml.files");
//		List < DBObject > dbarray = alldocs.toArray();
		findIterable.forEach((Block<? super org.bson.Document>) doc -> fileList.add(doc));
		if(!CollectionUtils.isEmpty(fileList))
			return new ApiResponse(fileList, StatusCode.RECORD_FETCHED.getValue(), true, Messages.RECORD_FETCHED);
		else
			return new ApiResponse(null, StatusCode.RECORD_NOT_FETCHED.getValue(), false, Messages.RECORD_NOT_FETCHED);
	}

	public ApiResponse getPastDataDetails(String filename) {
		List<Bson> filters = new ArrayList<>();
		filters.add(Filters.eq("fileName", filename));
		LinkedHashSet<Map<String, Object>> fileList = new LinkedHashSet<>();
		FindIterable<org.bson.Document> findIterable = MongoUtility.find("vehicle", Filters.and(filters));
		findIterable.forEach((Block<? super org.bson.Document>) doc -> fileList.add(doc));
		if(!CollectionUtils.isEmpty(fileList))
			return new ApiResponse(fileList, StatusCode.RECORD_FETCHED.getValue(), true, Messages.RECORD_FETCHED);
		else
			return new ApiResponse(null, StatusCode.RECORD_NOT_FETCHED.getValue(), false, Messages.RECORD_NOT_FETCHED);
	}

	public String getFileContent(MultipartFile file){
		String xmlString ="";
		try {
			XML xml = new XMLDocument(new File(file.getOriginalFilename()));
			xmlString = xml.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("XML as String using JCabi library : " );
		System.out.println(xmlString);
		return xmlString;
	}
}
