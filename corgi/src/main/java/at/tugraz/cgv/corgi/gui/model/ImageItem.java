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
  private Double distance;

  public ImageItem(String imagePath, String filename) {
    this.imagePath = imagePath;
    this.filename = filename;
  }

  public ImageItem(String imagePath, String filename, Double distance) {
    this.imagePath = imagePath;
    this.filename = filename;
    this.distance = distance;
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

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public String getDocumentName() {
    return filename.replaceAll("_[0-9][0-9][0-9].jpg", ".txt");
  }

}
