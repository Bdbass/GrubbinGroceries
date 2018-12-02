package gg.GUI;

import java.awt.Dimension;

import javax.swing.*;

public class GrubbinGUI extends JFrame
{
	JTabbedPane tabs;
	Login login;
	SignUp signup;
	Homepage homepage;
	UpdatePreferences upPref;
	CreateMealPlan createPlan;
	ViewMealPlan viewPlan;
	ViewPantry viewPantry;
	ViewShoppingList viewShoppingList;
	ViewRecipe viewRecipe;
	RecipeCreation createRecipe;
	
	String userId;
	
	
	public GrubbinGUI()
	{
		super();
		this.setTitle("Grubbin' Groceries");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 350));
		this.setMaximumSize(new Dimension(700, 350));
		buildGUI();
		this.add(tabs);
		pack();
		setVisible(true);
	}
	
	public void buildGUI()
	{
		tabs = new JTabbedPane();
		userId = new String();
		userId = "unknown";
	
		
		login = new Login(this);
		tabs.addTab("Log In", login);
		
		signup = new SignUp(this);
		tabs.addTab("Sign Up", signup);
		
		homepage = new Homepage(this);
		tabs.addTab("Home", homepage);
		
		upPref = new UpdatePreferences(this);
		tabs.addTab("Update Restrictions", upPref);
		
		createPlan = new CreateMealPlan(this);
		tabs.addTab("Create Plan", createPlan);
		
		viewPlan = new ViewMealPlan(userId, this);
		tabs.addTab("Meal Plans", viewPlan);
		
		viewPantry = new ViewPantry(userId, this);
		tabs.addTab("Pantry", viewPantry);
		
		viewShoppingList = new ViewShoppingList(userId, this);
		tabs.addTab("Shopping List", viewShoppingList);
		
		viewRecipe = new ViewRecipe(this);
		tabs.addTab("Recipes", viewRecipe);
		
		createRecipe = new RecipeCreation(this, viewRecipe);
		tabs.addTab("Create Recipe", createRecipe);
		
		
		
		
		
		
		
		
		
		
	}
	
	public void switchToSignUp()
	{
		tabs.setSelectedIndex(1);
	}
	
	public void switchToHome()
	{
		tabs.setSelectedIndex(2);
	}
	
	public static void main(String args[])
	{
		new GrubbinGUI();
	}
	
	
	
}
