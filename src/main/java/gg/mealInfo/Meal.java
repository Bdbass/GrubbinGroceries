package gg.mealInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import gg.physObjs.Food;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;


public class Meal {
	private String name;
	private Map<String, Double> items; 
	private String instructions; 
	private Date date;
	private String userID;
	
	public Meal()
	{
		name = "unknown";
		items = new HashMap<String, Double>();
		instructions = "unknown";
		date = new Date();
		userID = "unknown";
	}
	
	// create meal from recipe
	public Meal(Recipe r, String ID, boolean add)
	{
		name = r.getName();
		items = r.getItems();
		instructions = r.getInstructions();
		date = new Date();
		userID = ID;
		
		if (add)
			addMeal(); 
	}
	
	public Meal(Recipe r, Date d, String ID, boolean add)
	{
		name = r.getName();
		items = r.getItems();
		instructions = r.getInstructions();
		date = d;
		userID = ID;
		
		if (add)
			addMeal();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name, boolean update) {
		this.name = name;
		if (update) 
			editMeal("name", this.name);
	}
	public Map<String, Double>getItems() {
		return items;
	}
	public void setItems(Map<String, Double> items, boolean update) {
		this.items = items;
		if (update) 
			editMeal("items", this.items);
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions, boolean update) {
		this.instructions = instructions;
		if(update)
			editMeal("instructions", this.instructions);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date, boolean update) {
		this.date = date;
		if (update)
			editMeal("date", this.date);
	} 
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID, boolean update) {
		this.userID = userID;
		if (update)
			editMeal("userID", this.userID);
	}
	
	//returns your mongo collection so you can use it 
	public static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = database.getCollection("meals");
	    
	    return collection; 
	}
	
	public void addMeal() {
	    // get a handle to the "meals" collection
	    MongoCollection<Document> collection = getCollection(); 
        
	    //check if the meal already exists
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("Meal is already in the database! "); 
	    	return; 		
	    }
	    // create the meal     
		Document document = new Document(); 
		document.put("name", this.name); 
		document.put("items", this.items); 
		document.put("instructions", this.instructions);
		document.put("date", this.date);
		document.put("userID", userID);
		
		//insert the meal
		collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = collection.find(eq("name", this.name)).first();
		System.out.println("Meal was added");
		System.out.println(myDoc.toJson());		
	}
	
	public void editMeal(String field, Object value) {	
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Meal does not exist.");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("name", this.name), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    myDoc = collection.find(eq("name", this.name)).first();
	    System.out.println("Meal was updated");
	    System.out.println(myDoc.toJson());
	}

	//deletes the meal if it exists
	public void deleteMeal() {
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Meal does not exist!");
	    	return;
	    }
	    
	    DeleteResult deleteResult = collection.deleteOne(eq("name", this.name));
	    System.out.println("Meal was deleted");
	    System.out.println(deleteResult.getDeletedCount());	    
	}
	
	public void addItem(String item, Double amount, boolean update) {
		for (String key : items.keySet()) {
			if (key == item) {
				System.out.println(item + " is already an ingredient in this meal."); 
				return;
			}
			else {
				items.put(item, amount);
				if (update)
					editMeal("items", this.items);
				System.out.println(item + " was added to this meal.");
			}
		}
	}
	
	public void editItem(String item, Double amount, boolean update) {
		for (String key : items.keySet()) {
			if (key == item) {
				items.put(key, amount);
				if (update)
					editMeal("items", this.items);
				System.out.println(item + " was edited.");
			}
			else {
				System.out.println(item + " is not an ingredient in this meal.");
			}
		}
	}
	
	public void deleteItem(String item, Double amount, boolean update) {
		for (String key: items.keySet()) {
			if (key == item) {
				items.remove(key);
				if (update) 
					editMeal("items", this.items);
				System.out.println(item + " was deleted from this meal."); 
			}
			else {
				System.out.println(item + " is not an ingredient in this meal."); 
			}
		}
	}
}
