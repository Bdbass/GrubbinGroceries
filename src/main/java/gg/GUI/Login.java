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
	private JButton signUp;
	private JButton signIn;
	private String userID;
	private GrubbinGUI top;
	
	
	
	public Login(GrubbinGUI top)
	{
		super(new FlowLayout());
		userID = new String("unknown");
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
		signUp = new JButton();
		signIn = new JButton();
		
		usernameLabel.setText("Username:");
		passwordLabel.setText("Password");
		signUp.setText("Sign Up");
		signIn.setText("Sign in");
		signUp.addActionListener(new Listener());
		signIn.addActionListener(new Listener());
		
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
				.addGroup(layout.createSequentialGroup()
						.addComponent(signIn)
						.addComponent(signUp)
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
				.addGroup(layout.createParallelGroup()
						.addComponent(signIn)
						.addComponent(signUp)
				)
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
			else if (source.equals(signIn))
			{
				handleSignIn();
			}
			
		}
		
		private void handleSignUp()
		{
			top.switchToSignUp();
		}
		
		private void handleSignIn()
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
				userID = user;
				top.switchToHome();
				
			}
		}
	}
	
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		
	}
	
	
	
	
	
	
	

}
