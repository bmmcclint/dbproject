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
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

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
            execActionPerformed(evt);
          }
        });
      }
      {
        
      }
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
