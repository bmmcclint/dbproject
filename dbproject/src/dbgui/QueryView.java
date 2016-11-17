/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import dbproject.TableUpdate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author bmcclint
 */
class QueryView extends javax.swing.JFrame {
  private JLabel qnLabel;
  private JLabel valueListLabel;
  private JLabel secondaryValueList;
  private JComboBox queryCombo;
  private JTable table;
  private JButton jbutton1;
  private JButton jbutton2;
  private JScrollPane jScrollPane1;
  
  private Vector tableContent;
  private ResultSet rs;
  private Vector newRow = null;
  
  private TableInfo ti;
  private TableUpdate tu;
  private Connection conn;
  private JComboBox valueList = new JComboBox();
  private JComboBox secondaryList = new JComboBox();
  private String person_code = null;
  private String comp_code= null;
  private String jp_code = null;
  private String job_code = null;
  private String queryValue = null;
  private int queryNum = 0;
  private String missingNum = "0";
  
  private String[] queryList = {"Query 1", "Query 2", "Query 3", "Query 4", 
    "Query 5", "Query 6", "Query 7", "Query 8", "Query 9", "Query 10", 
    "Query 11", "Query 12", "Query 13", "Query 14", "Query 15", "Query 16", 
    "Query 17", "Query 18", "Query 19", "Query 20", "Query 21", "Query 22", 
    "Query 23" ,"Query 24", "Query 25", "Query 26", "Query 27", "Query 28"};

  public QueryView(TableUpdate tu, Connection conn) {
    super();
    this.tu = tu;
    this.ti = tu.getTableInfo();
    this.conn = conn;
    initGUI();
  }
  
  private void initGUI() {
    try {
      {
        this.qnLabel = new JLabel();
        this.getContentPane().add(this.qnLabel);
        this.qnLabel.setText("Query");
        this.qnLabel.setBounds(130, -10, 91, 28);
      }
      {
        this.valueListLabel = new JLabel();
        this.getContentPane().add(this.valueListLabel);
        this.valueListLabel.setText("Primary");
        this.valueListLabel.setBounds(50, 20, 125, 28);
        this.valueListLabel.setVisible(false);
      }
      {
        this.secondaryValueList = new JLabel();
        this.getContentPane().add(this.secondaryValueList);
        this.secondaryValueList.setText("Secondary");
        this.secondaryValueList.setBounds(460, 20, 125, 28);
        this.secondaryValueList.setVisible(false);
      }
      {
        ComboBoxModel queryComboModel = new DefaultComboBoxModel(this.queryList);
        this.queryCombo = new JComboBox();
        this.getContentPane().add(this.queryCombo);
        this.queryCombo.setModel(queryComboModel);
        this.queryCombo.setBounds(200, 0, 455, 20);
        this.queryCombo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            queryComboActionPerformed(evt);
          }
        });
      }
      {
        this.valueList = new JComboBox();
        this.getContentPane().add(this.valueList);
        this.valueList.setBounds(91, 26, 300, 20);
        this.valueList.setVisible(false);
        this.valueList.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            valueListActionPerformed(evt);
          }
        });
      }
      {
        this.secondaryList = new JComboBox();
        this.getContentPane().add(this.secondaryList);
        this.secondaryList.setBounds(536, 26, 300, 20);
        this.secondaryList.setVisible(false);
        this.secondaryList.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            secondaryListActionPerformed(evt);
          }
        });
      }
      {
        this.table = new JTable(new String[][] {{" ", " "}}, new String[] {"Column 1", "Column 2"});
        this.table.setBounds(45, 200, 826, 357);
      }
      {
        this.jScrollPane1 = new JScrollPane(table);
        this.getContentPane().add(this.jScrollPane1);
        this.jScrollPane1.setBounds(7, 49, 861, 378);
      }
      {
        this.jbutton1 = new JButton();
        this.getContentPane().add(this.jbutton1);
        this.jbutton1.setText("Add Row");
        this.jbutton1.setBounds(567, 14, 98, 28);
        this.jbutton1.setVisible(false);
        this.jbutton1.addActionListener(new ActionListener () {
          public void actionPerformed(ActionEvent evt) {
            jbutton1ActionPerformed(evt);
          }
        });
      }
      {
        this.jbutton2 = new JButton();
        this.getContentPane().add(this.jbutton2);
        this.jbutton2.setText("Insert");
        this.jbutton2.setBounds(686, 14, 112, 28);
        this.jbutton2.setVisible(false);
        this.jbutton2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            jbutton2ActionPerformed(evt);
          }
        });
      }
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.getContentPane().setLayout(null);
      this.pack();
      this.setSize(900, 550);
    }
    catch (Exception e) {
      e.printStackTrace();;
    }
  }
  
  private void queryComboActionPerformed(ActionEvent evt) throws SQLException {
    String chosenTable = (String) queryCombo.getSelectedItem();
    this.valueList.setVisible(false);
    this.valueListLabel.setVisible(false);
    this.secondaryList.setVisible(false);
    this.secondaryValueList.setVisible(false);
    
    try {
      Statement stmt = conn.createStatement();
      String query = queryParser(chosenTable);
      rs = stmt.executeQuery(query);
      
      this.tableContent = ti.resultSet2Vector(rs);
      Vector tableTitles = ti.getTitleAsVector(rs);
      TableModel tableModel = new DefaultTableModel(tableContent, tableTitles);
      this.table.setModel(tableModel);
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void ibutton1ActionPerformed(ActionEvent evt) throws SQLException {
    try {
      Vector titles = ti.getTitleAsVector(rs);
      this.newRow = new Vector(table.getColumnCount());
      this.tableContent.add(newRow);
      TableModel tableModel = new DefaultTableModel(tableContent, titles);
      this.table.setModel(tableModel);
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void jbutton2ActionPerformed(ActionEvent evt) throws SQLException {
    int numRow = 0;
    try {
      String tableName = (String) this.queryCombo.getSelectedItem();
      numRow = tu.insertRow(newRow, tableName, rs);
      if (numRow == 0) {
        System.out.println(this.getName() + ":jbutton2ActionPerformed: no row is"
                + " inserted.");
      }
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private String queryParser(String chosenQuery) {
    
  }
}
