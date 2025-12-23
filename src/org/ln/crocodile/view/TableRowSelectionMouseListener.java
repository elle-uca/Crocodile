package org.ln.crocodile.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

/**
 * Ensures row selection follows mouse clicks.
 */
public class TableRowSelectionMouseListener extends MouseAdapter {

    private final JTable table;

    public TableRowSelectionMouseListener(JTable table) {
        this.table = table;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        if (row >= 0) {
            table.setRowSelectionInterval(row, row);
        }
    }
}
