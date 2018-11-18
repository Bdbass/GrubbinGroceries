package gg.mealInfo;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
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
	
	//Edit the current meal object
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

	
	
	//deletes the current meal obj if it exists and updates the pantry and shopping list
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
	    	this.returnFood(myDoc);
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
	
	
	public void editItem(String item, Double amount) {
		//check if item exists 
		if (this.getItems().containsKey(item)) {
			//grab the items meta data 
			 MongoCollection<Document> collection = MealMetaData.getCollection();
			 Document data = collection.find(eq("_id", new ObjectId(this.getItems().get(item)))).first();
			 
			 //verify it exists
			 if (data == null) {
				 System.out.println("Item does not exist in meta data, and cannot be edited");
			 }
			 
			 Double pantryAmount = 0.0; 
			 Double shoppingAmount = 0.0; 
			 
			 //we need more of this item in the meal  
			 if (amount > data.getDouble("totalAmount")) {
				 if (data.getDouble("shoppingAmount") > 0) {
					 //just update shopping amount 
					 this.addToShoppingList(item, amount - data.getDouble("totalAmount"));
					 
					 //update the amounts 
					 pantryAmount = data.getDouble("pantryAmount"); 
					 shoppingAmount = data.getDouble("shoppingAmount")+ amount - data.getDouble("totalAmount"); 
				 } else {
					 //see if we have any in the pantry first
					 Double addedSoFar = this.removeFromPantry(item, amount - data.getDouble("totalAmount")); 
					 
					 //add the rest to the shopping list 
					 this.addToShoppingList(item, amount - data.getDouble("totalAmount") - addedSoFar); 
					 
					 //update the amounts 
					 pantryAmount = data.getDouble("pantryAmount") + addedSoFar;  
					 shoppingAmount = data.getDouble("shoppingAmount")+ amount - data.getDouble("totalAmount") - addedSoFar;
				 }
			 }else {// we need less of this item 
				 
				 //remove from shopping list first 
				 Double removedSoFar = this.removeFromShoppingList(item, data.getDouble("totalAmount") - amount);
				 //add back to pantry second
				 this.addToPantry(item, data.getDouble("totalAmount") - amount - removedSoFar);
				 
				 //update amounts 
				 shoppingAmount = data.getDouble("shoppingAmount") - removedSoFar;  
				 pantryAmount = data.getDouble("pantryAmount") - (data.getDouble("totalAmount") - amount - removedSoFar);
			
			 }
			 // update total amount 
			 collection.updateOne(
		                eq("_id", new ObjectId(data.get("_id").toString())), 
		                combine(set("totalAmount", amount), 
		                		set("shoppingAmount", shoppingAmount), 
		                		set("pantryAmount", pantryAmount))); 
			 
			 return; 
		}
		System.out.println(item + " is not an ingredient in this meal.");
	}
	
	public void deleteItem(String item) {
		//check if item exists 
		if (this.getItems().containsKey(item)) {
			//grab the items meta data 
			 MongoCollection<Document> collection = MealMetaData.getCollection();
			 Document data = collection.find(eq("_id", new ObjectId(this.getItems().get(item)))).first();
			 
			 //verify it exists
			 if (data == null) {
				 System.out.println("Item does not exist in meta data, and cannot be edited");
			 }
			 
			 //remove items Shopping amount from the shopping list 
			 this.removeFromShoppingList(item, data.getDouble("shoppingAmount")); 
			 //add items Pantry amount back to pantry 
			 this.addToPantry(item, data.getDouble("pantryAmount"));
			 //delete the meta data for item 
			 this.deleteMetaData(item);
			 //delete item from meal list 
			 this.items.remove(item); 
			 this.editMeal("items", this.items);
		
		}
		System.out.println(item + " is not an ingredient in this meal."); 
	}
	
	public void printMeal() {
		
		// get the meal collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //get the meal 
	    Document myMeal = collection.find(and(eq("mealType", this.getMealType().toString()), 
	    		eq("date", this.getDate()), eq("userID", this.getUserID()))).first();
	    
	    //get the meta data for the meal 
	    FindIterable<Document> items = MealMetaData.mealMetaData(myMeal); 
	    
	    
	    System.out.println(myMeal.get("date"));
	    System.out.println(myMeal.get("mealType"));
	    System.out.println("Ingredients");
	    for (Document d: items) {
	    	System.out.println(d.getString("name") +": "+ d.get("totalAmount"));
	    }
	    System.out.println("Instructions");
	    System.out.println(myMeal.get("instructions"));
	}
	
	//print a meal given a document 
	public static void printMeal(Document myMeal) {
	    
		//get the meta data for the meal 
	    FindIterable<Document> items = MealMetaData.mealMetaData(myMeal); 
	    
	    
	    System.out.println(myMeal.get("date"));
	    System.out.println(myMeal.get("mealType"));
	    System.out.println("Ingredients");
	    for (Document d: items) {
	    	System.out.println(d.getString("name") +": "+ d.get("totalAmount"));
	    }
	    System.out.println("Instructions");
	    System.out.println(myMeal.get("instructions"));
	}
	
	// PRIVATE HELPER FUNCTIONS 
	
	//deletes meta data for a given food item
	private void deleteMetaData(String item) {
		//grab metaData 
		 MongoCollection<Document> collection = MealMetaData.getCollection(); 
		 DeleteResult deleteResult = collection.deleteOne(and(eq("mealID", this.getMealID()), eq("name", item))); 
		 
		 if (deleteResult == null) {
			 System.out.println("This meta data does not exist, and could not be deleted");
		 }
		 
		 System.out.println("Meta Data deleted");
	}
	
	//returns food back to pantry, and updates shopping list 
	private void returnFood(Document meal) {
		 //grab metaData 
		 MongoCollection<Document> collection = MealMetaData.getCollection(); 
		 FindIterable<Document> metaData = collection.find(eq("mealID", meal.get("_id").toString()));
		 
		 //make sure meta data exists for this meal
		 if (metaData == null) {
			 System.out.println("Items were not found, and could not be readded to pantry/shopping list");
		 }
		 
		 //add items back to pantry 
		 addToPantry(metaData); 
		 
		 //remove items from shopping list 
		 removeFromShoppingList(metaData); 
		 
	}
	
	// adds a single food item back to pantry 
	private void addToPantry(String name, Double amount) {
		//grab the users pantry 
		 MongoCollection<Document> collection = Pantry.getCollection(); 
		 Document myDoc = collection.find(eq("userID", this.getUserID())).first(); 
		 
		 //make sure the pantry exists
		 if (myDoc == null) {
			 System.out.println("Pantry does not exist, could not add food back");
			 return; 
		 }
		 
		 //create a temporary pantry 
		 Pantry userPantry = new Pantry(myDoc);
		 
		 //add the food back to the pantry  
		 userPantry.addFood(name, amount, true);
	}
	
	//adds a collection of food documents back to pantry 
	private void addToPantry(FindIterable<Document> docs) {
		//grab the users pantry 
		 MongoCollection<Document> collection = Pantry.getCollection(); 
		 Document myDoc = collection.find(eq("userID", this.getUserID())).first(); 
		 
		 //make sure the pantry exists
		 if (myDoc == null) {
			 System.out.println("Pantry does not exist, could not add food back");
			 return; 
		 }
		 
		 //create a temporary pantry 
		 Pantry userPantry = new Pantry(myDoc);
		 
		 //add all meta data back to the pantry 
		 for (Document metaData: docs) {
			//add the food back to the pantry  
			userPantry.addFood(metaData.getString("name"), metaData.getDouble("pantryAmount"), true);
		 }	
	}
	
	//removes a single food item from the shopping list 
	private Double removeFromShoppingList(String name, Double amount) {
		 //grab the users shopping list  
		 MongoCollection<Document> collection = ShoppingList.getCollection(); 
		 Document myDoc = collection.find(eq("userID", this.getUserID())).first(); 
		 
		 //make sure the pantry exists
		 if (myDoc == null) {
			 System.out.println("Shopping list does not exist, could not remove food");
			 return 0.0; 
		 }
		 
		 //create temporary shopping list 
		 ShoppingList userShoppingList = new ShoppingList(myDoc); 
		 
		//remove food from shopping list 
		return userShoppingList.removeFood(name, amount);
	}
	
	
	//removes a collection of food docs from the shopping list 
	private void removeFromShoppingList(FindIterable<Document> docs) {
		 //grab the users shopping list  
		 MongoCollection<Document> collection = ShoppingList.getCollection(); 
		 Document myDoc = collection.find(eq("userID", this.getUserID())).first(); 
		 
		 //make sure the pantry exists
		 if (myDoc == null) {
			 System.out.println("Shopping list does not exist, could not remove food");
			 return; 
		 }
		 
		 //create temporary shopping list 
		 ShoppingList userShoppingList = new ShoppingList(myDoc); 
		 
		 //remove all meta data from shopping list 
		 for (Document metaData: docs) { 
			userShoppingList.removeFood(metaData.getString("name"), metaData.getDouble("shoppingAmount"));
		 }	
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

	
	// main driver 
	public static void main(String args[]) {
		// MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		// run the user driver, then the recipe driver, then this driver 
		
		System.out.println("Meal test Driver"); 
		
		//going to clear out the meals, pantries, and shopping lists collections so we get a 
		//clean run each time 
		deleteAllMeals(); 	
		Pantry.deleteAllPantries();
		ShoppingList.deleteAllShoppingLists();
		
		
		//user needs a pantry and shopping list before making a meal 
		System.out.println("creating a pantry and shopping list for user");
		Pantry p = new Pantry("bdbass@email.arizona.edu");
		p.addFood("grapes", 2.0, true); 
		p.addFood("banana", 1.0, true);
		p.addFood("milk", 2.0, true); 
		
		ShoppingList s = new ShoppingList("bdbass@email.arizona.edu");
		s.addFood("chicken", 1.0);
		s.addFood("green beans", 3.0);
		s.addFood("blueberries", 2.0);
		
		
		//test out the meal 
		System.out.println("test plain constructor and each setter");
		Meal m = new Meal();
		
		m.setInstructions("1. Place all ingredients, except raspberries and additional toppings in a medium sized bowl.\n" +
				 "2. Stir until well combined and then gently fold in 1/4 cup raspberries.\n" + 
				 "3. Store in a covered, airtight container, like a mason jar, in the fridge overnight.\n" + 
				 "4. In the morning, top with additional toppings, if desired, and enjoy!\n", true);
		
		m.addItem("oats", 0.5); 
		m.addItem("skim milk", 0.75); 
		m.addItem("greek yogurt vanilla", 1.0);
		m.addItem("penut butter", 2.0);
		m.addItem("cocoa powder", 1.0);
		m.addItem("salt", 1.0);
		m.addItem("banana", 1.0);
		m.addItem("raspberries", 10.0); 
		m.addItem("almonds", 10.0);
		m.setDate("12/25/2018", true);
		m.setUserID("bdbass@email.arizona.edu", true);
		
		System.out.println("test for repeat add error");
		m.addMeal();
		
		///////////////////////////////////////////////////////
		System.out.println("test adding an item & removing it from pantry");
		m.addItem("grapes", 2.0); 
		System.out.println("test adding an item & adding it to shopping list"); 
		m.addItem("honey", 1.0);
		System.out.println("test adding an item & its already in shopping list");
		m.addItem("blueberries", 1.0); 
		System.out.println("test adding an item that exists already");
		m.addItem("salt", 1.0); 
		
		System.out.println("test editing an item & in shopping list");
		m.editItem("blueberries", 4.0); 
		//test editing an item and pulling it from pantry (might have to add it to beginning pantry)
		System.out.println("test editing an item that is not there"); 
		m.editItem("pineapple", 1.0 );
		
		System.out.println("test deleting an item and adding it back to pantry");
		m.deleteItem("grapes");
		//test deleting and deleting it from shopping list
		System.out.println("test deleting an item that is not there"); 
		m.deleteItem("grapes"); 
		
		p.printPantry();
		s.printShoppingList();
		
		
		///////////////////////////////////
	
		System.out.println("test constructor given a recipe"); 
		Recipe r = new Recipe(Recipe.returnRecipe("Grilled Cheese")); 
		Meal m2 = new Meal(r, "bdbass@email.arizona.edu"); 
		m2.setDate("11/12/2018", true); 
		m2.printMeal();
  		
		System.out.println("test constructor given a recipe and a date");
		Meal m3 = new Meal(new Recipe(Recipe.returnRecipe("Breakfast Nachos")),
				"11/13/2018", "bdbass@email.arizona.edu");
		m3.printMeal();
	}
}
