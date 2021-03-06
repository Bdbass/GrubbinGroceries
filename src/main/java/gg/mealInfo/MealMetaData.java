package gg.mealInfo;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import gg.APIs.TempThread;

import static com.mongodb.client.model.Filters.*;

public class MealMetaData {
	
	private String name; 
	private double totalAmount; 
	private double pantryAmount;
	private double shoppingAmount;
	private String mealID; 
	
	// default constructor 
	public MealMetaData() {
		this.totalAmount = 0.0;
		this.pantryAmount = 0.0;
		this.shoppingAmount = 0.0;
		this.setMealID("unkown"); 
	}
	
	// Constructor for a meal  
	public MealMetaData(String mealID) {
		
		this.name = "unknown"; 
		this.totalAmount = 0.0;
		this.pantryAmount = 0.0;
		this.shoppingAmount = 0.0;
		this.setMealID(mealID); 
	}

	// constructor 
	public MealMetaData(double totalAmount, double pantryAmount, double shoppingAmount, String name,  String mealID) {
		this.totalAmount = totalAmount;
		this.pantryAmount = pantryAmount;
		this.shoppingAmount = shoppingAmount;
		this.name = name; 
		this.setMealID(mealID); 
	} 
	
	//constructor given a document 
	public MealMetaData(Document d) {
		this.totalAmount = d.getDouble("totalAmount");
		this.pantryAmount = d.getDouble("pantryAmount");
		this.shoppingAmount = d.getDouble("shoppingAmount"); 
		this.name = d.getString("name"); 
		this.setMealID(d.getString("mealID")); 
	}
	
	//getters and setters 
	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getPantryAmount() {
		return pantryAmount;
	}

	public void setPantryAmount(double pantryAmount) {
		this.pantryAmount = pantryAmount;
	}

	public double getShoppingAmount() {
		return shoppingAmount;
	}

	public void setShoppingAmount(double shoppingAmount) {
		this.shoppingAmount = shoppingAmount;
	}

	public String getMealID() {
		return mealID;
	}

	public void setMealID(String mealID) {
		this.mealID = mealID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String create(Double total, Double pantry, Double shopping, String mealID, String name) {
		TempThread t = getCollection();  
		// create the recipe     
		Document document = new Document(); 
		document.put("name", name); 
		document.put("totalAmount", total); 
		document.put("pantryAmount", pantry); 
		document.put("shoppingAmount", shopping); 
		document.put("mealID", mealID); 
		
		//insert the recipe
		t.collection.insertOne(document); 
		
		//return documents id 
		Document myDoc = t.collection.find(and(eq("name", name), eq("mealID", mealID))).first();
		
		//close collection 
		t.client.close();
		return myDoc.get("_id").toString(); 
	}
	
	public static TempThread mealMetaData(Document meal) {
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create("mongodb://guest:superSecretPassword@18.188.67.103/GrubbinGroceries");     	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = database.getCollection("mealMetaData"); //jg misspelled. Corrected assuming it should be	    
	    FindIterable<Document> d = collection.find(eq("mealID", meal.get("_id").toString()));
	    return new TempThread(d, mongoClient); 
	}
	
	//returns the shopping list collection 
	public static TempThread getCollection() {
		//create the client 
		MongoClient mongoClient = MongoClients.create("mongodb://guest:superSecretPassword@18.188.67.103/GrubbinGroceries"); 
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	    //find the users pantry and create a local copy of their pantry 
	    MongoCollection<Document> collection = database.getCollection("mealMetaData");
	    return new TempThread(collection, mongoClient); 
	} 
}
