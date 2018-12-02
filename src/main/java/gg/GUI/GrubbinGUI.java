package gg.GUI;

import javax.swing.*;

public class GrubbinGUI extends JFrame
{
	JTabbedPane tabs;
	Login login;
	SignUp signup;
	Homepage homepage;
	UpdatePreferences upPref;
	
	
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
		
		login = new Login();
		tabs.addTab("Log In", login);
		
		signup = new SignUp();
		tabs.addTab("Sign Up", signup);
		
		homepage = new Homepage();
		tabs.addTab("Home", homepage);
		
		upPref = new UpdatePreferences();
		tabs.addTab("Update Restrictions", upPref);
		
		
		
		
		
		
		
		
		
		
	}
	
	public static void main(String args[])
	{
		new GrubbinGUI();
	}
	
	
	
}
