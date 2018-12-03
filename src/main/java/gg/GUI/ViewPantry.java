package gg.GUI;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import gg.physObjs.*;	//Pantry class
import gg.userInfo.*;	//Person class

public class ViewPantry extends JPanel implements ActionListener
{
	private String userID;
	private GrubbinGUI top;
	
	private JButton addFood;
	private JButton deleteFood;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel title;
	private Pantry pantry;
	
	
	public ViewPantry(String userID, GrubbinGUI top) {
		super(new FlowLayout());
		this.userID = userID;
		this.top = top;
		this.pantry = new Pantry();
		buildViewPantry();
	}
	
	private void buildViewPantry() {
		title = new JLabel("Pantry");
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
		this.pantry = Pantry.findPantry(userID); //returns the pantry that belongs to this person
		String s = this.pantry.printPantry(); 
		this.textArea.setText(s);
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
			pantry.addFood(foodName.getText(), Double.parseDouble(amount.getText()), true);
			this.textArea = refreshPage();
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
			pantry.removeFood(foodName.getText(), Double.parseDouble(amount.getText()));
			this.textArea = refreshPage();
		}
	}
	//End of Button Actions
	
	public static void main(String agrs[]) {
		String userID = "bdbass@email.arizona.edu"; 	//need an actual userID for testing
		GrubbinGUI test = new GrubbinGUI();
		ViewPantry viewPantry = new ViewPantry(userID, test);
		test.add(viewPantry);
		test.pack();
		test.setVisible(true);
	}
}
