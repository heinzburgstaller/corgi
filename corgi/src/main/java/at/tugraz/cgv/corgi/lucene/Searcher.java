/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.lucene;

;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
        //


/**
 *
 * @author heinz
 */


public class Searcher {

  private final String indexPath;
  private Ranking ranking = Ranking.DEFAULT;

  public Searcher(String indexPath, Ranking rank) {
    this.indexPath = indexPath;
    this.ranking = rank;
  }
  
  public enum Ranking {
    DEFAULT, ABSOLUTE, BM25 
  }
  
  public void setRankingMode(Ranking rank){
    ranking = rank;
  }

  public List<SearchHit> search(String queryString) throws IOException, ParseException {
    List<SearchHit> result = new ArrayList<>();
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();

    QueryParser parser = new QueryParser("contents", analyzer);
    Query query = parser.parse(queryString);

    switch(ranking){
            case DEFAULT:
              searcher.setSimilarity(new ClassicSimilarity());
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
