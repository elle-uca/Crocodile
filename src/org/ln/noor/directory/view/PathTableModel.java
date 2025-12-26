package org.ln.noor.directory.view;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for displaying directories based on java.nio.file.Path.
 */
@SuppressWarnings("serial")
public class PathTableModel extends AbstractTableModel implements Serializable {


    private final List<String> columnNames;
    private final List<Path> data;

    public PathTableModel() {
        this.columnNames = List.of("Directory", "Contenuto");
        this.data = new ArrayList<>();
    }

    /* -------------------------
     *  TableModel basics
     * ------------------------- */

    @Override
    public int getRowCount() {
        return data.size();
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
        Path path = data.get(row);

        return switch (col) {
            case 0 -> path.toAbsolutePath().toString();
            case 1 -> countChildren(path);
            default -> "";
        };
    }

    /* -------------------------
     *  Data access
     * ------------------------- */

    public Path getRow(int row) {
        return data.get(row);
    }

    public List<Path> getData() {
        return data;
    }

    public void setData(Collection<Path> paths) {
        data.clear();
        if (paths != null) {
            data.addAll(paths);
        }
        fireTableDataChanged();
    }

    public void addRow(Path path) {
        int row = data.size();
        data.add(path);
        fireTableRowsInserted(row, row);
    }

    public void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void clear() {
        int size = data.size();
        if (size > 0) {
            data.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }

    /* -------------------------
     *  Helpers
     * ------------------------- */

    private int countChildren(Path dir) {
        if (!Files.isDirectory(dir)) {
            return 0;
        }
        try (var stream = Files.list(dir)) {
            return (int) stream.count();
        } catch (Exception e) {
            return 0;
        }
    }
}
