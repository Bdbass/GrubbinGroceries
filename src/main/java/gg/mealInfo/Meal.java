package gg.mealInfo;

import java.util.ArrayList;
import java.util.Date;
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
	private ArrayList<Food> items; 
	private String instructions; 
	private Date date; 
}
