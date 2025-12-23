package org.ln.crocodile.view;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.ln.crocodile.service.DirectoryStatsService;

/**
 * TableModel for directories only.
 */
public class DirectoryTableModel extends AbstractTableModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<String> columnNames = List.of(
            "Directory",
            "Contenuto"
    );

    private final List<Path> directories = new ArrayList<>();

    private final DirectoryStatsService statsService;

    public DirectoryTableModel(DirectoryStatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public int getRowCount() {
        return directories.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Path dir = directories.get(row);

        return switch (col) {
            case 0 -> dir.toAbsolutePath().toString();
            case 1 -> statsService.countDirectChildren(dir);
            default -> "";
        };
    }

    /* -------------------------
     *  Domain-specific API
     * ------------------------- */

    public Path getDirectoryAt(int row) {
        return directories.get(row);
    }

    public List<Path> getDirectories() {
        return directories;
    }

    public void setDirectories(Collection<Path> dirs) {
        directories.clear();
        if (dirs != null) {
            directories.addAll(dirs);
        }
        fireTableDataChanged();
    }

    public void clear() {
        int size = directories.size();
        if (size > 0) {
            directories.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}
