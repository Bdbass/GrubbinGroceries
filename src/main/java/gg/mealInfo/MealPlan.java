package gg.mealInfo;
import gg.physObjs.*;

import gg.userInfo.*;
import gg.mealInfo.*;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;

public class MealPlan {
	private String userID;
	private ArrayList<String> mealIDs; 
	private Date startDate; 
	private int numDays;
	
	public MealPlan()
	{
		userID = "unknown";
		mealIDs = new ArrayList<String>();
		startDate = new Date();
		numDays = 0;
	}
	
	public MealPlan(String user, Date start, int num)
	{
		userID = user;
		mealIDs = new ArrayList<String>();
		startDate = start;
		numDays = num;
		this.createMealPlan();
		
	}
	
	public void createMealPlan()
	{
		MongoCollection<Document> pantries = Pantry.getCollection();
		Document pantryD = pantries.find(eq("_id", userID)).first();
		Pantry pantry = new Pantry(pantryD);
		MongoCollection<Document> recipes = Recipe.getCollection();
		MongoCollection<Document> users = Person.getCollection();
		//formatting date
		Document userObj = users.find(eq("_id", userID)).first();
		String DATE_FORMAT = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		FindIterable<Document> restrictRecipes = recipes.find(eq("restrictions", userObj.get("restrictions")));
		ArrayList<Recipe> rRecipes = new ArrayList<Recipe>();
		ArrayList<Recipe> goodRecipes = new ArrayList<Recipe>();
		for (Document r : restrictRecipes)
		{
			Recipe r1 = new Recipe(r);
			rRecipes.add(r1);
		}
		for (Recipe r : rRecipes)
		{
			Map<String, Double> items = r.getItems();
			for (String key : items.keySet())
			{
				String first = pantry.getItems().keySet().stream().findFirst().get();
				if (key == first)
				{
					goodRecipes.add(r);
				}
			}
		}
		int day = 0;
		MongoCollection<Document> meals = Meal.getCollection();
		while (day < numDays)
		{
			//creating the meal
			Calendar cal = Calendar.getInstance();
	        cal.setTime(this.startDate);
	        cal.add(Calendar.DATE, day);
			Meal newMeal = new Meal(goodRecipes.get(day), sdf.format(cal.getTime()), this.userID, false);
			Document tempMeal = newMeal.addMeal();
			this.mealIDs.add(tempMeal.getString("_id"));
			
			// adding items to shopping list and remove from pantry	
			MongoCollection<Document> shoppingLists = ShoppingList.getCollection();
			Document shoppingListD = shoppingLists.find(eq("userID", this.userID)).first();
			ShoppingList shoppingList = new ShoppingList(shoppingListD);
			Map<String, Double> items = goodRecipes.get(day).getItems();
			for (String rKey : items.keySet())
			{
				Boolean inPantry = false;
				for (String pKey : pantry.getItems().keySet())
				{
					if (pKey == rKey)
					{
						inPantry = true;
						if(pantry.getItems().get(pKey) <= items.get(rKey)) //pantry amount <= recipe amount
						{
							double amount =  items.get(rKey) - pantry.getItems().get(pKey); 
							shoppingList.addFood(rKey, amount, true);
						}
						pantry.removeFood(pKey, pantry.getItems().get(pKey), true);
					}
				}
				if (!inPantry)
				{
					// add to shopping list
					shoppingList.addFood(rKey, items.get(rKey), true);
				}
			}
				
			day++;
		}
		
		this.addMealPlan();
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID, boolean update) {
		this.userID = userID;
		
		if (update)
		{
			this.editMealPlan("userID", this.userID);
		}
	}

	public ArrayList<String> getMealIDs() {
		return mealIDs;
	}

	public void setMealIDs(ArrayList<String> mealIDs, boolean update) {
		this.mealIDs = mealIDs;
		
		if (update)
		{
			this.editMealPlan("mealIDs", this.mealIDs);
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate, Boolean update) {
		this.startDate = startDate;
		
		if (update)
		{
			this.editMealPlan("startDate", this.startDate);
		}
	}

	public int getNumDays() {
		return numDays;
	}

	public void setNumDays(int numDays, boolean update) {
		this.numDays = numDays;
		
		if (update)
		{
			this.editMealPlan("numDays", this.numDays);
		}
	}

	//returns your mongo collection so you can use it 
	public static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = database.getCollection("mealPlans");
	    
	    return collection; 
	}
	
	public void addMealPlan() {	
		
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = getCollection(); 
        
	    // create the recipe     
		Document document = new Document(); 
		document.put("userID", this.userID); 
		document.put("mealIDs", this.mealIDs); 
		document.put("startDate", this.startDate); 
		document.put("numDays", this.numDays);  
		
		//insert the recipe
		collection.insertOne(document); 
	
	    // verify it has been added 
		document = collection.find(eq("startDate", this.startDate)).first();
		System.out.println("Meal Plan was added");
		System.out.println(document.toJson());			
	}
	
	public void editMealPlan(String field, Object value) {
		
		// get the collection 
	    MongoCollection<Document> collection = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    if (myDoc == null) {
	    	System.out.println("Meal Plan has not been added, be sure to add it first");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("userID", this.userID), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    myDoc = collection.find(eq("userID", this.userID)).first();
	    System.out.println("Meal Plan was updated");
	    System.out.println(myDoc.toJson());
	}
	
	public void printMealPlan()
	{
		MongoCollection<Document> meals = Meal.getCollection();
		for (String m : mealIDs)
		{
			Document meal = meals.find(eq("_id", m)).first();
			Meal.PrintMeal(meal);
		}
		
	}
	
	public void printMealsOnDay(Date d)
	{
		MongoCollection<Document> meals = Meal.getCollection();
		// formatting date object
		String DATE_FORMAT = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String date = sdf.format(d);
		
		for (String m : mealIDs)
		{
			Document meal = meals.find(and(eq("_id", m), eq("date", date))).first();
			Meal.PrintMeal(meal);
		}
		
	}
	
	public static void main(String args[])
	{
		
	}
}
