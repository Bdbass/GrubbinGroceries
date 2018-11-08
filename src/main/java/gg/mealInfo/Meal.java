package gg.mealInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import gg.physObjs.Food;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;


public class Meal {
	private String name;
	private Map<String, Double> items; 
	private String instructions; 
	private Date date;
	
	public Meal()
	{
		name = "unknown";
		items = new HashMap<String, Double>();
		instructions = "unknown";
		date = new Date();
	}
	
	// create meal from recipe
	public Meal(Recipe r)
	{
		name = r.getName();
		items = r.getItems();
		instructions = r.getInstructions();
		date = new Date();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Double>getItems() {
		return items;
	}
	public void setItems(Map<String, Double> items) {
		this.items = items;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	} 
	
	// add
	
	// edit
	
	// delete
	
	
}
