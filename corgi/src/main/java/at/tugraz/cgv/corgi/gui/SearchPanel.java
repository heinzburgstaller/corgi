/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.gui.model.SearchTableModel;
import at.tugraz.cgv.corgi.lucene.SearchHit;
import at.tugraz.cgv.corgi.lucene.Searcher;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author heinz
 */
public class SearchPanel extends JPanel {

  private JPanel actionPanel;
  private JTextField txtSearch;
  private JButton btnSearch;
  private JScrollPane scrollPane;
  private JTable table;
  private final SearchTableModel model = new SearchTableModel();

  public SearchPanel() {
    initUI();
  }

  private void initUI() {
    actionPanel = new JPanel();
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
    txtSearch = new JTextField(25);
    btnSearch = new JButton("Search");
    table = new JTable(model);
    scrollPane = new JScrollPane(table);
    table.getColumnModel().getColumn(0).setPreferredWidth(200);
    table.getColumnModel().getColumn(1).setPreferredWidth(40);

    actionPanel.add(txtSearch);
    actionPanel.add(btnSearch);

    add(actionPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    btnSearch.addActionListener(a -> {
      btnSearchClicked();
    });
  }

  private void btnSearchClicked() {

    try {
      MainFrame mf = (MainFrame) getTopLevelAncestor();
      Searcher searcher = new Searcher(mf.getSetupPanel().getIndexPath());
      mf.setBusyCursor();
      List<SearchHit> result = new ArrayList<>();
      if (txtSearch.getText().trim().length() > 0) {
        result = searcher.search(txtSearch.getText());
      }
      model.update(result);
      mf.setDefaultCursor();
    } catch (IOException | ParseException ex) {
      ex.printStackTrace();
    }
  }

}
