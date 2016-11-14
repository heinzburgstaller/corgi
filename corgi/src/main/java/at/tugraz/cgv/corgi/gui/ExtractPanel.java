/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.Extractor;
import at.tugraz.cgv.corgi.util.PropertyLoader;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author sbuersch
 */
public class ExtractPanel extends JPanel implements ActionListener {

    JTextField jtf_pdf_dir = new JTextField(PropertyLoader.getPdfPath());

    JButton jb_choose_pdf_dir = new JButton("Choose PDF Directory");
    JButton jb_extract_txt = new JButton("Extract!");

    String s_txt_dir;

    public ExtractPanel(String txt_dir) {
        this.setLayout(new FlowLayout());

        s_txt_dir = txt_dir;

        this.add(jtf_pdf_dir);
        this.add(jb_choose_pdf_dir);
        this.add(jb_extract_txt);

        jb_choose_pdf_dir.addActionListener(this);
        jb_extract_txt.addActionListener(this);
    }

    public String getPdfDir() {
        return jtf_pdf_dir.getText();
    }

    public String getTxtDir() {
        return s_txt_dir;
    }

    public void setPdfDir(String dir) {
        this.jtf_pdf_dir.setText(dir);
    }

    public void setTxtDir(String dir) {
        this.s_txt_dir = dir;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();

        if (obj == jb_choose_pdf_dir) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                setPdfDir(chooser.getSelectedFile().toString());
            }
        }
        if (obj == jb_extract_txt) {
            Extractor te = new Extractor(this.getPdfDir());
            te.extractText(s_txt_dir);
        }

    }
}
