package gg.mealInfo;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;

import gg.mealInfo.*;


public class Meal{
	private String name;
	private Map<String, Double> items; 
	private String instructions; 
	private String date;
	private String userID;
	
	public Meal()
	{
		name = "unknown";
		items = new HashMap<String, Double>();
		instructions = "unknown";
		date = "unknown";
		userID = "unknown";
	}
	
	// create meal from recipe
	public Meal(Recipe r, String ID, boolean add)
	{
		name = r.getName();
		items = r.getItems();
		instructions = r.getInstructions();
		date = "unknown";
		userID = ID;
		
		if (add)
			addMeal(); 
	}
	
	public Meal(Recipe r, String d, String ID, boolean add)
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
		if (update)
			editMeal("name", name); 
		this.name = name;
	}
	
	public Map<String, Double>getItems() {
		return items;
	}
	
	public void setItems(Map<String, Double> items, boolean update) {
		this.items = items;
		if (update) 
			editMeal("items", this.items);
	}
	
	public void setItems(Document d, boolean update) {
		HashMap<String, Double> temp = new HashMap<String, Double>(); 
		for (String s: d.keySet()) {
			temp.put(s, d.getDouble(s));
		}
		this.items = temp; 
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
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date, boolean update) {
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
	
	public Document addMeal() {
	    // get a handle to the "meals" collection
	    MongoCollection<Document> collection = getCollection(); 
        
	    //check if the meal already exists
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("Meal is already in the database! "); 
	    	return null; 		
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
		
		return myDoc;
	}
	
	public void editMeal(String field, Object value) {	
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Meal does not exist. Cannot be edited.");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("name", this.name), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    if (field == "name") {
	    	myDoc = collection.find(eq("name", value.toString())).first();
	    }else {
	    	myDoc = collection.find(eq("name", this.name)).first();
	    }
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
		}
		items.put(item, amount);
		if (update)
			editMeal("items", this.items);
		System.out.println(item + " was added to this meal.");
		//PANTRY?
	}
	
	public void editItem(String item, Double amount, boolean update) {
		for (String key : items.keySet()) {
			if (key == item) {
				items.put(key, amount);
				if (update)
					editMeal("items", this.items);
				System.out.println(item + " was edited.");
				//pantry?
				return;
			}
		}
		System.out.println(item + " is not an ingredient in this meal.");
		
	}
	
	public void deleteItem(String item, boolean update) {
		for (String key: items.keySet()) {
			if (key == item) {
				items.remove(key);
				if (update) 
					editMeal("items", this.items);
				System.out.println(item + " was deleted from this meal."); 
				//Pantry?
				return;
			}
		}
		System.out.println(item + " is not an ingredient in this meal."); 
	}
	
	public void printMeal() {
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    System.out.println(myDoc.get("date"));
	    System.out.println(myDoc.get("name"));
	    Document d = (Document) myDoc.get("items"); 
	    System.out.println("Ingredients");
	    for (String i: d.keySet()) {
	    	System.out.println(i +": "+ d.get(i));
	    }
	    System.out.println("Instructions");
	    System.out.println(myDoc.get("instructions"));
	}
	
	//Only use for this driver test function!!
	public static void deleteAllMeals() {
		MongoCollection<Document> collection = getCollection(); 
		collection.drop(); 
	}
	
	
	public static void main(String args[]) {
		//MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		
		System.out.println("Meal test Driver"); 
		
		//going to clear out the meals collection so we get a 
		//clean run each time 
		deleteAllMeals(); 	
		
		System.out.println("test plain constructor and each setter");
		Meal m = new Meal();
		m.setName("Chocolate Peanut Butter Jelly Overnight Oats", false);
		m.setInstructions("1. Place all ingredients, except raspberries and additional toppings in a medium sized bowl.\n" +
				 "2. Stir until well combined and then gently fold in 1/4 cup raspberries.\n" + 
				 "3. Store in a covered, airtight container, like a mason jar, in the fridge overnight.\n" + 
				 "4. In the morning, top with additional toppings, if desired, and enjoy!\n", false);
		
		HashMap<String, Double> foodItems = new HashMap<String, Double>(); 
		foodItems.put("oats", 0.5); 
		foodItems.put("skim milk", 0.75); 
		foodItems.put("greek yogurt vanilla", 1.0);
		foodItems.put("penut butter", 2.0);
		foodItems.put("cocoa powder", 1.0);
		foodItems.put("salt", 1.0);
		foodItems.put("banana", 1.0);
		foodItems.put("raspberries", 10.0); 
		foodItems.put("almonds", 10.0);
		m.setItems(foodItems, false);	
		m.setDate("12/25/2018", false);
		m.setUserID("1233456", false);
		
		System.out.println("check for editing a recipe in the db that doesn't exist");
		m.setName("Temp name", true); 
		
		System.out.println("add recipe to db");
		m.addMeal();
		
		System.out.println("test for repeat add error");
		m.addMeal();
		
		System.out.println("update the meal's name");
		m.setName("Chocolate Peanut Butter Jelly Overnight Oats", true);
		
		System.out.println("test adding an item");
		m.addItem("grapes", 2.0, true); 
		System.out.println("test adding an item that exists already");
		m.addItem("salt", 1.0, true); 
		
		System.out.println("test editing an item"); 
		m.editItem("grapes", 4.0, true); 
		System.out.println("test editing an item that is not there"); 
		m.editItem("pineapple", 1.0 , true);
		
		System.out.println("test deleting an item");
		m.deleteItem("grapes", true);
		System.out.println("test deleting an item that is not there"); 
		m.deleteItem("grapes", true); 
		
		System.out.println("test constructor given a recipe"); 
		Recipe r = new Recipe();
  		r.setName("Breakfast Nachos", false);
  		r.setInstructions("1. Saute peppers, onions, chicken sausage and beans for 5 minutes.\n" + 
  							"Push the mixture to one side of the pan and add eggs.\n" + 
  							"2. Cook until scrambled and mix well.\n" + 
  							"3. Spoon mixture over chips and top with cheese, avocado and salsa.\n" + 
  							"4. Serve warm.\n", false);
  		
  		HashMap<String, Double> foodItems2 = new HashMap<String, Double>(); 
  		foodItems2.put("bell pepper", 0.5); 
  		foodItems2.put("onion", 0.25); 
  		foodItems2.put("black beans", 0.5);
  		foodItems2.put("chicken sausage", 1.0);
  		foodItems2.put("eggs", 3.0);
  		foodItems2.put("avacado", 1.0);
  		foodItems2.put("salsa", 0.5);
  		foodItems2.put("cheese", 1.0); 
  		foodItems2.put("tortilla chips", 1.0);
  		r.setItems(foodItems2, false);
		r.addRecipe();  		
  		
		Meal m2 = new Meal(r, "54321", false); 
		m2.addMeal();
		m2.printMeal();
  		
		System.out.println("test constructor given a recipe and a date");
		Recipe r2 = new Recipe();
		r2.setName("Grilled Cheese", false);
		r2.setInstructions("1. Put slice of cheese between two pieces of bread to form sandwhich.\n" +
							"2. Place the sandwhich on a frypan on the stove.\n" +
							"3. Turn burner to medium heat.\n" +
							"4. Wait 3 minutes, then flip the sandwhich to the other side.\n" +
							"5. After another 3 minutes, remove from stove and serve warm.\n", false);
		HashMap<String, Double> foodItems3 = new HashMap<String, Double>();
		foodItems3.put("bread", 2.0);
		foodItems3.put("cheese", 1.0);
		r2.setItems(foodItems3, false);
		r2.addRecipe();
		
		Meal m3 = new Meal(r2, "01/01/2019", "246810", false);
		m3.addMeal();
		m3.printMeal();
		
	}
}
