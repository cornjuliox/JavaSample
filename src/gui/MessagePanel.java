package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

class ServerInfo {
	private String name;
	private int id;
	private boolean checked;
	
	public ServerInfo(String name, int id, boolean checked) {
		this.name = name;
		this.id = id;
		this.checked = checked;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return this.name;
	}
	
	public boolean isChecked() {
		return this.checked;
	}
	
	public void setChecked(boolean check) {
		checked = check;
	}
}

public class MessagePanel extends JPanel {
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;

	public MessagePanel() {
		serverTree = new JTree(createTree());
		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();
		
		serverTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.setEditable(true);

		serverTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)serverTree.getLastSelectedPathComponent();
				Object userObject = node.getUserObject();
				if(userObject instanceof ServerInfo) {
					System.out.println(userObject);
				}
			}
			
		});
		
		setLayout(new BorderLayout());

		add(new JScrollPane(serverTree), BorderLayout.CENTER);

	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York", 1, true));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo("Los Angeles", 2, false));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Boston", 3, false));
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("St. Louis", 4, false));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);
		branch1.add(server4);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("London", 5, true));
		DefaultMutableTreeNode server6 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 6, false));

		branch2.add(server5);
		branch2.add(server6);

		top.add(branch1);
		top.add(branch2);

		return top;
	}
}
