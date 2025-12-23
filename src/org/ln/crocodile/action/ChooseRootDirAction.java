package org.ln.crocodile.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ln.crocodile.CrocodileController;

public class ChooseRootDirAction implements ActionListener {

    private final CrocodileController controller;

    public ChooseRootDirAction(CrocodileController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.chooseRootDirectory();
    }
}
