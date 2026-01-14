package org.ln.noor.directory.view;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.table.AbstractTableModel;

import org.ln.noor.directory.DirectoryScanResult;
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
    private final List<DirectoryScanResult> rows = new ArrayList<>();
    
//    private final List<Path> directories = new ArrayList<>();
//
//    private final DirectoryStatsService statsService;
//    
//    private final Map<Path, Integer> cache = new ConcurrentHashMap<>();
    
    
    /**
     * Creates a new model bound to the provided statistics service.
     *
     * @param statsService service used to compute directory statistics
     */
//    public DirectoryTableModel(DirectoryStatsService statsService) {
//        this.statsService = statsService;
//    }

    /**
     * Returns the number of rows currently stored in the model.
     */
    @Override
    public int getRowCount() {
        return rows.size();
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
        DirectoryScanResult r = rows.get(row);
        return switch (col) {
            case 0 -> r.dir.toString();
            case 1 -> r.files + " file, " + r.subDirs + " dir";
            default -> "";
        };
    }
    
    
    
//    @Override
//    public Object getValueAt(int row, int col) {
//        Path dir = directories.get(row);
//
//        return switch (col) {
//            case 0 -> dir.toAbsolutePath().toString();
//            //case 1 -> statsService.countDirectChildren(dir);
//            case 1 -> cache.getOrDefault(dir, -1);
//            default -> "";
//        };
//    }

    /* -------------------------
     *  Domain-specific API
     * ------------------------- */

    /**
     * Retrieves the directory stored at the given row.
     *
     * @param row table row index
     * @return directory path for the row
     */
//    public Path getDirectoryAt(int row) {
//        return directories.get(row);
//    }

    /**
     * Exposes the list of directories for read-only use.
     *
     * @return backing list of directories
     */
//    public List<Path> getDirectories() {
//        return directories;
//    }

    public List<DirectoryScanResult> getRows() {
        return rows;
    }
    
    public DirectoryScanResult getRow(int row) {
        return rows.get(row);
    }
    
    /**
     * Replaces the current directories with the provided collection.
     *
     * @param dirs new directories to display
     */
    public void setResults(List<DirectoryScanResult> data) {
        rows.clear();
        rows.addAll(data);
        fireTableDataChanged();
    }

    /**
     * Clears all rows from the model when present.
     */
    public void clear() {
        int size = rows.size();
        if (size > 0) {
            rows.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}
