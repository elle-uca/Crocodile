package org.ln.noor.directory.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.ln.noor.directory.CrocodileController;

/**
 * Handles popup menu actions for the Crocodile table.
 */
public class CrocodilePopupActionListener implements ActionListener {

    private final CrocodileView view;
    private final CrocodileController controller;

    public CrocodilePopupActionListener(
            CrocodileView view,
            CrocodileController controller) {
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
    }
}
