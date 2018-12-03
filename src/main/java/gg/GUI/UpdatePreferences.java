package gg.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;

import gg.userInfo.Person;
import gg.userInfo.RestType;

public class UpdatePreferences extends JPanel implements ItemListener{
	
	private JLabel title;
	
	private JCheckBox gf;
	private JCheckBox lc;
	private JCheckBox vegt;
	private JCheckBox vegan;
	private JCheckBox nut;
	
	private JButton update;
	
	private GrubbinGUI top;
	private ArrayList<RestType> restrictions;
	
	public UpdatePreferences(GrubbinGUI top)
	{
		super(new FlowLayout());
		restrictions = new ArrayList<RestType>();
		this.top = top;
		buildSignUp();
	}
	
	public void buildSignUp()
	{
		ArrayList<RestType> current = new ArrayList<RestType>();//top.getUserID().getPerson().getRestrictions(); //brandon making this
		title = new JLabel();
		update = new JButton();
		gf = new JCheckBox();
		lc = new JCheckBox();
		vegt = new JCheckBox();
		vegan = new JCheckBox();
		nut = new JCheckBox();
		
		
		update.setText("Update");
		update.addActionListener(new Listener());
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
		title.setText("Update Restrictions");
		
		/*for (RestType r : current)
		{
			if (r == RestType.GF)
			{
				gf.setSelected(true);
			}
			else if (r == RestType.LOWCARB)
			{
				lc.setSelected(true);
			}
			else if (r == RestType.VEG)
			{
				vegt.setSelected(true);
			}
			else if (r == RestType.VEGAN)
			{
				vegan.setSelected(true);
			}
			else if (r == RestType.NUTALRGY)
			{
				nut.setSelected(true);
			}
		}*/ //waiting for brandons get person function
		
		gf.setSelected(true);
		lc.setSelected(true);
		
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addGap(250)
				.addComponent(title))
				
				.addGroup(layout.createSequentialGroup()
						.addGap(200)
						.addComponent(gf)
						.addComponent(lc)
						.addComponent(vegt)
				)
				.addGroup(layout.createSequentialGroup()
						.addGap(240)
						.addComponent(vegan)
						.addComponent(nut)
				)
				.addGroup(layout.createSequentialGroup()
						.addGap(260)
						.addComponent(update)
				)
				
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(title)
				.addGroup(layout.createParallelGroup()
						.addComponent(gf)
						.addComponent(lc)
						.addComponent(vegt)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(vegan)
						.addComponent(nut)
				)
				.addComponent(update)
				
		);
		
		
		
	}
	
	private class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) //this is the method MenuListener must implement, as it comes from the ActionListener interface.
		{
			//System.out.println("Button Pressed");
			JButton source = (JButton)(e.getSource());
			
			if (source.equals(update))
			{
				//System.out.println("Button Pressed");
				handleUpdate();
			}
			
		}
		private void handleUpdate()
		{
			Person p = new Person();//top.getUserID().getPerson(); //Brandon make this
			//p.setRestrictions(restrictions, true);
			/*for (RestType r : restrictions)
			{
				if (r.equals(RestType.NUTALRGY))
				{
					System.out.println("nut");
				}
				else if (r.equals(RestType.GF))
				{
					System.out.println("gf");
				}
				else if (r.equals(RestType.VEGAN))
				{
					System.out.println("vegan");
				}
				else if (r.equals(RestType.VEG))
				{
					System.out.println("veg");
				}
				else if (r.equals(RestType.LOWCARB))
				{
					System.out.println("lc");
				}
			}*/
			
		}
	}
		public void itemStateChanged(ItemEvent e)
		{
			//System.out.println("State change");
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
				
				//System.out.println(restrictions);
				
					
				//deselect or select case
				if (index == 0)
				{
					if (set)
					{
						//add the preference
						restrictions.add(RestType.GF);
						//System.out.println(restrictions);
						
					}
					else 
					{
						//delete the preference
						//System.out.println(restrictions);
						for (RestType r : restrictions)
						{
							if (r == RestType.GF)
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
						restrictions.add(RestType.LOWCARB);
						
					}
					else 
					{
						//delete the preference
						for (RestType r : restrictions)
						{
							if (r.equals(RestType.LOWCARB))
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
						restrictions.add(RestType.VEG);
						
					}
					else 
					{
						//delete the preference
						for (RestType r : restrictions)
						{
							if (r.equals(RestType.VEG))
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
						restrictions.add(RestType.VEGAN);
						
					}
					else 
					{
						//delete the preference
						for (RestType r : restrictions)
						{
							if (r.equals(RestType.VEGAN))
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
						restrictions.add(RestType.NUTALRGY);
						
					}
					else 
					{
						//delete the preference
						for (RestType r : restrictions)
						{
							if (r.equals(RestType.NUTALRGY))
							{
								restrictions.remove(r);
							}
						}
					}
					
				}
				
			}
			
	
	
	
	
	public static void main(String args[])
	{
		GrubbinGUI test = new GrubbinGUI();
		UpdatePreferences update = new UpdatePreferences(test);
		test.add(update);
		test.pack();
		test.setVisible(true);
	}


}
