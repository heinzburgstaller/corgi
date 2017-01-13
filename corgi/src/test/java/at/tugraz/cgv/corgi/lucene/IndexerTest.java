/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author heinz
 */
public class IndexerTest {

  public IndexerTest() {
  }

  @Test
  @Ignore
  public void testCreateNewIndex() {
    try {
      Path tempIndex = Files.createTempDirectory("corgi_");
      System.out.println("Temp Index Dir: " + tempIndex.toString());
      Indexer indexer = new Indexer(tempIndex.toString());
      File resourcesDirectory = new File("src/test/resources");
      indexer.index(resourcesDirectory.getAbsolutePath(), true);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
