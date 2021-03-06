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

import org.joda.time.DateTime;

import gg.mealInfo.Meal;
import gg.mealInfo.MealPlan;

public class ViewMealPlan extends JPanel {
	private JLabel currentLabel;
	private JLabel pastLabel;
	private JPanel currentPanel;
	private JPanel pastPanel;
	private JScrollPane scrollPane;
	private ArrayList<JButton> currentPlans;
	private ArrayList<ArrayList<String>> currentPlan;
	private ArrayList<JButton> pastPlans;
	private ArrayList<ArrayList<String>> pastPlan;
	private ArrayList<JButton> meal;
	private ArrayList<ArrayList<String>> meals = new ArrayList<ArrayList<String>>();
	private String userID;
	private GrubbinGUI top;
	private JButton deleteButton;
	private JButton mealDetails;
	private String ActionListenerMealPlanID;
	private JButton refresh;
	
	
	public ViewMealPlan(String userID, GrubbinGUI top) {
		super(new FlowLayout());
		this.userID = userID;
		this.top = top;
		this.currentPlans = new ArrayList<JButton>();
		this.currentPlan = new ArrayList<ArrayList<String>>();
		this.pastPlans = new ArrayList<JButton>();
		this.pastPlan = new ArrayList<ArrayList<String>>();
		this.currentPlan = MealPlan.getCurrentMealPlans(top.getUserID()); 		
		this.pastPlan = MealPlan.getPastMealPlans(top.getUserID()); 
		meal = new ArrayList<JButton>();
		System.out.println("pastPlan size:" + pastPlan.size());
		System.out.println("currentPlan size:" + currentPlan.size()); 
		populateArrayLists();
		buildViewMealPlan();
	}
	
