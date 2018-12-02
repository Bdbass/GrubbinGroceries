package gg.GUI;

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
	
		
		login = new Login();
		tabs.addTab("Log In", login);
		
		signup = new SignUp();
		tabs.addTab("Sign Up", signup);
		
		homepage = new Homepage();
		tabs.addTab("Home", homepage);
		
		upPref = new UpdatePreferences();
		tabs.addTab("Update Restrictions", upPref);
		
		createPlan = new CreateMealPlan();
		tabs.addTab("Create Plan", createPlan);
		
		viewPlan = new ViewMealPlan(userId);
		tabs.addTab("Meal Plans", viewPlan);
		
		viewPantry = new ViewPantry(userId);
		tabs.addTab("Pantry", viewPantry);
		
		viewShoppingList = new ViewShoppingList(userId);
		tabs.addTab("Shopping List", viewShoppingList);
		
		viewRecipe = new ViewRecipe();
		tabs.addTab("Recipes", viewRecipe);
		
		createRecipe = new RecipeCreation();
		tabs.addTab("Create Recipe", createRecipe);
		
		
		
		
		
		
		
		
		
		
	}
	
	public static void main(String args[])
	{
		new GrubbinGUI();
	}
	
	
	
}
