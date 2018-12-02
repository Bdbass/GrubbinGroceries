package gg.APIs;

import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

public class TempThread {
	
	public MongoCollection<Document> collection; 
	public FindIterable<Document> docs; 
	public MongoClient client; 
	
	public TempThread(FindIterable<Document> d, MongoClient c){
		client = c; 
		docs = d; 
		collection = null; 
	}
	
	public TempThread(MongoCollection<Document> col, MongoClient c){
		client = c; 
		docs = null; 
		collection = col; 
	}
	
	
}