	private void buildViewMealPlan() {
		currentLabel = new JLabel();
		pastLabel = new JLabel();
		refresh = new JButton();
		refresh.addActionListener(new MyActionListener()); 
		refresh.setText("Refresh Page");
		
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
				.addComponent(refresh)
				.addComponent(currentLabel)
				.addComponent(currentPanel)
				.addComponent(pastLabel)
				.addComponent(scrollPane)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(refresh)
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
	
	public void populateArrayLists()
	{ //a[i][0] = "startdate mealtype"
	  //a[i][1] = mealPlanId
		for (ArrayList<String> a : pastPlan)
		{
			JButton b1 = new JButton();
			b1.setText(a.get(0));
			System.out.println(a.get(0));  //debug
			b1.addActionListener(new MyActionListener());
			pastPlans.add(b1);
		}
		for (ArrayList<String> a : currentPlan)
		{
			JButton b1 = new JButton();
			b1.setText(a.get(0));
			System.out.println(a.get(0));  //debug
			b1.addActionListener(new MyActionListener());
			currentPlans.add(b1);
		}
	}
	
	private class MyActionListener implements ActionListener{
		private String mealID = new String();
		
		public void actionPerformed(ActionEvent e)
		{
			JButton source = (JButton) (e.getSource());
		
			for (JButton r : pastPlans)
			{
				if (source == r)
				{
					handleButtonPress(r, true);
				}
			}
			for (JButton r : currentPlans)
			{
				if (source == r)
				{
					handleButtonPress(r, false);
				}
			}
			for (JButton b : meal)
			{
				if (source == b)
				{
					handleSelectMeal(b);
				}
			}
			if (source.equals(deleteButton)) {
				handleDeleteMeal(mealID);
			}
			if (source.equals(mealDetails)) {
				handleMealDetails();
			}
			if (source.equals(refresh)) {
				handleRefresh();
			}
		}
	
		private void handleButtonPress(JButton r, boolean plan)
		{
			String planName = new String();
			if (plan == true) {
				for (ArrayList<String> a : pastPlan) {
					if (a.get(0).equals(r.getText())) {
						planName = a.get(1);
						ActionListenerMealPlanID = planName;
					}
				}
			}
			else {
				for (ArrayList<String> a : currentPlan) {
					if (a.get(0).equals(r.getText())) {
						planName = a.get(1);
						ActionListenerMealPlanID = planName;
						System.out.println(ActionListenerMealPlanID);
					}
				}
			}
			
			JPanel popUp = new JPanel();
			JLabel name = new JLabel(r.getText());
			JPanel meals = CreatePanel(planName);
			
			JScrollPane scrollPane = new JScrollPane(meals);
			
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
			System.out.println(ActionListenerMealPlanID);
			mealID = new String();
			for(ArrayList<String> a : meals) {
				if (r.getText().equals(a.get(0))) {
					mealID = a.get(1);
				}
			}
			
			JPanel popUp = new JPanel();
			JLabel name = new JLabel(r.getText());
			deleteButton = new JButton();
			deleteButton.addActionListener(this);
			deleteButton.setText("Delete Meal");
			mealDetails = new JButton();
			mealDetails.addActionListener(this);
			mealDetails.setText("Meal Details"); 
			
			JTextArea mealStuff = new JTextArea();
			mealStuff.setLineWrap(true);
			mealStuff.setWrapStyleWord(true);
			mealStuff.setEditable(false);
			
			String s = Meal.printMeal(mealID);
			mealStuff.setText(s);
			
			JScrollPane scrollPane = new JScrollPane(mealStuff);
			
			GroupLayout layout1 = new GroupLayout(popUp);
			popUp.setLayout(layout1);
			layout1.setAutoCreateGaps(true);
			layout1.setAutoCreateContainerGaps(true);
			
			layout1.setHorizontalGroup(layout1.createParallelGroup()
					.addComponent(name)
					.addComponent(scrollPane)
					.addGroup(layout1.createSequentialGroup()
							.addComponent(deleteButton)
							.addComponent(mealDetails)
					)
			);
			layout1.setVerticalGroup(layout1.createSequentialGroup()
					.addComponent(name)
					.addComponent(scrollPane)
					.addGroup(layout1.createParallelGroup()
							.addComponent(deleteButton)
							.addComponent(mealDetails)
					)
			);
			
			JOptionPane.showMessageDialog(null, 
					popUp, 
					r.getText(), 
					JOptionPane.PLAIN_MESSAGE);
			
		}
		
		private JPanel CreatePanel(String planID)
		{
			JPanel j = new JPanel();
			meals = MealPlan.getMeals(top.getUserID(), planID);
			for (ArrayList<String> m : meals)
			{
				JButton b1 = new JButton();
				b1.setText(m.get(0)); //name
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
		
		private void handleDeleteMeal(String mealID) {
			System.out.println(ActionListenerMealPlanID); 
			MealPlan mp = MealPlan.getMealPlan(ActionListenerMealPlanID);
			
			DateTime tempDate = new DateTime(mp.getStartDate());
			String tempString = tempDate.toString("MM/dd/yyyy");
			
			ArrayList<ArrayList<String>> meals = MealPlan.getMeals(top.getUserID(), ActionListenerMealPlanID);
			for (ArrayList<String> a : meals) {
				if(a.get(1).equals(mealID)) {
					MealPlan.deleteMeal(top.getUserID(), mp.getMealType(), tempString);
				}
			}
		}
		
		private void handleMealDetails() {
			String s = Meal.getMealInfo(mealID);
			JTextArea textArea = new JTextArea();
			textArea.setText(s);
			JOptionPane.showMessageDialog(null, textArea, "Meal Details", JOptionPane.PLAIN_MESSAGE);
			
		}
		
		private void handleRefresh()
		{
			currentPlan = MealPlan.getCurrentMealPlans(top.getUserID()); 
			currentPlans.clear();

			pastPlan = MealPlan.getPastMealPlans(top.getUserID());
			pastPlans.clear();

			meal.clear();
			
			removeAll();
			populateArrayLists();
			buildViewMealPlan();
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
