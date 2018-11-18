package gg.physObjs;

import java.util.Map;
import java.util.HashMap;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

public class Pantry {
	private String userID;
	private Map<String,Double> items;
	
	//default constructor 
	public Pantry() {
		this.userID = "unknown";
		this.items = new HashMap<String,Double>();
	}
	
	// Constructor for new user's pantry 
	public Pantry(String userID) {
		this.userID = userID;
		this.items = new HashMap<String,Double>();
		addPantry(); 
	}
	
	//create a pantry based off an object 
	public Pantry(Document d) {
		Document d1 = (Document) d.get("items");
		this.items = new HashMap<String, Double>(); 
		
		for (String i: d1.keySet()) {
			items.put(i, d1.getDouble(i)); 
		}
		
		this.userID = d.getString("userID"); 
	}
	
	//setters and getters 
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID, boolean update) {
		this.userID = userID;
		if (update)
			editPantry("userID", this.userID);
	}
	
	public Map<String, Double> getItems() {
		return items;
	}
	
	public void setItems(Map<String, Double> items, boolean update) {
		this.items = items;
		if (update) 
			editPantry("items", this.items);
	}
	
	public void setItems(Document d, boolean update) {
		for (String s: d.keySet()) {
			items.put(s, d.getDouble(s));
		}
		if (update)
			editPantry("items", this.items);
	}
	
	
	
	//return the pantries collection
	public static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "pantries" collection
	    MongoCollection<Document> collection = database.getCollection("pantries");
	    
	    return collection; 
	}
	
	//add the pantry to the database 
	public void addPantry() {
		
	    // get pantries collection
	    MongoCollection<Document> collection = getCollection(); 
        
	    //check if the Pantry already exists by checking for userID
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("Pantry is already in the database!"); 
	    	return; 		
	    }
	    
	    // create the Pantry     
		Document document = new Document(); 
		document.put("userID", userID);
		document.put("items", this.items); 
		
		//insert the Pantry
		collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = collection.find(eq("userID", this.userID)).first();
		System.out.println("Pantry was added");
		System.out.println(myDoc.toJson());				
	}
	
	//update the pantry in the database 
	public void editPantry(String field, Object value) {
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    if (myDoc == null) {
	    	System.out.println("Pantry does not exist. Cannot be edited.");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("userID", this.userID), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    if (field == "userID") {
	    	myDoc = collection.find(eq("userID", value.toString())).first();
	    }
	    else {
	    	myDoc = collection.find(eq("userID", this.userID)).first();
	    }
	    
	    System.out.println("Pantry was updated");
	    System.out.println(myDoc.toJson());			
	}
	
	//remove a specific amount of food from the pantry and return how much was removed 
	public Double removeFood(String item, Double amount) {
		
		//look for item in pantry
		if (items.containsKey(item)) {
			
			//if we have more of this item in the pantry then need, just update the items amount in the pantry
			if (items.get(item) > amount) {
				System.out.println("Previous value for " + item + ": " + items.get(item));
				
				items.put(item, (items.get(item) - amount)); 
				editPantry("items", this.items); 
				
				System.out.println(item + " updated in pantry. New amount: " + items.get(item));
				
				return amount; 
			}
			
			//if we're removing everything that was left of the item, delete the item from the pantry 
			Double amountRemoved = items.get(item); 
			items.remove(item);
			editPantry("items", this.items);
			
			System.out.println(item + " was removed from pantry."); 
			return amountRemoved; 
		}
		// item was not in pantry 
		System.out.println(item + " is not in your pantry."); 
		return 0.0; 
	}
	
	//add food to the pantry, and update the database if need be 
	public void addFood(String item, Double amount, boolean update) {
		
		//check if the pantry already contains this food, if it does just update the amount
		if (items.containsKey(item)) {
			System.out.println("Previous value for " + item + ": " + items.get(item)); 
			items.put(item, (items.get(item) + amount));
			System.out.println(item + " updated in pantry. New amount: " + items.get(item));
			if (update)
				editPantry("items", this.items);
			return;
		}
		
	    // if it doesn't just add it to the pantry as a new item
		items.put(item, amount);
		System.out.println(item + " added to your pantry.");
		if (update)
			editPantry("items", this.items);	
	}
	
	//prints the current pantry 
	public void printPantry() {
	    MongoCollection<Document> collection = getCollection(); 
	    
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    if (myDoc == null) {
	    	System.out.println("This pantry has not been saved to the database and cannot be printed");
	    	return; 
	    }
	    
	    System.out.println("User: " + myDoc.get("userID"));
	    Document d = (Document) myDoc.get("items"); 
	    System.out.println("Pantry items:");
	    for (String i: d.keySet()) {
	    	System.out.println(i +": "+ d.get(i));
	    }			
	}
	
	//ONLY USE FOR DRIVER
	public static void deleteAllPantries() {
		MongoCollection<Document> collection = getCollection(); 
		collection.drop(); 
	}
	
	public static void main(String args[]) {
		//MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		
		System.out.println("Pantry test Driver"); 
		
		//going to clear out the meals collection so we get a 
		//clean run each time 
		deleteAllPantries(); 	
		
		//test creating a shopping list with userID
		System.out.println("Creating a new Pantry");
		Pantry p = new Pantry("12345");
		p.addFood("banana", 2.0, false);
		p.addFood("chicken", 1.0, false);
		p.addPantry();
		
		//test add new item(s)
		p.addFood("pineapple", 3.0, true);
		//test add existing item
		p.addFood("chicken", 1.0, true);
		
		//test remove existing item partially
		p.removeFood("chicken", 1.0);
		//test remove existing item fully
		p.removeFood("banana", 2.0);
		//test remove non-existing item
		p.removeFood("cucumber", 1.0);
		
		//print it
		p.printPantry(); 
	}

}
