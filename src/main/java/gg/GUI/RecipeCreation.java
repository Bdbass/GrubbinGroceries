package gg.GUI;

import java.awt.FlowLayout;

import javax.swing.*;

public class RecipeCreation extends JPanel
{
	JLabel title;
	JLabel description;
	
	
	public RecipeCreation()
	{
		super(new FlowLayout());
		buildRecipe();
	}
	
	public void buildRecipe()
	{
		title = new JLabel("<html><center>Create a Recipe</center></html>");
		description = new JLabel("Upload text file formatted as **TODO**");
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(title)
				.addComponent(description)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addComponent(description)
				);
		
		//TODO the upload file
		
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		RecipeCreation login = new RecipeCreation();
		test.add(login);
		test.pack();
		test.setVisible(true);
	}
	
}
