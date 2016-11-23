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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author bmcclint
 */
public class LoginMenu extends javax.swing.JFrame {
  private JLabel menuTitle;
  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JLabel hostLabel;
  private JLabel portLabel;
  private JLabel sidLabel;
  
  private JButton sqlButton;
  private JButton tableModButton;
  private JButton tableSelectButton;
  private JButton tableViewButton;
  private JButton loginButton;
  private JButton queriesButton;
  private JButton logout;
  
  private JTextField usernameField;
  private JTextField passwordField;
  private JTextField hostField;
  private JTextField portField;
  private JTextField sidField;
  
  private JTextArea msgBox;
  
  private Connection conn;
  private TableInfo ti;
  private TableUpdate tu;
  
  
  public static void main(String[] args) {
    LoginMenu inst = new LoginMenu();
    inst.setVisible(true);
  }
  
  public LoginMenu() {
    super();
    initGUI();
  }
  
  private void initGUI() {
    try {
      {
        this.menuTitle = new JLabel();
        this.getContentPane().add(this.menuTitle);
        this.menuTitle.setText("Menu");
        this.menuTitle.setBounds(91, 133, 63, 28);
      }
      {
        this.usernameLabel = new JLabel();
        this.getContentPane().add(this.usernameLabel);
        this.usernameLabel.setText("Username");
        this.usernameLabel.setBounds(35, 14, 91, 28);
      }
      {
        this.usernameField = new JTextField();
        this.getContentPane().add(this.usernameField);
        this.usernameField.setBounds(133, 14, 119, 28);
      }
      { 
        this.passwordLabel = new JLabel();
        this.getContentPane().add(this.passwordLabel);
        this.passwordLabel.setText("Password");
        this.passwordLabel.setBounds(35, 49, 105, 28);
      }
      {
        this.passwordField = new JTextField();
        this.getContentPane().add(this.passwordField);
        this.passwordField.setBounds(133, 49, 119, 28);
      }
      {
        this.loginButton = new JButton();
        this.getContentPane().add(this.loginButton);
        this.loginButton.setText("Login");
        this.loginButton.setBounds(20, 91, 70, 28);
        this.loginButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
              loginButtonActionPerformed(evt);
          }
        });
      }
      {
        this.logout = new JButton();
        this.getContentPane().add(this.logout);
        this.logout.setText("Exit");
        this.logout.setBounds(120, 91, 70, 28);
        this.logout.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            logoutActionPerformed(evt);
          }
        });
      }
      {
        this.tableViewButton = new JButton();
        this.getContentPane().add(this.tableViewButton);
        this.tableViewButton.setText("View Table");
        this.tableViewButton.setBounds(14, 161, 175, 28);
        this.tableViewButton.setEnabled(false);
        this.tableViewButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            tableViewButtonActionPerformed(evt);
          }
        });
      }
      {
        this.tableSelectButton = new JButton();
        this.getContentPane().add(this.tableSelectButton);
        this.tableSelectButton.setText("Select rows from tables");
        this.tableSelectButton.setBounds(14, 196, 175, 28);
        this.tableSelectButton.setEnabled(false);
        this.tableSelectButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            tableSelectButtonActionPerformed(evt);
          }
        });
      }
      {
        this.tableModButton = new JButton();
        this.getContentPane().add(this.tableModButton);
        this.tableModButton.setText("Modify Tables");
        this.tableModButton.setBounds(14, 231, 175, 28);
        this.tableModButton.setEnabled(false);
        this.tableModButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            tableModButtonActionPerformed(evt);
          }
        });
      }
      {
        this.sqlButton = new JButton();
        this.getContentPane().add(this.sqlButton);
        this.sqlButton.setText("SQL-PLUS");
        this.sqlButton.setBounds(14, 266, 175, 28);
        this.sqlButton.setEnabled(false);
        this.sqlButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt) {
            sqlButtonActionPerformed(evt);
          }
        });
      }
      {
        this.queriesButton = new JButton();
        this.getContentPane().add(this.queriesButton);
        this.queriesButton.setText("Queries");
        this.queriesButton.setBounds(14, 301, 175, 28);
        this.queriesButton.setEnabled(false);
        this.queriesButton.addActionListener(new ActionListener(){
          public void actionPerformed( ActionEvent evt) {
            queriesButtonActionPerformed(evt);
          }
        });
      }
      {
        this.hostLabel = new JLabel();
        this.getContentPane().add(this.hostLabel);
        this.hostLabel.setText("Database Host");
        this.hostLabel.setBounds(287, 14, 105, 28);
      }
      {
        this.hostField = new JTextField();
        this.getContentPane().add(this.hostField);
//        this.hostField.setText("windowsplex.mynetgear.com");
        this.hostField.setBounds(392, 14, 200, 28);
      }
      {
        this.portLabel = new JLabel();
        this.getContentPane().add(this.portLabel);
        this.portLabel.setText("Database Port");
        this.portLabel.setBounds(294, 49, 98, 28);
      }
      {
        this.portField = new JTextField();
        this.getContentPane().add(this.portField);
        this.portField.setText("1521");
        this.portField.setBounds(392, 49, 50, 28);
      }
      {
        this.sidLabel = new JLabel();
        this.getContentPane().add(this.sidLabel);
        this.sidLabel.setText("Database Name (SID)");
        this.sidLabel.setBounds(245, 84, 147, 28);
      }
      {
        this.sidField = new JTextField();
        this.getContentPane().add(this.sidField);
        this.sidField.setText("xe");
        this.sidField.setBounds(392, 84, 50, 20);
      }
      {
        this.msgBox = new JTextArea();
        this.getContentPane().add(this.msgBox);
        this.msgBox.setText("Database Message");
        this.msgBox.setBounds(203, 119, 462, 231);
      }
      this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.getContentPane().setLayout(null);
      this.pack();
      this.setSize(680, 390);
   } catch (Exception e) {
    e.printStackTrace();
    } 
  }
  private void loginButtonActionPerformed(ActionEvent evt) {
    String username = this.usernameField.getText();
    String password = this.passwordField.getText();
    String host = this.hostField.getText();
    String port = this.portField.getText();
    String sid = this.sidField.getText();
    dbaccess dba = new dbaccess(host, port, sid);
    try {
      conn = dba.getDBConnection(username, password);
      tu = new TableUpdate(conn);
      ti = tu.getTableInfo();
      this.tableViewButton.setEnabled(true);
      this.tableModButton.setEnabled(true);
      this.tableSelectButton.setEnabled(true);
      this.sqlButton.setEnabled(true);
      this.queriesButton.setEnabled(true);
    } catch (SQLException sqle) {
      StringWriter strMsg = new StringWriter();
      PrintWriter prtMsg = new PrintWriter(strMsg);
      sqle.printStackTrace();
      this.msgBox.setText(strMsg.toString());
    }
  }
  
  private void tableViewButtonActionPerformed(ActionEvent evt) {
    TableView inst = new TableView(ti);
    inst.setVisible(true);
  }
  
  private void tableSelectButtonActionPerformed(ActionEvent evt) {
    TableSelect inst = new TableSelect(ti);
    inst.setVisible(true);
  }
  
  private void tableModButtonActionPerformed(ActionEvent evt) {
    EditTables inst = new EditTables(tu, conn);
    inst.setVisible(true);
  }
  
  private void sqlButtonActionPerformed(ActionEvent evt) {
    SQLPLUS inst =new SQLPLUS(ti);
    inst.setVisible(true);
  }
  
  private void queriesButtonActionPerformed(ActionEvent evt) {
    QueryView query = new QueryView(tu, conn);
    query.setVisible(true);
  }
  
  private void logoutActionPerformed(ActionEvent evt) {
    System.exit(0);
  }
}

