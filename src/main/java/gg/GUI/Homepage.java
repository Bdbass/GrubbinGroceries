package gg.GUI;

import java.awt.FlowLayout;

import javax.swing.*;

public class Homepage extends JPanel {

	String note;
	JTextArea text;
	JLabel welcome;
	
	public Homepage()
	{
		super(new FlowLayout());
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
		text = new JTextArea(20,10);
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		welcome = new JLabel();
		
		text.setText(note);
		welcome.setText("<HTML><center>Welcome to Grubbin' Groceries</center></HTML>");
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(welcome)
				.addComponent(text)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(welcome)
				.addComponent(text)
		);
	
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		Homepage homepage = new Homepage();
		test.add(homepage);
		test.pack();
		test.setVisible(true);
	}
	
	
	
}
