package gg.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RecipeCreation extends JPanel
{
	JLabel title;
	JLabel description;
	JFileChooser fileChooser;
	JButton choose;
	GrubbinGUI top;
	ViewRecipe recipes;
	
	
	public RecipeCreation(GrubbinGUI top, ViewRecipe recipes)
	{
		super(new FlowLayout());
		this.top = top;
		this.recipes = recipes;
		buildRecipe();
	}
	
	public void buildRecipe()
	{
		title = new JLabel("<html><center>Create a Recipe</center></html>");
		description = new JLabel("Upload text file formatted as **TODO**");
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
			fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
			fileChooser.setFileFilter(filter);
			int result = fileChooser.showOpenDialog(new JFrame());
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				String format = "f";//Recipe.addRecipe(selectedFile);
				if (format.equals("fail"))
				{
					JOptionPane.showMessageDialog(null, 
							"Wrong file format. Please try again.", "Incorrect File Format", 
							JOptionPane.PLAIN_MESSAGE);
				}
				else
				{
					recipes.addRecipe(format);
					JOptionPane.showMessageDialog(null, 
							"Recipe successfully added.", "Recipe Added", 
							JOptionPane.PLAIN_MESSAGE);
				}
			}
			
		}
	}
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		
	}
	
}
