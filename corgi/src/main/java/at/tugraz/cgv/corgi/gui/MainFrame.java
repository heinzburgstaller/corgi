/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import javax.swing.JFrame;

/**
 *
 * @author heinz
 */
public class MainFrame extends JFrame {

  public MainFrame() {
    initUI();
  }

  private void initUI() {
    setTitle("Corgi GUI");
    setSize(300, 200);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
}
