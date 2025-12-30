package org.ln.noor.directory.api;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ln.noor.directory.view.CrocodileView;

public final class DirectoryToolLauncher {

    private DirectoryToolLauncher() {}

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            new CrocodileView().setVisible(true);
        });
    }
    
    
    public static void openModal(JFrame owner) {
        CrocodileView view = new CrocodileView();

        JDialog dialog = new JDialog(owner, "Directory Tool", true);
        dialog.setContentPane(view.getContentPane());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
}
