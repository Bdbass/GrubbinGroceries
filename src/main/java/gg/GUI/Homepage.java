package gg.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

public class Homepage extends JPanel {

	String note;
	JTextArea text;
	JLabel welcome;
	GrubbinGUI top;
	
	public Homepage(GrubbinGUI top)
	{
		super(new FlowLayout());
		this.top = top;
		writeNote();
		buildHomepage();
	}
	
	public void writeNote()
	{
		note =
				"We are so happy to have you. To get started input food into your pantry. This will tell our super smart algorithm " +
				"what food you have so it can supply recipes that use your food! Less trips to the grocery store. Next, go ahead and create" + 
				" your first meal plan! You will tell us the number of days and meals you want accounted for. Happy meal planning and good luck in the kitchen!";
	}
	
	public void buildHomepage()
	{
		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setPreferredSize(new Dimension(600, 100));
		text.setMaximumSize(new Dimension(600, 100));
		welcome = new JLabel();
		
		text.setText(note);
		welcome.setText("<HTML><center>Welcome to Grubbin' Groceries</center></HTML>");
		
		welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 24));
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(200)
				.addComponent(welcome))
				.addGroup(layout.createSequentialGroup().addGap(100)
						.addComponent(text))
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(welcome)
				.addComponent(text)
		);
	
	}
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		Homepage homepage = new Homepage(test);
		test.add(homepage);
		test.pack();
		test.setVisible(true);
	}
	
	
	
}
