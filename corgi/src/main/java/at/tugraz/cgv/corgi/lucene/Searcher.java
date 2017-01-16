/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.lucene;

import at.tugraz.cgv.corgi.gui.model.ImageItem;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

  private final String indexPath;
  private Ranking ranking = Ranking.DEFAULT;

  public Searcher(String indexPath) {
    this.indexPath = indexPath;
    this.ranking = Ranking.DEFAULT;
  }

  public Searcher(String indexPath, Ranking rank) {
    this.indexPath = indexPath;
    this.ranking = rank;
  }

  public enum Ranking {
    DEFAULT, ABSOLUTE, BM25, CUSTOM_TFIDF
  }

  public void setRankingMode(Ranking rank) {
    ranking = rank;
  }

  public List<ImageItem> findSimilarImages(String imagePath) throws IOException {
    List<ImageItem> list = new ArrayList<>();
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
    ImageSearcher searcher = new GenericFastImageSearcher(30, CEDD.class);
    BufferedImage img = null;
    File f = new File(imagePath);
    if (f.exists()) {
      img = ImageIO.read(f);
    } else {
      throw new IOException("Image does not exist!");
    }

    // searching with a image file ...
    ImageSearchHits hits = searcher.search(img, reader);
    // searching with a Lucene document instance ...
    for (int i = 0; i < hits.length(); i++) {
      Document doc = reader.document(hits.documentID(i));
      String path = doc.get(Indexer.FIELD_PATH);
      String filename = doc.get(Indexer.FIELD_FILENAME);
      ImageItem imageItem = new ImageItem(path, filename, hits.score(i));
      list.add(imageItem);
    }

    return list;
  }

  public List<ImageItem> findAllImages() throws IOException, ParseException {
    List<ImageItem> list = new ArrayList<>();
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
    IndexSearcher searcher = new IndexSearcher(reader);

    Query q = new TermsQuery(new Term(Indexer.FIELD_FILETYPE, Indexer.Filetype.IMAGE.toString()));

    TopDocs results = searcher.search(q, 2000);
    ScoreDoc[] hits = results.scoreDocs;
    for (ScoreDoc scoreDoc : hits) {
      Document doc = searcher.doc(scoreDoc.doc);
      String path = doc.get(Indexer.FIELD_PATH);
      String filename = doc.get(Indexer.FIELD_FILENAME);
      ImageItem imageItem = new ImageItem(path, filename);
      list.add(imageItem);
    }

    return list;
  }

  public List<SearchHit> search(String queryString) throws IOException, ParseException {
    List<SearchHit> result = new ArrayList<>();
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();

    QueryParser parser = new QueryParser("contents", analyzer);
    Query query = parser.parse(queryString);

    switch (ranking) {//TFIDFSimilarity
      case DEFAULT:
        searcher.setSimilarity(new DefaultSimilarity());
        break;
      case CUSTOM_TFIDF:
        searcher.setSimilarity(new CustomScoringIgnoreCommon());
        break;
      case ABSOLUTE:
        searcher.setSimilarity(new CustomScoringAbsoluteNumber());
        break;
      case BM25:
        searcher.setSimilarity(new BM25Similarity());
        break;
    }

    TopDocs results = searcher.search(query, 100);
    ScoreDoc[] hits = results.scoreDocs;

    //int numTotalHits = results.totalHits;
    //System.out.println(numTotalHits + " total matching documents");
    for (ScoreDoc scoreDoc : hits) {
      SearchHit hit = new SearchHit();
      Document doc = searcher.doc(scoreDoc.doc);
      String path = doc.get("path");
      hit.setFilename(path);
      hit.setScore(new Double(scoreDoc.score));
      result.add(hit);
      //Explanation expl = searcher.explain(query, scoreDoc.doc);
      //System.out.println(expl.toString());
    }

    return result;
  }

}
