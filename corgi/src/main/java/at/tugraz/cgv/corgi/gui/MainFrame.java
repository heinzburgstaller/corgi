/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import java.awt.Cursor;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author heinz
 */
public class MainFrame extends JFrame {

  private SetupPanel setupPanel;
  private SearchPanel searchPanel;

  public MainFrame() {
    initUI();
    initTabbedPanel();
  }

  private void initTabbedPanel() {
    JTabbedPane tabpanel = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    setupPanel = new SetupPanel();
    searchPanel = new SearchPanel();

    tabpanel.add("Setup", setupPanel);
    tabpanel.add("Search", searchPanel);
    this.add(tabpanel);
  }

  private void initUI() {
    setTitle("Corgi GUI");
    setSize(600, 610);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void setBusyCursor() {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  }

  public void setDefaultCursor() {
    setCursor(Cursor.getDefaultCursor());
  }

  public SetupPanel getSetupPanel() {
    return setupPanel;
  }

  public SearchPanel getSearchPanel() {
    return searchPanel;
  }

}
