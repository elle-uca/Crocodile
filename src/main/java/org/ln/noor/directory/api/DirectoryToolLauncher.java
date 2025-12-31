package org.ln.noor.directory.api;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ln.noor.directory.view.DirectoryToolView;

public final class DirectoryToolLauncher {

    private DirectoryToolLauncher() {}

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            new DirectoryToolView().setVisible(true);
        });
    }
    
    
    public static void openModal(JFrame owner) {
        DirectoryToolView view = new DirectoryToolView();

        JDialog dialog = new JDialog(owner, "Directory Tool", true);
        dialog.setContentPane(view.getContentPane());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
}
