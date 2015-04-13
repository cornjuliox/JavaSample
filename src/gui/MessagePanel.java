package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Message;
import controller.MessageServer;

public class MessagePanel extends JPanel {
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;
	private Set<Integer> selectedServers;
	private MessageServer messageServer;
	private ProgressDialog progressDialog;

	public MessagePanel(JFrame parent) {
		selectedServers = new TreeSet<Integer>();
		messageServer = new MessageServer();
		progressDialog = new ProgressDialog(parent) ;

		selectedServers.add(1);
		selectedServers.add(5);

		serverTree = new JTree(createTree());
		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();

		serverTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.setEditable(true);

		treeCellEditor.addCellEditorListener(new CellEditorListener() {

			@Override
			public void editingStopped(ChangeEvent e) {
				// TODO Auto-generated method stub
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
		setLayout(new BorderLayout());

		add(new JScrollPane(serverTree), BorderLayout.CENTER);

	}
	
	private void retrieveMessages() {
		
		progressDialog.setMaximum(messageServer.getMessageCount());
		progressDialog.setVisible(true);
		
		SwingWorker<List<Message>, Integer> worker = new SwingWorker<List<Message>, Integer>() {

			protected void done() {
				try {
					List<Message> retrievedMessages = get();
					
					//System.out.println("Retrieved " + retrievedMessages.size() + " messages.");
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				progressDialog.setVisible(false);
				
			}
			
			@Override
			protected void process(List<Integer> counts) {
				// TODO Auto-generated method stub
				int retrieved = counts.get(counts.size() - 1);
				progressDialog.setValue(retrieved);
			}


			@Override
			protected List<Message> doInBackground() throws Exception {
				
				List<Message> retrievedMessages = new ArrayList<Message>();
				int count = 0;
				
				for(Message message: messageServer) {
					System.out.println(message.getTitle());
					
					retrievedMessages.add(message);
					
					count++;
					
					publish(count);
					
				}
				
				return null;
			}
		};
		
		worker.execute();
	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(
				new ServerInfo("New York", 1, selectedServers.contains(1)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo("Los Angeles", 2, selectedServers.contains(2)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(
				new ServerInfo("Boston", 3, selectedServers.contains(3)));
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(
				new ServerInfo("St. Louis", 4, selectedServers.contains(4)));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);
		branch1.add(server4);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(
				new ServerInfo("London", 5, selectedServers.contains(5)));
		DefaultMutableTreeNode server6 = new DefaultMutableTreeNode(
				new ServerInfo("Edinburgh", 6, selectedServers.contains(6)));

		branch2.add(server5);
		branch2.add(server6);

		top.add(branch1);
		top.add(branch2);

		return top;
	}
}
