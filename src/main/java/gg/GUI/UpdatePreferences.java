package gg.GUI;

import java.awt.FlowLayout;

import javax.swing.*;

public class UpdatePreferences extends JPanel {
	
	JLabel title;
	
	JCheckBox gf;
	JCheckBox lc;
	JCheckBox vegt;
	JCheckBox vegan;
	JCheckBox nut;
	
	JButton update;
	
	public UpdatePreferences()
	{
		super(new FlowLayout());
		buildSignUp();
	}
	
	public void buildSignUp()
	{
		
		title = new JLabel();
		update = new JButton();
		gf = new JCheckBox();
		lc = new JCheckBox();
		vegt = new JCheckBox();
		vegan = new JCheckBox();
		nut = new JCheckBox();
		
		
		update.setText("Update");
		gf.setText("Gluten Free");
		lc.setText("Low Carb");
		vegt.setText("Vegetarian");
		vegan.setText("Vegan");
		nut.setText("Nut Allergy");
		title.setText("Update Preferences");
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(title)
				
				.addGroup(layout.createSequentialGroup()
						.addComponent(gf)
						.addComponent(lc)
						.addComponent(vegt)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(vegan)
						.addComponent(nut)
				)
				.addComponent(update)
				
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addGroup(layout.createParallelGroup()
						.addComponent(gf)
						.addComponent(lc)
						.addComponent(vegt)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(vegan)
						.addComponent(nut)
				)
				.addComponent(update)
				
		);
		
		
		
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		UpdatePreferences update = new UpdatePreferences();
		test.add(update);
		test.pack();
		test.setVisible(true);
	}


}
