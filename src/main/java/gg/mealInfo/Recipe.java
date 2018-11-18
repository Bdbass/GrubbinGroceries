package gg.mealInfo;

import gg.userInfo.*;
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
	private ArrayList<String> restrictions; 
	private MealType mealType; 
	
	//constructors
	public Recipe() {
		this.items = new HashMap<String, Double>(); 
		this.instructions = "None"; 
		this.name = "unknown"; 
		this.restrictions = new ArrayList<String>(); 
	}
	
	public Recipe(Map<String, Double> items, String instructions, String name, boolean add, 
					ArrayList<String> restrictions, MealType mealType) {
		this.items = items; 
		this.instructions = instructions; 
		this.name = name; 
		this.mealType = mealType; 
		this.restrictions = restrictions; 
		
		//we may want to add the recipe to the db right away, or we may not want to 
		if (add)
			addRecipe(); 
	}
	
	public Recipe(Document d) {
		Document d1 = (Document) d.get("items");
		HashMap<String, Double> temp = new HashMap<String, Double>(); 
		for (String i: d1.keySet()) {
			temp.put(i, d1.getDouble(i)); 
		}
		this.items = temp;
		this.instructions = d.getString("instructions"); 
		this.name = d.getString("name"); 
		this.mealType = MealType.valueOf(d.getString("mealType")); 
		this.restrictions = (ArrayList<String>) d.get("restrictions"); 
	}
	
	//setters and getters
	public ArrayList<String> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(ArrayList<String> restrictions, boolean update) {
		this.restrictions = restrictions;
		if (update)
			editRecipe("restrictions", this.restrictions); 
		
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType, boolean update) {
		this.mealType = mealType;
		if (update)
			editRecipe("mealType", this.mealType.toString()); 
	}
 
	public Map<String, Double> getItems() {
		return items;
	}

	public void setItems(Map<String, Double> items, boolean update) {
		this.items = items;
		if (update)
			editRecipe("items", this.items); 	
	}
	
	public void setItems(Document d, boolean update) {
		HashMap<String, Double> temp = new HashMap<String, Double>(); 
		for (String s: d.keySet()) {
			temp.put(s, d.getDouble(s));
		}
		this.items = temp; 
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
		if (update)
			editRecipe("name", name); 
		this.name = name;
	}
	
	//returns your mongo collection so you can use it 
	public static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = database.getCollection("recipes"); //jg misspelled. Corrected assuming it should be
	    
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
		document.put("restrictions", this.restrictions); 
		document.put("mealType", this.mealType.toString()); 
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
	    if (field == "name") {
	    	myDoc = collection.find(eq("name", value.toString())).first();
	    }else {
	    	myDoc = collection.find(eq("name", this.name)).first();
	    }
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
	 
	public void printRecipe() {
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    Document myDoc = collection.find(eq("name", this.name)).first();
	    
	    if (myDoc == null) {
	    	System.out.println("This recipe has not been saved to the database and it cannot be printed");
	    	return; 
	    }
	    
	    System.out.println(myDoc.get("name")); 
	    System.out.println("meal type:" + myDoc.get("mealType"));
	    Document d = (Document) myDoc.get("items"); 
	    System.out.println("Ingredients");
	    for (String i: d.keySet()) {
	    	System.out.println(i +": "+ d.get(i));
	    }
	    System.out.println("Instructions");
	    System.out.println(myDoc.get("instructions"));
	    System.out.println("Restrictions"); 
	    ArrayList<String> a =  (ArrayList<String>) myDoc.get("restrictions"); 
	    for (String j: a) {
	    	System.out.println(j.toString());
	    }  
	    System.out.println(); 
	} 
	static 
	Block<Document> printBlock = new Block<Document>() {
		public void apply(final Document document) {
			System.out.println(document.get("name")); 
			System.out.println("meal type:" + document.get("mealType"));
			Document d = (Document) document.get("items"); 
		    System.out.println("Ingredients");
		    for (String i: d.keySet()) {
		    	System.out.println(i +": "+ d.get(i));
		    }
		    System.out.println("Instructions");
		    System.out.println(document.get("instructions"));
		    System.out.println("Restrictions"); 
		    ArrayList<String> a =  (ArrayList<String>) document.get("restrictions"); 
		    for (String j: a) {
		    	System.out.println(j.toString());		    
		    }  
		    System.out.println(); 
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
		//MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		
		
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
		ArrayList<String> restrictions = new ArrayList<String>(); 
		restrictions.add(RestType.GF.toString()); 
		r.setRestrictions(restrictions, false);
		r.setMealType(MealType.BREAKFAST, false);
		
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
	    Document items = (Document) myDoc.get("items"); 
	    
	    items.put("almonds", 8.0); 
	    r.setItems(items, true);
		
	    //print the recipe
	    System.out.println();
	    System.out.println("Print Recipe");
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
  		ArrayList<String> restrictions2 = new ArrayList<String>(); 
  		restrictions2.add(RestType.GF.toString()); 
		r2.setRestrictions(restrictions, false);
		r2.setMealType(MealType.BREAKFAST, false);
		r2.addRecipe();
		
	    //print all recipes
		System.out.println(); 
		System.out.println("Print all recipes"); 
		printAllRecipes(); 
  		
		System.out.println();
		System.out.println("Print all recipes with avacado");
  		//search for a recipe ingredient , avocado 
		collection.find(gt("items.avacado", 0.0)).forEach(printBlock);
		
	}
}
