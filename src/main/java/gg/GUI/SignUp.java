package gg.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.*;

public class SignUp extends JPanel
{
	
	private GrubbinGUI top;
	
	private JTextField username;
	private JTextField password;
	private JTextField verify;
	private JTextField name;
	
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel verifyLabel;
	private JLabel nameLabel;
	private JLabel title;
	
	private JCheckBox gf;
	private JCheckBox lc;
	private JCheckBox vegt;
	private JCheckBox vegan;
	private JCheckBox nut;
	
	private JButton signUp;
	
	public SignUp(GrubbinGUI top)
	{
		super(new FlowLayout());
		this.top = top;
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
		signUp.addActionListener(new Listener());
		gf.setText("Gluten Free");
		//gf.addItemListener();
		lc.setText("Low Carb");
		vegt.setText("Vegetarian");
		vegan.setText("Vegan");
		nut.setText("Nut Allergy");
		title.setText("Sign Up");
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		username.setPreferredSize(new Dimension(250, 25));
        username.setMaximumSize(new Dimension(250, 25));
		
		password.setPreferredSize(new Dimension(250, 25));
        password.setMaximumSize(new Dimension(250, 25));
		
        verify.setPreferredSize(new Dimension(250, 25));
        verify.setMaximumSize(new Dimension(250, 25));
        
        name.setPreferredSize(new Dimension(250, 25));
        name.setMaximumSize(new Dimension(250, 25));
		
		
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(100)
				.addComponent(title))
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
	
	private class Listener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) //this is the method MenuListener must implement, as it comes from the ActionListener interface.
		{
			JButton source = (JButton)(e.getSource());
			
			if (source.equals(signUp))
			{
				handleSignUp();
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
		
		private void handleSignUp()
		{
			
		}
		
	}
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		SignUp signup = new SignUp(test);
		test.add(signup);
		test.pack();
		test.setVisible(true);
	}

}
