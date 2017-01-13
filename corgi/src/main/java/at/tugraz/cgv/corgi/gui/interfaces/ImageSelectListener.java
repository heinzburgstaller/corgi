/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui.interfaces;

import at.tugraz.cgv.corgi.gui.event.ImageSelectEvent;
import java.util.EventListener;

/**
 *
 * @author heinz
 */
public interface ImageSelectListener extends EventListener {

  public void imageSelected(ImageSelectEvent event);

}
