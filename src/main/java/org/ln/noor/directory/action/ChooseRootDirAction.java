package org.ln.noor.directory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ln.noor.directory.DirectoryToolController;

/**
 * Action that opens a dialog for selecting the root directory.
 */
public class ChooseRootDirAction implements ActionListener {

    private final DirectoryToolController controller;

    /**
     * Creates the action with a reference to the controller.
     *
     * @param controller the controller coordinating directory selection
     */
    public ChooseRootDirAction(DirectoryToolController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.chooseRootDirectory();
    }
}
