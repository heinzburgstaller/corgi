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
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author heinz
 */
public class SearcherTest {

  @Test
  public void testSearch() {
    try {
      Path tempIndex = Files.createTempDirectory("corgi_");
      System.out.println("Temp Index Dir: " + tempIndex.toString());
      Indexer indexer = new Indexer(tempIndex.toString());
      File resourcesDirectory = new File("src/test/resources");
      indexer.index(resourcesDirectory.getAbsolutePath(), true);

      Searcher searcher = new Searcher(tempIndex.toString());
      List<SearchHit> searchResult = searcher.search("Sahil OR Ramesh");
      Assert.assertTrue(searchResult.size() > 0);

      searchResult.forEach(item -> {
        System.out.println(item.getFilename() + ": " + item.getScore());
      });

    } catch (IOException | ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

}
