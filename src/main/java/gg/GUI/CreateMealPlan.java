package gg.GUI;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

public class CreateMealPlan extends JPanel{
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
	
	public CreateMealPlan(GrubbinGUI top) {
		super(new FlowLayout());
		this.top = top;
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
		
		confirm.setText("Create Plan");
		
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
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		CreateMealPlan mealPlan = new CreateMealPlan(test);
		test.add(mealPlan);
		test.pack();
		test.setVisible(true);
	}
}
