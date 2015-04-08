package gui;
import java.io.File;

import javax.swing.filechooser.FileFilter;



public class PersonFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		// the goal here is to check the file extension.
		
		if(f.isDirectory()) {
			return true;
		}
		String name = f.getName();
		
		String extension = Utils.getFileExtension(name);
		
		if(extension == null) {
			return false;
		}
		
		if(extension.equals("per")) {
			return true;
		}
		
		return false;
		
	}

	@Override
	public String getDescription() {
		return "Person database files (*.per)";
	}

}
