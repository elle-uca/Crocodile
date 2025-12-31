package org.ln.noor.directory.view;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.ln.noor.directory.service.DirectoryStatsService;

/**
 * TableModel for directories only.
 *
 * @author Luca Noale
 */
public class DirectoryTableModel extends AbstractTableModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<String> columnNames = List.of(
            "Directory",
            "Contenuto"
    );

    private final List<Path> directories = new ArrayList<>();

    private final DirectoryStatsService statsService;

    /**
     * Creates a new model bound to the provided statistics service.
     *
     * @param statsService service used to compute directory statistics
     */
    public DirectoryTableModel(DirectoryStatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Returns the number of rows currently stored in the model.
     */
    @Override
    public int getRowCount() {
        return directories.size();
    }

    /**
     * Returns the number of available columns.
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Provides the column header for a given index.
     */
    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    /**
     * Returns the cell value for the requested row and column.
     */
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

    /**
     * Retrieves the directory stored at the given row.
     *
     * @param row table row index
     * @return directory path for the row
     */
    public Path getDirectoryAt(int row) {
        return directories.get(row);
    }

    /**
     * Exposes the list of directories for read-only use.
     *
     * @return backing list of directories
     */
    public List<Path> getDirectories() {
        return directories;
    }

    /**
     * Replaces the current directories with the provided collection.
     *
     * @param dirs new directories to display
     */
    public void setDirectories(Collection<Path> dirs) {
        directories.clear();
        if (dirs != null) {
            directories.addAll(dirs);
        }
        fireTableDataChanged();
    }

    /**
     * Clears all rows from the model when present.
     */
    public void clear() {
        int size = directories.size();
        if (size > 0) {
            directories.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}
