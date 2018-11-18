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
import gg.physObjs.Pantry;


public class Meal{
	
	//maps food to a mealmetadata obj
	private Map<String, String> items; 
	private String instructions; 
	private String date;
	private String userID;
	private MealType mealType; 
	
	// base meal constructor 
	public Meal()
	{
		items = new HashMap<String, String>();
		instructions = "none";
		date = java.time.LocalDate.now().toString();
		userID = "unknown";
		mealType = MealType.OTHER; 
	}
	
	// create meal from recipe for today
	public Meal(Recipe r, String userID)
	{		
		//make sure to update shopping list and pantry
		Map<String, Double> temp = r.getItems(); 
		for (String item: temp.keySet()) {
			this.addItem(item, temp.get(item));
		} 
		
		instructions = r.getInstructions();
		date = java.time.LocalDate.now().toString();
		mealType = r.getMealType(); 
		this.userID = userID;
		
		// add meal to db 
		addMeal(); 
	}
	
	// create meal from recipe for a specific date 
	public Meal(Recipe r, String userID, String date)
	{
		//make sure to update shopping list and pantry 
		Map<String, Double> temp = r.getItems(); 
		for (String item: temp.keySet()) {
			this.addItem(item, temp.get(item));
		}
		
		instructions = r.getInstructions();
		mealType = r.getMealType(); 
		this.date = date; 
		this.userID = userID;
		
		// add meal to db 
		addMeal(); 
	}
	
	//create meal from document 
	public Meal(Document d) {
		Document d1 = (Document) d.get("items");
		items = new HashMap<String, String>(); 
		
		//create all meal items 
		for (String item: d1.keySet()) {
			items.put(item, d1.getString(item)); 
		}
		this.instructions = d.getString("instructions"); 
		this.mealType = MealType.valueOf(d.getString("mealType")); 
		this.date = d.getString("date"); 
		this.userID = d.getString("userID"); 
	}
	
	//getters and setters 
	public Map<String, String>getItems() {
		return items;
	}
	
	public void setItems(Map<String, String> items, boolean update) {
		this.items = items;
		if (update) 
			editMeal("items", this.items);
	}
	
	public void setItems(Document d, boolean update) {
		for (String s: d.keySet()) {
			items.put(s, d.getString(s));
		}
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
	
	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType type, boolean update) {
		this.mealType = type;
		if (update)
			editMeal("mealType", this.getMealType().toString()); 
	}
	
	
	//returns Meals collection
	public static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "meals" collection
	    MongoCollection<Document> collection = database.getCollection("meals");
	    
