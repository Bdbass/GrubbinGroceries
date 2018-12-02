package gg.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.*;

public class UpdatePreferences extends JPanel {
	
	JLabel title;
	
	JCheckBox gf;
	JCheckBox lc;
	JCheckBox vegt;
	JCheckBox vegan;
	JCheckBox nut;
	
	JButton update;
	
	GrubbinGUI top;
	
	public UpdatePreferences(GrubbinGUI top)
	{
		super(new FlowLayout());
		this.top = top;
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
		title.setText("Update Restrictions");
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(75)
				.addComponent(title))
				
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
	
	private class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) //this is the method MenuListener must implement, as it comes from the ActionListener interface.
		{
			JButton source = (JButton)(e.getSource());
			
			if (source.equals(update))
			{
				handleUpdate();
			}
			
		}
		
		public void itemStateChanged(ItemEvent e)
		{
			JCheckBox source = (JCheckBox)(e.getSource());
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
			//deselect or select case
		}
		
		private void handleUpdate()
		{
			
		}
	}
	
	
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		UpdatePreferences update = new UpdatePreferences(test);
		test.add(update);
		test.pack();
		test.setVisible(true);
	}


}
