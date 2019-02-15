package com.vehicle.detection.repository;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.vehicle.detection.repository.MongoConnector.getMongoDB;
/**
 * Created by Ayushi on 25/01/19.
 */
public class MongoUtility {

	static MongoDatabase database = getMongoDB();

	public static boolean saveDocument(String collection, Document document) {
		getCollection(collection).insertOne(document);
		return true;
	}

	public static MongoCollection<Document> getCollection(String collection) {
		return getMongoDB().getCollection(collection);
	}

	public static FindIterable<Document> findAll(String collection) {
		return database.getCollection(collection).find();
	}

	public static FindIterable<Document> find(String collection, Bson var){
		return database.getCollection(collection).find(var);
	}
}
