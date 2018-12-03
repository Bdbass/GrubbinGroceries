package gg.GUI;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;

import gg.mealInfo.ShoppingList;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ViewShoppingList extends JPanel implements ActionListener{
private String userID;
	
	private GrubbinGUI top;
	private JButton addFood;
	private JButton deleteFood;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel title;
	private ShoppingList shoppingList;
	
	
	public ViewShoppingList(String userID, GrubbinGUI top) {
		super(new FlowLayout());
		this.userID = userID;
		this.top = top;
		this.shoppingList = new ShoppingList();
		buildViewPantry();
	}
	
	private void buildViewPantry() {
		title = new JLabel("Shopping List");
		addFood = new JButton();
		deleteFood = new JButton();
		addFood.setText("Add Food");
		deleteFood.setText("Delete Food");
		
		textArea = new JTextArea(25, 30);
		textArea.setEditable(false);
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		shoppingList = ShoppingList.findShoppingList(userID); //returns the shoppingList that belongs to this person
		String s = shoppingList.printShoppingList();

		
		PrintStream outStream = new PrintStream(new TextAreaOutputStream(textArea));
		System.setOut(outStream);
		System.setErr(outStream);
		
		System.out.println(s);
		
		scrollPane = new JScrollPane(textArea);
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(275)
						.addComponent(title))
				.addComponent(scrollPane)
				.addGroup(layout.createSequentialGroup()
						.addComponent(addFood)
						.addComponent(deleteFood)
				)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
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
	
	//Start of Button Actions
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
		JTextField foodName = new JTextField();
		JTextField amount = new JTextField();
			
		Object[] message = {
			    "Food:", foodName,
			    "Amount:" , amount
		};
			
		int option = JOptionPane.showConfirmDialog(null, message, "Add Food", JOptionPane.OK_CANCEL_OPTION);
			
		if (option == JOptionPane.OK_OPTION) {
			shoppingList.addFood(foodName.getText(), Double.parseDouble(amount.getText())); 
		}
	}
		
	private void handleDeleteFood() {
		JTextField foodName = new JTextField();
		JTextField amount = new JTextField();
			
		Object[] message = {
			    "Food:", foodName,
			    "Amount:" , amount
		};
			
		int option = JOptionPane.showConfirmDialog(null, message, "Delete Food", JOptionPane.OK_CANCEL_OPTION);
			
		if (option == JOptionPane.OK_OPTION) {
			shoppingList.removeFood(foodName.getText(), Double.parseDouble(amount.getText()));
		}
	}
	//End of Button Actions
	
	
	
	public static void main(String agrs[]) {
		String userID = "bdbass@email.arizona.edu";
		GrubbinGUI test = new GrubbinGUI();
		ViewShoppingList viewShoppingList = new ViewShoppingList(userID, test);
		test.add(viewShoppingList);
		test.pack();
		test.setVisible(true);
	}
}
