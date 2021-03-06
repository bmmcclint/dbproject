/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import dbproject.TableUpdate;
import dbproject.dbaccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author bmcclint
 */
public class EditTables extends javax.swing.JFrame {
  private JLabel tnLable;
  private JComboBox tnJCombo;
  private JComboBox primaryCombo;
  private JTable table;
  private JButton jbutton1;
  private JButton jbutton2;
  private JScrollPane jScrollPane1;
  
  private Vector tableContent;
  private ResultSet rs;
  private Vector newRow = new Vector();
  
  private TableInfo ti;
  private TableUpdate tu;
  private java.sql.Connection conn = null;
  
  private String function = null;
  private String[] functionList = {"New Person", "New Job Profile", "New Job", "New Course"};
  private int numOfAtributes = 0;
  
  JTextField[] tableFields = null;
  JLabel[] tableLabesls = null;
  JTextField message;
  
  public EditTables(TableUpdate tu, Connection conn) {
    super();
    this.tu = tu;
    this.ti = tu.getTableInfo();
    this.conn = conn;
    initGUI();
  }
  
  private void initGUI() {
    try {
      {
        this.tnLable = new JLabel();
        this.getContentPane().add(this.tnLable);
        this.tnLable.setText("Function");
        this.tnLable.setBounds(15, 4, 91, 28);
      }
      {
        this.message = new JTextField();
        this.getContentPane().add(this.message);
        this.message.setBounds(400, 100, 200, 100);
        this.message.setVisible(false);
      }
      {
        this.numOfAtributes = 8;
        this.tableFields = new JTextField[this.numOfAtributes];
        this.tableLabesls = new JLabel[this.numOfAtributes];
        for (int i = 0; i < this.numOfAtributes; i++) {
          this.tableFields[i] = new JTextField();
          this.tableLabesls[i] = new JLabel();
          this.getContentPane().add(this.tableFields[i]);
          this.getContentPane().add(this.tableLabesls[i]);
          this.tableFields[i].setBounds(150, 100 + 35 * i, 119, 30);
          this.tableLabesls[i].setBounds(40, 100 + 35 * i, 119, 30);
        }
      }
      {
        ComboBoxModel tnJComboModel = new DefaultComboBoxModel(this.functionList);
        this.tnJCombo = new JComboBox();
        this.getContentPane().add(this.tnJCombo);
        this.tnJCombo.setModel(tnJComboModel);
        this.tnJCombo.setBounds(91, 14, 455, 28);
        this.tnJCombo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            try {
              tnJComboActionPerformed(evt);
            } catch (SQLException ex) {
              Logger.getLogger(EditTables.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        });
      }
      {
        this.primaryCombo = new JComboBox();
        this.getContentPane().add(this.primaryCombo);
        this.primaryCombo.setBounds(91, 45, 455, 28);
        this.primaryCombo.setVisible(false);
      }
      {
        this.table = new JTable(new String[][] {{" ", " "}}, new String[] {"Column 1", "Column 2"});
        this.table.setBounds(21, 56, 826, 357);
      }
      {
        this.jScrollPane1 = new JScrollPane(table);
        this.getContentPane().add(this.jScrollPane1);
        this.jScrollPane1.setBounds(7, 49, 861, 378);
      }
      {
        this.jbutton1 = new JButton();
        this.getContentPane().add(this.jbutton1);
        this.jbutton1.setText("Default");
        this.jbutton1.setBounds(567, 14, 130, 28);
        this.jbutton1.setVisible(false);
        this.jbutton1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            try {
              jbutton1ActionPerformed(evt);
            } catch (SQLException ex) {
              Logger.getLogger(EditTables.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        });
      }
      {
        this.jbutton2 = new JButton();
        this.getContentPane().add(this.jbutton2);
        this.jbutton2.setText("Default");
        this.jbutton2.setBounds(567, 45, 120, 28);
        this.jbutton2.setVisible(false);
        this.jbutton2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            try {
              jbutton2ActionPerformed(evt);
            } catch (SQLException ex) {
              Logger.getLogger(EditTables.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        });
      }
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.getContentPane().setLayout(null);
      this.pack();
      this.setSize(883, 485);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void tnJComboActionPerformed(ActionEvent evt) throws SQLException {
   this.message.setVisible(false);
   this.jbutton1.setVisible(false);
   this.jbutton2.setVisible(false);
   
   for (int i = 0; i < this.numOfAtributes; i++) {
     this.tableFields[i].setVisible(false);
     this.tableLabesls[i].setVisible(false);
   }
   
   String chosenFunction = (String) this.tnJCombo.getSelectedItem();
   String chosenTable = null;
   
   if (chosenFunction.equals("New Person")) {
     this.function = "person";
     chosenTable = "Person";
     Vector tableTitles = null;
     
     try {
       rs = ti.getTable(chosenTable);
       tableTitles = ti.getTitleAsVector(rs);
       System.out.println(tableTitles);
     } catch (SQLException sqle) {
       sqle.printStackTrace();
     }
     
     this.numOfAtributes = tableTitles.size();
     
     for (int i = 0; i < this.numOfAtributes; i++) {
       this.tableLabesls[i].setText((String) tableTitles.elementAt(i));
       this.tableLabesls[i].setVisible(true);
       this.tableFields[i].setVisible(true);
     }
     
     this.jbutton1.setText("Add Person");
     this.jbutton1.setVisible(true);
   }
   else if (chosenFunction.equals("New Job Profile")) {
     this.function = "job_profile";
     chosenTable = "job_profile";
     Vector tableTitles = null;
     
     try {
       rs = ti.getTable(chosenTable);
       tableTitles = ti.getTitleAsVector(rs);
       System.out.println(tableTitles);
     } catch (SQLException sqle) {
       sqle.printStackTrace();
     }
     
     this.numOfAtributes = tableTitles.size();
     
     for (int i = 0; i < this.numOfAtributes; i++) {
       this.tableLabesls[i].setText((String) tableTitles.elementAt(i));
       this.tableLabesls[i].setVisible(true);
       this.tableFields[i].setVisible(true);
     }
     
     this.jbutton1.setText("Add Job Profile");
     this.jbutton1.setVisible(true);
   }
   else if (chosenFunction.equals("New Job")) {
     this.function = "job";
     chosenTable = "job";
     Vector tableTitles = null;
     String[] jobCodes = null;
     
     try {
       rs = ti.getTable(chosenTable);
       tableTitles = ti.getTitleAsVector(rs);
       System.out.println(tableTitles);
       jobCodes = ti.getColumn("job", "job_code");
     }
     catch (SQLException sqle) {
       sqle.printStackTrace();
     }
     
     ComboBoxModel jobComboModel = new DefaultComboBoxModel(jobCodes);
     this.primaryCombo.setModel(jobComboModel);
     this.primaryCombo.setVisible(true);
     
     this.numOfAtributes = tableTitles.size();
     
     for (int i = 0; i < this.numOfAtributes; i++) {
       this.tableLabesls[i].setText((String) tableTitles.elementAt(i));
       this.tableLabesls[i].setVisible(true);
       this.tableFields[i].setVisible(true);
     }
     
     this.jbutton1.setText("Add Course");
     this.jbutton1.setVisible(true);
     this.jbutton2.setText("Set Inactive");
     this.jbutton2.setVisible(true);
   }
   
   try {
     Statement stmt = conn.createStatement();
     rs = ti.getTable(chosenTable);
     this.tableContent = ti.resultSet2Vector(rs);
     Vector tableTitles = ti.getTitleAsVector(rs);
     TableModel tableModel = new DefaultTableModel(this.tableContent, tableTitles);
     this.table.setModel(tableModel);
   }
   catch (SQLException sqle) {
     sqle.printStackTrace();
   }
  }
  
  private void primaryComboActionPerformed(ActionEvent evt) {}
  
  private void jbutton1ActionPerformed(ActionEvent evt) throws SQLException {
    if (this.function.equals("person")) {
      String[] tableValues = new String[this.numOfAtributes];
      for (int i = 0; i < this.numOfAtributes; i++) {
        tableValues[i] = this.tableFields[i].getText();
      }
      Person newPerson = new Person();
      Statement stmt = null;
      try {
        stmt = conn.createStatement();
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
      if (!newPerson.create(conn, stmt, tableValues)) {
        this.message.setText("Failed to add new person");
      }
      else {
        this.message.setText("New person was added successfully to database");
      }
      this.message.setVisible(true);
    }
    
    else if (this.function.equals("job_profile")) {
      String[] tableValues = new String[this.numOfAtributes];
      for (int i = 0; i < this.numOfAtributes; i++) {
        tableValues[i] = this.tableFields[i].getText();
      }
      Job_profile profile = new Job_profile();
      Statement stmt = null;
      try {
        stmt = conn.createStatement();
      }
      catch (SQLException sqle) {
        sqle.printStackTrace();
      }
      
      if(!profile.create(conn, stmt, tableValues)) {
        this.message.setText("Failed to add new job profile");
      }
      else {
        this.message.setText("New job profile successfull added to database");
      }
      this.message.setVisible(true);
    }
    
    else if (this.function.equals("course")) {
      String[] tableValues = new String[this.numOfAtributes];
      for (int i = 0; i < this.numOfAtributes; i++) {
        tableValues[i] = this.tableFields[i].getText();
      }
      Course course = new Course();
      Statement stmt = null;
      try {
        stmt = conn.createStatement();
      }
      catch (SQLException sqle) {
        sqle.printStackTrace();
      }
      
      if (!course.create(conn, stmt, tableValues)) {
        this.message.setText("Failed to add new course to database");
      }
      else {
        this.message.setText("New course was successfully added to database");
      }
      this.message.setVisible(true);
    }
  }
  
  private void jbutton2ActionPerformed(ActionEvent evt) throws SQLException {
     if (function.equals("Course")) {
       Course course = new Course();
       String inactiveCourse = (String) primaryCombo.getSelectedItem();
       Statement stmt = null;
       try {
         stmt = conn.createStatement();
       }
       catch (SQLException sqle) {
         sqle.printStackTrace();
       }
       
       if (!course.inactive(stmt, inactiveCourse)) {
         this.message.setText("Failed to set \ncourse " + inactiveCourse +
                 " to inactive");
       }
       else {
         this.message.setText("Added course " + inactiveCourse + " to inactive courses");
       }
       this.message.setVisible(true);
     }
   }
  
//  public static void main(String[] args) throws SQLException {
//    if (args.length < 2) {
//      System.out.println("usage: java TableInfo db-username db-password");
//      System.exit(1);
//    }
//    dbaccess tc = new dbaccess("windowsplex.mynetgear.com", "1521", "test");
//    Connection conn = tc.getDBConnection(args[0], args[1]);
//    TableUpdate tu = new TableUpdate(conn);
//    EditTables inst = new EditTables(tu, conn);
//    inst.setVisible(true);
//  }
}
