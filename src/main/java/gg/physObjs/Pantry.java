package gg.physObjs;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;

public class Pantry {
	private ArrayList<Food> food; 
	
	public Pantry()
	{
		food = new ArrayList<Food>();
	}
	
	public Pantry(ArrayList<Food> food)
	{
		this.food = food;
	}
	
	//should this check if they already have some of the food and instead of adding it they just take the amount and add it to the amount of the other one
	public void addFood(Food f1) 
	{
		this.food.add(f1);
	}
	
	public void removeFood(Food f1)
	{
		for (Food f : food)
		{
			if (f1.getName() == f.getName())
			{
				if (f1.getAmount() != f.getAmount())
				{
					f.setAmount(f.getAmount()-f1.getAmount());
				}
				else
				{
					food.remove(f);
				}
			}
		}
	}
}
