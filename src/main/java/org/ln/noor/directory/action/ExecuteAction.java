package org.ln.noor.directory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.ln.noor.directory.DirectoryToolController;

public class ExecuteAction implements ActionListener {

    private final DirectoryToolController controller;

    public ExecuteAction(DirectoryToolController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Sei sicuro di procedere?",
                "Conferma",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.executeMainAction();
        }
    }
}
