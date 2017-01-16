/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.gui.model.SearchTableModel;
import at.tugraz.cgv.corgi.lucene.SearchHit;
import at.tugraz.cgv.corgi.lucene.Searcher;
import at.tugraz.cgv.corgi.lucene.Searcher.Ranking;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
  private JPanel jpranking;
  private final SearchTableModel model = new SearchTableModel();
  JRadioButton rankDefault = new JRadioButton("Default(TFIDF)");
  JRadioButton rankAbsolute = new JRadioButton("Absolute Occurences");
  JRadioButton rankbm25 = new JRadioButton("BM25");
  JRadioButton rankctfidf = new JRadioButton("Custom TFIDF");
  Searcher searcher;

  Searcher.Ranking rank = Ranking.DEFAULT;

  public SearchPanel() {
    super(new BorderLayout());
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

    jpranking = new JPanel();
    jpranking.setLayout(new GridLayout(3, 1));
    jpranking.add(rankDefault);
    jpranking.add(rankAbsolute);
    jpranking.add(rankbm25);
    jpranking.add(rankctfidf);

    rankDefault.setSelected(true);

    ButtonGroup group = new ButtonGroup();
    group.add(rankDefault);
    group.add(rankAbsolute);
    group.add(rankbm25);
    group.add(rankctfidf);

    rankDefault.addActionListener((ActionEvent e) -> {
      rank = Searcher.Ranking.DEFAULT;
    });
    rankAbsolute.addActionListener((ActionEvent e) -> {
      rank = Searcher.Ranking.ABSOLUTE;
    });
    rankbm25.addActionListener((ActionEvent e) -> {
      rank = Searcher.Ranking.BM25;
    });
    rankctfidf.addActionListener((ActionEvent e) -> {
      rank = Searcher.Ranking.CUSTOM_TFIDF;
    });

    add(actionPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(jpranking, BorderLayout.SOUTH);

    btnSearch.addActionListener(a -> {
      btnSearchClicked();
    });
  }

  private void btnSearchClicked() {

    try {
      MainFrame mf = (MainFrame) getTopLevelAncestor();
      searcher = new Searcher(mf.getSetupPanel().getIndexPath(), rank);
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
