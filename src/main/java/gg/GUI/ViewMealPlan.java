package gg.GUI;

import java.awt.FlowLayout;
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
	private Map<MealPlan,JButton> plans;
	private String userID;
	
	
	public ViewMealPlan(String userID) {
		super(new FlowLayout());
		this.userID = userID;
		this.plans = new HashMap<MealPlan,JButton>();
		buildViewMealPlan();
	}
	
	private void buildViewMealPlan() {
		currentLabel = new JLabel();
		pastLabel = new JLabel();
		
		currentLabel.setText("Current Plan:");
		pastLabel.setText("Past Plans:");
		
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
		
		ArrayList<MealPlan> mealPlans = new ArrayList<MealPlan>();
		//mealPlans = MealPlan.getAllCurrentMealPlans(); //will need to be static
		
		for (MealPlan mp : mealPlans) {
			JButton button = new JButton();
			button.setText("Details");
			plans.put(mp, button);
		}
		
		ArrayList<SequentialGroup> seqGroups = new ArrayList<SequentialGroup>();
		ArrayList<ParallelGroup> parGroups = new ArrayList<ParallelGroup>();
		
		ParallelGroup p = layout.createParallelGroup();
		SequentialGroup s = layout.createSequentialGroup();
		
		for (MealPlan mp : plans.keySet()) {
			JLabel label = new JLabel();
			label.setText("MEAL TYPE IDK HOW TO GET THIS");		//FIXME
			SequentialGroup s1 = layout.createSequentialGroup().addComponent(label).addComponent(plans.get(mp));
			seqGroups.add(s1);
		}
		for (SequentialGroup s1: seqGroups) {
			p.addGroup(s1);
		}
		layout.setHorizontalGroup(p); 
		
		for (MealPlan mp : plans.keySet()) {
			JLabel label = new JLabel();
			label.setText("MEAL TYPE IDK HOW TO GET THIS");		//FIXME
			ParallelGroup p1 = layout.createParallelGroup().addComponent(label).addComponent(plans.get(mp));
			parGroups.add(p1);
		}
		for (ParallelGroup p1: parGroups) {
			s.addGroup(p1);
		}
		layout.setVerticalGroup(s); 
		
	}
	
	private void buildPastPanel() {
		ArrayList<MealPlan> mealPlans = new ArrayList<MealPlan>();
		//mealPlans = MealPlan.getAllPastMealPlans(); //will need to be static
		
		for (MealPlan mp : mealPlans) {
			JButton button = new JButton();
			button.setText("Details");
			plans.put(mp, button);
		}
		
		ArrayList<SequentialGroup> seqGroups = new ArrayList<SequentialGroup>();
		ArrayList<ParallelGroup> parGroups = new ArrayList<ParallelGroup>();
		
		GroupLayout layout = new GroupLayout(pastPanel);
		pastPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		ParallelGroup p = layout.createParallelGroup();
		SequentialGroup s = layout.createSequentialGroup();
		
		for (MealPlan mp : plans.keySet()) {
			JLabel label = new JLabel();
			label.setText("MEAL TYPE IDK HOW TO GET THIS");		//FIXME
			SequentialGroup s1 = layout.createSequentialGroup().addComponent(label).addComponent(plans.get(mp));
			seqGroups.add(s1);
		}
		for (SequentialGroup s1: seqGroups) {
			p.addGroup(s1);
		}
		layout.setHorizontalGroup(p); 
		
		for (MealPlan mp : plans.keySet()) {
			JLabel label = new JLabel();
			label.setText("MEAL TYPE IDK HOW TO GET THIS");		//FIXME
			ParallelGroup p1 = layout.createParallelGroup().addComponent(label).addComponent(plans.get(mp));
			parGroups.add(p1);
		}
		for (ParallelGroup p1: parGroups) {
			s.addGroup(p1);
		}
		layout.setVerticalGroup(s); 
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		String userID = "123456789"; //need a way to get the real one.
		ViewMealPlan viewMealPlan = new ViewMealPlan(userID);
		test.add(viewMealPlan);
		test.pack();
		test.setVisible(true);
	}
}
