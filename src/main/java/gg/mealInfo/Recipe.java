package gg.mealInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;


public class Recipe {
	
	//String is item name, double is fraction 
	//or multiple of serving i.e. ("oats", 0.5) 
	private Map<String, Double> items; 
	private String instructions; 
	private String name; 
	
	
	//constructors
	public Recipe() {
		this.items = new HashMap<String, Double>(); 
		this.instructions = "None"; 
		this.name = "unknown"; 
	}
	
	public Recipe(Map<String, Double> items, String instructions, String name, boolean add) {
		this.items = items; 
		this.instructions = instructions; 
		this.name = name; 
		
		//we may want to add the recipe to the db right away, or we may not want to 
		if (add)
			addRecipe(); 
	}
	
	//setters and getters 
	public Map<String, Double> getItems() {
		return items;
	}

	public void setItems(Map<String, Double> items, boolean update) {
		this.items = items;
		if (update)
			editRecipe("items", this.items); 	
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions, boolean update) {
		this.instructions = instructions;
		if (update)
			editRecipe("instructions", this.instructions); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name, boolean update) {
		this.name = name;
		if (update)
			editRecipe("name", this.name); 
	}
	
	//returns your mongo collection so you can use it 
	private static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = database.getCollection("recpies");
	    
	    return collection; 
	}
	
	//adds the current recipe to the database if it does not already exist 
	public void addRecipe() {	
	
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = getCollection(); 
        
	    //check if the recipe already exists
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("Recipe is already in the database! "); 
	    	return; 		
	    }
	    // create the recipe     
		Document document = new Document(); 
		document.put("name", this.name); 
		document.put("instructions", this.instructions); 
		document.put("items", this.items); 
		
		//insert the recipe
		collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = collection.find(eq("name", this.name)).first();
		System.out.println("Recipe was added");
		System.out.println(myDoc.toJson());			
	}
	
	//edits the current recipe if it exists in the db 
	public void editRecipe(String field, Object value) {
		
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Recipe has not been added, be sure to add it first");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("name", this.name), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    myDoc = collection.find(eq("name", this.name)).first();
	    System.out.println("Recipe was updated");
	    System.out.println(myDoc.toJson());
	}
	
	//deletes the recipe if it exists
	public void deleteRecipe() {
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Recipe does not exist!");
	    	return;
	    }
	    
	    DeleteResult deleteResult = collection.deleteOne(eq("name", this.name));
	    System.out.println("Recipe was deleted");
	    System.out.println(deleteResult.getDeletedCount());	    
	}
	
	//TO DO 
	public void printRecipe() {
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    System.out.println(myDoc.get("name")); 
	    HashMap<String, Double> h = myDoc.get("items", HashMap.class); 
	    System.out.println("Ingredients");
	    for (String i: h.keySet()) {
	    	System.out.println(i + h.get(i));
	    }
	    System.out.println("Instructions");
	    System.out.println(myDoc.get("instructions"));
	   
	}
	static //TO DO
	Block<Document> printBlock = new Block<Document>() {
		public void apply(final Document document) {
			System.out.println(document.get("name")); 
		    HashMap<String, Double> h = document.get("items", HashMap.class); 
		    System.out.println("Ingredients");
		    for (String i: h.keySet()) {
		    	System.out.println(i + h.get(i));
		    }
		    System.out.println("Instructions");
		    System.out.println(document.get("instructions"));
		}
	};
	
	
	public static void printAllRecipes() {
		MongoCollection<Document> collection = getCollection(); 		    
	    collection.find().forEach(printBlock);;
	}
	
	//Only use for this driver test function!!
	public static void deleteAllRecipes() {
		MongoCollection<Document> collection = getCollection(); 
		collection.drop(); 
	}
	
	public static void main(String args[]) {
		System.out.println("Recipe test Driver"); 
		
		//going to clear out the recipe collection so we get a 
		//clean run each time 
		deleteAllRecipes(); 
		
		//create recipe
		Recipe r = new Recipe(); 
		r.setName("Chocolate Peanut Butter Jelly Overnight Oats", false);
		r.setInstructions("1. Place all ingredients, except raspberries and additional toppings in a medium sized bowl.\n" +
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
		r.setItems(foodItems, false);
		
		
		//check for editing a recipe in the db that doesn't exist
		r.setName("Temp name", true); 
		
		//add recipe to db 
		r.addRecipe();
		
		//test for repeat add error
		r.addRecipe();
		
		//update the recipe's name
		r.setName("Chocolate Peanut Butter Jelly Overnight Oats", true);
		
		//update the recipe's instruction
		MongoCollection<Document> collection = getCollection(); 
		
	    Document myDoc = collection.find(eq("name", r.getName())).first();
	    
	    String instructions = myDoc.get("instructions").toString(); 
	    
	    instructions += "5. P.S. Almonds are bomb af on this!"; 
	    
	    r.setInstructions(instructions, true);
	    
	    //update the recipe's items 	    
	    HashMap<String, Double> items = myDoc.get("items", HashMap.class); 
	   
	    items.replace("almonds", 10.0, 8.0); 
	    r.setItems(items, true);
		
	    //print the recipe
	    r.printRecipe();
	    	    
	    //add another recipe 
  		Recipe r2 = new Recipe(); 
  		r2.setName("Breakfast Nachos", false);
  		r2.setInstructions("1. Saute peppers, onions, chicken sausage and beans for 5 minutes.\n" + 
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
  		r2.setItems(foodItems2, false);
	    	    
	    //print all recipes 
		printAllRecipes(); 
  		
  		//search for a recipe ingredient , avacado 
		collection.find(gt("items.avacado", 0.0)).forEach(printBlock);
		
	}
}
