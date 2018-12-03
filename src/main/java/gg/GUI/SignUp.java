package gg.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;

import gg.mealInfo.ShoppingList;
import gg.physObjs.Pantry;
import gg.userInfo.Person;
import gg.userInfo.RestType;

public class SignUp extends JPanel implements ItemListener
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
	
	private ArrayList<String> restrictions;
	
	
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
		gf = new JCheckBox();
		lc = new JCheckBox();
		vegt = new JCheckBox();
		vegan = new JCheckBox();
		nut = new JCheckBox();
		
		
		usernameLabel.setText("Username:");
		passwordLabel.setText("Password:");
		verifyLabel.setText("Verify Password:");
		nameLabel.setText("Name:");
		gf.setText("Gluten Free");
		gf.addItemListener(this);
		lc.setText("Low Carb");
		lc.addItemListener(this);
		vegt.setText("Vegetarian");
		vegt.addItemListener(this);
		vegan.setText("Vegan");
		vegan.addItemListener(this);
		nut.setText("Nut Allergy");
		nut.addItemListener(this);
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
		
				
		);
		
		
		
	}
	
	
		
	public void itemStateChanged(ItemEvent e)
	{
		JCheckBox source = (JCheckBox) e.getItemSelectable();
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
			if (index == 0)
			{
				if (set)
				{
					//add the preference
					restrictions.add("GF");
					
				}
				else 
				{
					//delete the preference
					for (String r : restrictions)
					{
						if (r.equals("GF"))
						{
							restrictions.remove(r);
						}
					}
				}
			}
			else if (index == 1)
			{
				if (set)
				{
					//add the preference
					restrictions.add("LOWCARB");
					
				}
				else 
				{
					//delete the preference
					for (String r : restrictions)
					{
						if (r.equals("LOWCARB"))
						{
							restrictions.remove(r);
						}
					}
				}
			}
			else if (index == 2)
			{
				if (set)
				{
					//add the preference
					restrictions.add("VEG");
					
				}
				else 
				{
					//delete the preference
					for (String r : restrictions)
					{
						if (r.equals("VEG"))
						{
							restrictions.remove(r);
						}
					}
				}
			}
			else if (index == 3)
			{
				if (set)
				{
					//add the preference
					restrictions.add("VEGAN");
					
				}
				else 
				{
					//delete the preference
					for (String r : restrictions)
					{
						if (r.equals("VEGAN"))
						{
							restrictions.remove(r);
						}
					}
				}
			}
			else if (index == 4)
			{
				if (set)
				{
					//add the preference
					restrictions.add("NUTALRGY");
					
				}
				else 
				{
					//delete the preference
					for (String r : restrictions)
					{
						if (r.equals("NUTALRGY"))
						{
							restrictions.remove(r);
						}
					}
				}
			}
		}
		
		public void handleSignUp()
		{
			String success = Person.addPerson(name.getText(), username.getText(), password.getText(), verify.getText(), restrictions); //Brandon's new function to add a person
			top.setUserID(username.getText());
			new Pantry(top.getUserID());
			new ShoppingList(top.getUserID());
			if (success.equals("success"))
			{
				top.setUserID(username.getText());
				top.buildGUI();
				return;
			}
			else //fail
			{
				JOptionPane.showMessageDialog(null, 
						"An account is already associated with this username.", "User Exists", 
						JOptionPane.PLAIN_MESSAGE);
			}
		}
		
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		
	}

}
