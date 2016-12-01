/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import dbproject.TableUpdate;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Brandon McClinton
 */

public class DBServices extends JFrame {
    private JComboBox select;
    private JTextField dbsdesc;
    private JComboBox options;
    private JLabel selectlabel;
    private JLabel optionlabel;
    private JScrollPane rspane;
    private JTextArea dbsrs;
    private JComboBox valueList;
    private JLabel valueListLabel;
    
    private ResultSet rs;
    private TableInfo ti;
    private TableUpdate tu;
    private Connection conn;
    
    private String comp_code = null;
    private String person_code = null;
    private String job_code = null;
    private String jp_code;
    
    private final String[] optionList = {"Hire Employee", "Job Search", 
        "Qualified Person Search", "Sector Opportunities"};
     
    public DBServices(Connection conn, TableUpdate tu) {
        super();
        this.tu = tu;
        this.ti = tu.getTableInfo();
        this.conn = conn;
        initGUI();
    }
    
    private void initGUI() {
        try {
            {
                this.selectlabel = new JLabel();
                this.getContentPane().add(this.selectlabel);
                this.selectlabel.setText("Select Database Service");
                this.selectlabel.setBounds(7, 133, 63, 50);
            }
            {
                this.optionlabel = new JLabel();
                
            }
        }
    }
    
}
