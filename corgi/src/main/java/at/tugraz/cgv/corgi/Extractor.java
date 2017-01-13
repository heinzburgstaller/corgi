/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi;

import eu.medsea.mimeutil.MimeUtil;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author sbuersch
 */
public final class Extractor {

  ArrayList jlfiles;

  public Extractor(String directory) {
    jlfiles = indexPDFs(directory);
  }

  public ArrayList indexPDFs(String directory) {

    File dir1 = new File(directory);

    if (dir1.isDirectory()) {
      System.out.println(dir1.getAbsolutePath());
      return getPdfFilesRecursively(dir1);
    }

    return null;
  }

  public int extractText(String targetDir) {

    File theDir = new File(targetDir);
    if (!theDir.exists()) {
      theDir.mkdir();
    }

    if (jlfiles == null) {
      return 0;
    }

    if (jlfiles.isEmpty()) {
      return 0;
    }

    Iterator<File> itr = jlfiles.iterator();
    while (itr.hasNext()) {
      try {
        File file = itr.next();

        System.out.println("Extracting " + file.getName());
        try (PDDocument document = PDDocument.load(file)) {
          PDFTextStripper pdfStripper = new PDFTextStripper();
          String text = pdfStripper.getText(document);
          String pdf_file_name = file.getName();
          String file_name = targetDir + FilenameUtils.removeExtension(pdf_file_name);
          File txt_file = new File(file_name + ".txt");
          txt_file.createNewFile();
          try (PrintWriter out = new PrintWriter(txt_file)) {
            out.write(text);
          }
          extractImages(document, file_name);
        }
      } catch (IOException ex) {
        Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    return 0;
  }

  private void extractImages(PDDocument document, String filename) throws IOException {
    int counter = 0;

    for (PDPage page : document.getPages()) {
      for (RenderedImage image : getImagesFromResources(page.getResources())) {
        String imageFilename = String.format(filename + "_%03d.jpg", ++counter);
        File outputfile = new File(imageFilename);
        ImageIO.write(image, "jpg", outputfile);
      }
    }

  }

  private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
    List<RenderedImage> images = new ArrayList<>();

    for (COSName xObjectName : resources.getXObjectNames()) {
      PDXObject xObject = resources.getXObject(xObjectName);

      if (xObject instanceof PDFormXObject) {
        images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
      } else if (xObject instanceof PDImageXObject) {
        images.add(((PDImageXObject) xObject).getImage());
      }
    }

    return images;
  }

  private ArrayList getPdfFilesRecursively(File dir) {
    ArrayList al_files = new ArrayList();

    File[] files_ar = dir.listFiles();

    for (File file : files_ar) {

      if (file.isDirectory()) {
        al_files.addAll(getPdfFilesRecursively(file));

      } else {
        if (MimeUtil.getExtension(file).equalsIgnoreCase("pdf")) {
          al_files.add(file);
        }
      }
    }
    return al_files;
  }

}
