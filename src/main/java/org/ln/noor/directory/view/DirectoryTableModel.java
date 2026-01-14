package org.ln.noor.directory.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.ln.noor.directory.DirectoryScanResult;

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
    


    /* -------------------------
     *  Domain-specific API
     * ------------------------- */



    public List<DirectoryScanResult> getRows() {
        return rows;
    }
    
    public DirectoryScanResult getRow(int row) {
        return rows.get(row);
    }
    
    public void upsert(DirectoryScanResult r) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).dir.equals(r.dir)) {
                rows.set(i, r);
                fireTableRowsUpdated(i, i);
                return;
            }
        }
        rows.add(r);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
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
