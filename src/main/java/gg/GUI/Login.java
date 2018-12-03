package gg.GUI;



import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.GroupLayout.*;

import gg.userInfo.Person;



public class Login extends JPanel
{
	
	private JLabel title;
	private JTextField username;
	private JPasswordField password;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private GrubbinGUI top;
	
	
	
	public Login(GrubbinGUI top)
	{
		super(new FlowLayout());
		this.top = top;
		buildLogin();
	}
		
	
	
	public void buildLogin()
	{
		title = new JLabel("<html><center>Log In</center></html>");
		username = new JTextField();
		password = new JPasswordField();
		usernameLabel = new JLabel();
		passwordLabel = new JLabel();

		
		usernameLabel.setText("Username:");
		passwordLabel.setText("Password:");
	
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		username.setPreferredSize(new Dimension(250, 25));
        username.setMaximumSize(new Dimension(250, 25));
        
        //username.addActionListener(new Listener());
		
		password.setPreferredSize(new Dimension(250, 25));
        password.setMaximumSize(new Dimension(250, 25));
		
        //password.addActionListener(new Listener());
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(100)
				.addComponent(title))
				.addGroup(layout.createSequentialGroup()
						.addComponent(usernameLabel)
						.addComponent(username)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(passwordLabel)
						.addComponent(password)
				)
			
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(title))
				.addGroup(layout.createParallelGroup()
						.addComponent(usernameLabel)
						.addComponent(username)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(passwordLabel)
						.addComponent(password)
				)
			
		);
		
		
		
		
		
		
		
	}
	
		
		public void handleSignUp()
		{
			Object[] options = {"Sign Up"};
			SignUp signUp = new SignUp(top);
			int option = JOptionPane.showOptionDialog(null, signUp, "Grubbin' Groceries", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			if (option == JOptionPane.OK_OPTION)
			{
				signUp.handleSignUp();
			}
		}
		
		public void handleSignIn()
		{
			String user = username.getText();
			char [] pass = password.getPassword();
			String pass1 = new String(pass);
			Boolean correct = Person.validateUser(user, pass1);
			if (!correct)
			{
				JOptionPane.showMessageDialog(null, 
						"Incorrect username or password. Please try again.", "Incorrect Username or Password", 
						JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				top.setUserID(user);
				top.buildGUI();
				return;
				
			}
		}
	
	
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		
	}
	
	
	
	
	
	
	

}
