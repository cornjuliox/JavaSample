package gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FormPanel extends JPanel {
	private JLabel nameLabel;
	private JLabel occupationLabel;
	private JTextField occupationField;
	private JTextField nameField;
	private JButton okBtn;
	private JList ageList;
	private JComboBox empCombo;
	private JCheckBox citizenCheck;
	private JLabel taxLabel;
	private JTextField taxField;
	
	private JRadioButton maleRadio;
	private JRadioButton femaleRadio;
	// necessary to use radio buttons
	// its what enables the 'mutually exclusive'
	// behavior of radio buttons.
	private ButtonGroup genderGroup;

	private FormListener formListener;

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		setMinimumSize(dim);

		// label and field creation
		nameLabel = new JLabel("Name: ");
		occupationLabel = new JLabel("Occupation: ");
		nameField = new JTextField(10);
		occupationField = new JTextField(10);
		empCombo = new JComboBox();
		citizenCheck = new JCheckBox();
		taxLabel = new JLabel("Tax ID");
		taxField = new JTextField(10);
		okBtn = new JButton("OK");
		
		nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
		nameLabel.setLabelFor(nameField);
		
		occupationLabel.setDisplayedMnemonic(KeyEvent.VK_C);
		occupationLabel.setLabelFor(occupationField);

		// mnemonics set up
		okBtn.setMnemonic(KeyEvent.VK_O);
		
		maleRadio = new JRadioButton("Male");
		femaleRadio = new JRadioButton("Female");
		genderGroup = new ButtonGroup();
		maleRadio.setSelected(true);
		maleRadio.setActionCommand("male");
		femaleRadio.setActionCommand("female");
		
		// gender radio button setup section.
		genderGroup.add(maleRadio);
		genderGroup.add(femaleRadio);

		// set up tax ID field
		taxLabel.setEnabled(false);
		taxField.setEnabled(false);

		citizenCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean isTicked = citizenCheck.isSelected();
				taxLabel.setEnabled(isTicked);
				taxField.setEnabled(isTicked);
			}
		});
		System.out.println();
		// set up combo box
		// same as before - you need a JComboBox() instance
		// and a DefaultComboBoxModel instance()
		// call addElement() method as many times as you need
		// elements, and then call setModel() on the JComboBox()
		// instance.
		DefaultComboBoxModel empModel = new DefaultComboBoxModel();
		empModel.addElement("employed");
		empModel.addElement("self-employed");
		empModel.addElement("unemployed");

		empCombo.setModel(empModel);

		// listbox instructions
		// create a JList() instance
		// create a DefaultListModel() instance
		// call the DefaultListModel's addElement() method.
		// to add elements
		ageList = new JList();

		DefaultListModel ageModel = new DefaultListModel();
		ageModel.addElement(new AgeCategory(0, "Under 18"));
		ageModel.addElement(new AgeCategory(1, "18 to 65"));
		ageModel.addElement(new AgeCategory(2, "65 and over"));
		ageList.setModel(ageModel);
		ageList.setPreferredSize(new Dimension(110, 66));
		ageList.setBorder(BorderFactory.createEtchedBorder());
		ageList.setSelectedIndex(0);

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String occupation = occupationField.getText();
				AgeCategory ageCat = (AgeCategory) ageList.getSelectedValue();
				
				String empCat = (String) empCombo.getSelectedItem();
				
				String taxId = taxField.getText();
				boolean usCitizen = citizenCheck.isSelected();
				String gender = genderGroup.getSelection().getActionCommand();

				FormEvent ev = new FormEvent(this, name, occupation, ageCat
						.getId(), empCat, taxId, usCitizen, gender);

				if (formListener != null) {
					formListener.formEventOccured(ev);
				}
			}
		});

		// all layout management code moved into its own
		// function to keep the constructor clean.
		layoutComponents();
	}

	public void setFormListener(FormListener fl) {
		this.formListener = fl;
	}

	public void layoutComponents() {
		Border innerBoarder = BorderFactory.createTitledBorder("Add Person");
		Border outerBoarder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory
				.createCompoundBorder(outerBoarder, innerBoarder));

		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 0;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;

		// ////////////// FIRST ROW
		gc.gridx = 0;
		gc.gridy = 0;
		add(nameLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		add(nameField, gc);
		// ////////////// FIRST ROW

		// ////////////// SECOND ROW
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		add(occupationLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(occupationField, gc);
		// ////////////// SECOND ROW

		// ///////////// THIRD ROW
		gc.gridx = 0;
		gc.gridy++;
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Age"), gc);

		// gc.gridy++;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(ageList, gc);
		// ///////////// THIRD ROW

		// ///////////// FOURTH ROW
		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Employment: "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(empCombo, gc);

		/////////////// FOURTH ROW

		/////////////// FIFTH ROW
		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("US Citizen? "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(citizenCheck, gc);

		/////////////// FIFTH ROW

		/////////////// SIXTH ROW
		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(taxLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(taxField, gc);

		// ///////////// SIXTH ROW
		
		/////////////// SIXTH ROW
		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Gender: "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(maleRadio, gc);

		// ///////////// SIXTH ROW
		
		/////////////// SIXTH ROW
		gc.gridy++;

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(femaleRadio, gc);
		// ///////////// SIXTH ROW

		// ///////////// LAST ROW
		gc.weightx = 1;
		gc.weighty = 2.0;
		gc.gridy++;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(okBtn, gc);
		// ///////////// THIRD ROW
	}
}

class AgeCategory {
	private int id;
	private String text;

	public AgeCategory(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public String toString() {
		return this.text;
	}

	public int getId() {
		return this.id;
	}
}
