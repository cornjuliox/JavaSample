package gui;

public class ServerInfo {
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