package gg.mealInfo;
import static com.mongodb.client.model.Filters.eq;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.APIs.TempThread;

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
	
	//returns the shopping list collection 
	public static TempThread getCollection() {
		//create the client 
		MongoClient mongoClient = MongoClients.create("mongodb://guest:superSecretPassword@18.188.67.103/GrubbinGroceries"); 
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	    //find the users pantry and create a local copy of their pantry 
	    MongoCollection<Document> collection = database.getCollection("shoppingLists");
	    return new TempThread(collection, mongoClient); 
	} 
	
	//adds the shopping list to the database 
	public void addShoppingList() {
		TempThread t = getCollection();    
	    //check if the ShoppingList already exists by checking for userID
	    Document myDoc = t.collection.find(eq("userID", this.userID)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println("ShoppingList is already in the database!"); 
	    	return; 		
	    }
	    
	    // create the ShoppingList     
		Document document = new Document(); 
		document.put("userID", userID);
		document.put("items", this.items); 
		
		//insert the ShoppingList
		t.collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = t.collection.find(eq("userID", this.userID)).first();
		System.out.println("ShoppingList was added");
		System.out.println(myDoc.toJson());		
		
		//close thread
		t.client.close();
	}
	
	//edit the shopping list 
	public void editShoppingList(String field, Object value) {
		TempThread t = getCollection();	    
	    //verify it is in the db 
	    Document myDoc = t.collection.find(eq("userID", this.userID)).first();
	    if (myDoc == null) {
	    	System.out.println("ShoppingList does not exist. Cannot be edited.");
	    	return; 
	    }
	    
	    //update the document
	    t.collection.updateOne(eq("userID", this.userID), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
	    if (field == "userID") {
	    	myDoc = t.collection.find(eq("userID", value.toString())).first();
	    }
	    else {
	    	myDoc = t.collection.find(eq("userID", this.userID)).first();
	    }
	    
	    System.out.println("ShoppingList was updated");
	    System.out.println(myDoc.toJson());	
	    
	    //close thread
	    t.client.close();
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
	public String printShoppingList() {
		TempThread t = getCollection();     
	    Document myDoc = t.collection.find(eq("userID", this.userID)).first();
	    String output = ""; 
	    
	    if (myDoc == null) {
	    	return("Shopping list does not exist in the database"); 
	    }
	    
	    output += "User: " + myDoc.get("userID") + "\n";
	    Document d = (Document) myDoc.get("items"); 
	    output += "Shopping List:\n"; 
	    for (String i: d.keySet()) {
	    	output += i +": "+ d.get(i) + "\n";
	    }		
	    
	    //close thread
	    t.client.close();
	    return output; 
	}
	
	// 
	public static ShoppingList findShoppingList(String userID) {
		TempThread t = getCollection();
		Document myDoc = t.collection.find(eq("userID", userID)).first();
		if (myDoc == null) {
			t.client.close();
			return null; 
		}
		return new ShoppingList(myDoc); 
	}

	//ONLY USE FOR DRIVER
	public static void deleteAllShoppingLists() {
		TempThread t = getCollection();			    
		t.collection.drop(); 
		//close thread
		t.client.close();
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
		System.out.println(s.printShoppingList());
	}
}
