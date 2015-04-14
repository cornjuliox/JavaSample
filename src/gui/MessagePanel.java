package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Message;
import controller.MessageServer;

public class MessagePanel extends JPanel implements ProgressDialogListener {
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;
	private Set<Integer> selectedServers;
	private MessageServer messageServer;
	private ProgressDialog progressDialog;
	private SwingWorker<List<Message>, Integer> worker;
	private TextPanel textPanel;
	private JList messageList;
	private JSplitPane upperPane;
	private JSplitPane lowerPane;
	private DefaultListModel messageListModel;

	public MessagePanel(JFrame parent) {
		selectedServers = new TreeSet<Integer>();
		messageServer = new MessageServer();
		progressDialog = new ProgressDialog(parent, "Message Downloading...") ;
		textPanel = new TextPanel();
		messageListModel = new DefaultListModel();
		messageList = new JList(messageListModel);
		
		progressDialog.setListener(this);

		selectedServers.add(1);
		selectedServers.add(2);

		serverTree = new JTree(createTree());
		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();

		serverTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.setEditable(true);
		
		lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(messageList), textPanel);
		upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(serverTree), lowerPane);
		
		upperPane.setResizeWeight(0.5);
		lowerPane.setResizeWeight(0.5);
		
		textPanel.setMinimumSize(new Dimension(10, 100));
		messageList.setMinimumSize(new Dimension(10, 115));
		messageList.setCellRenderer(new MessageListRenderer());
		
		messageServer.setSelectedServers(selectedServers);

		treeCellEditor.addCellEditorListener(new CellEditorListener() {
			@Override
			public void editingStopped(ChangeEvent e) {
				ServerInfo info = (ServerInfo) treeCellEditor
						.getCellEditorValue();
				int serverId = info.getId();
				
				if(info.isChecked()) {
					selectedServers.add(serverId);
				} else {
					selectedServers.remove(serverId);
				}
				
				
				messageServer.setSelectedServers(selectedServers);
				
				retrieveMessages();
			}

			@Override
			public void editingCanceled(ChangeEvent e) {
				// TODO Auto-generated method stubdd

			}
		});
		
		messageList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// I'm not entirely sure why buy sometimes message ends up
				// being null. I think it has something to do with the given index
				// not being set right after the list is changed.
				// I've written a small hack to work around it but its puzzling
				// nonetheless.
				Message message = (Message)messageList.getSelectedValue();
				if(message == null) {
					messageList.setSelectedIndex(0);
				} else {
					textPanel.setText(message.getContents());
				}
			}
		});
		
		setLayout(new BorderLayout());

		add(upperPane, BorderLayout.CENTER);

	}
	
	private void retrieveMessages() {
		
		progressDialog.setMaximum(messageServer.getMessageCount());
		progressDialog.setVisible(true);
		
		worker = new SwingWorker<List<Message>, Integer>() {

			protected void done() {
				progressDialog.setVisible(false);
				if(isCancelled()) {
					return;
				}
				try {
					List<Message> retrievedMessages = get();
					messageListModel.removeAllElements();
					
					for(Message message: retrievedMessages) {
						messageListModel.addElement(message);
					}
					
					//messageList.setSelectedIndex(0);
					
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			protected void process(List<Integer> counts) {
				int retrieved = counts.get(counts.size() - 1);
				progressDialog.setValue(retrieved);
			}
			
			// how to cancel a thread properly
			// call thread.cancel()
			// use isCancelled() within processing code to check if
			// thread has been cancelled so you can move out of 
			// process gracefully.
			// Swing will NOT step in on your behalf.
			@Override
			protected List<Message> doInBackground() throws Exception {
				
				List<Message> retrievedMessages = new ArrayList<Message>();
				int count = 0;
				
				for(Message message: messageServer) {
					if(isCancelled()) {
						break;
					}
					retrievedMessages.add(message);
					count++;
					publish(count);
				}
				
				return retrievedMessages;
			}
		};
		
		worker.execute();
	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(
				new ServerInfo("New York", 0, selectedServers.contains(1)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo("Los Angeles", 1, selectedServers.contains(2)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(
				new ServerInfo("Boston", 2, selectedServers.contains(3)));
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(
				new ServerInfo("St. Louis", 3, selectedServers.contains(4)));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);
		branch1.add(server4);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(
				new ServerInfo("London", 4, selectedServers.contains(5)));

		branch2.add(server5);

		top.add(branch1);
		top.add(branch2);

		return top;
	}

	@Override
	public void progressDialogCancelled() {
		// TODO Auto-generated method stub
		if(worker != null) {
			worker.cancel(true);
		}
	}
	
	public void refresh() {
		retrieveMessages();
	}
}
