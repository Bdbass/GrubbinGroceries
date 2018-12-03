package gg.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.GroupLayout.*;

import gg.mealInfo.MealPlan;

public class ViewMealPlan extends JPanel {
	private JLabel currentLabel;
	private JLabel pastLabel;
	private JPanel currentPanel;
	private JPanel pastPanel;
	private JScrollPane scrollPane;
	private ArrayList<JButton> currentPlans;
	private ArrayList<String> currentPlan;
	private ArrayList<JButton> pastPlans;
	private ArrayList<String> pastPlan;
	private ArrayList<JButton> meal;
	private String userID;
	private GrubbinGUI top;
	private JButton deleteButton;
	
	
	public ViewMealPlan(String userID, GrubbinGUI top) {
		super(new FlowLayout());
		this.userID = userID;
		this.top = top;
		this.currentPlans = new ArrayList<JButton>();
		this.currentPlan = new ArrayList<String>();
		this.pastPlans = new ArrayList<JButton>();
		this.pastPlan = new ArrayList<String>();
		//this.currentPlan = MealPlan.getAllCurrentPlans(top.getUserId()); //Brandon making
		//this.pastPlan = MealPlan.getAllPastPlans(top.getUserId()); //Brandon making
		populateMaps();
		buildViewMealPlan();
	}
	
