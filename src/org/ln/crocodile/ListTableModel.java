package org.ln.crocodile;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class ListTableModel<E> extends AbstractTableModel implements Serializable {
  private static final long serialVersionUID = 4322824557861734745L;
  
  protected List<String> columnNames;
  
  protected List<E> data;
  
  public ListTableModel() {
    setDataList(this.data, this.columnNames);
  }
  
  public ListTableModel(List<E> data) {
    setDataList(data, this.columnNames);
  }
  
  public ListTableModel(List<E> dataList, List<String> columnNames) {
    setDataList(dataList, columnNames);
  }
  
  public List<String> getColumnNames() {
    return this.columnNames;
  }
  
  public void setColumnNames(List<String> columnNames) {
    this.columnNames = columnNames;
    fireTableStructureChanged();
  }
  
  public List<E> getData() {
    return this.data;
  }
  
  public void setData(List<E> data) {
	//removeAll();
    this.data = data;
    fireTableDataChanged();
  }
  
  public void setDataList(List<E> data, List<String> columnNames) {
    if (data == null)
      data = new ArrayList<E>(); 
    if (columnNames == null)
      columnNames = new ArrayList<String>(); 
    this.data = data;
    this.columnNames = columnNames;
    fireTableStructureChanged();
    fireTableDataChanged();
  }
  
  public void addRow(E item) {
    insertRow(getRowCount(), item);
  }
  
  public void insertRow(int row, E item) {
    this.data.add(row, item);
    fireTableRowsInserted(row, row);
  }
  
  public void removeRow(int row) {
    this.data.remove(row);
    fireTableRowsDeleted(row, row);
  }
  
  public void removeItem(E item) {
    int row = this.data.indexOf(item);
    removeRow(row);
  }
  
  public int getColumnCount() {
    return this.columnNames.size();
  }
  
  public int getRowCount() {
    return this.data.size();
  }
  
  public E getRow(int row) {
    return this.data.get(row);
  }
  
  public abstract Object getValueAt(int paramInt1, int paramInt2);
  
  public String getColumnName(int column) {
    Object id = null;
    if (column < this.columnNames.size() && column >= 0)
      id = this.columnNames.get(column); 
    return (id == null) ? super.getColumnName(column) : id.toString();
  }
  
  public void removeAll() {
    int i = this.data.size();
    if (i > 0) {
      this.data.clear();
      fireTableRowsDeleted(0, i - 1);
    } 
  }
  
  public void replace(Collection<E> c) {
    this.data.clear();
    addAll(c);
  }
  
  public void addAll(Collection<E> c) {
    this.data.addAll(c);
    fireTableRowsInserted(getRowCount(), getRowCount() + c.size() - 1);
  }
}