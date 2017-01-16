package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.gui.event.ImageSelectEvent;
import at.tugraz.cgv.corgi.gui.model.ImageItem;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author heinz
 */
public class ImageContainer extends JPanel implements MouseListener{

  private BufferedImage image;
  private JLabel headline;
  private JLabel property1;
  private JLabel property2;
  private final ImageBrowser imageBrowser;
  private final ImageItem imageItem;
  private boolean chosen = false;

  public ImageContainer(ImageBrowser imageBrowser, ImageItem imageItem) {
    this.imageBrowser = imageBrowser;
    this.imageItem = imageItem;

    try {
      image = ImageIO.read(new File(imageItem.getImagePath()));
      JLabel picLabel = new JLabel(new ImageIcon(image.getScaledInstance(200, -1, Image.SCALE_FAST)));
      picLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
      picLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          imageBrowser.dispatchImageSelectEvent(new ImageSelectEvent(this, imageItem));
        }
      });

      JPanel infoPanel = new JPanel();
      infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

      headline = new JLabel(imageItem.getFilename());
      property1 = new JLabel("Document: " + imageItem.getDocumentName());

      infoPanel.add(headline);
      infoPanel.add(property1);
      if (imageItem.getDistance() != null) {
        DecimalFormat df = new DecimalFormat("#0.00");
        property2 = new JLabel("Distance: " + df.format(imageItem.getDistance()));
        infoPanel.add(property2);
      }

      add(picLabel, BorderLayout.CENTER);
      add(infoPanel, BorderLayout.EAST);
      picLabel.addMouseListener(this);
      infoPanel.addMouseListener(this);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    
    if(chosen){
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(new Color(166, 149, 149));
      g2.setStroke(new BasicStroke(10));
      g2.drawRect(0, 0, this.getWidth(), this.getHeight());
     
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    chosen = !chosen;
     this.repaint();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    chosen = !chosen;
     this.repaint();
  }

}
