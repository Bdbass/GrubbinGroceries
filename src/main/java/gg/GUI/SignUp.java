package gg.GUI;

import java.awt.FlowLayout;

import javax.swing.*;

public class SignUp extends JPanel
{
	
	JTextField username;
	JTextField password;
	JTextField verify;
	JTextField name;
	
	JLabel usernameLabel;
	JLabel passwordLabel;
	JLabel verifyLabel;
	JLabel nameLabel;
	JLabel title;
	
	JCheckBox gf;
	JCheckBox lc;
	JCheckBox vegt;
	JCheckBox vegan;
	JCheckBox nut;
	
	JButton signUp;
	
	public SignUp()
	{
		super(new FlowLayout());
		buildSignUp();
	}
	
	public void buildSignUp()
	{
		username = new JTextField();
		password = new JTextField();
		verify = new JTextField();
		name = new JTextField();
		usernameLabel = new JLabel();
		passwordLabel = new JLabel();
		verifyLabel = new JLabel();
		nameLabel = new JLabel();
		title = new JLabel();
		signUp = new JButton();
		gf = new JCheckBox();
		lc = new JCheckBox();
		vegt = new JCheckBox();
		vegan = new JCheckBox();
		nut = new JCheckBox();
		
		
		usernameLabel.setText("Username:");
		passwordLabel.setText("Password:");
		verifyLabel.setText("Verify Password:");
		nameLabel.setText("Name:");
		signUp.setText("Sign Up");
		gf.setText("Gluten Free");
		lc.setText("Low Carb");
		vegt.setText("Vegetarian");
		vegan.setText("Vegan");
		nut.setText("Nut Allergy");
		title.setText("Sign Up");
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(title)
				.addGroup(layout.createSequentialGroup()
						.addComponent(nameLabel)
						.addComponent(name)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(usernameLabel)
						.addComponent(username)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(passwordLabel)
						.addComponent(password)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(verifyLabel)
						.addComponent(verify)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(gf)
						.addComponent(lc)
						.addComponent(vegt)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(vegan)
						.addComponent(nut)
				)
				.addComponent(signUp)
				
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addGroup(layout.createParallelGroup()
						.addComponent(nameLabel)
						.addComponent(name)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(usernameLabel)
						.addComponent(username)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(passwordLabel)
						.addComponent(password)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(verifyLabel)
						.addComponent(verify)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(gf)
						.addComponent(lc)
						.addComponent(vegt)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(vegan)
						.addComponent(nut)
				)
				.addComponent(signUp)
				
		);
		
		
		
	}
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		SignUp signup = new SignUp();
		test.add(signup);
		test.pack();
		test.setVisible(true);
	}

}
