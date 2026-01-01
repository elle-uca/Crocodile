package org.ln.noor.directory.view;

import java.awt.Dimension;
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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.ln.noor.directory.DirectoryToolController;
import org.ln.noor.directory.action.ChooseRootDirAction;
import org.ln.noor.directory.action.ExecuteAction;
import org.ln.noor.directory.action.RefreshSearchAction;
import org.ln.noor.directory.service.DirectoryStatsService;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

/**
 * Swing-based UI for managing directory operations such as creation, renaming,
 * reordering, and flattening.
 * 
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class DirectoryToolView extends JFrame {
	

    // Variables declaration                    
    private JButton rootDirButton;
    private JButton searchDirButton;
    private JButton actionButton;
    private JLabel rootDirLabel;
    private JLabel searchDirLabel;
    private JLabel reportLabel;
    private JLabel infoLabel;
    private ButtonGroup buttonGroup;
    private JRadioButton emptyButton;
    private JRadioButton cancelButton;
    private JScrollPane scrollPane;
    private JTextField rootDirField;
    private JTextField searchDirField;
    
	private DirectoryToolController controller;
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
     * Builds the main application window and initializes all components.
     */
    public DirectoryToolView() {
                super("Crocodile");
                initComponents();
    }

    /**
     * Configures the table, model, renderer, and popup menu used to manage directories.
     */
    private void initTable() {
        table = new JTable();
        model = new DirectoryTableModel(new DirectoryStatsService());
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new DirectoryCellRenderer());
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(
        		ListSelectionModel.SINGLE_SELECTION);
         
        popupMenu = new JPopupMenu();
        menuItemAdd = new JMenuItem("Add New Dir");
        menuItemRename = new JMenuItem("Rename Current Dir");
        menuItemMoveFiles = new JMenuItem("Move Files To...");
        menuItemDeleteDir = new JMenuItem("Delete Directory");
        menuDeleteIntermediateDir = new JMenuItem("Delete Intermediate Directory");
  
        menuItemAdd.addActionListener(
        		new DirectoryToolPopupActionListener(this, controller));
        menuItemRename.addActionListener(
        		new DirectoryToolPopupActionListener(this, controller));
        menuItemMoveFiles.addActionListener(
        		new DirectoryToolPopupActionListener(this, controller));
        menuItemDeleteDir.addActionListener(
        		new DirectoryToolPopupActionListener(this, controller));
        menuDeleteIntermediateDir.addActionListener(
        		new DirectoryToolPopupActionListener(this, controller));
        
        menuItemReorder = new JMenuItem("Reorder Directory...");
        popupMenu.add(menuItemReorder);
        menuItemReorder.addActionListener(
        		new DirectoryToolPopupActionListener(this, controller));
       
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRename);       
        popupMenu.add(menuItemMoveFiles);
        popupMenu.add(menuItemDeleteDir);
        popupMenu.add(menuDeleteIntermediateDir);
        
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(
        		new TableRowSelectionMouseListener(table, controller));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.onDirectorySelected();
            }
        });
	}


    /**
     * Initializes all Swing controls, wiring them to the controller and arranging the layout.
     */
    private void initComponents() {
        controller = new DirectoryToolController(this);
        dirList = new ArrayList<Path>();
    	
        searchDirField = new JTextField();
        rootDirField = new JTextField();
        searchDirButton = new JButton();
        rootDirButton = new JButton();
        actionButton = new JButton();
        searchDirLabel = new JLabel();
        rootDirLabel = new JLabel();
        reportLabel = new JLabel();
        infoLabel = new JLabel();
        rootDirLabel = new JLabel();
        rootDirField = new JTextField();
        rootDirButton = new JButton();
        searchDirLabel = new JLabel();
        searchDirField = new JTextField();
        searchDirButton = new JButton();
        actionButton = new JButton();

        reportLabel = new JLabel();
        infoLabel = new JLabel();
        
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
        
        panel.add(reportLabel, 		"cell 0 4 4 1, growx, wrap");
        panel.add(infoLabel, 		"cell 0 5 4 1, growx");
        
       
        URL iconUrl = DirectoryToolView.class
                .getClassLoader()
                .getResource("icons/croc.png");

        if (iconUrl == null) {
            throw new IllegalStateException("Icon not found: icons/croc.png");
        }

        Image icon = new ImageIcon(iconUrl).getImage();
        setIconImage(icon);
        
        panel.setPreferredSize(new Dimension(600, 600));
        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600, 600);
        pack();
    }



    
    // Getters and Setters
    
        /**
         * Returns the current list of directories shown in the UI.
         *
         * @return list of directories managed by the tool
         */
        public List<Path> getDirList() {
                return dirList;
        }

        /**
         * Replaces the list of directories managed by the view.
         *
         * @param dirList new directory list
         */
        public void setDirList(List<Path> dirList) {
                this.dirList = dirList;
        }

        /**
         * Returns the search string used to filter directories.
         *
         * @return current search value
         */
        public String getSearchDir() {
                return searchDir;
        }

        /**
         * Updates the search filter used by the controller.
         *
         * @param searchDir new search value
         */
        public void setSearchDir(String searchDir) {
                this.searchDir = searchDir;
        }

        /**
         * Returns the directory currently selected in the table.
         *
         * @return selected directory path
         */
        public Path getSelectedDir() {
                return selectedDir;
        }

        /**
         * Updates the selected directory tracked by the view.
         *
         * @param selectedDir directory corresponding to the current selection
         */
        public void setSelectedDir(Path selectedDir) {
                this.selectedDir = selectedDir;
        }

        /**
         * Provides access to the table model showing directories.
         *
         * @return table model instance
         */
        public DirectoryTableModel getModel() {
                return model;
        }

        /**
         * Returns the text field that holds the root directory path.
         *
         * @return root directory text field
         */
        public JTextField getRootDirField() {
                return rootDirField;
        }

        /**
         * Returns the text field used for directory searches.
         *
         * @return search text field
         */
        public JTextField getSearchDirField() {
                return searchDirField;
        }

        /**
         * Returns the option indicating that directories should be emptied.
         *
         * @return radio button representing the "empty" action
         */
        public JRadioButton getEmptyButton() {
                return emptyButton;
        }

        /**
         * Returns the option indicating directories should be deleted.
         *
         * @return radio button representing the "delete" action
         */
        public JRadioButton getCancelButton() {
                return cancelButton;
        }

        /**
         * Label used to display summary information.
         *
         * @return report label
         */
        public JLabel getReportLabel() {
                return reportLabel;
        }



        /**
         * Label used for contextual feedback.
         *
         * @return info label
         */
        public JLabel getInfoLabel() {
                return infoLabel;
        }

        /**
         * Main action button that triggers the selected operation.
         *
         * @return action button
         */
        public JButton getActionButton() {
                return actionButton;
        }

        /**
         * Button that refreshes the search results.
         *
         * @return search refresh button
         */
        public JButton getSearchDirButton() {
                return searchDirButton;
        }

        /**
         * Menu item that opens the reorder dialog.
         *
         * @return reorder menu item
         */
        public JMenuItem getMenuItemReorder() {
            return menuItemReorder;
        }

    /**
         * Menu item that triggers directory flattening.
         *
         * @return intermediate-directory deletion menu item
         */
        public JMenuItem getMenuDeleteIntermediateDir() {
                return menuDeleteIntermediateDir;
        }

        /**
         * Table instance displayed in the UI.
         *
         * @return directory table
         */
        public JTable getTable() {
                return table;
        }

        /**
         * Menu item that adds a new directory below the selection.
         *
         * @return add-directory menu item
         */
        public JMenuItem getMenuItemAdd() {
                return menuItemAdd;
        }

        /**
         * Menu item used to rename the selected directory.
         *
         * @return rename menu item
         */
        public JMenuItem getMenuItemRename() {
                return menuItemRename;
        }

        /**
         * Menu item that moves files out of the selected directory.
         *
         * @return move-files menu item
         */
        public JMenuItem getMenuItemMoveFiles() {
            return menuItemMoveFiles;
        }

        /**
         * Menu item that deletes the currently selected directory.
         *
         * @return delete-directory menu item
         */
        public JMenuItem getMenuItemDeleteDir() {
            return menuItemDeleteDir;
        }

        // Delegate methods


        /**
         * Returns the index of the selected row in the directory table.
         *
         * @return selected row index
         * @see javax.swing.JTable#getSelectedRow()
         */
        public int getSelectedRow() {
                return table.getSelectedRow();
        }


        public static void main(String[] args) {
        	FlatLightLaf.setup();
        	SwingUtilities.invokeLater(() -> {
        		new DirectoryToolView().setVisible(true);
        	});
        }

}
