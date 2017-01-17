/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author sbuersch
 */
public class SearchPropertiePanel extends JPanel {

  JCheckBox jcbautoc = new JCheckBox("AUTO_COLOR_CORRELOGRAM");
  JCheckBox jcbbinary = new JCheckBox("BINARY_PATTERNS_PYRAMID");
  JCheckBox jcbcedd = new JCheckBox("CEDD");
  JCheckBox jcbsimplec = new JCheckBox("SIMPLE_COLOR_HISTOGRAM");
  JCheckBox jcbcolorl = new JCheckBox("COLOR_LAYOUT");
  JCheckBox jcbedgeh = new JCheckBox("EDGE_HISTOGRAM");
  JCheckBox jcbfcth = new JCheckBox("FCTH");
  JCheckBox jcbgabor = new JCheckBox("GABOR");
  JCheckBox jcbjcd = new JCheckBox("JCD");
  JCheckBox jcbjointh = new JCheckBox("JOINT_HISTOGRAM");
  JCheckBox jcbjpeg = new JCheckBox("JPEG_COEFFICIENT_HISTOGRAM");
  JCheckBox jcblocalb = new JCheckBox("LOCAL_BINARY_PATTERNS");
  JCheckBox jcbluminancel = new JCheckBox("LUMINANCE_LAYOUT");
  JCheckBox jcbopponenth = new JCheckBox("OPPONENT_HISTOGRAM");
  JCheckBox jcbphog = new JCheckBox("PHOG");
  JCheckBox jcbrotation = new JCheckBox("ROTATION_INVARIANT_LOCAL_BINARY_PATTERNS");
  JCheckBox jcbscalable = new JCheckBox("SCALABLE_COLOR");
  JCheckBox jcbtamura = new JCheckBox("TAMURA");
  JButton jbok = new JButton("confirm");

  public SearchPropertiePanel() {
    this.setLayout(new GridLayout(3, 0));
    this.add(jcbautoc);
    this.add(jcbbinary);
    this.add(jcbcedd);
    this.add(jcbsimplec);
    this.add(jcbcolorl);
    this.add(jcbedgeh);
    this.add(jcbfcth);
    this.add(jcbgabor);
    this.add(jcbjcd);
    this.add(jcbjointh);
    this.add(jcbjpeg);
    this.add(jcblocalb);
    this.add(jcbluminancel);
    this.add(jcbopponenth);
    this.add(jcbphog);
    this.add(jcbrotation);
    this.add(jcbscalable);
    this.add(jcbtamura);
    this.add(jbok);
    this.setVisible(true);
  }

}
