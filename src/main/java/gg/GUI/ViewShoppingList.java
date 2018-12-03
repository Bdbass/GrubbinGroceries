package gg.GUI;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;

import gg.mealInfo.ShoppingList;
import gg.physObjs.Pantry;

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
		addFood.addActionListener(this);
		deleteFood.addActionListener(this);
		
		textArea = new JTextArea(25, 30);
		textArea.setEditable(false);
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		textArea = refreshPage();
		
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

	private JTextArea refreshPage() {
		this.shoppingList = ShoppingList.findShoppingList(userID); //returns the pantry that belongs to this person
		this.textArea.setText(this.shoppingList.printShoppingList());
		return textArea;
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
			textArea = refreshPage();
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
			textArea = refreshPage();
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
