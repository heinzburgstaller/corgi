/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.lucene;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

/**
 *
 * @author sbuersch This Similarity Class ignores if a term is more common in
 * all documents or not
 */
public class CustomScoringIgnoreCommon extends SimilarityBase {

  @Override
  protected float score(BasicStats bs, float f, float f1) {
    float idf = (float) (1 + log2(bs.getNumberOfDocuments() / (1 + bs.getDocFreq())));
    float tf = (float) Math.sqrt(f);

    return idf * tf;
  }

  @Override
  public String toString() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
