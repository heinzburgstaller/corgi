/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi;

import at.tugraz.cgv.corgi.gui.MainFrame;
import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author heinz
 */
public class Starter {

  public static void main(String[] args) {
    setNimbus();

    EventQueue.invokeLater(() -> {
      MainFrame mainFrame = new MainFrame();
      mainFrame.setVisible(true);
    });
  }

  public static void setNimbus() {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }
  }
}
