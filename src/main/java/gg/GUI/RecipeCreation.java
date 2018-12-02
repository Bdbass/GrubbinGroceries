package gg.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RecipeCreation extends JPanel
{
	JLabel title;
	JLabel description;
	JFileChooser fileChooser;
	JButton choose;
	
	
	public RecipeCreation()
	{
		super(new FlowLayout());
		buildRecipe();
	}
	
	public void buildRecipe()
	{
		title = new JLabel("<html><center>Create a Recipe</center></html>");
		description = new JLabel("Upload text file formatted as **TODO**");
        fileChooser = new JFileChooser();
		choose = new JButton();
		choose.setText("Select a file");
		choose.addActionListener(new Listener());
		
		
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(75)
				.addComponent(title))
				.addComponent(description)
				.addComponent(choose)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addComponent(description)
				.addComponent(choose)
				);
		
		
		
	}
	
	private class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) //this is the method MenuListener must implement, as it comes from the ActionListener interface.
		{
			JButton source = (JButton)(e.getSource());
			
			if (source.equals(choose))
			{
				handleChoose();
			}
		}
		
		private void handleChoose()
		{
			//open file chooser
		}
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
