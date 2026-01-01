package org.ln.noor.directory.api;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ln.noor.directory.view.DirectoryToolView;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Utility class that provides entry points for launching the Directory Tool UI.
 *
 * @author Luca Noale
 */
public final class DirectoryToolLauncher {

    private DirectoryToolLauncher() {}

    /**
     * Launches the Directory Tool as a standalone window on the Swing event thread.
     */
    public static void open() {
    	FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new DirectoryToolView().setVisible(true);
        });
    }


    /**
     * Opens the Directory Tool as a modal dialog relative to the provided owner frame.
     *
     * @param owner parent frame that owns the modal dialog
     */
    public static void openModal(JFrame owner) {
    	FlatLightLaf.setup();
        DirectoryToolView view = new DirectoryToolView();

        // Build a modal dialog that hosts the tool's content pane.
        JDialog dialog = new JDialog(owner, "Directory Tool", true);
        dialog.setContentPane(view.getContentPane());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
}