	private void buildViewMealPlan() {
		currentLabel = new JLabel();
		pastLabel = new JLabel();
		
		currentLabel.setText("Current Plan:");
		currentLabel.setFont(new Font(currentLabel.getFont().getName(), Font.PLAIN, 24));
		pastLabel.setText("Past Plans:");
		pastLabel.setFont(new Font(pastLabel.getFont().getName(), Font.PLAIN, 24));
		
		currentPanel = new JPanel();
		pastPanel = new JPanel();
		currentPanel.setPreferredSize(new Dimension(600,200));
		pastPanel.setPreferredSize(new Dimension(600,100)); 
		
		buildCurrentPanel();
		buildPastPanel();
		
		scrollPane = new JScrollPane(pastPanel);
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(currentLabel)
				.addComponent(currentPanel)
				.addComponent(pastLabel)
				.addComponent(scrollPane)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(currentLabel)
				.addComponent(currentPanel)
				.addComponent(pastLabel)
				.addComponent(scrollPane)
		);
		
	}
	
	private void buildCurrentPanel() {
		GroupLayout layout = new GroupLayout(currentPanel);
		currentPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
	
		
		ArrayList<SequentialGroup> seqGroups = new ArrayList<SequentialGroup>();
		ArrayList<ParallelGroup> parGroups = new ArrayList<ParallelGroup>();
		
		ParallelGroup p = layout.createParallelGroup();
		SequentialGroup s = layout.createSequentialGroup();
		
		for (JButton mp : currentPlans) {
			SequentialGroup s1 = layout.createSequentialGroup().addComponent(mp);
			seqGroups.add(s1);
		}
		for (SequentialGroup s1: seqGroups) {
			p.addGroup(s1);
		}
		layout.setHorizontalGroup(p); 
		
		for (JButton mp : currentPlans) {
			ParallelGroup p1 = layout.createParallelGroup().addComponent(mp);
			parGroups.add(p1);
		}
		for (ParallelGroup p1: parGroups) {
			s.addGroup(p1);
		}
		layout.setVerticalGroup(s); 
		
	}
	
	private void buildPastPanel() {
		
		
		ArrayList<SequentialGroup> seqGroups = new ArrayList<SequentialGroup>();
		ArrayList<ParallelGroup> parGroups = new ArrayList<ParallelGroup>();
		
		GroupLayout layout = new GroupLayout(pastPanel);
		pastPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		ParallelGroup p = layout.createParallelGroup();
		SequentialGroup s = layout.createSequentialGroup();
		
		for (JButton mp : pastPlans) {
			SequentialGroup s1 = layout.createSequentialGroup().addComponent(mp);
			seqGroups.add(s1);
		}
		for (SequentialGroup s1: seqGroups) {
			p.addGroup(s1);
		}
		layout.setHorizontalGroup(p); 
		
		for (JButton mp : pastPlans) {
			ParallelGroup p1 = layout.createParallelGroup().addComponent(mp);
			parGroups.add(p1);
		}
		for (ParallelGroup p1: parGroups) {
			s.addGroup(p1);
		}
		layout.setVerticalGroup(s); 
	}
	
	public void populateMaps()
	{
		
		for (String r : pastPlan)
		{
			JButton b1 = new JButton();
			b1.setText(r);
			b1.addActionListener(new MyActionListener());
			pastPlans.add(b1);
		}
		for (String r : currentPlan)
		{
			JButton b1 = new JButton();
			b1.setText(r);
			b1.addActionListener(new MyActionListener());
			currentPlans.add(b1);
		}
	}
	
	private class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			JButton source = (JButton) (e.getSource());
			System.out.println("button pressed");
		
			for (JButton r : pastPlans)
			{
				if (source == r)
				{
					handleButtonPress(r);
				}
			}
			for (JButton r : currentPlans)
			{
				if (source == r)
				{
					handleButtonPress(r);
				}
			}
			for (JButton b : meal)
			{
				if (source == b)
				{
					handleSelectMeal(b);
				}
			}
			/*for (JButton m : meal) {
				if (source.equals(deleteButton)) {
					handleDeleteMeal(m);
				}
			}*/
		}
	
		private void handleButtonPress(JButton r)
		{
			System.out.println("found the button");
			String planName = r.getText();
			JPanel popUp = new JPanel();
			JLabel name = new JLabel(planName);
			JPanel meals = CreatePanel(planName);
//			JTextArea recipeStuff = new JTextArea();
//			recipeStuff.setLineWrap(true);
//			recipeStuff.setWrapStyleWord(true);
//			recipeStuff.setEditable(false);
//			PrintStream outStream = new PrintStream(new TextAreaOutputStream(recipeStuff));
//			System.setOut(outStream);
//			System.setErr(outStream);
//			System.out.print("Just a test!");
//			//Recipe.PrintRecipe(recipeName); //Brandon is making this
//			
//			JScrollPane scrollPane = new JScrollPane(recipeStuff);
			
			GroupLayout layout1 = new GroupLayout(popUp);
			popUp.setLayout(layout1);
			layout1.setAutoCreateGaps(true);
			layout1.setAutoCreateContainerGaps(true);
			
			layout1.setHorizontalGroup(layout1.createParallelGroup().addComponent(name).addComponent(scrollPane));
			layout1.setVerticalGroup(layout1.createSequentialGroup().addComponent(name).addComponent(scrollPane));
			
			JOptionPane.showMessageDialog(null, 
					popUp, 
					"Meals", 
					JOptionPane.PLAIN_MESSAGE);
			
	
			
		}
		
		private void handleSelectMeal(JButton r)
		{
			String planName = r.getText();
			JPanel popUp = new JPanel();
			JLabel name = new JLabel(planName);
			
			JTextArea mealStuff = new JTextArea();
			mealStuff.setLineWrap(true);
			mealStuff.setWrapStyleWord(true);
			mealStuff.setEditable(false);
			
			mealStuff.setText("Just a Test!");
			//Meal.PrintMeal(planName); //Brandon is making this
			
			JScrollPane scrollPane = new JScrollPane(mealStuff);
			
			GroupLayout layout1 = new GroupLayout(popUp);
			popUp.setLayout(layout1);
			layout1.setAutoCreateGaps(true);
			layout1.setAutoCreateContainerGaps(true);
			
			layout1.setHorizontalGroup(layout1.createParallelGroup().addComponent(name).addComponent(scrollPane).addComponent(deleteButton));
			layout1.setVerticalGroup(layout1.createSequentialGroup().addComponent(name).addComponent(scrollPane).addComponent(deleteButton));
			
			JOptionPane.showMessageDialog(null, 
					popUp, 
					planName, 
					JOptionPane.PLAIN_MESSAGE);
			
		}
		
		private JPanel CreatePanel(String plan)
		{
			JPanel j = new JPanel();
			meal = new ArrayList<JButton>();
			ArrayList<String> meals = new ArrayList<String>();
			//meals = MealPlan.getMeals(plan, top.getUserID()); //brandon
			for (String m : meals)
			{
				JButton b1 = new JButton();
				b1.setText(m);
				b1.addActionListener(new MyActionListener());
				meal.add(b1);
			}
			
			ArrayList<SequentialGroup> seqGroups = new ArrayList<SequentialGroup>();
			ArrayList<ParallelGroup> parGroups = new ArrayList<ParallelGroup>();
			
			GroupLayout layout = new GroupLayout(j);
			j.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			ParallelGroup p = layout.createParallelGroup();
			SequentialGroup s = layout.createSequentialGroup();
			
			for (JButton mp : meal) {
				SequentialGroup s1 = layout.createSequentialGroup().addComponent(mp);
				seqGroups.add(s1);
			}
			for (SequentialGroup s1: seqGroups) {
				p.addGroup(s1);
			}
			layout.setHorizontalGroup(p); 
			
			for (JButton mp : meal) {
				ParallelGroup p1 = layout.createParallelGroup().addComponent(mp);
				parGroups.add(p1);
			}
			for (ParallelGroup p1: parGroups) {
				s.addGroup(p1);
			}
			layout.setVerticalGroup(s); 
			
			return j;
		}
		
		private void handleDeleteMeal() {
			//write this
		}
		
	}
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		String userID = "123456789"; //need a way to get the real one.
		ViewMealPlan viewMealPlan = new ViewMealPlan(userID, test);
		test.add(viewMealPlan);
		test.pack();
		test.setVisible(true);
	}
}
