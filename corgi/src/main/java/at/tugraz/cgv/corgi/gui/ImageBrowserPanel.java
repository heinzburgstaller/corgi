/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.gui.event.ImageSelectEvent;
import at.tugraz.cgv.corgi.gui.model.ImageItem;
import at.tugraz.cgv.corgi.lucene.Searcher;
import at.tugraz.cgv.corgi.util.PropertyLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author heinz
 */
public class ImageBrowserPanel extends JPanel {

  private static final int IMAGES_PER_PAGE = 16;
  private int currentPage = 0;
  private int maxPages;

  private JPanel navPanel;
  private List<ImageItem> images = new ArrayList<>();
  private final ImageBrowser imageBrowser;
  Searcher searcher;
  private JButton btnFirst = new JButton("<<");
  private JButton btnLast = new JButton(">>");
  private JButton btnNext = new JButton(">");
  private JButton btnPrevious = new JButton("<");
  private JLabel navLabel = new JLabel();

  public ImageBrowserPanel() {
    super(new BorderLayout());
    
    navPanel = new JPanel();
    navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
    navPanel.add(btnFirst);
    navPanel.add(btnPrevious);
    navPanel.add(navLabel);
    navPanel.add(btnNext);
    navPanel.add(btnLast);
    navPanel.setBackground(new Color(112, 100, 100));

    searcher = new Searcher(PropertyLoader.getIndexPath() + "/images");
    try {
      images = searcher.findAllImages();
      maxPages = images.size() / IMAGES_PER_PAGE;
    } catch (IndexNotFoundException ine) {
      System.out.println(ine.toString());
    } catch (IOException | ParseException ex) {
      throw new RuntimeException(ex);
    }

    imageBrowser = new ImageBrowser();
    navigate();
    add(new JScrollPane(imageBrowser), BorderLayout.CENTER);
    add(navPanel, BorderLayout.SOUTH);

    this.validate();
    this.repaint();

    btnNext.addActionListener((ActionEvent e) -> {
      if (currentPage < maxPages) {
        currentPage++;
        navigate();
      }
    });

    btnPrevious.addActionListener((ActionEvent e) -> {
      if (currentPage > 0) {
        currentPage--;
        navigate();
      }
    });

    btnFirst.addActionListener((ActionEvent e) -> {
      currentPage = 0;
      navigate();
    });

    btnLast.addActionListener((ActionEvent e) -> {
      currentPage = maxPages;
      navigate();
    });

    imageBrowser.addImageSelectListener((ImageSelectEvent event) -> {
      try {
        MainFrame mf = (MainFrame) getTopLevelAncestor();
        searcher = new Searcher(mf.getSetupPanel().getIndexPath() + "/images");
        mf.setBusyCursor();
        List<ImageItem> result = searcher.findSimilarImages(event.getImageItem().getImagePath());
        mf.setDefaultCursor();

        createFrame(result);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
  }

  private void navigate() {
    int from = currentPage * IMAGES_PER_PAGE;
    int to = currentPage * IMAGES_PER_PAGE + IMAGES_PER_PAGE;
    if (to > images.size()) {
      to = images.size();
    }
    imageBrowser.addImages(images.subList(from, to));
    navLabel.setText("  Page " + (currentPage + 1) + " of " + (maxPages + 1) + "  ");
  }

  public static void createFrame(List<ImageItem> result) {
    EventQueue.invokeLater(() -> {
      JFrame frame = new JFrame("Test");
      frame.setSize(800, 600);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setLocationRelativeTo(null);

      ImageBrowser browser = new ImageBrowser();
      browser.addImages(result);

      frame.add(new JScrollPane(browser), BorderLayout.CENTER);
      frame.setVisible(true);
      frame.setResizable(true);
      frame.validate();
      frame.repaint();
    });
  }
}
