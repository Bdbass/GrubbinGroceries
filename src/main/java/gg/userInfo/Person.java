package gg.userInfo;

import static com.mongodb.client.model.Filters.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import gg.APIs.TempThread;

public class Person {
	private String name; 
	private String username; 
	private String password; 
	private ArrayList<RestType> restrictions; 
	
	//constructors 
	public Person() {
		name = "unkown"; 
		username = "unknown"; 
		password = "password"; 
		restrictions = new ArrayList<RestType>(); 
	}
	
	public Person(String n, String email, ArrayList<RestType> r) {
		name = n; 
		username = email; 
		password = "password"; 
		restrictions = r;   
	}
	
	public Person(String n, String email, String password, ArrayList<RestType> restrictions, boolean add) {
		name = n; 
		username = email; 
		this.password = password; 
		this.restrictions = restrictions; 	
		
		if (add) {
			addPerson(); 
		}
	}
	
	public Person(Document d) {
		name = d.getString("name"); 
		username = d.getString("username"); 
	}
	
	//setters and getters 
	public String getName() {
		return name;
	}
	public void setName(String name, boolean update) {
		this.name = name;
		if (update)
			editPerson("name", this.name); 
	}	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password, boolean update) {
		this.password = password;
		if (update)
			editPerson("password", this.password); 
	}

	public ArrayList<RestType> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(ArrayList<RestType> restrictions, boolean update) {
		this.restrictions = restrictions;
		if (update) {
			ArrayList<String> temp = RestType.convert(restrictions); 
			editPerson("restrictions", temp); 
		}
	}
	
	// STATIC FUNCTIONS 
	public static TempThread getCollection() {
		//create the client 
		MongoClient mongoClient = MongoClients.create("mongodb://guest:superSecretPassword@18.188.67.103/GrubbinGroceries"); 
	    // get handle to database
	    MongoDatabase  database = mongoClient.getDatabase("GrubbinGroceries");
	    //find the users pantry and create a local copy of their pantry 
	    MongoCollection<Document> collection = database.getCollection("persons");
	    return new TempThread(collection, mongoClient); 
	}
		
	//adds the current person to the database if the username doesn't already exist
	public void addPerson() {	
		TempThread t = getCollection(); 
	    //check if the person already exists
	    Document myDoc = t.collection.find(eq("username", this.username)).first();
	    
	    if  (myDoc != null) {
	    	System.out.println(this.getUsername() + "is already in the database! "); 
	    	return; 		
	    }
	    
	    // create the person     
		Document document = new Document(); 
		document.put("name", this.name); 
		document.put("username", this.username); 
		document.put("restrictions", RestType.convert(this.restrictions)); 
		document.put("password", this.password); 
		
		//insert the person into db 
		t.collection.insertOne(document); 
	
	    // verify it has been added 
		myDoc = t.collection.find(eq("username", this.username)).first();
		System.out.println(this.getUsername() + " was added");
		System.out.println(myDoc.toJson());	
		
		//close thread 
		t.client.close();
	}
		
	//edits the current person if it exists in the db 
	public void editPerson(String field, Object value) {
		
		//don't allow users to edit username at this time
		if (field.equals("username")) {
			System.out.println("Cannot edit username sorry!");
			return; 
		}
		TempThread t = getCollection(); 
	    
	    //verify it is in the db 
	    Document myDoc = t.collection.find(eq("username", this.username)).first();
	    if (myDoc == null) {
	    	System.out.println("Person has not been added, be sure to add it first");
	    	return; 
	    }
	    
	    //update the document
	    t.collection.updateOne(eq("username", this.username), new Document("$set", new Document(field, value)));
	    
	    //verify it has been updated
    	myDoc = t.collection.find(eq("username", this.username)).first();
	    
	    System.out.println("Person was updated");
	    System.out.println(myDoc.toJson());
	    
	    //close thread 
	    t.client.close();
	}
		
	//deletes the person if they exist
	public void deletePerson() {
		TempThread t = getCollection(); 
	    //verify it is in the db 
	    Document myDoc = t.collection.find(eq("username", this.username)).first();
	    if (myDoc == null) {
	    	System.out.println("Person does not exist!");

	    	//close thread 
	    	t.client.close();
	    	return;
	    }
	    
	    DeleteResult deleteResult = t.collection.deleteOne(eq("username", this.username));
	    System.out.println(this.username + " was deleted");
	    System.out.println(deleteResult.getDeletedCount());
	    
	    //close thread 
	    t.client.close();
	}
	
	//prints the person information 
	public void printPerson() {
		TempThread t = getCollection();  
	    Document myDoc = t.collection.find(eq("username", this.username)).first();
	    System.out.println(myDoc.get("name")); 
	    System.out.println("username :" + myDoc.get("username"));
	    System.out.println("Restrictions"); 
	    @SuppressWarnings("unchecked")
		ArrayList<String> a =  (ArrayList<String>) myDoc.get("restrictions"); 
	    for (String j: a) {
	    	System.out.println(j);
	    }  
	    System.out.println(); 
	    
	    //close thread 
	    t.client.close();
	} 
	
	//helps print all persons
	static 
	Block<Document> printBlock = new Block<Document>() {
		public void apply(final Document document) {
			System.out.println(document.get("name")); 
		    System.out.println("username :" + document.get("username"));
		    System.out.println("Restrictions"); 
		    @SuppressWarnings("unchecked")
			ArrayList<String> a =  (ArrayList<String>) document.get("restrictions"); 
		    for (String j: a) {
		    	System.out.println(j);
		    }  
		    System.out.println(); 
		}
	};
	
	//prints all persons
	public static void printAllPersons() {
		TempThread t = getCollection(); 	
	    t.collection.find().forEach(printBlock);    
	    //close thread 
	    t.client.close();
	}
	
	//verifies input passwords match 
	private static boolean passwordMatch(String p, String p2) {
		return p.equals(p2); 
	}
	
	//deletes all persons, only use for the driver!
	public static void deleteAllPersons() {
		TempThread t = getCollection();  
		t.collection.drop(); 	
		//close thread 
		t.client.close();
	}
	
	//login  user 
	public static Document login(String username, String password) {
		TempThread t = getCollection(); 	    
	    Document d = t.collection.find(and(eq("username", username), eq("password", password))).first();     
	    //close collection 
	    t.client.close();
	    return d; 
	}
	
	//find  user 
	public static Document find(String username) {
		TempThread t = getCollection(); 	    
	    Document d = t.collection.find(eq("username", username)).first(); 
	    //close thread 
	    t.client.close();
	    return d; 	
	}
	
	public static void main(String args[]) {
		
		//MAKE SURE MONGOD IS RUNNING BEFORE RUNNING!
		
		System.out.println("Person test Driver"); 
		
		System.out.println("To login, press l");
		System.out.println("To crete a new account, press n: "); 
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		Document d = new Document(); 
		String input = ""; 
		String username = ""; 
		String password = "";
		Person p = new Person(); 
		
		// Reading from System.in
		while (!(input.equals("l") || input.equals("n") )) {  
			System.out.println("Enter a response: ");
			input = reader.next(); 
		}
		
		//log the user in and print their information 
		if (input.equals("l")) {
			// try logging the user in 
			boolean success = false;  
		
			while (!success) {
				System.out.println("Enter username: ");
				username = reader.next(); 
				System.out.println("Enter password: ");
				password = reader.next(); 
				d = login(username, password); 
				if (d != null) {
					success = true; 
					p = new Person(d); 
					System.out.println("User was logged in!");
				}else {
					System.out.println("Not a valid username or password please try again: ");
				}
			}
		}
		//create a new user 
		else {
			System.out.println("Register a user:");
			System.out.println("Enter your name: "); 
			String name = reader.next(); 
			
			boolean userName = false; 
			boolean pass = false; 
			String vPassword = "";  
			
			//create username
			while (!userName) {
				System.out.println("Enter in a new username: ");
				username = reader.next();
				if (find(username) == null) {
					userName = true; 
				}else {
					System.out.println("That username is already in use, please create a new one");
				}
			}
			
			//create password
			while (!pass) {
				System.out.println("Enter in a password: ");
				password = reader.next(); 
				System.out.println("Verify password: ");
				vPassword = reader.next(); 
				
				if (passwordMatch(password, vPassword)) {
					pass = true; 
				}else {
					System.out.println("Passwords do not match, please try again!");
				}
			}
			
			
			//add user restrictions
			String resp; 
			ArrayList<RestType> a = new ArrayList<RestType>(); 
			System.out.println("Do you have any food allergies (y or n): ");
			resp = reader.next(); 
			if (resp.equals("y")) {
				System.out.println("Food restrictions: ");
				System.out.println("Enter GF for gluten free\n" + 
									"Enter VEG for vegatarian\n" + 
									"Enter VEGAN for vegan\n" + 
									"Enter LOWCAR for low carb diet\n" +
									"Enter NUTALRGY for a nut allergy\n"); 
				resp = reader.next(); 
				
				if (resp.equals("GF") || resp.equals("VEG") || resp.equals("VEGAN")
						|| resp.equals("LOWCAR") || resp.equals("NUTALRGY")) {
					a.add(RestType.valueOf(resp)); 
				}else {				
					System.out.println("Invalid response, if you do have a restriction add it later" );
				}
			}
			
			
			//create user 
			p = new Person(name, username, password, a, true); 
			
			System.out.println(username + " was successfully created!");
		}
		
		
		//edit the user's password 
		p.setPassword("newPassword", true);
		
		//edit the user's name 
		p.setName("Brandon Bass", true);
		
		//edit the user's restrictions
		ArrayList<RestType> a = new ArrayList<RestType>(); 
		a.add(RestType.GF); 
		//a.add(RestType.LOWCARB);
		
		p.setRestrictions(a, true);
		
		//try editing username 
		p.editPerson("username", "newUserName");
		
		//print all users 
		printAllPersons(); 
		
	}
}
