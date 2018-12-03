package gg.GUI;
import gg.mealInfo.*;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.GroupLayout.*;

public class ViewRecipe extends JPanel
{
	private GrubbinGUI top;
	private JLabel title;
	private JScrollPane scroll;
	private JPanel list;
	/* JLabels for all the recipes */
	private ArrayList<JButton> recipes;
	private ArrayList<String> recipe;
	/* Buttons for all the recipes */
	
	public ViewRecipe(GrubbinGUI top)
	{
		super(new FlowLayout());
		recipe = new ArrayList<String>();

		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		recipes = new ArrayList<JButton>();
		this.top = top;
		
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
		for (JButton r : recipes)
		{
			//JLabel name = new JLabel();
			//name.setText(r);
			SequentialGroup s1 = layout.createSequentialGroup().addComponent(r);
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
		for (JButton r : recipes)
		{
			//JLabel name = new JLabel();
			//name.setText(r);
			ParallelGroup p1 = layout.createParallelGroup().addComponent(r);
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
				.addGroup(layout1.createSequentialGroup().addGap(275)
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
			b1.setText(r);
			b1.addActionListener(new MyActionListener());
			recipes.add(b1);
		}
	}
	
	public void addRecipe(String recipeName)
	{
		JButton b1 = new JButton();
		b1.setText(recipeName);
		b1.addActionListener(new MyActionListener());
		recipes.add(b1);
		buildRecipe();
	}
	
	private class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			JButton source = (JButton) (e.getSource());
			System.out.println("button pressed");
		
			for (JButton r : recipes)
			{
				if (source == r)
				{
					handleButtonPress(r);
				}
			}
		}
	
		private void handleButtonPress(JButton r)
		{
			System.out.println("found the button");
			String recipeName = r.getText();
			JPanel popUp = new JPanel();
			JLabel name = new JLabel(recipeName);
			JTextArea recipeStuff = new JTextArea();
			recipeStuff.setLineWrap(true);
			recipeStuff.setWrapStyleWord(true);
			recipeStuff.setEditable(false);
			PrintStream outStream = new PrintStream(new TextAreaOutputStream(recipeStuff));
			System.setOut(outStream);
			System.setErr(outStream);
			System.out.print("Just a test!");
			//Recipe.PrintRecipe(recipeName); //Brandon is making this
			
			JScrollPane scrollPane = new JScrollPane(recipeStuff);
			
			GroupLayout layout1 = new GroupLayout(popUp);
			popUp.setLayout(layout1);
			layout1.setAutoCreateGaps(true);
			layout1.setAutoCreateContainerGaps(true);
			
			layout1.setHorizontalGroup(layout1.createParallelGroup().addComponent(name).addComponent(scrollPane));
			layout1.setVerticalGroup(layout1.createSequentialGroup().addComponent(name).addComponent(scrollPane));
			
			JOptionPane.showMessageDialog(null, 
					popUp, 
					"Recipes", 
					JOptionPane.PLAIN_MESSAGE);
			
			
			
			
		}
		
		private class TextAreaOutputStream extends OutputStream {
			private JTextArea area;
			
			public TextAreaOutputStream(JTextArea area1) {
				this.area = area1;
			}
			
			public void write(int i) throws IOException {
				area.append(String.valueOf((char)i));
				area.setCaretPosition(area.getDocument().getLength());
			}
			
			public void write(char[] c, int off, int length) throws IOException {
				area.append(new String(c, off, length)); 
				area.setCaretPosition(area.getDocument().getLength());
			}
		}
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		String userID = "123456789"; //need a way to get the real one.
		//ViewRecipe viewMealPlan = new ViewRecipe();
		//test.add(viewMealPlan);
		test.pack();
		test.setVisible(true);
	}
		
	
	
}
