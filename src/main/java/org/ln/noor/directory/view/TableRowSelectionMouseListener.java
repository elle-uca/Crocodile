package org.ln.noor.directory.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.ln.noor.directory.DirectoryToolController;

/**
 * Ensures row selection follows mouse clicks.
 *
 * @author Luca Noale
 */
public class TableRowSelectionMouseListener extends MouseAdapter {

    private final JTable table;
    private final DirectoryToolController controller;

    /**
     * Builds a listener that updates table selection and controller state on click.
     *
     * @param table       table whose rows should be selected when clicked
     * @param controller  controller notified after selection changes
     */
    public TableRowSelectionMouseListener(JTable table, DirectoryToolController controller) {
        this.table = table;
        this.controller = controller;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        controller.updateMoveMenuState();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        if (row >= 0) {
            table.setRowSelectionInterval(row, row);
        }
    }
}
