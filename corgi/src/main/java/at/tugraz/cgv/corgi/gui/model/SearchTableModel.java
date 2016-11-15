/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui.model;

import at.tugraz.cgv.corgi.lucene.SearchHit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author heinz
 */
public class SearchTableModel extends AbstractTableModel {

  private final String[] columns = {"Filename", "Score"};
  private final List<SearchHit> data = new ArrayList<>();

  public void update(List<SearchHit> newData) {
    data.clear();
    data.addAll(newData);
    fireTableDataChanged();
  }

  @Override
  public int getRowCount() {
    return data.size();
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return data.get(rowIndex).getFilename();
    }

    if (columnIndex == 1) {
      return data.get(rowIndex).getScore();
    }

    return null;
  }

  @Override
  public String getColumnName(int col) {
    return columns[col];
  }

  @Override
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

}
