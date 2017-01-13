package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.gui.event.ImageSelectEvent;
import at.tugraz.cgv.corgi.gui.interfaces.ImageSelectListener;
import at.tugraz.cgv.corgi.gui.layouts.WrapLayout;
import at.tugraz.cgv.corgi.gui.model.ImageItem;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author heinz
 */
public class ImageBrowser extends JPanel {

  protected EventListenerList imageSelectListenerList = new EventListenerList();

  public ImageBrowser() {
    super(new WrapLayout());
  }

  public void addImages(List<ImageItem> images) {
    this.removeAll();
    images.forEach((imageItem) -> {
      this.add(new ImageContainer(this, imageItem));
    });
    this.validate();
    this.repaint();
  }

  public void removeAllImages() {
    this.removeAll();
    this.validate();
    this.repaint();
  }

  public void addImageSelectListener(ImageSelectListener listener) {
    imageSelectListenerList.add(ImageSelectListener.class, listener);
  }

  public void removeImageSelectListener(ImageSelectListener listener) {
    imageSelectListenerList.remove(ImageSelectListener.class, listener);
  }

  public void dispatchImageSelectEvent(ImageSelectEvent event) {
    Object[] listeners = imageSelectListenerList.getListenerList();
    for (int i = 0; i < listeners.length; i = i + 2) {
      if (listeners[i] == ImageSelectListener.class) {
        ((ImageSelectListener) listeners[i + 1]).imageSelected(event);
      }
    }
  }

}
