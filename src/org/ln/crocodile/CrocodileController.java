package org.ln.crocodile;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;




public class CrocodileController implements ActionListener{
	
	public CrocodileController(CrocodileView crocodileView) {
		super();
		this.crocodileView = crocodileView;
	}

//	public CController(View view2) {
//		this.view = new CFormView();
//		// TODO Auto-generated constructor stub
//	}

	private final CrocodileView crocodileView;
	
	
	void refreshTable() {
		crocodileView.getDirList().clear();
		displayDirectory(crocodileView.getSelectedDir());
		crocodileView.getModel().setData(crocodileView.getDirList());
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cerca")) {
			
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(null);

			if (returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			crocodileView.setSelectedDir(fc.getSelectedFile()); 
			crocodileView.getRootDirField().setText(crocodileView.getSelectedDir().getAbsolutePath().toLowerCase());
			crocodileView.setSearchDir(crocodileView.getSearchDirField().getText());
			
//			view.getDirList().clear();
//			displayDirectory(view.getSelectedDir());
//			view.getModel().setData(view.getDirList());
//			
			refreshTable();
			System.out.println("Directory  :" + crocodileView.getModel().getRowCount());
			crocodileView.getReportLabel().setText("Directory  :" + crocodileView.getModel().getRowCount());
			crocodileView.getSearchDirButton().setEnabled(true);
			return;
		}
		if(e.getActionCommand().equals("Refresh")) {
			crocodileView.setSearchDir(crocodileView.getSearchDirField().getText());
			
			if (crocodileView.getSearchDir() == null || crocodileView.getSearchDir().equals("")) {
				return;
			}
			
			if (crocodileView.getSelectedDir() == null) {
				return;
			}
			
			crocodileView.getDirList().clear();
			displayDirectorySearch(crocodileView.getSelectedDir());
			crocodileView.getModel().setData(crocodileView.getDirList());

			//refreshTable();
			crocodileView.getReportLabel().setText("Directory  da svuotare:" + crocodileView.getModel().getRowCount());
			crocodileView.getActionButton().setEnabled(true);
			return;
		}
		if(e.getActionCommand().equals("Go")) {
			
        	Object[] options = {"Sì", "No","Annulla"};
        	int n = JOptionPane.showOptionDialog( crocodileView, 
        			"Sei sicuro ",
        			"Conferma ", 
        			JOptionPane.YES_NO_CANCEL_OPTION,
        			JOptionPane.WARNING_MESSAGE,
        			null,
        			options,
        			options[2]);  
        	System.out.println(" Return    :" + n);
        	if(n > 0) {
        	return;
        	}
			
			List<File> list = crocodileView.getModel().getData();
			
			if (list == null || list.isEmpty() ){
				return;
			}
			delete(list);
			return;
		}
	}
	
	/**
	 * @param path
	 * @param name
	 * @param rep
	 * @return
	 */
	static String pathToRen(String path, String name, String rep) {
		String s = path.substring(0, path.length() - name.length());
		return s+rep;
	}
	
    /**
     * 
     */
    void addNewDir() {
    	File f = crocodileView.getModel().getRow(crocodileView.getSelectedRow());
    	FileNameDialog dialog = new FileNameDialog(crocodileView, "");
    	dialog.setVisible(true);
    	
    	if(dialog.getReturnStatus() == FileNameDialog.RET_CANCEL) {
    		return;
    	}
    	
    	String s = f.getAbsolutePath()+File.separator+dialog.getText();
    	File dir = new File(s);
    	dir.mkdir();
    	refreshTable();
     }
    
    /**
     * 
     */
    void renameCurrentDir() {
    	File f = crocodileView.getModel().getRow(crocodileView.getSelectedRow());
    	FileNameDialog dialog = new FileNameDialog(crocodileView, f.getName());
    	dialog.setVisible(true);
    	
    	if(dialog.getReturnStatus() == FileNameDialog.RET_CANCEL) {
    		return;
    	}
    	
    	System.out.println("path   "+File.separator+dialog.getText());
    	System.out.println(Renamer.renameDir(f, new File(pathToRen(f.getAbsolutePath(), f.getName(), dialog.getText() ))));
    	refreshTable();
    }
	
	/**
	 * @param dir
	 */
	void displayDirectory(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			// Checking of file inside directory
			if (file.isDirectory()) {
				crocodileView.getDirList().add(file);
				displayDirectory(file);
			}
		}
	}
	
	/**
	 * @param dir
	 */
	void displayDirectorySearch(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			// Checking of file inside directory
			if (file.isDirectory()) {
				if(file.getName().equals(crocodileView.getSearchDir())) {
					//System.out.println("listfile    "+file.listFiles().length);
					crocodileView.getDirList().add(file);
				}
				displayDirectorySearch(file);
			}
		}
	}
	
	/**
	 * @param list
	 */
	private void delete(List<File> list) {
		for (File file : list) {
			deleteAll(file);
			if (crocodileView.getCancelButton().isSelected()) {
				file.delete();
			}
		}
	}

	/**
	 * @param file
	 */
	private void deleteAll(File file) {
		for (File subfile : file.listFiles()) {
			if (subfile.isDirectory()) {
				deleteAll(subfile);
			}
			subfile.delete();
		}
	}
	
	/**
	 * 
	 */
	class PopupActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
	        JMenuItem menu = (JMenuItem) e.getSource();
	        if (menu == crocodileView.getMenuItemAdd()) {
	            addNewDir();
	        } else if (menu == crocodileView.getMenuItemRename()) {
	        	renameCurrentDir();
	        } 
		}
	}

	/**
	 * 
	 */
	class TableMouseListener extends MouseAdapter {

//		private JTable table;
//
//		public TableMouseListener(JTable table) {
//			this.table = table;
//		}

		@Override
		public void mousePressed(MouseEvent event) {
			Point point = event.getPoint();
			int currentRow = crocodileView.getTable().rowAtPoint(point);
			System.out.println(currentRow);
			if(currentRow > -1) {
				crocodileView.getTable().setRowSelectionInterval(currentRow, currentRow);
			}
		}
	}
	
}
