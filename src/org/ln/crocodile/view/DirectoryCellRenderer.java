package org.ln.crocodile.view;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class DirectoryCellRenderer extends DefaultTableCellRenderer {

    private static final Color LEAF_BG = new Color(220, 255, 220); // verde chiaro
    private static final Color LEAF_FG = new Color(0, 128, 0);     // verde scuro

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // NON alterare selezione
        if (isSelected) {
            setToolTipText(null);
            return c;
        }

        // Reset colori
        c.setForeground(table.getForeground());
        c.setBackground(table.getBackground());

        // Recupero File della riga (colonna 0 = path)
        Object rowObj = table.getModel().getValueAt(row, 0);
        if (!(rowObj instanceof String path)) {
            setToolTipText(null);
            return c;
        }

        File dir = new File(path);
        if (!dir.isDirectory()) {
            setToolTipText(null);
            return c;
        }

        // Conteggio file / directory
        int fileCount = 0;
        int dirCount = 0;

        File[] children = dir.listFiles();
        if (children != null) {
            for (File f : children) {
                if (f.isDirectory()) {
                    dirCount++;
                } else if (f.isFile()) {
                    fileCount++;
                }
            }
        }

        // Tooltip
        if (fileCount == 0 && dirCount == 0) {
            setToolTipText("Directory vuota");
        } else {
            setToolTipText(fileCount + " file, " + dirCount + " directory");
        }

        // Evidenzia directory foglia (nessuna sottodirectory)
        if (dirCount == 0) {
            c.setBackground(LEAF_BG);
            c.setForeground(LEAF_FG);
        }

        return c;
    }
}
