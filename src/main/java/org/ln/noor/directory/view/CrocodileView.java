package org.ln.noor.directory.view;



import java.awt.Image;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import org.ln.noor.directory.CrocodileController;
import org.ln.noor.directory.action.ChooseRootDirAction;
import org.ln.noor.directory.action.ExecuteAction;
import org.ln.noor.directory.action.RefreshSearchAction;
import org.ln.noor.directory.service.DirectoryStatsService;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author luke
 */
@SuppressWarnings("serial")
public class CrocodileView extends JFrame {
	

    // Variables declaration                    
    private JButton rootDirButton;
    private JButton searchDirButton;
    private JButton actionButton;
    private JLabel rootDirLabel;
    private JLabel searchDirLabel;
    private JLabel reportLabel;
    private JLabel jLabel4;
    private ButtonGroup buttonGroup;
    private JRadioButton emptyButton;
    private JRadioButton cancelButton;
    private JScrollPane scrollPane;
    private JTextField rootDirField;
    private JTextField searchDirField;
    
	private CrocodileController controller;
    private List<Path> dirList;
    private String searchDir = "invio";
    private Path selectedDir;
 
    private JTable table;
    private DirectoryTableModel model;

    private JPopupMenu popupMenu;
    private JMenuItem menuItemAdd;
    private JMenuItem menuItemRename;
    private JMenuItem menuItemMoveFiles;
    private JMenuItem menuItemDeleteDir;
    private JMenuItem menuDeleteIntermediateDir;


    
    private JMenuItem menuItemReorder;
    
    /**
     * Creates new form View
     */
    public CrocodileView() {
		super("Crocodile");
		initComponents();
    }

    /**
     * 
     */
    private void initTable() {
        table = new JTable();
        model = new DirectoryTableModel(new DirectoryStatsService());
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new DirectoryCellRenderer());
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
        popupMenu = new JPopupMenu();
        menuItemAdd = new JMenuItem("Add New Dir");
        menuItemRename = new JMenuItem("Rename Current Dir");
        menuItemMoveFiles = new JMenuItem("Move Files To...");
        menuItemDeleteDir = new JMenuItem("Delete Directory");
        menuDeleteIntermediateDir = new JMenuItem("Delete Intermediate Directory");
  
        menuItemAdd.addActionListener(new CrocodilePopupActionListener(this, controller));
        menuItemRename.addActionListener(new CrocodilePopupActionListener(this, controller));
        menuItemMoveFiles.addActionListener(new CrocodilePopupActionListener(this, controller));
        menuItemDeleteDir.addActionListener(new CrocodilePopupActionListener(this, controller));
        menuDeleteIntermediateDir.addActionListener(new CrocodilePopupActionListener(this, controller));
        
