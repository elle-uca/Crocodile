//package org.ln.noor.directory.view;
//
//import java.io.Serializable;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import javax.swing.table.AbstractTableModel;
//
///**
// * TableModel for displaying directories based on java.nio.file.Path.
// *
// * @author Luca Noale
// */
//@SuppressWarnings("serial")
//public class PathTableModel extends AbstractTableModel implements Serializable {
//
//
//    private final List<String> columnNames;
//    private final List<Path> data;
//
//    /**
//     * Builds an empty table model with predefined column names.
//     */
//    public PathTableModel() {
//        this.columnNames = List.of("Directory", "Contenuto");
//        this.data = new ArrayList<>();
//    }
//
//    /* -------------------------
//     *  TableModel basics
//     * ------------------------- */
//
//    /**
//     * Returns the number of rows currently stored in the model.
//     */
//    @Override
//    public int getRowCount() {
//        return data.size();
//    }
//
//    /**
//     * Returns the number of available columns.
//     */
//    @Override
//    public int getColumnCount() {
//        return columnNames.size();
//    }
//
//    /**
//     * Provides the column header for a given index.
//     */
//    @Override
//    public String getColumnName(int column) {
//        return columnNames.get(column);
//    }
//
//    /**
//     * Returns the cell value for the requested row and column.
//     */
//    @Override
//    public Object getValueAt(int row, int col) {
//        Path path = data.get(row);
//
//        return switch (col) {
//            case 0 -> path.toAbsolutePath().toString();
//            case 1 -> countChildren(path);
//            default -> "";
//        };
//    }
//
//    /* -------------------------
//     *  Data access
//     * ------------------------- */
//
//    /**
//     * Retrieves the path stored at the given row.
//     *
//     * @param row table row index
//     * @return path associated with the row
//     */
//    public Path getRow(int row) {
//        return data.get(row);
//    }
//
//    /**
//     * Exposes the backing list for read-only consumption.
//     *
//     * @return list containing all paths in the model
//     */
//    public List<Path> getData() {
//        return data;
//    }
//
//    /**
//     * Replaces the current data set with the provided collection.
//     *
//     * @param paths new collection of paths to display
//     */
//    public void setData(Collection<Path> paths) {
//        data.clear();
//        if (paths != null) {
//            data.addAll(paths);
//        }
//        fireTableDataChanged();
//    }
//
//    /**
//     * Appends a single path to the model and fires a table update.
//     *
//     * @param path path to add to the end of the table
//     */
//    public void addRow(Path path) {
//        int row = data.size();
//        data.add(path);
//        fireTableRowsInserted(row, row);
//    }
//
//    /**
//     * Removes a row at the given index and notifies listeners.
//     *
//     * @param row index to remove
//     */
//    public void removeRow(int row) {
//        data.remove(row);
//        fireTableRowsDeleted(row, row);
//    }
//
//    /**
//     * Clears all rows from the model when present.
//     */
//    public void clear() {
//        int size = data.size();
//        if (size > 0) {
//            data.clear();
//            fireTableRowsDeleted(0, size - 1);
//        }
//    }
//
//    /* -------------------------
//     *  Helpers
//     * ------------------------- */
//
//    private int countChildren(Path dir) {
//        if (!Files.isDirectory(dir)) {
//            return 0;
//        }
//        try (var stream = Files.list(dir)) {
//            // Count only direct children; stream auto-closes via try-with-resources
//            return (int) stream.count();
//        } catch (Exception e) {
//            // Ignore access errors and treat as zero children
//            return 0;
//        }
//    }
//}