	    return collection; 
	}
	
	
	
	//TODO FIX FROM HERE ON 
	
	
	//adds current meal obj to database 
	public Document addMeal() {
	    // get "meals" collection
	    MongoCollection<Document> collection = getCollection(); 
        
	    //check if the user already has a meal of this type on this day 
	    Document myDoc = collection.find(and(eq("mealType", this.getMealType().toString()), 
	    		eq("date", this.getDate()), eq("userID", this.getUserID()))).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("User already has " + this.getMealType().toString() + " planned for " + this.getDate()); 
	    	return null; 		
	    }
	    
	    // create the meal     
		Document document = new Document(); 
		document.put("items", this.items); 
		document.put("instructions", this.instructions);
		document.put("date", this.date);
		document.put("userID", this.userID);
		document.put("mealType", this.getMealType()); 
		
		//insert the meal
		collection.insertOne(document);
	
	    // verify it has been added 
		myDoc = collection.find(and(eq("mealType", this.getMealType().toString()), eq("date", this.getDate()), 
				eq("userID", this.getUserID()))).first();
		System.out.println("Meal was added");
		System.out.println(myDoc.toJson());	
		
		return myDoc;
	}
	
	//Edit the current meal obj, do not use for editing meal items 
	public void editMeal(String field, Object value) {	
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify this meal is in the database  
	    Document myDoc = collection.find(and(eq("mealType", this.getMealType().toString()), 
	    		eq("date", this.getDate()), eq("userID", this.getUserID()))).first();

	    if (myDoc == null) {
	    	System.out.println("Meal does not exist. Cannot be edited.");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("_id", myDoc.get("_id")), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated 
	    myDoc = collection.find(eq("_id", myDoc.get("_id"))).first();
	    
	    if (myDoc == null) {
	    	System.out.println("An error occured while updating");
	    	return; 
	    }
	  
	    System.out.println("Meal was updated");
	    System.out.println(myDoc.toJson());
	}

	//deletes the current meal obj if it exists
	public void deleteMeal() {
		
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(and(eq("mealType", this.getMealType().toString()), eq("date", this.getDate()), 
				eq("userID", this.getUserID()))).first();
	    
	    if (myDoc == null) {
	    	System.out.println("Meal does not exist!");
	    	return;
	    }
	    
	    //delete this meal from the db 
	    DeleteResult deleteResult = collection.deleteOne(eq("_id", myDoc.get("_id")));
	   
	    if (deleteResult == null) {
	    	 System.out.println("Meal was not deleted");
	    	 return; 
	    }else {
	    	//update pantry and shopping list 
	    }
	    
	    
	    
	    System.out.println("Meal was deleted");
	    System.out.println(deleteResult.getDeletedCount());	    
	}
	
	// add an item to this meal obj and update the meal in the db if needed, 
	// also update the pantry or shopping list as needed 
	public void addItem(String item, Double amount) {
		
		//check if the item already exists in the meal 
		if (this.getItems().containsKey(item)) {
			System.out.println(item + " is already an ingredient in this meal."); 
			return;
		}
		
		//remove what you can from pantry 
		Double amountRemoved = removeFromPantry(item, amount); 
		
		if (amount - amountRemoved > 0) {
			addToShoppingList(item, amount - amountRemoved); 
		}
		
		// create new meal meta data for this item
		String meta = MealMetaData.create(amount, amountRemoved, amount - amountRemoved, getMealID(), item); 
	
		items.put(item, meta);
		editMeal("items", this.items);
		
		System.out.println(item + " was added to this meal.");
		
	}
	
	public void editItem(String item, Double amount, boolean update) {
		for (String key : items.keySet()) {
			if (key.equals(item)) {
				double ogAmount = items.get(item);
				items.put(key, amount);
				if (update)
					editMeal("items", this.items);
				System.out.println(item + " was edited.");
				
				//access user's pantry
				MongoCollection<Document> pantries = Pantry.getCollection();
				Document pantryDoc = pantries.find(eq("userID",this.userID)).first();
			    if (pantryDoc == null) {
			    	System.out.println("User doesn't have a pantry");
			    	return;
			    }
				Pantry userPantry = new Pantry(pantryDoc, false);
				
				//access user's shoppinglist
				MongoCollection<Document> sls = ShoppingList.getCollection();
				Document slDoc = sls.find(eq("userID",this.userID)).first();
				if (slDoc == null) {
					System.out.println("User does not have a shopping list");
					return;
				}
				ShoppingList userSL = new ShoppingList(slDoc, false);
				
				//check in pantry (update amount)
				for (String key2 : userPantry.getItems().keySet()) {
					if (key2.equals(item)) {
						if (amount > ogAmount) {
							userPantry.addFood(item, /*(amount - */ogAmount, true);
						}
						else if(amount < ogAmount){
							userPantry.removeFood(item, /*(ogAmount - */amount, true); 
						}
						else {
							System.out.println("Given amount same as original amount. No updates made.");
						}
					}
				}
				//check in shoppingList (update amount)
				for (String key2 : userSL.getItems().keySet()) {
					if (key2.equals(item)) {
						if (amount > ogAmount) {
							userSL.addFood(item, ogAmount, true);
						}
						else if(amount < ogAmount){
							userSL.removeFood(item, amount, true); 
						}
						else {
							System.out.println("Given amount same as original amount. No updates made.");
						}
					}
				}
				return;
			}
		}
		System.out.println(item + " is not an ingredient in this meal.");
		
	}
	
	public void deleteItem(String item, boolean update) {
		for (String key: items.keySet()) {
			if (key.equals(item)) {
				double ogAmount = items.get(key);
				items.remove(key);
				if (update) 
					editMeal("items", this.items);
				System.out.println(item + " was deleted from this meal."); 

				//access user's pantry
				MongoCollection<Document> pantries = Pantry.getCollection();
				Document pantryDoc = pantries.find(eq("userID",this.userID)).first();
			    if (pantryDoc == null) {
			    	System.out.println("User doesn't have a pantry");
			    	return;
			    }
				Pantry userPantry = new Pantry(pantryDoc, false);
				
				//access user's shoppinglist
				MongoCollection<Document> sls = ShoppingList.getCollection();
				Document slDoc = sls.find(eq("userID",this.userID)).first();
				if (slDoc == null) {
					System.out.println("User does not have a shopping list");
					return;
				}
				ShoppingList userSL = new ShoppingList(slDoc, false);
				
				//check in shopping list
				for (String key2 : userSL.getItems().keySet()) {
					if (key2.equals(item)) {
						userSL.removeFood(item, items.get(item), true); 
						return;
					}
				}
				//if the item isn't in the shoppinglist it means it was in the pantry so, add it back
				userPantry.addFood(item, ogAmount, true);
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
	
	//print a meal given a document 
	public static void printMeal(Document myDoc) {
	    
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
	
	//get the meal id from the database 
	private String getMealID() {
		 // get "meals" collection
	    MongoCollection<Document> collection = getCollection(); 
       
	    //grab the meal from the db 
	    Document myDoc = collection.find(and(eq("mealType", this.getMealType().toString()), 
	    		eq("date", this.getDate()), eq("userID", this.getUserID()))).first();
	    
	    return myDoc.get("_id").toString(); 
	}
	
	//removed item from the pantry 
	private Double removeFromPantry(String item, Double amount) {
		//access user's pantry
		MongoCollection<Document> pantries = Pantry.getCollection();
		Document pantryDoc = pantries.find(eq("userID", this.userID)).first();
	    
		// if the user does not have a pantry return back a 0 metaDataObj 
		if (pantryDoc == null) {
	    	System.out.println("User doesn't have a pantry");
	    	return 0.0;
	    }
		
		//Temporarily create a user pantry obj so we can manipulate it 
		Pantry userPantry = new Pantry(pantryDoc); 
		
		//check if the item is in the pantry 
		if (userPantry.getItems().containsKey(item)) {
			return userPantry.removeFood(item, amount);
		}else {
			return 0.0;
		}
	}
		
	//adds item to the shopping list 
	private void addToShoppingList(String item, Double amount) {
		//access user's shopping list
		MongoCollection<Document> sls = ShoppingList.getCollection();
		Document slDoc = sls.find(eq("userID",this.userID)).first();
		
		//if the user does not have a shopping list just return 
		if (slDoc == null) {
			System.out.println("User does not have a shopping list");
			return;
		}
		
		ShoppingList userSL = new ShoppingList(slDoc);	
		// add the food to the shopping list 
		userSL.addFood(item, amount);
	}
	
	//Only use for this driver test function!!
	public static void deleteAllMeals() {
		MongoCollection<Document> collection = getCollection(); 
		collection.drop(); 
	}
	
	
	public static void main(String args[]) {
		//MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		
		System.out.println("Meal test Driver"); 
		
		//going to clear out the meals, pantries, and shoppinglists collections so we get a 
		//clean run each time 
		deleteAllMeals(); 	
		Pantry.deleteAllPantries();
		ShoppingList.deleteAllShoppingLists();
		
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
		m.setUserID("123456", false);
		
		System.out.println("check for editing a meal in the db that doesn't exist");
		m.setName("Temp name", true); 
		
		System.out.println("add meal to db");
		m.addMeal();
		
		System.out.println("test for repeat add error");
		m.addMeal();
		
		System.out.println("update the meal's name");
		m.setName("Chocolate Peanut Butter Jelly Overnight Oats", true);
		
		System.out.println("creating a pantry and shopping list for user");
		Pantry p = new Pantry("123456", true);
		p.addFood("grapes", 2.0, true); 
		p.addFood("banana", 1.0, true);
		p.addFood("milk", 2.0, true); 
		
		ShoppingList s = new ShoppingList("123456", true);
		s.addFood("chicken", 1.0, true);
		s.addFood("green beans", 3.0, true);
		s.addFood("blueberries", 2.0, true);
		
		System.out.println("test adding an item & removing it from pantry");
		m.addItem("grapes", 2.0, true); 
		System.out.println("test adding an item & adding it to shopping list"); 
		m.addItem("honey", 1.0, true);
		System.out.println("test adding an item & its already in shopping list");
		m.addItem("blueberries", 1.0, true); 
		System.out.println("test adding an item that exists already");
		m.addItem("salt", 1.0, true); 
		
		System.out.println("test editing an item & in shopping list");
		m.editItem("blueberries", 4.0, true); 
		//test editing an item and pulling it from pantry (might have to add it to beginning pantry)
		System.out.println("test editing an item that is not there"); 
		m.editItem("pineapple", 1.0 , true);
		
		System.out.println("test deleting an item and adding it back to pantry");
		m.deleteItem("grapes", true);
		//test deleting and deleting it from shopping list
		System.out.println("test deleting an item that is not there"); 
		m.deleteItem("grapes", true); 
		
		p.printPantry();
		s.printShoppingList();
		
		///////////////////////////////////
		
		
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
		m2.setDate("03/04/1956", true); 
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
