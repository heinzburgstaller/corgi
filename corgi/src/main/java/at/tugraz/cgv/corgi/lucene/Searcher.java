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
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.BinaryPatternsPyramid;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.imageanalysis.features.global.Gabor;
import net.semanticmetadata.lire.imageanalysis.features.global.JCD;
import net.semanticmetadata.lire.imageanalysis.features.global.JpegCoefficientHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.LocalBinaryPatterns;
import net.semanticmetadata.lire.imageanalysis.features.global.LuminanceLayout;
import net.semanticmetadata.lire.imageanalysis.features.global.OpponentHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.PHOG;
import net.semanticmetadata.lire.imageanalysis.features.global.RotationInvariantLocalBinaryPatterns;
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor;
import net.semanticmetadata.lire.imageanalysis.features.global.SimpleColorHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.Tamura;
import net.semanticmetadata.lire.imageanalysis.features.global.joint.JointHistogram;
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

  public boolean bAUTO_COLOR_CORRELOGRAM;
  public boolean bBINARY_PATTERNS_PYRAMID;
  public boolean bCEDD = true;
  public boolean bSIMPLE_COLOR_HISTOGRAM;
  public boolean bCOLOR_LAYOUT;
  public boolean bEDGE_HISTOGRAM;
  public boolean bFCTH;
  public boolean bGABOR;
  public boolean bJCD;
  public boolean bJOINT_HISTOGRAM;
  public boolean bJPEG_COEFFICIENT_HISTOGRAM;
  public boolean bLOCAL_BINARY_PATTERNS;
  public boolean bLUMINANCE_LAYOUT;
  public boolean bOPPONENT_HISTOGRAM;
  public boolean bPHOG;
  public boolean bROTATION_INVARIANT_LOCAL_BINARY_PATTERNS;
  public boolean bSCALABLE_COLOR;
  public boolean bTAMURA;

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

  public List<ImageItem> findSimilarImages(String imagePath, int featurenumber) throws IOException {
    List<ImageItem> list = new ArrayList<>();
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
    GenericFastImageSearcher g;
    Object feature = CEDD.class;
    if (featurenumber != 0) {
      if (featurenumber == 1) {
        feature = AutoColorCorrelogram.class;
      }
      if (featurenumber == 2) {
        feature = BinaryPatternsPyramid.class;
      }
      if (featurenumber == 3) {
        feature = CEDD.class;
      }
      if (featurenumber == 4) {
        feature = SimpleColorHistogram.class;
      }
      if (featurenumber == 5) {
        feature = ColorLayout.class;
      }
      if (featurenumber == 6) {
        feature = EdgeHistogram.class;
      }
      if (featurenumber == 7) {
        feature = FCTH.class;
      }
      if (featurenumber == 8) {
        feature = Gabor.class;
      }
      if (featurenumber == 9) {
        feature = JCD.class;
      }
      if (featurenumber == 10) {
        feature = JointHistogram.class;
      }
      if (featurenumber == 11) {
        feature = JpegCoefficientHistogram.class;
      }
      if (featurenumber == 12) {
        feature = LocalBinaryPatterns.class;
      }
      if (featurenumber == 13) {
        feature = LuminanceLayout.class;
      }
      if (featurenumber == 14) {
        feature = OpponentHistogram.class;
      }
      if (featurenumber == 15) {
        feature = PHOG.class;
      }
      if (featurenumber == 16) {
        feature = RotationInvariantLocalBinaryPatterns.class;
      }
      if (featurenumber == 17) {
        feature = ScalableColor.class;
      }
      if (featurenumber == 18) {
        feature = Tamura.class;
      }
    }

    g = new GenericFastImageSearcher(30, (Class<? extends GlobalFeature>) feature);
    ImageSearcher searcher = g;
    //ImageSearcher searcher = new GenericFastImageSearcher(2, JCD.class);

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

  private void setAllFeaturesFalse() {
    bAUTO_COLOR_CORRELOGRAM = false;
    bBINARY_PATTERNS_PYRAMID = false;
    bCEDD = false;
    bSIMPLE_COLOR_HISTOGRAM = false;
    bCOLOR_LAYOUT = false;
    bEDGE_HISTOGRAM = false;
    bFCTH = false;
    bGABOR = false;
    bJCD = false;
    bJOINT_HISTOGRAM = false;
    bJPEG_COEFFICIENT_HISTOGRAM = false;
    bLOCAL_BINARY_PATTERNS = false;
    bLUMINANCE_LAYOUT = false;
    bOPPONENT_HISTOGRAM = false;
    bPHOG = false;
    bROTATION_INVARIANT_LOCAL_BINARY_PATTERNS = false;
    bSCALABLE_COLOR = false;
    bTAMURA = false;
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
