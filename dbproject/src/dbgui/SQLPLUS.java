/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
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
public class SQLPLUS extends JFrame {
  private JTextArea stmt;
  private JTextArea message;
  private JScrollPane msgPane;
  private JScrollPane rsPane;
  private JScrollPane stmtPane;
  private JLabel stmtLabel;
  private JButton exec;
  private JTable rsTable;
  
  private TableInfo ti;
  

  public SQLPLUS(TableInfo ti) {
    super();
    this.ti = ti;
    initGUI();
  }
  
  private void initGUI() {
    try {
      {
        this.message = new JTextArea();
        this.getContentPane().add(this.message);
        this.message.setText("database message");
        this.message.setBounds(42, 154, 623, 84);
      }
      {
        this.msgPane = new JScrollPane();
        this.getContentPane().add(this.msgPane);
        this.msgPane.setBounds(35, 147, 658, 98);
        this.msgPane.setViewportView(this.message);
        this.message.setPreferredSize(new Dimension(655, 55));
      }
      {
        this.exec = new JButton();
        this.getContentPane().add(this.exec);
        this.exec.setText("Execute");
        this.setBounds(35, 595, 161, 35);
        this.exec.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            try {
              execActionPerformed(evt);
            } catch (SQLException ex) {
              Logger.getLogger(SQLPLUS.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        });
      }
      {
        this.rsPane = new JScrollPane();
        this.getContentPane().add(this.rsPane);
        this.rsPane.setBounds(35, 252, 658, 329);
        {
          TableModel tablModel = new DefaultTableModel(
                  new String[][] {{" ", " "}}, new String[] {"", ""});
          this.rsTable = new JTable();
          this.rsPane.setViewportView(this.rsTable);
          this.rsTable.setModel(tablModel);
          this.rsTable.setPreferredSize(new Dimension(658, 308));
        }
      }
      {
        this.stmtLabel = new JLabel();
        this.getContentPane().add(this.stmtLabel);
        this.stmtLabel.setText("Your SQL statement");
        this.stmtLabel.setBounds(35, 7, 154, 28);
      }
      {
        this.stmtPane = new JScrollPane();
        this.getContentPane().add(this.stmtPane);
        this.stmtPane.setBounds(35, 35, 658, 105);
        {
          this.stmt = new JTextArea();
          this.stmtPane.setViewportView(this.stmt);
          this.stmt.setText("");
          this.stmt.setBounds(35, 112, 651, 28);
          this.stmt.setPreferredSize(new Dimension(654, 102));
        }
      }
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.getContentPane().setLayout(null);
      this.pack();
      this.setSize(722, 671);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void execActionPerformed(ActionEvent evt) throws SQLException {
    String stmt2 = stmt.getText();
    String command = stmt2.trim().split(" ")[0];
    int numRow = 0;
    try {
      if (command.toLowerCase().equals("select")) {
        ResultSet rs = ti.runSQLQuery(stmt.getText());
        TableModel tableModel = new DefaultTableModel(ti.resultSet2Vector(rs), 
                ti.getTitleAsVector(rs));
        this.rsTable.setModel(tableModel);
      }
      else {
        numRow = ti.runUpdate(stmt.getText());
        if (numRow > 0) {
          if (command.toLowerCase().equals("insert")) {
            this.message.setText(numRow + " row(s) " + command.toLowerCase() 
                    + "ed.");
          }
          else {
            this.message.setText(numRow + " row(s) " + command.toLowerCase() 
                    + "ed.");
          }
        }
        else {
          if (command.toLowerCase().equals("insert")) {
            this.message.setText("Now row was " + command.toLowerCase() + "ed.");
          }
          else {
            this.message.setText("No row was " + command.toLowerCase() + "ed.");
          }
        }
      }
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
    
  public static void main(String[] args) throws SQLException {
    if (args.length < 2) {
      System.out.println("usage: SQLPLUS db-username db-password");
    }
    TableInfo ti = new TableInfo(args[0], args[1]);
    SQLPLUS sp = new SQLPLUS(ti);
    sp.setVisible(true);
  }
 }
