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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author heinz
 */
public class ImageBrowserPanel extends JPanel implements ActionListener {

  private static final int IMAGES_PER_PAGE = 16;
  private int currentPage = 0;
  private int maxPages;

  private JPanel navPanel;
  private List<ImageItem> images = new ArrayList<>();
  private ImageBrowser imageBrowser;
  Searcher searcher;
  private JButton btnFirst = new JButton("<<");
  private JButton btnLast = new JButton(">>");
  private JButton btnNext = new JButton(">");
  private JButton btnPrevious = new JButton("<");
  private JLabel navLabel = new JLabel();
  private JPanel jpsearch = new JPanel();
  private JLabel jlsearch = new JLabel("Document: ");
  private JTextField jtfsearch = new JTextField("", 30);
  private JButton jbsearch = new JButton("GO");

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
    navigate(false);
    add(new JScrollPane(imageBrowser), BorderLayout.CENTER);
    add(navPanel, BorderLayout.SOUTH);

    jpsearch.setLayout(new FlowLayout());
    jpsearch.add(jlsearch);
    jpsearch.add(jtfsearch);
    jpsearch.add(jbsearch);
    jbsearch.addActionListener(this);
    this.add(jpsearch, BorderLayout.NORTH);

    this.validate();
    this.repaint();

    btnNext.addActionListener((ActionEvent e) -> {
      if (currentPage < maxPages) {
        currentPage++;
        navigate(false);
      }
    });

    btnPrevious.addActionListener((ActionEvent e) -> {
      if (currentPage > 0) {
        currentPage--;
        navigate(false);
      }
    });

    btnFirst.addActionListener((ActionEvent e) -> {
      currentPage = 0;
      navigate(false);
    });

    btnLast.addActionListener((ActionEvent e) -> {
      currentPage = maxPages;
      navigate(false);
    });

    imageBrowser.addImageSelectListener((ImageSelectEvent event) -> {
      try {
        MainFrame mf = (MainFrame) getTopLevelAncestor();
        searcher = new Searcher(mf.getSetupPanel().getIndexPath() + "/images");
        mf.setBusyCursor();
        List<ImageItem> result = searcher.findSimilarImages(event.getImageItem().getImagePath());
        mf.setDefaultCursor();

        createFrame(result, event.getImageItem().getFilename());
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
  }

  private void navigate(boolean usedocname) {
    int from = currentPage * IMAGES_PER_PAGE;
    int to = currentPage * IMAGES_PER_PAGE + IMAGES_PER_PAGE;
    if (to > images.size()) {
      to = images.size();
    }
    if (!usedocname) {
      imageBrowser.addImages(images.subList(from, to));
    } else {
      List<ImageItem> sublist = new ArrayList<>();
      for (ImageItem item : images) {
        if (item.getDocumentName().contains(jtfsearch.getText())) {
          sublist.add(item);
        }
      }
      from = currentPage * IMAGES_PER_PAGE;
      to = currentPage * IMAGES_PER_PAGE + IMAGES_PER_PAGE;
      if (to > sublist.size()) {
        to = sublist.size();
      }
      imageBrowser.addImages(sublist.subList(from, to));
    }
    navLabel.setText("  Page " + (currentPage + 1) + " of " + (maxPages + 1) + "  ");
  }

  public static void createFrame(List<ImageItem> result, String imageName) {
    EventQueue.invokeLater(() -> {
      JFrame frame = new JFrame("Search results for: " + imageName);
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

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals((jbsearch))) {
      this.navigate(true);
    }
  }
}
