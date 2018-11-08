package gg.physObjs;
import java.util.ArrayList;
import gg.userInfo.*;

public abstract class Food {
	private double servingSize;
	private String name; 
	private ArrayList<Restriction> restrictions; 
	private String nutritionalValue; 
	
	public Food() {
		servingSize = 0.00;
		name = "Unknown"; 
		restrictions = new ArrayList<Restriction>(); 
		nutritionalValue = "Unknown"; 
	}

	public double getAmount() {
		return servingSize;
	}

	public void setAmount(double amount) {
		this.servingSize = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Restriction> getRestrictions() {
		return restrictions;
	}

	public void addRestriction(Restriction restriction) {
		this.restrictions.add(restriction);
	}

	public String getNutritionalValue() {
		return nutritionalValue;
	}

	public void setNutritionalValue(String nutritionalValue) {
		this.nutritionalValue = nutritionalValue;
	}
	
	
}
