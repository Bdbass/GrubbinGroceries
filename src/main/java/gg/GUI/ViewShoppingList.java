package gg.GUI;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;
import java.awt.FlowLayout;


public class ViewShoppingList extends JPanel {
private String userID;
	
	private JButton addFood;
	private JButton deleteFood;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	
	public ViewShoppingList(String userID) {
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
		
		//ShoppingList shoppingList = new ShoppingList();
		//ShoppingList = getShoppingList(userID); //returns the pantry that belongs to this person
		//shoppingList.printShoppingList();
		
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
	
	//this will need to be ADDFOOD and DELETEFOOD
	
	public static void main(String agrs[]) {
		String userID = "1234567"; 	//need an actual userID for testing
		JFrame test = new JFrame();
		ViewShoppingList viewShoppingList = new ViewShoppingList(userID);
		test.add(viewShoppingList);
		test.pack();
		test.setVisible(true);
	}
}
