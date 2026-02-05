package org.ln.noor.directory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ln.noor.directory.DirectoryToolController;

/**
 * Action that confirms and triggers the main directory operation.
 *
 * @author Luca Noale
 */
public class ExecuteAction implements ActionListener {

    private final DirectoryToolController controller;

    /**
     * Creates the action with a reference to the controller.
     *
     * @param controller the controller coordinating the execution
     */
    public ExecuteAction(DirectoryToolController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	 controller.executeMainAction();

    }
}
