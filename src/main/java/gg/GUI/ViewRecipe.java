package gg.GUI;
import gg.mealInfo.*;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.GroupLayout.*;

public class ViewRecipe extends JPanel 
{
	JLabel title;
	JScrollPane scroll;
	JPanel list;
	/* JLabels for all the recipes */
	Map<Recipe, JButton> recipes;
	/* Buttons for all the recipes */
	
	public ViewRecipe()
	{
		super(new FlowLayout());
		recipes = new HashMap<Recipe, JButton>();
		ArrayList<Recipe> recipe = new ArrayList<Recipe>();
		//recipe = Recipe.getAllRecipes();
		populateMap(recipe);
		buildRecipe();
	}
	
	public void buildRecipe()
	{
		title = new JLabel("Recipes");
		list = new JPanel();
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));

		
		//builds panel for scroll pane
		GroupLayout layout = new GroupLayout(list);
		list.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		ParallelGroup p = layout.createParallelGroup();
		
		// sequential group loop that is on paper
		ArrayList<SequentialGroup> sGroups = new ArrayList<SequentialGroup>();
		for (Recipe r : recipes.keySet())
		{
			JLabel name = new JLabel();
			name.setText(r.getName());
			SequentialGroup s1 = layout.createSequentialGroup().addComponent(name).addComponent(recipes.get(r));
			sGroups.add(s1);
		}
		
		for (SequentialGroup s : sGroups)
		{
			p.addGroup(s);
		}
		
		layout.setHorizontalGroup(p);
		
		SequentialGroup s = layout.createSequentialGroup();
		
		// parallel group loop that is same as above
		ArrayList<ParallelGroup> pGroups = new ArrayList<ParallelGroup>();
		for (Recipe r : recipes.keySet())
		{
			JLabel name = new JLabel();
			name.setText(r.getName());
			ParallelGroup p1 = layout.createParallelGroup().addComponent(name).addComponent(recipes.get(r));
			pGroups.add(p1);
		}
		
		for (ParallelGroup p2 : pGroups)
		{
			s.addGroup(p2);
		}
		
		layout.setVerticalGroup(s);
		
		scroll = new JScrollPane(list);
		
		GroupLayout layout1 = new GroupLayout(this);
		this.setLayout(layout1);
		layout1.setAutoCreateGaps(true);
		layout1.setAutoCreateContainerGaps(true);

		layout1.setHorizontalGroup(
				layout1.createParallelGroup()
				.addGroup(layout1.createSequentialGroup().addGap(200)
						.addComponent(title))
				.addComponent(scroll)
				);
		
		layout1.setVerticalGroup(
				layout1.createSequentialGroup()
				.addComponent(title)
				.addComponent(scroll)
				);
		
	}
	
	public void populateMap(ArrayList<Recipe> recipe)
	{
		for (Recipe r : recipe)
		{
			JButton b1 = new JButton();
			b1.setText("Details");
			recipes.put(r, b1);
		}
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		ViewRecipe login = new ViewRecipe();
		test.add(login);
		test.pack();
		test.setVisible(true);
	}
	
}
