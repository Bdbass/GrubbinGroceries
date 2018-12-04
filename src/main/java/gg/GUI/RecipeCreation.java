package gg.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import gg.APIs.SearchFood;
import gg.userInfo.RestType;

public class RecipeCreation extends JPanel implements ItemListener
{
	private JLabel title;
	private JTextArea description;
	private JFileChooser fileChooser;
	private JButton choose;
	private JButton confirm;
	private GrubbinGUI top;
	private ViewRecipe recipes;
	
	private JCheckBox gf;
	private JCheckBox lc;
	private JCheckBox vegt;
	private JCheckBox vegan;
	private JCheckBox nut;
	
	private ButtonGroup type;
	private JRadioButton breakfast;
	private JRadioButton lunch;
	private JRadioButton dinner;
	
	private ArrayList<String> restrictions;
	private String mealType;
	private File selectedFile;
	
	
	
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
		description = new JTextArea();
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		description.setMaximumSize(new Dimension(600,100));
		description.setText("Upload text file formatted with the recipe name on the first line and the recipe description on a next line. After that," 
				+ " put the word ingredients on a line followed by all the ingredients on individual lines. Next, put the word instructions on a line followed by all the "
				+ "instructions for the recipe, each step on its own line!");
		choose = new JButton();
		choose.setText("Select a file");
		choose.addActionListener(new Listener());
		confirm = new JButton();
		confirm.setText("Confirm");
		confirm.addActionListener(new Listener());
		
		gf = new JCheckBox();
		lc = new JCheckBox();
		vegt = new JCheckBox();
		vegan = new JCheckBox();
		nut = new JCheckBox();
		
		breakfast = new JRadioButton();
		lunch = new JRadioButton();
		dinner = new JRadioButton();
		type = new ButtonGroup();
		
		gf.setText("Gluten Free");
		gf.addItemListener(this);
		lc.setText("Low Carb");
		lc.addItemListener(this);
		vegt.setText("Vegetarian");
		vegt.addItemListener(this);
		vegan.setText("Vegan");
		vegan.addItemListener(this);
		nut.setText("Nut Allergy");
		nut.addItemListener(this);
		
		type.add(breakfast);
		type.add(lunch);
		type.add(dinner);
		
		breakfast.setText("Breakfast");
		breakfast.addActionListener(new Listener());
		lunch.setText("Lunch");
		lunch.addActionListener(new Listener());
		dinner.setText("Dinner");
		dinner.addActionListener(new Listener());
		
		
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(275)
				.addComponent(title))
				.addGroup(layout.createSequentialGroup().addGap(100)
				.addComponent(description))
				.addGroup(layout.createSequentialGroup().addGap(300)
				.addComponent(choose).addComponent(breakfast).addComponent(lunch).addComponent(dinner))
				.addGroup(layout.createSequentialGroup().addGap(300)
						.addComponent(gf).addComponent(lc).addComponent(vegt).addComponent(vegan).addComponent(nut))
				.addGroup(layout.createSequentialGroup().addGap(300)
						.addComponent(confirm))
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addComponent(description)
				.addGroup(layout.createParallelGroup()
						.addComponent(choose).addComponent(breakfast).addComponent(lunch).addComponent(dinner))
				.addGroup(layout.createParallelGroup()
						.addComponent(gf).addComponent(lc).addComponent(vegt).addComponent(vegan).addComponent(nut))
				.addGroup(layout.createParallelGroup()
						.addComponent(confirm))
				);
		
		
		
	}
	

	
	private class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) //this is the method MenuListener must implement, as it comes from the ActionListener interface.
		{
				JButton source = new JButton();
				String source1 = new String();
			if (e.getSource() == confirm || e.getSource() == choose)
			{
				source = (JButton)(e.getSource());
			}
			else 
			{
				source1 = (e.getActionCommand());
			}
			
			if (source.equals(choose))
			{
				handleChoose();
			}
			else if (source1.equals("Breakfast"))
			{
				mealType = "BREAKFAST";
			}
			else if (source1.equals("Lunch"))
			{
				mealType = "LUNCH";
			}
			else if (source1.equals("Dinner"))
			{
				mealType = "DINNER";
			}
			else if (source.equals(confirm))
			{
				handleConfirm();
			}
		}
		
		private void handleConfirm()
		{
			String format = new String();
			try {
				format = SearchFood.parseRecipe(selectedFile.getAbsolutePath(), restrictions, mealType);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (format.equals("success"))
			{
				JOptionPane.showMessageDialog(null, 
						"Recipe successfully added recipe.", "Recipe Added", 
						JOptionPane.PLAIN_MESSAGE);
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, 
						"Wrong file format. Please try again.", "Incorrect File Format", 
						JOptionPane.PLAIN_MESSAGE);
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
				selectedFile = fileChooser.getSelectedFile();
				
			}
			
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		//System.out.println("State change");
		JCheckBox source = (JCheckBox) e.getItemSelectable();
		int index = -1; //0 = gf 1=lc 2=vegt 3=vegan 4 = nut
		if (source.equals(gf))
		{
			index = 0;
		}
		else if (source.equals(lc))
		{
			index = 1;
		}
		else if (source.equals(vegt))
		{				
			index = 2;
		}
		else if (source.equals(vegan))
		{
			index = 3;
		}
		else if (source.equals(nut))
		{				
			index = 4;
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
		if (restrictions == null)
		{
			restrictions = new ArrayList<String>();
		}
		//deselect or select case
		if (index == 0)
		{
			if (set)
			{
				//add the preference
				restrictions.add("GF");
				//System.out.println(restrictions);
				
			}
			else 
			{
				//delete the preference
				//System.out.println(restrictions);
				for (String r : restrictions)
				{
					if (r.equals("GF"))
					{
						restrictions.remove(r);
					}
				}
			}
		}
		else if (index == 1)
		{
			if (set)
			{
				//add the preference
				restrictions.add("LOWCARB");
				
			}
			else 
			{
				//delete the preference
				for (String r : restrictions)
				{
					if (r.equals("LOWCARB"))
					{
						restrictions.remove(r);
					}
				}
			}
		}
		else if (index == 2)
		{
			if (set)
			{
				//add the preference
				restrictions.add("VEG");
				
			}
			else 
			{
				//delete the preference
				for (String r : restrictions)
				{
					if (r.equals("VEG"))
					{
						restrictions.remove(r);
					}
				}
			}
		}
		else if (index == 3)
		{
			if (set)
			{
				//add the preference
				restrictions.add("VEGAN");
				
			}
			else 
			{
				//delete the preference
				for (String r : restrictions)
				{
					if (r.equals("VEGAN"))
					{
						restrictions.remove(r);
					}
				}
			}
		}
		else if (index == 4)
		{
			if (set)
			{
				//add the preference
				restrictions.add("NUTALRGY");
				
			}
			else 
			{
				//delete the preference
				for (String r : restrictions)
				{
					if (r.equals("NUTALRGY"))
					{
						restrictions.remove(r);
					}
				}
			}
			
		}
		
	}
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		
	}
	
}
