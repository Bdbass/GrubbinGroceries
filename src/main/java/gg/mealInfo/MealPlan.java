package gg.mealInfo;

import java.util.ArrayList;
import java.util.Date;

public class MealPlan {
	private ArrayList<Meal> meals; 
	private Date startDate; 
	private int numDays;
	
	public MealPlan()
	{
		meals = new ArrayList<Meal>();
		startDate = new Date();
		numDays = 0;
	}
	
	public MealPlan(Date start, int num)
	{
		meals = new ArrayList<Meal>();
		startDate = start;
		numDays = num;
		Map<String, Double> tempPantry = new HashMap<String, Double>();
		
	}
}
