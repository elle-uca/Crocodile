package org.ln.crocodile;



import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

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
    
//    private JLabel jLabel2;
//    private JLabel jLabel3;
//    private JLabel jLabel4;

    private JTable table;
    private FileTableModel model;
    private JPopupMenu popupMenu;
    private JMenuItem menuItemAdd;
    private JMenuItem menuItemRename;
//    private final ImageIcon crocIcon;
//    private final JLabel iconLabel;

    /**
     * Creates new form View
     */
    public CrocodileView() {
		super("Crocodile");
		
		
		
		
//		crocIcon = new ImageIcon(getClass().getResource("/icons/croc.png"));
//		iconLabel = new JLabel(crocIcon);
//		//ImageIcon img = new ImageIcon("croc.png");
//		setIconImage(crocIcon.getImage());
		//setIconImage(img.getImage());
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
//        jLabel2 = new JLabel();
//        jLabel3 = new JLabel();
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

  
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints;

        rootDirLabel.setText("Root dir");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(9, 6, 0, 0);
        getContentPane().add(rootDirLabel, gridBagConstraints);

        //rootDirField.setText("jTextField1");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.5;
       // gridBagConstraints.ipadx = 600;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(6, 12, 0, 0);
        getContentPane().add(rootDirField, gridBagConstraints);

        rootDirButton.setText("Cerca");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
       // gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(6, 18, 0, 6);
        getContentPane().add(rootDirButton, gridBagConstraints);

        searchDirLabel.setText("Dir da cercare");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(9, 6, 0, 0);
        getContentPane().add(searchDirLabel, gridBagConstraints);

        //searchDirField.setText("jTextField1");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.5;
        //gridBagConstraints.ipadx = 600;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(6, 12, 0, 0);
        getContentPane().add(searchDirField, gridBagConstraints);

        searchDirButton.setText("Refresh");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
       // gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(6, 18, 0, 6);
        getContentPane().add(searchDirButton, gridBagConstraints);


        //emptyButton.setText("jRadioButton1");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(13, 18, 0, 0);
        getContentPane().add(emptyButton, gridBagConstraints);

        //cancelButton.setText("jRadioButton2");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        //gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(13, 131, 0, 0);
        getContentPane().add(cancelButton, gridBagConstraints);
        
        //actionButton.setText("jButton3");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
       // gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(12, 18, 0, 6);
        getContentPane().add(actionButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 608;
        gridBagConstraints.ipady = 344;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(6, 6, 0, 6);
        getContentPane().add(scrollPane, gridBagConstraints);

        //reportLabel.setText("jLabel3");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 333;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(12, 6, 0, 0);
        getContentPane().add(reportLabel, gridBagConstraints);

        jLabel4.setText("jLabel3");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 333;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(6, 6, 15, 0);
        getContentPane().add(jLabel4, gridBagConstraints);
        
		Image icon = Toolkit.getDefaultToolkit().getImage(CrocodileView.class.getResource("/icons/mine_32.png"));
		
		System.out.println(icon);
		URL iconUrl = getClass().getResource("/icons/mine_32.png");
		System.out.println("Icon path: " + iconUrl);  // Controllo
        setIconImage(icon);

        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }// </editor-fold>                        





	/**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(CrocodileView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CrocodileView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CrocodileView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CrocodileView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrocodileView().setVisible(true);
            }
        });
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


}
