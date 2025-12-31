package org.ln.noor.directory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ln.noor.directory.DirectoryToolController;

public class ChooseRootDirAction implements ActionListener {

    private final DirectoryToolController controller;

    public ChooseRootDirAction(DirectoryToolController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.chooseRootDirectory();
    }
}
