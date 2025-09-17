package org.ln.crocodile;



import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
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
    private List<File> dirList;
    private String searchDir = "invio";
    private File selectedDir;
    


    private JTable table;
    private FileTableModel model;
    private JPopupMenu popupMenu;
    private JMenuItem menuItemAdd;
    private JMenuItem menuItemRename;

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
        model = new FileTableModel();
        table.setModel(model);
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
        popupMenu = new JPopupMenu();
        menuItemAdd = new JMenuItem("Add New Dir");
        menuItemRename = new JMenuItem("Rename Current Dir");
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRename);
        menuItemAdd.addActionListener(controller.new PopupActionListener());
        menuItemRename.addActionListener(controller.new PopupActionListener());
        
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(controller.new TableMouseListener());
	}


    /**
     * 
     */
    private void initComponents() {
    	controller = new CrocodileController(this);
    	dirList = new ArrayList<File>();
    	
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
        rootDirButton.addActionListener(controller);
        
        searchDirButton.setText("Refresh");
        searchDirButton.setActionCommand("Refresh");
        searchDirButton.addActionListener(controller);
        searchDirButton.setEnabled(false);
        
        actionButton.setText("Go");
        actionButton.setActionCommand("Go");
        actionButton.addActionListener(controller);
        actionButton.setEnabled(false);
       
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
        panel.add(rootDirButton, 	"cell 3 0");
        
        panel.add(searchDirLabel, 	"cell 0 1, wrap");
        panel.add(searchDirField,	"cell 1 1 2 1, growx, wrap, w :250:");
        panel.add(searchDirButton, 	"cell 3 1");
        
        panel.add(emptyButton, 		"cell 1 2");
        panel.add(cancelButton, 	"cell 2 2");
        panel.add(actionButton, 	"cell 3 2");
        
        panel.add(scrollPane, 		"cell 0 3 4 1, growx, wrap");
        
        panel.add(reportLabel, 		"cell 0 4");
        panel.add(jLabel4, 			"cell 0 5");
        
       
		Image icon = Toolkit.getDefaultToolkit().getImage(
				CrocodileView.class.getResource("/icons/croc.png"));
		
		System.out.println(icon);
		URL iconUrl = getClass().getResource("/icons/croc.png");
		System.out.println("Icon path: " + iconUrl);  // Controllo
        setIconImage(icon);

        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }            



    
    // Getters and Setters
    
	public List<File> getDirList() {
		return dirList;
	}

	public void setDirList(List<File> dirList) {
		this.dirList = dirList;
	}

	public String getSearchDir() {
		return searchDir;
	}

	public void setSearchDir(String searchDir) {
		this.searchDir = searchDir;
	}

	public File getSelectedDir() {
		return selectedDir;
	}

	public void setSelectedDir(File selectedDir) {
		this.selectedDir = selectedDir;
	}

	public FileTableModel getModel() {
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
	
	// Delegate methods


	/**
	 * @return
	 * @see javax.swing.JTable#getSelectedRow()
	 */
	public int getSelectedRow() {
		return table.getSelectedRow();
	}


	/**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrocodileView().setVisible(true);
            }
        });
    }
}
