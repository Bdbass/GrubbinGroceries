package gg.GUI;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import gg.physObjs.*;	//Pantry class
import gg.userInfo.*;	//Person class

public class ViewPantry extends JPanel
{
	private String userID;
	
	private JButton addFood;
	private JButton deleteFood;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	
	public ViewPantry(String userID) {
		super(new FlowLayout());
		this.userID = userID;
		buildViewPantry();
	}
	
	private void buildViewPantry() {
		addFood = new JButton();
		deleteFood = new JButton();
		addFood.setText("Add Food");
		deleteFood.setText("Delete Food");
		
		textArea = new JTextArea(25, 30);
		textArea.setEditable(false);
		
		//PrintStream outStream = new PrintStream(new TextAreaOutputStream(textArea));
		//System.setOut(outStream);
		//System.setErr(outStream);
		
		//Pantry pantry = new Pantry();
		//pantry = getPantry(userID); //returns the pantry that belongs to this person
		//pantry.printPantry();
		
		scrollPane = new JScrollPane(textArea);
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(scrollPane)
				.addGroup(layout.createSequentialGroup()
						.addComponent(addFood)
						.addComponent(deleteFood)
				)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addGroup(layout.createParallelGroup()
						.addComponent(addFood)
						.addComponent(deleteFood)
				)
		);
		
		
		
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
	
	private class Listener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			JButton source = (JButton)(e.getSource());
			
			if(source.equals(addFood)) {
				handleAddFood();
			}
			else if (source.equals(deleteFood)) {
				handleDeleteFood();
			}
		}
		
		private void handleAddFood() {
			//addFood pop up
		}
		
		private void handleDeleteFood() {
			//deletefood pop up
		}
	}
	

	
	public static void main(String agrs[]) {
		String userID = "1234567"; 	//need an actual userID for testing
		JFrame test = new JFrame();
		ViewPantry viewPantry = new ViewPantry(userID);
		test.add(viewPantry);
		test.pack();
		test.setVisible(true);
	}
}