        menuItemReorder = new JMenuItem("Reorder Directory...");
        popupMenu.add(menuItemReorder);
        menuItemReorder.addActionListener(new CrocodilePopupActionListener(this, controller));

       
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRename);       
        popupMenu.add(menuItemMoveFiles);
        popupMenu.add(menuItemDeleteDir);
        popupMenu.add(menuDeleteIntermediateDir);
        
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new TableRowSelectionMouseListener(table));
	}


    /**
     * 
     */
    private void initComponents() {
    	controller = new CrocodileController(this);
    	dirList = new ArrayList<Path>();
    	
        searchDirField = new JTextField();
        rootDirField = new JTextField();
        searchDirButton = new JButton();
        rootDirButton = new JButton();
        actionButton = new JButton();
        searchDirLabel = new JLabel();
        rootDirLabel = new JLabel();
        reportLabel = new JLabel();
        jLabel4 = new JLabel();
        rootDirLabel = new JLabel();
        rootDirField = new JTextField();
        rootDirButton = new JButton();
        searchDirLabel = new JLabel();
        searchDirField = new JTextField();
        searchDirButton = new JButton();
        actionButton = new JButton();

        reportLabel = new JLabel();
        jLabel4 = new JLabel();
        
        buttonGroup = new ButtonGroup();
        cancelButton = new JRadioButton();
        emptyButton = new JRadioButton();
        cancelButton.setText("Cancella");
        emptyButton.setText("Svuota");
        emptyButton.setSelected(true);
        cancelButton.setActionCommand("Cancella");
    	emptyButton.setActionCommand("Svuota");
        buttonGroup.add(emptyButton);
        buttonGroup.add(cancelButton);


        rootDirLabel.setText("Dir base");
        rootDirButton.setText("Cerca");
        rootDirButton.setActionCommand("Cerca");
        
        searchDirButton.setText("Refresh");
        searchDirButton.setActionCommand("Refresh");
        searchDirButton.setEnabled(false);
        
        actionButton.setText("Go");
        actionButton.setActionCommand("Go");
        actionButton.setEnabled(false);
       
        rootDirButton.addActionListener(
                new ChooseRootDirAction(controller));

        searchDirButton.addActionListener(
                new RefreshSearchAction(controller));

        actionButton.addActionListener(
                new ExecuteAction(controller));
        
        initTable();

        rootDirLabel.setText("Root dir");
        rootDirButton.setText("Cerca");
        searchDirLabel.setText("Dir da cercare");
        searchDirButton.setText("Refresh");
        jLabel4.setText("jLabel3");

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "[][grow][grow][]", "20[][][][][]20"));
        
        panel.add(rootDirLabel, 	"cell 0 0, wrap");
        panel.add(rootDirField,		"cell 1 0 2 1, growx, wrap, w :250:");
        panel.add(rootDirButton, 	"cell 3 0, sg btn");
        
        panel.add(searchDirLabel, 	"cell 0 1, wrap");
        panel.add(searchDirField,	"cell 1 1 2 1, growx, wrap, w :250:");
        panel.add(searchDirButton, 	"cell 3 1, sg btn");
        
        panel.add(emptyButton, 		"cell 1 2");
        panel.add(cancelButton, 	"cell 2 2");
        panel.add(actionButton, 	"cell 3 2, sg btn");
        
        panel.add(scrollPane, 		"cell 0 3 4 1, growx, wrap");
        
        panel.add(reportLabel, 		"cell 0 4");
        panel.add(jLabel4, 			"cell 0 5");
        
       
        URL iconUrl = CrocodileView.class
                .getClassLoader()
                .getResource("icons/croc.png");

        if (iconUrl == null) {
            throw new IllegalStateException("Icon not found: icons/croc.png");
        }

        Image icon = new ImageIcon(iconUrl).getImage();
        setIconImage(icon);

        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }            



    
    // Getters and Setters
    
	public List<Path> getDirList() {
		return dirList;
	}

	public void setDirList(List<Path> dirList) {
		this.dirList = dirList;
	}

	public String getSearchDir() {
		return searchDir;
	}

	public void setSearchDir(String searchDir) {
		this.searchDir = searchDir;
	}

	public Path getSelectedDir() {
		return selectedDir;
	}

	public void setSelectedDir(Path selectedDir) {
		this.selectedDir = selectedDir;
	}

	public DirectoryTableModel getModel() {
		return model;
	}

	public JTextField getRootDirField() {
		return rootDirField;
	}

	public JTextField getSearchDirField() {
		return searchDirField;
	}

	public JRadioButton getEmptyButton() {
		return emptyButton;
	}

	public JRadioButton getCancelButton() {
		return cancelButton;
	}

	public JLabel getReportLabel() {
		return reportLabel;
	}

	public JButton getActionButton() {
		return actionButton;
	}

	public JButton getSearchDirButton() {
		return searchDirButton;
	}

	public JMenuItem getMenuItemReorder() {
	    return menuItemReorder;
	}

    /**
	 * @return the menuDeleteIntermediateDir
	 */
	public JMenuItem getMenuDeleteIntermediateDir() {
		return menuDeleteIntermediateDir;
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @return the menuItemAdd
	 */
	public JMenuItem getMenuItemAdd() {
		return menuItemAdd;
	}

	/**
	 * @return the menuItemRename
	 */
	public JMenuItem getMenuItemRename() {
		return menuItemRename;
	}
	
	public JMenuItem getMenuItemMoveFiles() {
	    return menuItemMoveFiles;
	}
	
	public JMenuItem getMenuItemDeleteDir() {
	    return menuItemDeleteDir;
	}
	
	// Delegate methods


	/**
	 * @return
	 * @see javax.swing.JTable#getSelectedRow()
	 */
	public int getSelectedRow() {
		return table.getSelectedRow();
	}


}
