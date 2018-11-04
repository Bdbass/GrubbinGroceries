package gg.userInfo;

import gg.mealInfo.*;
import gg.physObjs.*;
import java.util.ArrayList;

public class Person {
	private String name; 
	private Restriction restriction; 
	private Pantry pantry; 
	private MealPlan mealPlan; 
	private ArrayList<Recipe> recipes;
	
	public Person() {
		name = "unkown"; 
		restriction = new Restriction(); 
		pantry = new Pantry(); 
		mealPlan = new MealPlan(); 
		recipes = new ArrayList<Recipe>(); 
	}
	
	public Person(String n) {
		name = n; 
		restriction = new Restriction(); 
		pantry = new Pantry(); 
		mealPlan = new MealPlan(); 
		recipes = new ArrayList<Recipe>(); 
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Pantry getPantry() {
		return pantry;
	}
	public void setPantry(Pantry pantry) {
		this.pantry = pantry;
	}
	public MealPlan getMealPlan() {
		return mealPlan;
	}
	public void setMealPlan(MealPlan mealPlan) {
		this.mealPlan = mealPlan;
	} 
		
}
