/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui.model;

/**
 *
 * @author heinz
 */
public class ImageItem {

  private String imagePath;
  private String filename;

  public ImageItem(String imagePath, String filename) {
    this.imagePath = imagePath;
    this.filename = filename;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

}
