/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.lucene;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.imageio.ImageIO;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author heinz
 */
public class Indexer {

  public final static String FIELD_FILETYPE = "filetype";
  public final static String FIELD_FILENAME = "filename";
  public final static String FIELD_PATH = "path";

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

  public enum Filetype {
    TXT, IMAGE
  };

  private final String indexPath;
  private GlobalDocumentBuilder globalDocumentBuilder;

  public Indexer(String indexPath) {
    this.indexPath = indexPath;
  }

  public void index(String txtPath, boolean create) throws IOException {
    // Creating a CEDD document builder and indexing all files.
    globalDocumentBuilder = new GlobalDocumentBuilder(CEDD.class);

    // and here we add those features we want to extract in a single run:
    if (bAUTO_COLOR_CORRELOGRAM) {
      globalDocumentBuilder.addExtractor(AutoColorCorrelogram.class);
    }
    if (bBINARY_PATTERNS_PYRAMID) {
      globalDocumentBuilder.addExtractor(BinaryPatternsPyramid.class);
    }
    if (bCEDD) {
      globalDocumentBuilder.addExtractor(CEDD.class);
    }
    if (bSIMPLE_COLOR_HISTOGRAM) {
      globalDocumentBuilder.addExtractor(SimpleColorHistogram.class);
    }
    if (bCOLOR_LAYOUT) {
      globalDocumentBuilder.addExtractor(ColorLayout.class);
    }
    if (bEDGE_HISTOGRAM) {
      globalDocumentBuilder.addExtractor(EdgeHistogram.class);
    }
    if (bFCTH) {
      globalDocumentBuilder.addExtractor(FCTH.class);
    }
    if (bGABOR) {
      globalDocumentBuilder.addExtractor(Gabor.class);
    }
    if (bJCD) {
      globalDocumentBuilder.addExtractor(JCD.class);
    }
    if (bJOINT_HISTOGRAM) {
      globalDocumentBuilder.addExtractor(JointHistogram.class);
    }
    if (bJPEG_COEFFICIENT_HISTOGRAM) {
      globalDocumentBuilder.addExtractor(JpegCoefficientHistogram.class);
    }
    if (bLOCAL_BINARY_PATTERNS) {
      globalDocumentBuilder.addExtractor(LocalBinaryPatterns.class);
    }
    if (bLUMINANCE_LAYOUT) {
      globalDocumentBuilder.addExtractor(LuminanceLayout.class);
    }
    if (bOPPONENT_HISTOGRAM) {
      globalDocumentBuilder.addExtractor(OpponentHistogram.class);
    }
    if (bPHOG) {
      globalDocumentBuilder.addExtractor(PHOG.class);
    }
    if (bROTATION_INVARIANT_LOCAL_BINARY_PATTERNS) {
      globalDocumentBuilder.addExtractor(RotationInvariantLocalBinaryPatterns.class);
    }
    if (bSCALABLE_COLOR) {
      globalDocumentBuilder.addExtractor(ScalableColor.class);
    }
    if (bTAMURA) {
      globalDocumentBuilder.addExtractor(Tamura.class);
    }

    Directory dir = FSDirectory.open(Paths.get(indexPath));
    Analyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

    if (create) {
      // Create a new index in the directory, removing any
      // previously indexed documents:
      iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    } else {
      // Add new documents to an existing index:
      iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    try (IndexWriter writer = new IndexWriter(dir, iwc)) {
      indexDocs(writer, Paths.get(txtPath));
    }

    Directory dirImages = FSDirectory.open(Paths.get(indexPath + "/images"));
    Analyzer analyzerImages = new StandardAnalyzer();
    IndexWriterConfig iwcImages = new IndexWriterConfig(analyzerImages);

    try (IndexWriter writer = new IndexWriter(dirImages, iwcImages)) {
      indexImages(writer, Paths.get(txtPath));
    }

    System.out.println("Indexing done!");
  }

  private void indexImages(final IndexWriter writer, Path path) throws IOException {
    if (Files.isDirectory(path)) {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (file.toString().toLowerCase().endsWith(".jpg")) {
            indexImage(writer, file, attrs.lastModifiedTime().toMillis());
          } // don't index files that can't be read.
          return FileVisitResult.CONTINUE;
        }
      });
    } else {
      if (path.toString().toLowerCase().endsWith(".jpg")) {
        indexImage(writer, path, Files.getLastModifiedTime(path).toMillis());
      }
    }
  }

  /**
   * Indexes the given file using the given writer, or if a directory is given,
   * recurses over files and directories found under the given directory.
   *
   * NOTE: This method indexes one document per input file. This is slow. For
   * good throughput, put multiple documents into your input file(s). An example
   * of this is in the benchmark module, which can create "line doc" files, one
   * document per line, using the
   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
   * >WriteLineDocTask</a>.
   *
   * @param writer Writer to the index where the given file/dir info will be
   * stored
   * @param path The file to index, or the directory to recurse into to find
   * files to index
   * @throws IOException If there is a low-level I/O error
   */
  private void indexDocs(final IndexWriter writer, Path path) throws IOException {
    if (Files.isDirectory(path)) {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          try {
            if (!file.toString().toLowerCase().endsWith(".jpg")) {
              indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
            }
          } catch (IOException ignore) {
            // don't index files that can't be read.
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } else {
      if (!path.toString().toLowerCase().endsWith(".jpg")) {
        indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
      }
    }
  }

  private void indexImage(IndexWriter writer, Path file, long lastModified) {
    if (file.toFile().length() == 0) {
      System.out.println("Indexing image: " + file.toString() + ", is empty -> ignore");
      return;
    }

    System.out.println("Indexing image: " + file.toString());
    try {
      BufferedImage img = ImageIO.read(new FileInputStream(file.toString()));
      Document document = globalDocumentBuilder.createDocument(img, file.toString());
      Field filename = new StringField(FIELD_FILENAME, file.toFile().getName(), Field.Store.YES);
      document.add(filename);
      Field pathField = new StringField(FIELD_PATH, file.toString(), Field.Store.YES);
      document.add(pathField);
      Field typeField = new StringField(FIELD_FILETYPE, Filetype.IMAGE.toString(), Field.Store.YES);
      document.add(typeField);
      writer.addDocument(document);
    } catch (IOException e) {
      System.err.println("Error reading image or indexing it.");
      e.printStackTrace();
    }
  }

  /**
   * Indexes a single document
   */
  private void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
    try (InputStream stream = Files.newInputStream(file)) {
      // make a new, empty document
      Document doc = new Document();

      Field filename = new StringField(FIELD_FILENAME, file.toFile().getName(), Field.Store.YES);
      doc.add(filename);
      Field pathField = new StringField(FIELD_PATH, file.toString(), Field.Store.YES);
      doc.add(pathField);
      Field typeField = new StringField(FIELD_FILETYPE, Filetype.TXT.toString(), Field.Store.YES);
      doc.add(typeField);

      // Add the last modified date of the file a field named "modified".
      // Use a LongPoint that is indexed (i.e. efficiently filterable with
      // PointRangeQuery).  This indexes to milli-second resolution, which
      // is often too fine.  You could instead create a number based on
      // year/month/day/hour/minutes/seconds, down the resolution you require.
      // For example the long value 4 would mean
      // February 17, 1, 2-3 PM.
      // doc.add(new LongField("modified", lastModified));
      // Add the contents of the file to a field named "contents".  Specify a Reader,
      // so that the text of the file is tokenized and indexed, but not stored.
      // Note that FileReader expects the file to be in UTF-8 encoding.
      // If that's not the case searching for special characters will fail.
      doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

      if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
        // New index, so we just add the document (no old document can be there):
        System.out.println("adding " + file);
        writer.addDocument(doc);
      } else {
        // Existing index (an old copy of this document may have been indexed) so 
        // we use updateDocument instead to replace the old one matching the exact 
        // path, if present:
        System.out.println("updating " + file);
        writer.updateDocument(new Term("path", file.toString()), doc);
      }
    }
  }

}
