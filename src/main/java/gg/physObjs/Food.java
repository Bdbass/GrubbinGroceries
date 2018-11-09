package gg.physObjs;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Food {
	
	public static void printFood(String name) {
		
		//make sure that the food exists 
	    Document myDoc = getFood(name); 
	    
	    if (myDoc == null) {
	    	System.out.println("Food is not in the database!"); 
	    	return; 		
	    }
		
	    System.out.println(" name: " + myDoc.getString("name") + 
	    		"\n grams : " + myDoc.get("grams") + 
	    		"\n calories : " + myDoc.get("calories") + 
	    		"\n fat : " + myDoc.get("fat") + 
	    		"\n potassium : " + myDoc.get("potassium") + 
	    		"\n protein : " + myDoc.get("protein") + 
	    		"\n sugar : " + myDoc.get("sugar") + 
	    		"\n potassium : " + myDoc.get("potassium") + 
	    		"\n fiber : " + myDoc.get("fiber") + 
	    		"\n cholesterol : " + myDoc.get("cholesterol") +
	    		"\n carbs : " + myDoc.get("carbs") +
	    		"\n vitA : " + myDoc.get("vitA") +
	    		"\n vitD : " + myDoc.get("vitD") +
	    		"\n vitK : " + myDoc.get("vitK") ); 

	}
	
	//returns a food document of a food name
	public static Document getFood(String name) {
		MongoCollection<Document> c = getCollection();
		Document d = c.find(eq("name", name)).first(); 
		return d; 
	}
	
	//returns calories of a food
	public static double getCalories(String name) {
		Document d = getFood(name);
		if (d == null) {
			System.out.println("Food is not in the database");
			return -1; 
		}
		return d.getDouble("calories"); 
	}
	
	//returns your foods collection so you can use it 
	public static MongoCollection<Document> getCollection(){
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "recipes" collection
	    MongoCollection<Document> collection = database.getCollection("foods");
	    
	    return collection; 
	}
	
	static 
	Block<Document> printBlock = new Block<Document>() {
		public void apply(final Document document) {
			System.out.println(" name: " + document.getString("name") + 
		    		"\n grams : " + document.get("grams") + 
		    		"\n calories : " + document.get("calories") + 
		    		"\n fat : " + document.get("fat") + 
		    		"\n potassium : " + document.get("potassium") + 
		    		"\n protein : " + document.get("protein") + 
		    		"\n sugar : " + document.get("sugar") + 
		    		"\n potassium : " + document.get("potassium") + 
		    		"\n fiber : " + document.get("fiber") + 
		    		"\n cholesterol : " + document.get("cholesterol") +
		    		"\n carbs : " + document.get("carbs") +
		    		"\n vitA : " + document.get("vitA") +
		    		"\n vitD : " + document.get("vitD") +
		    		"\n vitK : " + document.get("vitK") ); 
		    System.out.println(); 
		}
	};
	
	
	public static void printAllFood() {
		MongoCollection<Document> collection = getCollection(); 		    
	    collection.find().forEach(printBlock);;
	}
	public static void deleteAllFood() {
		//Only use for this driver test function!!
		MongoCollection<Document> collection = getCollection(); 
		collection.drop(); 
	}
	
	public static void main(String args[]) {
		//deleteAllFood(); 
		//Directions
		//Follow read me and copy and paste this into the search bar
		// 'banana,avocado,apple,bacon,oats,milk,salt,pepper,yogurt,tortilla chips,cheese'
		
		System.out.println("Print all foods"); 
		printAllFood(); 
		
		System.out.println("Printing food by name");
		printFood("bacon"); 
		printFood("banana"); 
		printFood("avocado"); 
		
		System.out.println("Get caloris of avacado");
		System.out.println(getCalories("avocado")); 
		
		System.out.println("Get calories of apple"); 
		System.out.println(getCalories("apple")); 
	}
}
