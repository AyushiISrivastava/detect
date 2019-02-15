package com.vehicle.detection.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ayushi on 25/01/19.
 */
@Controller
public class MongoConnector {
		static String host;
		static String username;
		static String password;
		static String dbname;

		@Value("${mongo.host}")
		public void setHost(String host) {
			MongoConnector.host = host;
		}


		private static Map<String, MongoDatabase> dbs = new HashMap();

		private MongoConnector() {
			// do nothing
		}

		public static MongoDatabase getMongoDB() {
			try {
				MongoClient mongoClient = new MongoClient(new MongoClientURI(host));
				MongoDatabase database = mongoClient.getDatabase("vehicle_db");
				dbs.put("vehicle", database);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getMessage());
			}
			return dbs.get("vehicle");

		}
	}

