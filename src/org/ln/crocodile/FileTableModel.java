package org.ln.crocodile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTableModel extends ListTableModel<File> {

	private static final long serialVersionUID = 1745016302978345760L;
	
	

	public FileTableModel() {
		super();
		List<String> colum = new ArrayList<String>();
		colum.add("Directory");
		colum.add("Vuota");
		setColumnNames(colum);
	}



	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return data.get(row).getAbsolutePath();
		case 1:
			return data.get(row).listFiles().length > 0 ? "No": "Si";
		default:
			return data.get(row).getAbsolutePath();
		}
	}

}
