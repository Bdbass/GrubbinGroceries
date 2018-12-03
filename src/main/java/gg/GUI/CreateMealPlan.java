package gg.GUI;

import javax.swing.*;

import gg.mealInfo.MealPlan;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CreateMealPlan extends JPanel implements ItemListener, ActionListener{ 
	private JLabel title;
	private JLabel startDateLabel;
	private JLabel endDateLabel;
	private JLabel mealTypeLabel;
	private JTextField startDate;
	private JTextField endDate;
	private JCheckBox breakfast;
	private JCheckBox lunch;
	private JCheckBox dinner;
	private JButton confirm;
	private GrubbinGUI top;
	
	private Boolean bPlan;
	private Boolean lPlan;
	private Boolean dPlan;
	
	public CreateMealPlan(GrubbinGUI top) {
		super(new FlowLayout());
		this.top = top;
		buildCreateMealPlan();
	}
	
	private void buildCreateMealPlan() {
		bPlan = false;
		lPlan = false;
		dPlan = false;
		
		startDateLabel = new JLabel();
		endDateLabel =  new JLabel();
		mealTypeLabel = new JLabel();
		
		startDate = new JTextField();
		endDate = new JTextField();
		
		breakfast = new JCheckBox();
		lunch = new JCheckBox();
		dinner = new JCheckBox();
		
		title = new JLabel("Create Meal Plan");
		
		confirm = new JButton();
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		startDateLabel.setText("Enter Start Date in this format: \"MM/DD/YYYY\":");
		endDateLabel.setText("Enter End Date in this format: \"MM/DD/YYYY\":");
		mealTypeLabel.setText("Select desired meal type(s):");
		
		startDate.setPreferredSize(new Dimension(250, 25));
        startDate.setMaximumSize(new Dimension(250, 25));
		
		endDate.setPreferredSize(new Dimension(250, 25));
        endDate.setMaximumSize(new Dimension(250, 25));
		
		breakfast.setText("Breakfast");
		lunch.setText("Lunch");
		dinner.setText("Dinner"); 
		breakfast.addItemListener(this);
		lunch.addItemListener(this);
		dinner.addItemListener(this); 
		
		confirm.setText("Create Plan");
		confirm.addActionListener(this); 
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGap(100)
						.addComponent(title)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(startDateLabel)
						.addComponent(startDate)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(endDateLabel)
						.addComponent(endDate)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(mealTypeLabel)
						.addComponent(breakfast)
						.addComponent(lunch)
						.addComponent(dinner)
				)
				.addGroup(layout.createSequentialGroup()
						.addGap(100)
						.addComponent(confirm)
				)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addGroup(layout.createParallelGroup()
						.addComponent(startDateLabel)
						.addComponent(startDate)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(endDateLabel)
						.addComponent(endDate)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(mealTypeLabel)
						.addComponent(breakfast)
						.addComponent(lunch)
						.addComponent(dinner)
				) 
				.addComponent(confirm)
		);
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		JButton source = (JButton)(e.getSource());
			
		if(source.equals(confirm)) {
			handleConfirm();
		}
	}
	
	private void handleConfirm() {
		String startDateString = startDate.getText();
		String endDateString = endDate.getText();
		
		if (bPlan == true) {
			MealPlan b = new MealPlan(top.getUserID(), startDateString, endDateString, "BREAKFAST");
			if (b.getMealIDs().size() == 0) {
				JOptionPane.showMessageDialog(null, 
						"Sorry, there are no current recipes that match your dietary restrictions.\n" + 
						"You can use the \"Create Recipes\" tab to add your own recipes.",
						"Error creating meal plan",
						JOptionPane.PLAIN_MESSAGE);
			}
		}
		if (lPlan == true) {
			MealPlan l = new MealPlan(top.getUserID(), startDateString, endDateString, "LUNCH");
			if (l.getMealIDs().size() == 0) {
				JOptionPane.showMessageDialog(null, 
						"Sorry, there are no current recipes that match your dietary restrictions.\n" + 
						"You can use the \"Create Recipes\" tab to add your own recipes.",
						"Error creating meal plan",
						JOptionPane.PLAIN_MESSAGE);
			}
		}
		if (dPlan == true) {
			MealPlan d = new MealPlan(top.getUserID(), startDateString, endDateString, "DINNER");
			if (d.getMealIDs().size() == 0) {
				JOptionPane.showMessageDialog(null, 
						"Sorry, there are no current recipes that match your dietary restrictions.\n" + 
						"You can use the \"Create Recipes\" tab to add your own recipes.",
						"Error creating meal plan",
						JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		JCheckBox source = (JCheckBox) e.getItemSelectable();
		int index = -1;
		if (source.equals(breakfast))
		{
			index = 0;
		}
		else if (source.equals(lunch))
		{
			index = 1;
		}
		else if (source.equals(dinner))
		{				
			index = 2;
		}
		if (e.getStateChange() == ItemEvent.DESELECTED)
		{				
			handlePreferences(index, false);
		}
		else 
		{
			handlePreferences(index, true);
		}
	}
	
	private void handlePreferences(int index, boolean set)
	{
		if (index == 0) {
			if (set == true) {
				bPlan = true;
			}
			else {
				bPlan = false;
			}
		}
		else if (index == 1) {
			if (set == true) {
				lPlan = true;
			}
			else {
				lPlan = false;
			}
		}
		else if (index == 2) {
			if (set == true) {
				dPlan = true;
			}
			else {
				dPlan = false;
			}
		}
	}
	
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		CreateMealPlan mealPlan = new CreateMealPlan(test);
		test.add(mealPlan);
		test.pack();
		test.setVisible(true);
	}
}
