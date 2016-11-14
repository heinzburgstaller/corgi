/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.corgi.gui;

import at.tugraz.cgv.corgi.util.PropertyLoader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author heinz
 */
public class MainFrame extends JFrame {

    JPanel jpextract;
    JPanel jpsearch = new JPanel();

    public MainFrame() {
        initUI();
        initTabbedPanel();
    }

    private void initTabbedPanel() {
        JTabbedPane tabpanel = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        
        jpextract = new ExtractPanel(PropertyLoader.getTxtPath());
        //TODO: create Panel for search
        
        tabpanel.add("Extract", jpextract);
        tabpanel.add("Search", jpsearch);
        this.add(tabpanel);
    }

    private void initUI() {
        setTitle("Corgi GUI");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
