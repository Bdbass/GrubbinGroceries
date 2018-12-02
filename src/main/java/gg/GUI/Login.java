package gg.GUI;



import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.GroupLayout.*;



public class Login extends JPanel
{
	
	JLabel title;
	JTextField username;
	JTextField password;
	JLabel usernameLabel;
	JLabel passwordLabel;
	JButton signUp;
	JButton signIn;
	
	
	
	public Login()
	{
		super(new FlowLayout());
		buildLogin();
	}
		
	
	
	public void buildLogin()
	{
		title = new JLabel("<html><center>Log In</center></html>");
		username = new JTextField();
		password = new JTextField();
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
		
		password.setPreferredSize(new Dimension(250, 25));
        password.setMaximumSize(new Dimension(250, 25));
		
		
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
			
		}
		
		private void handleSignIn()
		{
			
		}
	}
	
	
	public static void main(String args[])
	{
		JFrame test = new JFrame();
		Login login = new Login();
		test.add(login);
		test.pack();
		test.setVisible(true);
	}
	
	
	
	
	
	
	

}
