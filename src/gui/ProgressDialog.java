package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {
	
	private JButton cancelButton;
	private JProgressBar progressBar;
	private ProgressDialogListener listener;
	
	public ProgressDialog(Window parent, String title) {
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		cancelButton = new JButton("Cancel");
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		
		progressBar.setMaximum(10);
		
		progressBar.setString("I ARE DOWNLOADING");
		
		setLayout(new FlowLayout());
		
		Dimension size = cancelButton.getPreferredSize();
		size.width = 400;
		progressBar.setPreferredSize(size);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(listener != null) {
					listener.progressDialogCancelled();
				}
				
			}
		});
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	
		// overrides the cross button to call the progressDialogCancelled()
		// method. This way both clicking cancel and the cross will do the
		// same thing.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				listener.progressDialogCancelled();
			}

		});
		
		add(progressBar);
		add(cancelButton);
	
		pack();
		
		//setSize(400, 200);
		
		setLocationRelativeTo(parent);
	}
	
	public void setMaximum(int value) {
		// sets maximum value for progress bar.
		progressBar.setMaximum(value);
	}
	
	public void setValue(int value) {
		// sets current value of progress bar
		int progress = value * 100 / progressBar.getMaximum();
		progressBar.setString(String.format("%d%%", progress));
		progressBar.setValue(value);
	}
	
	public void setListener(ProgressDialogListener listener) {
		this.listener = listener;
	}

	@Override
	public void setVisible(final boolean visible) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(visible == false) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				} else {
					progressBar.setValue(0);
				}
				
				if(visible) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				ProgressDialog.super.setVisible(visible);
			}
			
		});
	}
	
	
}
