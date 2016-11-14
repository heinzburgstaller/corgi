/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi;

import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
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
                    String txt_file_name = targetDir + FilenameUtils.removeExtension(pdf_file_name) + ".txt";
                    File txt_file = new File(txt_file_name);
                    txt_file.createNewFile();
                    try (PrintWriter out = new PrintWriter(txt_file)) {
                        out.write(text);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
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
