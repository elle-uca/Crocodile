package org.ln.noor.directory.view;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Custom cell renderer that highlights leaf directories and shows child counts.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class DirectoryCellRenderer extends DefaultTableCellRenderer {

    private static final Color LEAF_BG = new Color(220, 255, 220); // light green background
    private static final Color LEAF_FG = new Color(0, 128, 0);     // dark green text

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

        // Do not alter selection highlighting handled by JTable
        if (isSelected) {
            setToolTipText(null);
            return c;
        }

        // Reset to default colors before applying custom styling
        c.setForeground(table.getForeground());
        c.setBackground(table.getBackground());

        // Retrieve the row path (column 0 holds the directory path)
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

        // Count how many files and directories exist directly under the path
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

        // Build a tooltip summarizing the child counts
        if (fileCount == 0 && dirCount == 0) {
            setToolTipText("Directory vuota");
        } else {
            setToolTipText(fileCount + " file, " + dirCount + " directory");
        }

        // Highlight leaf directories that contain no subdirectories
        if (dirCount == 0) {
            c.setBackground(LEAF_BG);
            c.setForeground(LEAF_FG);
        }

        return c;
    }
}
