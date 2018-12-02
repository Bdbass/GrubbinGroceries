package gg.mealInfo;
import gg.APIs.TempThread;
import gg.physObjs.*;
import gg.userInfo.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;


public class MealPlan {
	
	private String userID;
	private ArrayList<String> mealIDs; 
	private Date startDate; 
	private Date endDate;
	private String mealType; 
	
	//default constructor 
	public MealPlan()
	{
		userID = "unknown";
		mealIDs = new ArrayList<String>();
		startDate = new Date();
		endDate = new Date();
		this.mealType = "OTHER"; 
	}
	
	//overloaded constructor 
	public MealPlan(String user, Date start, Date end, String mealType)
	{
		userID = user;
		mealIDs = new ArrayList<String>();
		startDate = start;
		endDate = end; 
		this.mealType = mealType; 
		this.createMealPlan();
		
	}
	
	//constructor from document 
	@SuppressWarnings("unchecked")
	public MealPlan(Document d) {
		userID = d.getString("username");
		mealIDs = (ArrayList<String>) d.get("mealIDs"); 
		startDate = d.getDate("startDate"); 
		endDate = d.getDate("endDate"); 
		mealType = d.getString("mealType"); 
	}
	
	//setters and getters 
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID, boolean update) {
		this.userID = userID;
		
		if (update)
			this.editMealPlan("userID", this.userID);
	}

	public ArrayList<String> getMealIDs() {
		return mealIDs;
	}

	public void setMealIDs(ArrayList<String> mealIDs, boolean update) {
		this.mealIDs = mealIDs;
		
		if (update)
			this.editMealPlan("mealIDs", this.mealIDs);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate, Boolean update) {
		this.startDate = startDate;
		
		if (update)
			this.editMealPlan("startDate", this.startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setNumDays(Date endDate, boolean update) {
		this.endDate = endDate;
		
		if (update)
			this.editMealPlan("endDate", this.endDate);
	}
	
	//create meal plan 
	public boolean createMealPlan()
	{
		TempThread pantryThread = Pantry.getCollection();
	    Document pantry = pantryThread.collection.find(eq("userID", userID)).first();
		Pantry Userpantry = new Pantry(pantry);
		
		//grab the users restrictions 
		TempThread personThread = Person.getCollection();
	    Document person = personThread.collection.find(eq("username", userID)).first();
		@SuppressWarnings("unchecked")
		ArrayList<String> restrictions = (ArrayList<String>) person.get("restrictions"); 
		
		//grab the available recipes 
		TempThread recipeThread = Recipe.getCollection();
		Document findRecipes = new Document("mealType", this.mealType); 
			
		for (String s: restrictions) {
			findRecipes.append("restrictions", s); 
		}
		
		//find recipes that we have some ingredients for 
		FindIterable<Document> recipes = recipeThread.collection.find(findRecipes);
		
		HashMap<String, Recipe> myRecipes = new HashMap<String, Recipe>(); 		
		HashMap<String, Integer> mapRecipes = new HashMap<>();  
		String name; 
		for (Document r: recipes) {
			name = r.getString("name"); 
			myRecipes.put(name, new Recipe(r));
			Document items = (Document) r.get("items"); 
			for (String s: Userpantry.getItems().keySet()) {
				if (items.get(s) != null) {
					if (mapRecipes.containsKey(name)) {
						mapRecipes.put(name, mapRecipes.get(name) + 1); 
					}else {
						mapRecipes.put(name, 1);
					}
				}
			}
		}
		
		//if we don't have any recipes return false 
		if (myRecipes.size() < 1) {
			return false; 
		}
		//sort the recipes by which one we have the most ingredients for 
		ArrayList<SortableRecipe> sortedRecipes = new ArrayList<>(); 
		for (String s: mapRecipes.keySet()) {
			sortedRecipes.add(new SortableRecipe(s, mapRecipes.get(s))); 
		}		
		Collections.sort(sortedRecipes);
		
		//create meals for each day 
		int j = 0; 
		Date temp = this.startDate;
		DateTime Jodatime = new DateTime(temp); 
		
		int numDays = Days.daysBetween(new DateTime(this.getStartDate()).toLocalDate(), new DateTime(this.getEndDate()).toLocalDate()).getDays(); 
		
		for (int i = 0; i < numDays+1; i++) {
			Meal m = new Meal(myRecipes.get(sortedRecipes.get(j).name), this.userID, Jodatime.toString("MM/dd/yyyy")); 
			Jodatime = Jodatime.plusDays(1); 
			//temp.setDate(temp.getDate()+1);
			if (j < sortedRecipes.size()-1) {
				j++; 
			}
			this.mealIDs.add(m.returnMealID());
		}		
		this.addMealPlan();
		recipeThread.client.close();
		pantryThread.client.close();
		personThread.client.close();
		return true; 
	}
	
	public static TempThread getCollection() {
		//create the client 
		MongoClient mongoClient = MongoClients.create();
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	    //find the users pantry and create a local copy of their pantry 
	    MongoCollection<Document> collection = database.getCollection("mealPlans");
	    return new TempThread(collection, mongoClient); 
	}
	
	public void addMealPlan() {	
		
	    // get a handle to the "recipes" collection
	    TempThread thread = getCollection(); 
        
	    // create the recipe     
		Document document = new Document(); 
		document.put("userID", this.userID); 
		document.put("mealIDs", this.mealIDs); 
		document.put("startDate", this.startDate); 
		document.put("endDate", this.endDate);  
		document.put("mealType", this.mealType); 
		
		//insert the recipe
		thread.collection.insertOne(document); 
	
	    // verify it has been added 
		document = thread.collection.find(eq("startDate", this.startDate)).first();
		System.out.println("Meal Plan was added");
		System.out.println(document.toJson());
		thread.client.close();
	}
	
	public void editMealPlan(String field, Object value) {
		
		// get the collection 
	    TempThread thread = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = thread.collection.find(eq("userID", this.userID)).first();
	    if (myDoc == null) {
	    	System.out.println("Meal Plan has not been added, be sure to add it first");
	    	return; 
	    }
	    
	    //update the document
	    thread.collection.updateOne(eq("userID", this.userID), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    myDoc = thread.collection.find(eq("userID", this.userID)).first();
	    System.out.println("Meal Plan was updated");
	    System.out.println(myDoc.toJson());
	    thread.client.close();
	}
	public void editMealPlan(String _id, String field, Object value) {
			
			// get the collection 
		    TempThread thread = getCollection(); 
		    
		    //verify it is in the db 
		    Document myDoc = thread.collection.find(eq("_id", new ObjectId(_id))).first();
		    
		    if (myDoc == null) {
		    	System.out.println("Meal Plan has not been added, be sure to add it first");
		    	thread.client.close();
		    	return; 
		    }
		    
		    //update the document
		    thread.collection.updateOne(eq("_id", new ObjectId(_id)), new Document("$set", new Document(field, value)));
		    
		    //verify it has been updated
		    myDoc = thread.collection.find(eq("_id", new ObjectId(_id))).first();
		    System.out.println("Meal Plan was updated");
		    System.out.println(myDoc.toJson());
		    thread.client.close();
		}
	
	
	
	public static void deleteMeal(String userID, String mealType, String date) {
		
		//first delete the meal itself 
		TempThread t = Meal.getCollection();
		Document meal = t.collection.find(and(eq("userID",userID), eq("date", date), eq("mealType", mealType))).first();
		
		if (meal == null) {
			System.out.println("meal does not exist for " + userID + " on " + date);
		}
		
		Meal m = new Meal(meal); 
		m.deleteMeal();
		t.client.close();
		
		//then delete the mealId from the mealPlan 
		t = getCollection(); 
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		DateTime dt = formatter.parseDateTime(date);
				
		Document searchMealPlan = new Document("userID", userID).append("mealType", mealType); 
		searchMealPlan.append("startDate", new Document("$lte", dt.toDate())); 
		searchMealPlan.append("endDate", new Document("$gte", dt.toDate())); 
		
		Document mealPlan = t.collection.find(searchMealPlan).first(); 
		
		MealPlan mp = new MealPlan(mealPlan); 
		ArrayList<String> ids = mp.getMealIDs(); 
		ids.remove(ids.indexOf(meal.get("_id").toString())); 
		mp.editMealPlan(mealPlan.get("_id").toString(), "mealIDs", ids);
		
	}
	
	
	
	public void printMealPlan()
	{
		TempThread t = Meal.getCollection();
		for (String m : mealIDs)
		{
			Document meal = t.collection.find(eq("_id",new ObjectId(m))).first();
			Meal.printMeal(meal);
		}
		t.client.close();
	}
	
	public void printMealsOnDay(Date d)
	{
		TempThread thread  = Meal.getCollection();
		// formatting date object
		String DATE_FORMAT = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String date = sdf.format(d);
		
		for (String m : mealIDs)
		{
			Document meal = thread.collection.find(and(eq("_id", new ObjectId(m)), eq("date", date))).first();
			Meal.printMeal(meal);
		}
		thread.client.close();
	}
	
	public static void deleteMealPlans() {
		TempThread thread = getCollection(); 
		thread.collection.drop(); 
		thread.client.close();
	}
	
	
	public static void main(String args[])
	{	
		//first delete all pantries and meal plans and meals
		Pantry.deleteAllPantries();	
		deleteMealPlans(); 
		Meal.deleteAllMeals();
		
		Pantry userPantry = new Pantry("bdbass@email.arizona.edu");
		
		//lets add some pantry items 
		userPantry.addFood("brown rice flour", 0.5, true);
		userPantry.addFood("almond meal", 0.5, true);
		userPantry.addFood("honey", 0.5, true);
		
		userPantry.addFood("banana", 2.0, true);
		userPantry.addFood("penut butter", 1.0, true);
		
		userPantry.addFood("eggs", 4.0, true);
		
		
		//lets create a meal plan for breakfast for two days 
		MealPlan m = new MealPlan("bdbass@email.arizona.edu", new DateTime().toLocalDate().toDate(), new DateTime().toLocalDate().plusDays(1).toDate(), "BREAKFAST"); 
		
		m.printMealPlan();
		
		deleteMeal("bdbass@email.arizona.edu", "BREAKFAST", "12/01/2018");
		
	}
}
