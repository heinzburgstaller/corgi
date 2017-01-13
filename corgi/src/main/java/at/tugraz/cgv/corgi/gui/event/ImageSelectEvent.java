/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui.event;

import at.tugraz.cgv.corgi.gui.model.ImageItem;
import java.util.EventObject;

/**
 *
 * @author heinz
 */
public class ImageSelectEvent extends EventObject {

  private final ImageItem imageItem;

  public ImageSelectEvent(Object source, ImageItem imageItem) {
    super(source);
    this.imageItem = imageItem;
  }

  public ImageItem getImageItem() {
    return imageItem;
  }

}
