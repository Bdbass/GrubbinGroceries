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
	//private GrubbinGUI top;
	private JLabel title;
	private JScrollPane scroll;
	private JPanel list;
	/* JLabels for all the recipes */
	private Map<String, JButton> recipes;
	private ArrayList<String> recipe;
	/* Buttons for all the recipes */
	
	public ViewRecipe(/*GrubbinGUI top*/)
	{
		super(new FlowLayout());
		recipe = new ArrayList<String>();

		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		recipes = new HashMap<String, JButton>();
		//this.top = top;
		
		recipe = Recipe.getAllRecipes();
		populateMap();
		
		
		//populateMap();
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
		for (String r : recipes.keySet())
		{
			//JLabel name = new JLabel();
			//name.setText(r);
			SequentialGroup s1 = layout.createSequentialGroup()/*.addComponent(name)*/.addComponent(recipes.get(r));
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
		for (String r : recipes.keySet())
		{
			//JLabel name = new JLabel();
			//name.setText(r);
			ParallelGroup p1 = layout.createParallelGroup()/*.addComponent(name)*/.addComponent(recipes.get(r));
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
	
	public void populateMap()
	{
		
		for (String r : recipe)
		{
			JButton b1 = new JButton();
			b1.setText("Details");
			recipes.put(r, b1);
		}
	}
	
	public void addRecipe(String recipeName)
	{
		JButton b1 = new JButton();
		b1.setText("Details");
		recipes.put(recipeName, b1);
		buildRecipe();
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		String userID = "123456789"; //need a way to get the real one.
		ViewRecipe viewMealPlan = new ViewRecipe();
		test.add(viewMealPlan);
		test.pack();
		test.setVisible(true);
	}
		
	
	
}
