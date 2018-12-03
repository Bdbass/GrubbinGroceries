package gg.mealInfo;

import gg.APIs.TempThread;
import gg.userInfo.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
	
	@SuppressWarnings("unchecked")
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
	
	//adds the current recipe to the database if it does not already exist 
	public void addRecipe() {	
		TempThread t = getCollection();	     
        
	    //check if the recipe already exists
	    Document myDoc = t.collection.find(eq("name", this.name)).first();
	    
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
		t.collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = t.collection.find(eq("name", this.name)).first();
		System.out.println("Recipe was added");
		System.out.println(myDoc.toJson());
		
		//close the thread 
		t.client.close();
	}
	
	//edits the current recipe if it exists in the db 
	public void editRecipe(String field, Object value) {
		TempThread t = getCollection();	         
	    //verify it is in the db 
	    Document myDoc = t.collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Recipe has not been added, be sure to add it first");
	    	return; 
	    }   
	    //update the document
	    t.collection.updateOne(eq("name", this.name), new Document("$set", new Document(field, value)));
	    //verify it has been updated
	    if (field == "name") {
	    	myDoc = t.collection.find(eq("name", value.toString())).first();
	    }else {
	    	myDoc = t.collection.find(eq("name", this.name)).first();
	    }
	    System.out.println("Recipe was updated");
	    System.out.println(myDoc.toJson());    
	    //close thread
	    t.client.close();
	}
	
	//deletes the recipe if it exists
	public void deleteRecipe() {
		// get the collection 
		TempThread t = getCollection();	    
	    //verify it is in the db 
	    Document myDoc = t.collection.find(eq("name", this.name)).first();
	    if (myDoc == null) {
	    	System.out.println("Recipe does not exist!");
	    	return;
	    }	    
	    DeleteResult deleteResult = t.collection.deleteOne(eq("name", this.name));
	    System.out.println("Recipe was deleted");
	    System.out.println(deleteResult.getDeletedCount());
	    
	    //close the thread 
	    t.client.close();
	}
	 
	public void printRecipe() {
		// get the collection 
		TempThread t = getCollection();	     
	    Document myDoc = t.collection.find(eq("name", this.name)).first();
	    
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
	    @SuppressWarnings("unchecked")
		ArrayList<String> a =  (ArrayList<String>) myDoc.get("restrictions"); 
	    for (String j: a) {
	    	System.out.println(j.toString());
	    }  
	    System.out.println(); 
	    //close the thread 
	    t.client.close();
	} 
	// STATIC FUNCTIONS 
	public static TempThread getCollection() {
		//create the client 
		MongoClient mongoClient = MongoClients.create("mongodb://guest:superSecretPassword@18.188.67.103/GrubbinGroceries"); 
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	    //find the users pantry and create a local copy of their pantry 
	    MongoCollection<Document> collection = database.getCollection("recipes");
	    return new TempThread(collection, mongoClient); 
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
		    @SuppressWarnings("unchecked")
			ArrayList<String> a =  (ArrayList<String>) document.get("restrictions"); 
		    for (String j: a) {
		    	System.out.println(j.toString());		    
		    }  
		    System.out.println(); 
		}
	};
	public static Document returnRecipe(String name) {
		TempThread t = getCollection(); 
		Document d = t.collection.find(eq("name", name)).first();
	    //close the thread 
	    t.client.close();    
	    return d; 
	}
	public static String parseTextFile(String input) {
		return "fail"; 
	}
	
	public static ArrayList<String> getAllRecipes(){
		TempThread t = getCollection();
		MongoCursor<Document> cursor = t.collection.find().iterator();
		ArrayList<String> temp = new ArrayList<>(); 
		try {
		    while (cursor.hasNext()) {
		       temp.add(cursor.next().getString("name")); 
		    }
		} finally {
		    cursor.close(); 
		}
		t.client.close();
		return temp;
	}
	
	public static void printAllRecipes() {
		TempThread t = getCollection();
		t.collection.find().forEach(printBlock);   
	    //close the thread 
	    t.client.close();
	}
	
	//Only use for this driver test function!!
	public static void deleteAllRecipes() {
		TempThread t = getCollection();
		t.collection.drop(); 
		t.client.close();
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
		TempThread t = getCollection();		     		    
	    Document myDoc = t.collection.find(eq("name", r.getName())).first();
	    
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
		
		
		//add a third recipe 
		Recipe r3 = new Recipe();
		r3.setName("Grilled Cheese", false);
		r3.setInstructions("1. Put slice of cheese between two pieces of bread to form sandwhich.\n" +
							"2. Place the sandwhich on a frypan on the stove.\n" +
							"3. Turn burner to medium heat.\n" +
							"4. Wait 3 minutes, then flip the sandwhich to the other side.\n" +
							"5. After another 3 minutes, remove from stove and serve warm.\n", false);
		HashMap<String, Double> foodItems3 = new HashMap<String, Double>();
		foodItems3.put("bread", 2.0);
		foodItems3.put("cheese", 1.0);
		r3.setItems(foodItems3, false);
		r3.setMealType(MealType.LUNCH, false);
		r3.addRecipe();
		
		//add a fourth recipe 
		Recipe r4 = new Recipe();
		r4.setName("Cheesy Cornmeal Muffins with Thyme and Scallion", false);
		r4.setInstructions("1. Preheat the oven to 375°F. Line a muffin tin with paper liners. Set aside.\n" + 
				"2. In a small bowl combine 2 tbsp of ground flax with 6 tbsp of water. Stir and set aside to thicken up.\n" + 
				"3. In a large bowl, mix together the brown rice flour, corn grits, almond meal, \n" + 
				"baking powder, baking soda, fresh thyme and salt.\n" + 
				"4. In a small bowl, whisk together the buttermilk, melted butter, honey and flax eggs.\n" + 
				"5. Add the wet ingredients to the dry and mix until it has almost come together. Reserving\n" + 
				"a little scallion and cheese for the top, fold the cheese, corn, and scallions into the batter.\n" + 
				"6. Spoon the batter into the muffin tins and sprinkle with the reserved scallion and cheese and a \n" + 
				"few cracks of fresh black pepper.\n" + 
				"7. Bake for 18 – 20 minutes, until golden. Let cool for a few minutes, then transfer to a cooling rack. \n" + 
				" Store extra muffins in the refrigerator for 3 days or freeze them for longer storage.", false);
		
		HashMap<String, Double> foodItems4 = new HashMap<String, Double>();
		foodItems4.put("brown rice flour", 0.75);
		foodItems4.put("Nature’s Path Organic Yellow Corn Grits", 0.75);
		foodItems4.put("almond meal", 0.25);
		foodItems4.put("baking powder", 1.0);
		foodItems4.put("baking soda", 1.0);
		foodItems4.put("fresh thyme, finely chopped", 1.0);
		foodItems4.put("tsp salt", 0.50);
		foodItems4.put("ground flax", 2.0);
		foodItems4.put("buttermilk", 1.0);
		foodItems4.put("unsalted butter, melted", 0.25);
		foodItems4.put("honey", 1.0);
		foodItems4.put("grated aged cheddar cheese", 1.25);
		foodItems4.put("fresh or frozen corn", 0.75);
		foodItems4.put("scallions", 2.0);
		
		r4.setItems(foodItems4, false);
		r4.setMealType(MealType.BREAKFAST, false);
		r4.setRestrictions(restrictions2, false);
		r4.addRecipe();
  		
	    //print all recipes
		System.out.println(); 
		System.out.println("Print all recipes"); 
		printAllRecipes(); 
  		
		System.out.println();
		System.out.println("Print all recipes with avacado");
  		//search for a recipe ingredient , avocado 
		t.collection.find(gt("items.avacado", 0.0)).forEach(printBlock);
		
		t.client.close();
	}
}
