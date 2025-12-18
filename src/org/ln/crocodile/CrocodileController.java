package org.ln.crocodile;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.ln.crocodile.util.DirectoryUtils;




public class CrocodileController implements ActionListener{
	
	private final CrocodileView crocodileView;
	
    private static final Preferences prefs = Preferences.userRoot().node("Crocodile");
    private static final String LAST_DIR_KEY = "lastDir";

	
	public CrocodileController(CrocodileView crocodileView) {
		super();
		this.crocodileView = crocodileView;
	}

	
	void refreshTable() {
		crocodileView.getDirList().clear();
		displayDirectory(crocodileView.getSelectedDir());
		crocodileView.getModel().setData(crocodileView.getDirList());
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cerca")) {
			
			String lastPath = prefs.get(LAST_DIR_KEY, null);
			
			JFileChooser fc = (lastPath != null)
                    ? new JFileChooser(new File(lastPath))
                    : new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(null);

			if (returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			crocodileView.setSelectedDir(fc.getSelectedFile()); 
			crocodileView.getRootDirField().setText(crocodileView.getSelectedDir().getAbsolutePath().toLowerCase());
			crocodileView.setSearchDir(crocodileView.getSearchDirField().getText());
			
			
			refreshTable();
			crocodileView.getReportLabel().setText("Directory  :" + crocodileView.getModel().getRowCount());
			crocodileView.getSearchDirButton().setEnabled(true);
			prefs.put(LAST_DIR_KEY, fc.getSelectedFile().getAbsolutePath());
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

			crocodileView.getReportLabel().setText("Directory  da svuotare:" + crocodileView.getModel().getRowCount());
			crocodileView.getActionButton().setEnabled(true);
			return;
		}
		if (e.getActionCommand().equals("Go")) {

		    Object[] options = {"Sì", "No", "Annulla"};
		    int n = JOptionPane.showOptionDialog(
		            crocodileView,
		            "Sei sicuro di procedere?",
		            "Conferma",
		            JOptionPane.YES_NO_CANCEL_OPTION,
		            JOptionPane.WARNING_MESSAGE,
		            null,
		            options,
		            options[2]
		    );

		    if (n > 0) {
		        return;
		    }

		    List<File> list = crocodileView.getModel().getData();

		    // Caso 1: directory selezionate in tabella
		    if (list != null && !list.isEmpty()) {
		        delete(list);
		        return;
		    }

		    // Caso 2: operazione globale per nome directory
		    processAllDirectoriesByName();
		    return;
		}

//		if(e.getActionCommand().equals("Go")) {
//			
//        	Object[] options = {"Sì", "No","Annulla"};
//        	int n = JOptionPane.showOptionDialog( crocodileView, 
//        			"Sei sicuro ",
//        			"Conferma ", 
//        			JOptionPane.YES_NO_CANCEL_OPTION,
//        			JOptionPane.WARNING_MESSAGE,
//        			null,
//        			options,
//        			options[2]);  
//        	System.out.println(" Return    :" + n);
//        	if(n > 0) {
//        	return;
//        	}
//			
//			List<File> list = crocodileView.getModel().getData();
//			
//			if (list == null || list.isEmpty() ){
//				return;
//			}
//			delete(list);
//			return;
//		}
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
					crocodileView.getDirList().add(file);
				}
				displayDirectorySearch(file);
			}
		}
	}
	
	/**
	 * Move files and/or directories from the selected directory
	 * to a user-chosen destination.
	 */
	void moveFilesFromSelectedDir() {

	    // 1) Verifica selezione
	    int row = crocodileView.getSelectedRow();
	    if (row < 0) {
	        JOptionPane.showMessageDialog(
	                crocodileView,
	                "Seleziona una directory dalla tabella.",
	                "Nessuna selezione",
	                JOptionPane.WARNING_MESSAGE
	        );
	        return;
	    }

	    File sourceDir = crocodileView.getModel().getRow(row);

	    // 2) Scelta modalità spostamento
	    Object[] modeOptions = {
	            "Solo file",
	            "Solo directory",
	            "File + directory"
	    };

	    int modeChoice = JOptionPane.showOptionDialog(
	            crocodileView,
	            "Cosa vuoi spostare?",
	            "Modalità spostamento",
	            JOptionPane.DEFAULT_OPTION,
	            JOptionPane.QUESTION_MESSAGE,
	            null,
	            modeOptions,
	            modeOptions[0]
	    );

	    if (modeChoice < 0) {
	        return;
	    }

	    //enum MoveMode { FILES_ONLY, DIRS_ONLY, FILES_AND_DIRS }

	    FileMode mode = switch (modeChoice) {
	        case 0 -> FileMode.FILES_ONLY;
	        case 1 -> FileMode.DIRS_ONLY;
	        case 2 -> FileMode.FILES_AND_DIRS;
	        default -> throw new IllegalStateException("Unexpected value: " + modeChoice);
	    };

	    // 3) Selezione directory di destinazione
	    JFileChooser fc = new JFileChooser(sourceDir);
	    fc.setDialogTitle("Seleziona directory di destinazione");
	    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	    int ret = fc.showOpenDialog(crocodileView);
	    if (ret != JFileChooser.APPROVE_OPTION) {
	        return;
	    }

	    File targetDir = fc.getSelectedFile();

	    // 4) Controlli di sicurezza
	    if (sourceDir.equals(targetDir)) {
	        JOptionPane.showMessageDialog(
	                crocodileView,
	                "La directory di origine e destinazione coincidono.",
	                "Destinazione non valida",
	                JOptionPane.WARNING_MESSAGE
	        );
	        return;
	    }

	    // 5) Conferma finale
	    String message =
	            "Origine:\n" + sourceDir.getAbsolutePath() +
	            "\n\nDestinazione:\n" + targetDir.getAbsolutePath() +
	            "\n\nModalità: " + modeOptions[modeChoice] +
	            "\n\nConfermi lo spostamento?";

	    int confirm = JOptionPane.showConfirmDialog(
	            crocodileView,
	            message,
	            "Conferma spostamento",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.WARNING_MESSAGE
	    );

	    if (confirm != JOptionPane.YES_OPTION) {
	        return;
	    }

	    // 6) Esecuzione
	    try {
	        Path src = sourceDir.toPath();
	        Path dst = targetDir.toPath();

	        switch (mode) {
	            case FILES_ONLY ->
	                DirectoryUtils.moveFiles(src, dst);

	            case DIRS_ONLY ->
	                DirectoryUtils.moveDirectories(src, dst);

	            case FILES_AND_DIRS ->
	                DirectoryUtils.moveAll(src, dst);
	        }

	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(
	                crocodileView,
	                "Errore durante lo spostamento:\n" + ex.getMessage(),
	                "Errore",
	                JOptionPane.ERROR_MESSAGE
	        );
	        return;
	    }

	    // 7) Refresh UI
	    refreshTable();
	    crocodileView.getReportLabel().setText("Spostamento completato");
	}


	
	/**
	 * Empty or delete all directories with the searched name under selected root.
	 */
	private void processAllDirectoriesByName() {

	    File root = crocodileView.getSelectedDir();
	    String dirName = crocodileView.getSearchDir();

	    if (root == null || dirName == null || dirName.isBlank()) {
	        return;
	    }

	    Path rootPath = root.toPath();

	    boolean deleteDir = crocodileView.getCancelButton().isSelected(); // Cancella
	    boolean emptyDir  = crocodileView.getEmptyButton().isSelected();  // Svuota

	    try {
	        if (deleteDir) {
	            DirectoryUtils.deleteAllDirectoriesNamed(rootPath, dirName);
	        } else if (emptyDir) {
	            DirectoryUtils.emptyAllDirectoriesNamed(rootPath, dirName);
	        }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(
	                crocodileView,
	                "Errore durante l'operazione:\n" + ex.getMessage(),
	                "Errore",
	                JOptionPane.ERROR_MESSAGE
	        );
	        return;
	    }

	    refreshTable();
	    crocodileView.getReportLabel().setText(
	            (deleteDir ? "Cancellate" : "Svuotate") +
	            " tutte le directory con nome: " + dirName
	    );
	}

	
	/**
	 * Executes delete or empty operation based on selected radio button.
	 */
	private void delete(List<File> list) {

	    boolean deleteDir = crocodileView.getCancelButton().isSelected(); // Cancella
	    boolean emptyDir  = crocodileView.getEmptyButton().isSelected();  // Svuota

	    for (File file : list) {
	        Path dir = file.toPath();

	        try {
	            if (deleteDir) {
	                DirectoryUtils.deleteDirectoryRecursively(dir);
	            } else if (emptyDir) {
	                DirectoryUtils.emptyDirectory(dir);
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(
	                    crocodileView,
	                    "Errore su directory:\n" + dir + "\n\n" + ex.getMessage(),
	                    "Errore",
	                    JOptionPane.ERROR_MESSAGE
	            );
	            return; // stop on first error
	        }
	    }

	    refreshTable();
	    crocodileView.getReportLabel().setText("Operazione completata");
	}

	/**
	 * Deletes the selected directory (leaf).
	 * If the directory is not empty, asks explicit confirmation
	 * for deleting its contents as well.
	 */
	void deleteSelectedLeafDirectory() {

	    int row = crocodileView.getSelectedRow();
	    if (row < 0) {
	        return;
	    }

	    File dir = crocodileView.getModel().getRow(row);
	    Path path = dir.toPath();

	    if (!dir.isDirectory()) {
	        return;
	    }

	    // Verifica se directory foglia (non ha sottodirectory)
	    File[] children = dir.listFiles();
	    boolean hasSubDirs = false;
	    boolean hasFiles = false;

	    if (children != null) {
	        for (File f : children) {
	            if (f.isDirectory()) {
	                hasSubDirs = true;
	                break;
	            }
	            if (f.isFile()) {
	                hasFiles = true;
	            }
	        }
	    }

	    if (hasSubDirs) {
	        JOptionPane.showMessageDialog(
	                crocodileView,
	                "Questa directory non è una foglia.\n" +
	                "Elimina prima le sottodirectory.",
	                "Operazione non consentita",
	                JOptionPane.WARNING_MESSAGE
	        );
	        return;
	    }

	    // Directory vuota
	    if (!hasFiles) {
	        int confirm = JOptionPane.showConfirmDialog(
	                crocodileView,
	                "Vuoi cancellare la directory?\n\n" + dir.getAbsolutePath(),
	                "Conferma cancellazione",
	                JOptionPane.YES_NO_OPTION,
	                JOptionPane.WARNING_MESSAGE
	        );

	        if (confirm != JOptionPane.YES_OPTION) {
	            return;
	        }

	        try {
	            DirectoryUtils.deleteDirectoryRecursively(path);
	        } catch (Exception ex) {
	            showDeleteError(ex);
	            return;
	        }

	        refreshTable();
	        return;
	    }

	    // Directory non vuota (file presenti)
	    int confirm = JOptionPane.showConfirmDialog(
	            crocodileView,
	            "La directory contiene file.\n\n" +
	            "Vuoi cancellare la directory E TUTTO IL CONTENUTO?\n\n" +
	            dir.getAbsolutePath(),
	            "Conferma cancellazione completa",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.WARNING_MESSAGE
	    );

	    if (confirm != JOptionPane.YES_OPTION) {
	        return;
	    }

	    try {
	        DirectoryUtils.deleteDirectoryRecursively(path);
	    } catch (Exception ex) {
	        showDeleteError(ex);
	        return;
	    }

	    refreshTable();
	}

	private void showDeleteError(Exception ex) {
	    JOptionPane.showMessageDialog(
	            crocodileView,
	            "Errore durante la cancellazione:\n" + ex.getMessage(),
	            "Errore",
	            JOptionPane.ERROR_MESSAGE
	    );
	}

	
//	/**
//	 * @param list
//	 */
//	private void delete(List<File> list) {
//		for (File file : list) {
//			deleteAll(file);
//			if (crocodileView.getCancelButton().isSelected()) {
//				file.delete();
//			}
//		}
//	}
//
//	/**
//	 * @param file
//	 */
//	private void deleteAll(File file) {
//		for (File subfile : file.listFiles()) {
//			if (subfile.isDirectory()) {
//				deleteAll(subfile);
//			}
//			subfile.delete();
//		}
//	}
	
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

		    } else if (menu == crocodileView.getMenuItemMoveFiles()) {
		        moveFilesFromSelectedDir();
		        
		    }else if (menu == crocodileView.getMenuItemDeleteDir()) {
		        deleteSelectedLeafDirectory();
		    }
		}
	}

	/**
	 * 
	 */
	class TableMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent event) {
			Point point = event.getPoint();
			int currentRow = crocodileView.getTable().rowAtPoint(point);
			//System.out.println(currentRow);
			if(currentRow > -1) {
				crocodileView.getTable().setRowSelectionInterval(currentRow, currentRow);
			}
		}
	}
	
}
