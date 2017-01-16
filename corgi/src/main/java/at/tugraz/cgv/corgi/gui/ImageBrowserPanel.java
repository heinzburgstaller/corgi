/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.gui.model.ImageItem;
import at.tugraz.cgv.corgi.lucene.Searcher;
import at.tugraz.cgv.corgi.util.PropertyLoader;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author heinz
 */
public class ImageBrowserPanel extends JPanel {
  
  private final ImageBrowser imageBrowser;
  Searcher searcher;
  
  public ImageBrowserPanel() {
    super(new BorderLayout());
    
    searcher = new Searcher(PropertyLoader.getIndexPath());
    List<ImageItem> images = new ArrayList<>();
    try {
      images = searcher.findAllImages();
    } catch (IOException | ParseException ex) {
      throw new RuntimeException(ex);
    }
    
    imageBrowser = new ImageBrowser();
    imageBrowser.addImages(images.subList(0, 20));
    add(new JScrollPane(imageBrowser), BorderLayout.CENTER);
    
    this.validate();
    this.repaint();
  }
}
