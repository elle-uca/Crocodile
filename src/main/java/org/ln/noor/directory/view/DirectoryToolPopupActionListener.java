package org.ln.noor.directory.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.ln.noor.directory.DirectoryToolController;

/**
 * Handles popup menu actions for the Crocodile table.
 *
 * @author Luca Noale
 */
public class DirectoryToolPopupActionListener implements ActionListener {

    private final DirectoryToolView view;
    private final DirectoryToolController controller;

    /**
     * Creates a listener that dispatches menu actions to the controller.
     *
     * @param view        source view containing the popup menu
     * @param controller  controller used to execute directory operations
     */
    public DirectoryToolPopupActionListener(
            DirectoryToolView view,
            DirectoryToolController controller) {
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem item = (JMenuItem) e.getSource();

        if (item == view.getMenuItemAdd()) {
            controller.addNewDir();

        } else if (item == view.getMenuItemRename()) {
            controller.renameCurrentDir();

        } else if (item == view.getMenuItemMoveFiles()) {
            controller.moveFilesFromSelectedDir();

        } else if (item == view.getMenuItemDeleteDir()) {
            controller.deleteSelectedDirectory();

        }else if (item == view.getMenuItemReorder()) {
            controller.reorderSelectedDirectory();
        }

        else if (item == view.getMenuDeleteIntermediateDir()) {
            controller.flattenSelectedDirectory();
        }
        
        else if (item == view.getMenuItemOpen()) {
            controller.openSelectedDirectory();
        }
    }
}
