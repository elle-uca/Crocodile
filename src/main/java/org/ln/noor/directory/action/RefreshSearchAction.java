package org.ln.noor.directory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ln.noor.directory.CrocodileController;

public class RefreshSearchAction implements ActionListener {

    private final CrocodileController controller;

    public RefreshSearchAction(CrocodileController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.refreshSearch();
    }
}

