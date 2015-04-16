package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;


public class Toolbar extends JToolBar implements ActionListener{
	private JButton saveButton;
	private JButton refreshButton;
	private TextPanel textarea;
	
	private ToolbarListener textListener;
	
	public Toolbar() {
		saveButton = new JButton();
		refreshButton = new JButton();
		
		saveButton.setIcon(Utils.createIcon("/images/Save16.gif"));
		refreshButton.setIcon(Utils.createIcon("/images/Refresh16.gif"));
		
		saveButton.setToolTipText("Save");
		refreshButton.setToolTipText("Refresh");
		
		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);
		
		//setLayout(new FlowLayout(FlowLayout.LEFT));
		add(saveButton);
		add(refreshButton);
	}

	public void setToolbarListener(ToolbarListener listener) {
		this.textListener = listener;
	}

	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();
		if(clicked == saveButton) {
			// it is important to note that at this point
			// textEmitted() has been overridden
			// by a reference to the new StringListener interface
			// passed in by setStringListener().
			if(textListener != null) {
				textListener.saveEventOccurred();
			}
		} 
		else if(clicked == refreshButton) {
			if(textListener != null) {
				textListener.refreshEventOccurred();
			}
		}
	}
	
}
