package gg.mealInfo;
import static com.mongodb.client.model.Filters.eq;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ShoppingList {
	
	private String userID;
	private Map<String,Double> items;
	
	// default constructor 
	public ShoppingList() {
		this.userID = "unknown";
		this.items = new HashMap<String,Double>();
	}
	
	// Constructor for a user 
	public ShoppingList(String userID) {
		this.userID = userID;
		this.items = new HashMap<String,Double>(); 
		addShoppingList();
	}
	
	//constructor based off a document from the database 
	public ShoppingList(Document d) {
		Document d1 = (Document) d.get("items");
		items = new HashMap<String, Double>(); 
		for (String i: d1.keySet()) {
			items.put(i, d1.getDouble(i)); 
		}
		this.userID = d.getString("userID"); 
	}
	
	// setters and getters 
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID, boolean update) {
		this.userID = userID;
		if (update)
			editShoppingList("userID", this.userID);
	}
	
	public Map<String, Double> getItems() {
		return items;
	}
	
	public void setItems(Map<String, Double> items, boolean update) {
		this.items = items;
		if (update) 
			editShoppingList("items", this.items);
	}
	
	public void setItems(Document d, boolean update) {
		for (String s: d.keySet()) {
			items.put(s, d.getDouble(s));
		}
		if (update)
			editShoppingList("items", this.items);
	}
	
//	//returns the shopping list collection 
//	public static MongoCollection<Document> getCollection(){
//		// connect to the local database server  
//		MongoClient mongoClient = MongoClients.create();
//	    	
//	    // get handle to database
//	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
//	
//	    // get a handle to the "shoppingLists" collection
//	    MongoCollection<Document> collection = database.getCollection("shoppingLists");
//	    
//	    return collection; 
//	}
	
	//adds the shopping list to the database 
	public void addShoppingList() {
	    // get a handle to the "shoppingLists" collection
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "shoppingLists" collection
	    MongoCollection<Document> collection = database.getCollection("shoppingLists");
			     
	    //check if the ShoppingList already exists by checking for userID
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("ShoppingList is already in the database!"); 
	    	return; 		
	    }
	    
	    // create the ShoppingList     
		Document document = new Document(); 
		document.put("userID", userID);
		document.put("items", this.items); 
		
		//insert the ShoppingList
		collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = collection.find(eq("userID", this.userID)).first();
		System.out.println("ShoppingList was added");
		System.out.println(myDoc.toJson());		
		
		//close thread
		mongoClient.close();
	}
	
	//edit the shopping list 
	public void editShoppingList(String field, Object value) {
		// get the collection 
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "shoppingLists" collection
	    MongoCollection<Document> collection = database.getCollection("shoppingLists");
			    
	    //verify it is in the db 
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    if (myDoc == null) {
	    	System.out.println("ShoppingList does not exist. Cannot be edited.");
	    	return; 
	    }
	    
	    //update the document
	    collection.updateOne(eq("userID", this.userID), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    if (field == "userID") {
	    	myDoc = collection.find(eq("userID", value.toString())).first();
	    }
	    else {
	    	myDoc = collection.find(eq("userID", this.userID)).first();
	    }
	    
	    System.out.println("ShoppingList was updated");
	    System.out.println(myDoc.toJson());	
	    
	    //close thread
	    mongoClient.close();
	}
	
	//remove food from the shopping list 
	public Double removeFood(String item, Double amount) {
		
		// look for food in shopping list 
		if (items.containsKey(item)) {
			
			//check the amount of item in shopping list, if we still have some left, just update it 
			if (items.get(item) > amount) {
				
				System.out.println("Previous value for " + item + ": " + items.get(item)); 
				items.put(item, (items.get(item) - amount)); 
				editShoppingList("items", this.items);
				System.out.println(item + " updated on list. New amount: " + items.get(item)); 
				
				return amount;
			}
			else {
				Double temp = amount - items.get(item); 
				items.remove(item);
				editShoppingList("items", this.items);
				System.out.println(item + " was removed from shopping list."); 
				
				return temp; 
			}
		}
		// not in the shopping list 	
		System.out.println(item + " is not on the shopping list."); 
		return 0.0; 
	}
	
	//add food to the shopping list 
	public void addFood(String item, Double amount) {
		
		// look for food in shopping list, and update its amount if its already in there 
		if (items.containsKey(item)) {
			System.out.println("Previous value for " + item + ": " + items.get(item)); //debugging might not need
			items.put(item, (items.get(item) + amount));
			System.out.println(item + " updated on list. New amount: " + items.get(item));
			editShoppingList("items", this.items);
			return;
		}
		
		//else add it to the shopping list 
		items.put(item, amount);
		editShoppingList("items", this.items);
		System.out.println(item + " added to shopping list.");
	}
	
	//print shopping list only if it is in the database 
	public void printShoppingList() {
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "shoppingLists" collection
	    MongoCollection<Document> collection = database.getCollection("shoppingLists");
			     
	    Document myDoc = collection.find(eq("userID", this.userID)).first();
	    
	    if (myDoc == null) {
	    	System.out.println("Shopping list does not exist in the database"); 
	    	return; 
	    }
	    
	    System.out.println("User: " + myDoc.get("userID"));
	    Document d = (Document) myDoc.get("items"); 
	    System.out.println("Shopping List:");
	    for (String i: d.keySet()) {
	    	System.out.println(i +": "+ d.get(i));
	    }		
	    
	    //close thread
	    mongoClient.close();
	}

	//ONLY USE FOR DRIVER
	public static void deleteAllShoppingLists() {
		// connect to the local database server  
		MongoClient mongoClient = MongoClients.create();
	    	
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	
	    // get a handle to the "shoppingLists" collection
	    MongoCollection<Document> collection = database.getCollection("shoppingLists");
			    
		collection.drop(); 
		
		//close thread
		mongoClient.close();
	}
	
	public static void main(String args[]) {
		//MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		
		System.out.println("ShoppingList test Driver"); 
		
		//going to clear out the meals collection so we get a 
		//clean run each time 
		deleteAllShoppingLists(); 	
		
		//test creating a shopping list with userID
		System.out.println("Creating a new shoppinglist");
		ShoppingList s = new ShoppingList("12345");
		s.addFood("banana", 2.0);
		s.addFood("chicken", 1.0);
		s.addShoppingList();
		
		//test add new item(s)
		s.addFood("pineapple", 3.0);
		//test add existing item
		s.addFood("chicken", 1.0);
		
		//test remove existing item partially
		s.removeFood("chicken", 1.0);
		//test remove existing item fully
		s.removeFood("banana", 2.0);
		//test remove non-existing item
		s.removeFood("cucumber", 1.0);
		
		//print it
		s.printShoppingList();
	}
}
