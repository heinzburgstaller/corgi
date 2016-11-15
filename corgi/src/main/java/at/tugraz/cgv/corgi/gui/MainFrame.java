/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import java.awt.Cursor;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author heinz
 */
public class MainFrame extends JFrame {

  public MainFrame() {
    initUI();
    initTabbedPanel();
  }

  private void initTabbedPanel() {
    JTabbedPane tabpanel = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    tabpanel.add("Setup", new SetupPanel());
    tabpanel.add("Search", new SearchPanel());
    this.add(tabpanel);
  }

  private void initUI() {
    setTitle("Corgi GUI");
    setSize(600, 540);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void setBusyCursor() {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  }

  public void setDefaultCursor() {
    setCursor(Cursor.getDefaultCursor());
  }

}
