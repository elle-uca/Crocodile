package org.ln.noor.directory.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.ln.noor.directory.CrocodileController;

/**
 * Ensures row selection follows mouse clicks.
 */
public class TableRowSelectionMouseListener extends MouseAdapter {

    private final JTable table;
    private final CrocodileController controller;

    public TableRowSelectionMouseListener(JTable table, CrocodileController controller) {
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
