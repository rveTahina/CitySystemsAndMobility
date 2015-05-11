package repastcity3.main;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.JTextField;

import repast.simphony.engine.environment.GUIRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.visualization.IDisplay;

import java.io.FileNotFoundException;

/**
 * Custom Controls and Displays Demo Advance Repast Course 2013
 * 
 * Window builder panel.
 * 
 * @author jozik
 *
 */
public class WBPanel extends JPanel {
	private JLabel lbl1;
	private JLabel lbl2;
	private JTextField txtTextPanel;
	private JButton btnPressMe;
	int param;

	/**
	 * Create the panel.
	 */
	public WBPanel() {
		
		btnPressMe = new JButton("Export into KML");
		btnPressMe.setBounds(5, 100, 200, 25);
		
		//addActionListener for the button
		btnPressMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//lblHello.setText("Thanks!");
				//Context context = RepastEssentials.FindContext("UserPanelProject");

				//On click on the button, export the simulation into KML
				int a = Integer.parseInt(txtTextPanel.getText());
			
				intoKml exportSimulation=new intoKml(a);
				try {
					exportSimulation.go();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



				GUIRegistry guiRegistry = RunState.getInstance().getGUIRegistry();
				for (IDisplay display : guiRegistry.getDisplays()){
					display.update();
					display.render();
				}
			}
		});
		setLayout(null);

		lbl1 = new JLabel("Time stamp:");
		lbl1.setBounds(5, 50, 75, 16);
		add(lbl1);
		add(btnPressMe);
		
		lbl2 = new JLabel("ticks");
		lbl2.setBounds(130, 50, 50, 16);
		add(lbl2);
		

		txtTextPanel = new JTextField();
		txtTextPanel.setBounds(80, 45, 40, 28);
		txtTextPanel.setText("1");
		//txtTextPanel.setColumns(2);
		add(txtTextPanel);
		
		param=Integer.parseInt(txtTextPanel.getText());		
		addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorRemoved(AncestorEvent event) {
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				Container ancestor = event.getAncestor();
				ancestor.revalidate();
				ancestor.repaint();
			}
		});
	}

	/*protected JLabel getLbl1() {
		return lbl1;
	}*/
	
	protected JTextField getTextPanel(){
	return txtTextPanel;
	}
	
	public JButton getBtnPressMe() {
		return btnPressMe;
	}
	
	public int getpasdeTemps(){
		return param;
	}
}
