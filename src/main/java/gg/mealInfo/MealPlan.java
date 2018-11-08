package gg.mealInfo;
import gg.physObjs.*;
import gg.userInfo.*;
import gg.mealInfo.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
		MongoCollection<Document> pantries = Pantry.getCollection();
		FindIterable<Document> pantry = pantries.find(eq("_id", userID));
		Map<String, Double> tempPantry = new HashMap<String, Double>();
		for (Document d : pantry)
		{
			// TODO populate tempPantry
		}
		MongoCollection<Document> recipes = Recipe.getCollection();
		MongoCollection<Document> users = Person.getCollection();
		Document userObj = users.find(eq("_id", userID)).first();
		
		MongoCollection<Document> restrictRecipes = (MongoCollection<Document>) recipes.find(eq("restrictions", userObj.get("restrictions")));
		// TODO check that restrictRecipes has something in the temppantry and get the list of recipes to be used
		// TODO for loop that creates the meals for the meal plan
			// TODO create meal with recipe from list
			// TODO remove items from temppantry 
			// TODO add items to shopping list
		
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
	    MongoCollection<Document> collection = this.getCollection(); 
        
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
}
