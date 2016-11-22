/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author bmcclint
 */
public class TableView extends JFrame {
  private TableInfo ti;
  private JLabel tnLabel;
  private JComboBox tnJCombo;
  private JTable table;
  private JTextArea msgBox;
  private JScrollPane msgPane;
  private JScrollPane jScrollPane1;

  public TableView(TableInfo ti) {
    super();
    this.ti = ti;
    initGUI();
  }
  
  private void initGUI() {
    try {
      {
        this.tnLabel = new JLabel();
        this.getContentPane().add(this.tnLabel);
        this.tnLabel.setText("Table Name: ");
        this.tnLabel.setBounds(7, 0, 91, 28);
      }
      {
        ComboBoxModel tnJComboModel = new DefaultComboBoxModel(ti.listTableName());
        this.tnJCombo = new JComboBox();
        this.getContentPane().add(this.tnJCombo);
        this.tnJCombo.setModel(tnJComboModel);
        this.tnJCombo.setBounds(7, 28, 211, 28);
        this.tnJCombo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            try {
              tnJComboActionPerformed(evt);
            } catch (SQLException ex) {
              Logger.getLogger(TableView.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        });
      }
      {
        TableModel tableModel = new DefaultTableModel(
                new String[][] {{" ", " "}}, 
                new String[] {"Column 1", "Column 2" });
        this.table = new JTable();
        this.table.setModel(tableModel);
        this.table.setBounds(7, 56, 826, 357);
      }
      {
        this.jScrollPane1 = new JScrollPane();
        this.getContentPane().add(this.jScrollPane1);
        this.jScrollPane1.setBounds(7, 98, 861, 329);
      }
      {
        this.msgPane = new JScrollPane();
        this.getContentPane().add(this.msgPane);
        this.msgPane.setBounds(245, 0, 623, 91);
        {
          this.msgBox = new JTextArea();
          this.msgPane.setViewportView(this.msgBox);
          this.msgBox.setText("messages from the database");
        }
      }
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.getContentPane().setLayout(null);
      this.pack();
      this.setSize(883, 485);
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void tnJComboActionPerformed(ActionEvent evt) throws SQLException {
    String chosenTable = (String) this.tnJCombo.getSelectedItem();
    try {
      ResultSet rs = ti.getTable(chosenTable);
      Vector res = ti.resultSet2Vector(rs);
      TableModel tableModel = new DefaultTableModel(res, ti.getTitleAsVector(rs));
      this.table.setModel(tableModel);
      this.msgBox.append("\nNumber of records in " + chosenTable + " is " + res.size());
    }
    catch (SQLException sqle) {
      this.msgBox.append("\n" + sqle.toString());
    }
  }
  
  public static void main(String[] args) throws SQLException {
    if (args.length < 2) {
      System.out.println("usage: TableInfo db-username db-password");
      System.exit(1);
    }
    String username = "brandon";
    String password = "Obeytdojtyl7";
    TableInfo ti = new TableInfo(username, password);
    TableView inst = new TableView(ti);
    inst.setVisible(true);
  }
}
