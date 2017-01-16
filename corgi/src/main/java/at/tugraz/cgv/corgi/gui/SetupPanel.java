/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.Extractor;
import at.tugraz.cgv.corgi.lucene.Indexer;
import at.tugraz.cgv.corgi.util.PropertyLoader;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author heinz
 */
public class SetupPanel extends JPanel {

  private final JPanel setupPanel;
  private final JTextField tfPdfPath;
  private final JTextField tfTxtPath;
  private final JTextField tfIndexPath;
  private final JButton btnChoosePdfPath;
  private final JButton btnChooseTxtPath;
  private final JButton btnChooseIndexPath;
  private final JButton btnExtract;
  private final JButton btnIndex;
  private final JButton btnExtractAndIndex;

  public SetupPanel() {
    super(new BorderLayout());
    BufferedImage image = null;
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      File file = new File(classLoader.getResource("Logo.png").getFile());
      image = ImageIO.read(file);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    JLabel picLabel = new JLabel(new ImageIcon(image));
    tfPdfPath = new JTextField(PropertyLoader.getPdfPath(), 20);
    tfTxtPath = new JTextField(PropertyLoader.getTxtPath(), 20);
    tfIndexPath = new JTextField(PropertyLoader.getIndexPath(), 20);
    tfPdfPath.setEditable(false);
    tfTxtPath.setEditable(false);
    tfIndexPath.setEditable(false);
    btnChoosePdfPath = new JButton("Choose...");
    btnChooseTxtPath = new JButton("Choose...");
    btnChooseIndexPath = new JButton("Choose...");
    btnExtract = new JButton("Extract!");
    btnIndex = new JButton("Index!");
    btnExtractAndIndex = new JButton("Extract and index!");

    setupPanel = new JPanel(new GridBagLayout());

    addItem(new JLabel("PDF Path:"), 0, 0, 1, 1, GridBagConstraints.EAST);
    addItem(tfPdfPath, 1, 0, 1, 1, GridBagConstraints.WEST);
    addItem(btnChoosePdfPath, 2, 0, 1, 1, GridBagConstraints.WEST);
    addItem(btnExtract, 3, 0, 1, 1, GridBagConstraints.WEST);

    addItem(new JLabel("Text Path:"), 0, 1, 1, 1, GridBagConstraints.EAST);
    addItem(tfTxtPath, 1, 1, 1, 1, GridBagConstraints.WEST);
    addItem(btnChooseTxtPath, 2, 1, 1, 1, GridBagConstraints.WEST);
    addItem(btnIndex, 3, 1, 1, 1, GridBagConstraints.WEST);

    addItem(new JLabel("Index Path:"), 0, 2, 1, 1, GridBagConstraints.EAST);
    addItem(tfIndexPath, 1, 2, 1, 1, GridBagConstraints.WEST);
    addItem(btnChooseIndexPath, 2, 2, 1, 1, GridBagConstraints.WEST);
    addItem(btnExtractAndIndex, 3, 2, 1, 1, GridBagConstraints.WEST);

    add(picLabel, BorderLayout.NORTH);
    add(setupPanel, BorderLayout.CENTER);

    btnChoosePdfPath.addActionListener((e) -> {
      String path = choosePath();
      if (path != null) {
        tfPdfPath.setText(path);
      }
    });

    btnChooseTxtPath.addActionListener((e) -> {
      String path = choosePath();
      if (path != null) {
        tfTxtPath.setText(path);
      }
    });

    btnChooseIndexPath.addActionListener((e) -> {
      String path = choosePath();
      if (path != null) {
        tfIndexPath.setText(path);
      }
    });

    btnExtract.addActionListener((e) -> {
      MainFrame mf = (MainFrame) getTopLevelAncestor();
      mf.setBusyCursor();
      Extractor extractor = new Extractor(tfPdfPath.getText());
      extractor.extractText(tfTxtPath.getText());
      mf.setDefaultCursor();
    });

    btnIndex.addActionListener((e) -> {
      MainFrame mf = (MainFrame) getTopLevelAncestor();
      mf.setBusyCursor();
      Indexer indexer = new Indexer(tfIndexPath.getText());
      try {
        indexer.index(tfTxtPath.getText(), true);
      } catch (IOException ex) {
        ex.printStackTrace();
      } finally {
        mf.setDefaultCursor();
      }
    });

    btnExtractAndIndex.addActionListener((e) -> {
      MainFrame mf = (MainFrame) getTopLevelAncestor();
      mf.setBusyCursor();
      Extractor extractor = new Extractor(tfPdfPath.getText());
      extractor.extractText(tfTxtPath.getText());
      Indexer indexer = new Indexer(tfIndexPath.getText());
      try {
        indexer.index(tfTxtPath.getText(), true);
      } catch (IOException ex) {
        ex.printStackTrace();
      } finally {
        mf.setDefaultCursor();
      }
    });
  }

  private String choosePath() {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new java.io.File("."));
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setAcceptAllFileFilterUsed(false);

    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().toString();
    }

    return null;
  }

  private void addItem(JComponent c, int x, int y, int width, int height, int align) {
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = x;
    gc.gridy = y;
    gc.gridwidth = width;
    gc.gridheight = height;
    gc.weightx = 100.0;
    gc.weighty = 100.0;
    gc.insets = new Insets(5, 5, 5, 5);
    gc.anchor = align;
    gc.fill = GridBagConstraints.NONE;
    setupPanel.add(c, gc);
  }

  public String getIndexPath() {
    return tfIndexPath.getText();
  }

}
