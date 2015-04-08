package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class PrefsDialog extends JDialog {
	
	private JButton okButton;
	private JButton cancelButton;
	private JSpinner portSpinner;
	private SpinnerNumberModel spinnerModel;
	private JTextField userField;
	private JPasswordField passField;
	private PrefsListener prefsListener;
	
	public PrefsDialog(JFrame parent) {
		super(parent, "Preferences", false);
		
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		spinnerModel = new SpinnerNumberModel(3306, 0, 9999, 1);
		portSpinner = new JSpinner(spinnerModel);
		userField = new JTextField(10);
		passField = new JPasswordField(10);
		
		layoutControls();

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int value = (int)portSpinner.getValue();
				String user = userField.getText();
				// note: wrapping this in a string will
				// give you the plaintext of the password
				// printing the char[] directly gives you the
				// hash of the password.
				char[] password = passField.getPassword();
				int port = (int)portSpinner.getValue();
				
				if(prefsListener != null) {
					prefsListener.preferencesSet(user, new String(password), port);
				}
				
				setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		
		setSize(340, 280);
		setLocationRelativeTo(parent);
	}
	
	private void layoutControls() {
		// layout junk
		// i'm really disliking the gridbaglayout.
		JPanel controlsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();

		int space = 15;
		Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		Border titleBorder = BorderFactory.createTitledBorder("Database Preferences");
		
		controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		controlsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.gridy = 0;
		
		Insets rightPadding = new Insets(0, 0, 0, 15);
		Insets noPadding = new Insets(0, 0, 0, 0);
		
		/// First Row
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("User: "), gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(userField, gc);
		
		/// First Row
		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Password: "), gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(passField, gc);
		
		/// NEXT Row
		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Port: "), gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(portSpinner, gc);
		
		//buttons panel.
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		Dimension btnSize = cancelButton.getPreferredSize();
		okButton.setPreferredSize(btnSize);
		buttonsPanel.add(okButton, gc);
		buttonsPanel.add(cancelButton, gc);
		
		// now to add the panels to the dialog itself.
		setLayout(new BorderLayout());
		add(controlsPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
		
	}

	public void setDefaults(String user, String password, int port) {
		userField.setText(user);
		passField.setText(password);
		portSpinner.setValue(port);
	}
	public void setPrefsListener(PrefsListener prefsListener) {
		// TODO Auto-generated method stub
		this.prefsListener = prefsListener;
	}
}
