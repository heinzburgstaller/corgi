/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

  private static final String PROP_FILE = "/config.properties";

  public static String getProperty(String key) {
    Properties props = new Properties();
    InputStream stream = PropertyLoader.class
            .getResourceAsStream(PROP_FILE);
    try {
      props.load(stream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return props.getProperty(key);
  }

  public static String getPath(String key) {
    return getProperty(key).replaceFirst("^~", System.getProperty("user.home"));
  }

  public static String getPdfPath() {
    return getPath("pdf_path");
  }

  public static String getIndexPath() {
    return getPath("index_path");
  }

}
