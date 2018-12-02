package gg.GUI;

import javax.swing.*;
import java.awt.FlowLayout;

public class CreateMealPlan extends JPanel{
	private JLabel startDateLabel;
	private JLabel endDateLabel;
	private JLabel mealTypeLabel;
	private JTextField startDate;
	private JTextField endDate;
	private JCheckBox breakfast;
	private JCheckBox lunch;
	private JCheckBox dinner;
	
	public CreateMealPlan() {
		super(new FlowLayout());
		buildCreateMealPlan();
	}
	
	private void buildCreateMealPlan() {
		startDateLabel = new JLabel();
		endDateLabel =  new JLabel();
		mealTypeLabel = new JLabel();
		startDate = new JTextField();
		endDate = new JTextField();
		breakfast = new JCheckBox();
		lunch = new JCheckBox();
		dinner = new JCheckBox();
		
		startDateLabel.setText("Enter Start Date in this format: \"MM/DD/YYYY\":");
		endDateLabel.setText("Enter End Date in this format: \"MM/DD/YYYY\":");
		mealTypeLabel.setText("Select desired meal type(s):");
		
		startDate.setColumns(10);
		endDate.setColumns(10);
		
		breakfast.setText("Breakfast");
		lunch.setText("Lunch");
		dinner.setText("Dinner"); 
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
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
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
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
		);
		
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		CreateMealPlan mealPlan = new CreateMealPlan();
		test.add(mealPlan);
		test.pack();
		test.setVisible(true);
	}
}
