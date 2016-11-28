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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
class QueryView extends JFrame {
  private JLabel qnLabel;
  private JLabel valueListLabel;
  private JLabel secondaryValueList;
  
  private JTable table;
  private JButton jbutton1;
  private JButton jbutton2;
  
  private JComboBox queryCombo;
  
  private JScrollPane jScrollPane1;
  private JScrollPane stmtdisplaypane;
  private JScrollPane querydisplaypane;
  
  private JTextArea stmtdisplay;
  private JTextArea querydisplay;
  
  private Vector tableContent;
  private ResultSet rs;
  private Vector newRow = null;
  
  private final TableInfo ti;
  private TableUpdate tu;
  private Connection conn;
  private JComboBox valueList = new JComboBox();
  private JComboBox secondaryList = new JComboBox();
  private String person_code = null;
  private String comp_code = null;
  private String jp_code = null;
  private String job_code = null;
  private String queryValue = null;
  private String pay_type = null;
  private String status = null;
  private String primary_sector = null;
  private int queryNum = 0;
  private String missingNum = "0";
  
  private final String[] queryList = {"Query 1", "Query 2", "Query 3", "Query 4", 
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
        this.valueListLabel.setBounds(75, 20, 125, 28);
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
        this.stmtdisplay = new JTextArea();
        this.stmtdisplaypane = new JScrollPane(this.stmtdisplay);
        this.getContentPane().add(this.stmtdisplaypane);
        this.stmtdisplaypane.setBounds(7, 50, 880, 40);
        this.stmtdisplay.setVisible(false);
        this.stmtdisplaypane.setVisible(false);
      }
      {
        this.querydisplay = new JTextArea();
        this.querydisplaypane = new JScrollPane(this.querydisplay);
        this.getContentPane().add(this.querydisplaypane);
        this.querydisplaypane.setBounds(7, 305, 880, 200);
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
        this.valueList.setBounds(200, 26, 150, 20);
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
        this.secondaryList.setBounds(536, 26, 150, 20);
        this.secondaryList.setVisible(false);
        this.secondaryList.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            secondaryListActionPerformed(evt);
          }
        });
      }
      {
        this.table = new JTable(new String[][] {{" ", " "}}, 
                new String[] {"Column 1", "Column 2"});
        this.table.setBounds(45, 200, 826, 357);
      }
      {
        this.jScrollPane1 = new JScrollPane(table);
        this.getContentPane().add(this.jScrollPane1);
        this.jScrollPane1.setBounds(7, 100, 880, 200);
      }
      {
        this.jbutton1 = new JButton();
        this.getContentPane().add(this.jbutton1);
        this.jbutton1.setText("Add Row");
        this.jbutton1.setBounds(567, 14, 98, 28);
        this.jbutton1.setVisible(false);
        this.jbutton1.addActionListener(new ActionListener () {
          public void actionPerformed(ActionEvent evt) {
            ibutton1ActionPerformed(evt);
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
  
  private void queryComboActionPerformed(ActionEvent evt) {
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
  
  private void ibutton1ActionPerformed(ActionEvent evt) {
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
  
  private void jbutton2ActionPerformed(ActionEvent evt) {
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
  
  private String queryParser(String chosenQuery) throws SQLException {
    if (chosenQuery.equals("Query 1")) {
        queryNum = 1;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("Company", "comp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Company Code");
        this.valueListLabel.setVisible(true);
        query1();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("1. List a company's workers in descending order");
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 2")) {
        queryNum = 2;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("company", "comp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("company");
        this.valueListLabel.setVisible(true);
        query2();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("2. List a company's staff by salary in descending order");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 3")) {
        queryNum = 3;
        query3();
        this.stmtdisplay.setText("3. List companies' labor costs (total salaries and wage rates "
            + "by 1920 hours) in descending order");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
        this.querydisplay.setText(queryValue.toString());
    }
    else if (chosenQuery.equals("Query 4")) {
        queryNum = 4;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Person Code");
        this.valueListLabel.setVisible(true);
        query4();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("4. Find all the jobs a person is currently holding and worked "
            + "in the past");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 5")) {
        queryNum = 5;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("person code");
        this.valueListLabel.setVisible(true);
        query5();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("5. List a person's knowledge/skills for a specific job profile"
            + "in a readable format.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 6")) {
        queryNum = 6;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("person code");
        this.valueListLabel.setVisible(true);
        query6();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("6. List the skill gap of a worker between his/her job(s) and "
            + "his/her skills.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 7")) {
        queryNum = 7;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("job", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query7();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("7. List the required knowledge/skill of a job profile in a "
            + "readable format");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 8")) {
        queryNum = 8;
        String[] colVal = null;
        String[] secVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
            secVal = ti.getColumn("job", "job_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        ComboBoxModel secoModel = new DefaultComboBoxModel(secVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Person Code");
        this.valueListLabel.setVisible(true);
        this.secondaryList.setModel(secoModel);
        this.secondaryList.setVisible(true);
        this.secondaryValueList.setText("Job Code");
        this.secondaryValueList.setVisible(true);
        query8();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("8. List a person's missing knowledge/skills for a specific job"
            + " in a readable format.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 9")) {
        queryNum = 9;
        String[] colVal = null;
        String[] secVal = null;
        try {
            colVal = ti.getColumn("job", "job_code");
            secVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        ComboBoxModel secoModel = new DefaultComboBoxModel(secVal); 
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Code");
        this.valueListLabel.setVisible(true);
        this.secondaryList.setModel(secoModel);
        this.secondaryList.setVisible(true);
        this.secondaryValueList.setText("Person Code");
        this.secondaryValueList.setVisible(true);
        query9();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("9. List the courses (course id and title) that each alone "
            + "teaches all the missing knowledge/skills fro a person to pursue "
            + "a specific job.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 10")) {
        queryNum = 10;
        String[] colVal = null;
        String[] secVal = null;
        try {
            colVal = ti.getColumn("job_profile", "jp_code");
            secVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        ComboBoxModel secoModel = new DefaultComboBoxModel(secVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        this.secondaryList.setModel(secoModel);
        this.secondaryList.setVisible(true);
        this.secondaryValueList.setText("Person Code");
        this.secondaryValueList.setVisible(true);
        query10();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("10. Suppose the skill gap of a worker and the requirement of a"
            + " desired job can be covered by one course. Find the 'quickest' "
            + "solution for this worker. Show the course, section information "
            + "and the complate date");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 11")) {
        queryNum = 11;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("person_code");
        this.valueListLabel.setVisible(true);
        query11();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("11. Find the cheapest course to make up one's skill gap by "
            + "showing the course to take and the cost (of the section price)");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 12")) {
        queryNum = 12;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("company", comp_code);
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("");
        this.valueListLabel.setVisible(true);
        query12();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("12. If query #9 returns nothing, then find the course sets "
            + "that their combination covers all the missing knowledge/skills "
            + "for a person to pursue a specific job. The considered course "
            + "sets will not include more than three courses. If multiple "
            + "course sets are found, list the course sets (with their course "
            + "IDs) in the order of the ascending order of the course sets' "
            + "total costs.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 13")) {
        queryNum = 13;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Person Code");
        this.valueListLabel.setVisible(true);
        query13();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("13. List all the job profiles that a person is qualified for.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 14")) {
        queryNum = 14;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("person", "person_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Person Code");
        this.valueListLabel.setVisible(true);
        query14();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("14. Find the job with the highest pay rate for a person "
            + "according to his/her skill qualifications.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 15")) {
        queryNum = 15;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("job_profile", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query15();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("15. List all the names along with the emails of the persons "
            + "who are qualified for a job profile.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 16")) {
        queryNum = 16;
        String[] colVal = null;
        missingNum = "1";
        try {
            colVal = ti.getColumn("jp_skill", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query16();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("16. When a company cannot find any qualified person for a job,"
            + " secondary solution is to find a person who is almost qualified"
            + " to the job. Make a 'missing-one' list that lists people who"
            + " miss only one skill for a specified job profile.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 17")) {
        queryNum = 17;
        String[] colVal = null;
        missingNum = "1";
        try {
            colVal = ti.getColumn("jp_skill", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query17();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("17. List the skillID and the number of people in the missing"
            + " one list for a given job profile in the ascending order of the"
            + " people counts.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 18")) {
        queryNum = 18;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("jp_skill", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query18();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("18. Suppose there is a new job profile that no one is"
            + " qualified. List the persons who miss the least number of skills"
            + " and report the 'least number'.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 19")) {
        queryNum = 19;
        String[] colVal = null;
        missingNum = "3";
        try {
            colVal = ti.getColumn("jp_skill", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query19();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("19. For a specified job profile and a given small number k,"
            + " make a 'missing-k' list that lists the people's IDs and the"
            + " number of missing skills for the people who miss only up to k"
            + " skills in the ascending order of missing skills.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 20")) {
        queryNum = 20;
        String[] colVal = null;
        missingNum = "1";
        try {
            colVal = ti.getColumn("jp_skill", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Profile Code");
        this.valueListLabel.setVisible(true);
        query20();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("20. Given a job profile and its corresponding missing-k list"
            + " specified Question 19. Find every skill that is needed by at"
            + " least one person in the given missing-k list. List each skillID"
            + " and the number of people who need it in the descending order of"
            + " the people counts.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 21")) {
        queryNum = 21;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("employment", "job_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Job Code");
        this.valueListLabel.setVisible(true);
        query21();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("21. In a local or national crisis, we need to find all the"
            + " people who once held a job of the special job-profile"
            + " identifier.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 22")) {
        queryNum = 22;
        String[] colVal = null;
        String[] secVal = null;
        try {
            colVal = ti.getColumn("employment", "status");
            secVal = ti.getColumn("employment", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        ComboBoxModel secoModel = new DefaultComboBoxModel(secVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Status");
        this.valueListLabel.setVisible(true);
        this.secondaryList.setModel(secoModel);
        this.secondaryList.setVisible(true);
        this.secondaryValueList.setText("Job Profile Code");
        this.secondaryValueList.setVisible(true);
        query22();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("22. Find all the unemployed people who once held a job of the"
            + " given job-profile identifier.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 23")) {
        queryNum = 23;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("employment", "status");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Status");
        this.valueListLabel.setVisible(true);
        query23();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("23. Find the biggest employer in terms of number of employees"
            + " or the total amount of salaries and wages paid to employees.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 24")) {
        queryNum = 24;
        String[] colVal = null;
        try {
            colVal = ti.getColumn("employment", "status");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Status");
        this.valueListLabel.setVisible(true);
        query24();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("24. Find out the job distribution among business sectors; find"
            + " out the biggest sector in terms of number of employes or the"
            + " total amount of salaries and wages paid to employees.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 25")) {
        queryNum = 25;
        query25();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("25. Find out the ratio between the people whose earnings"
            + " increase and those whose earnings decrease; find the average"
            + " rate of earning improvement for the workers in a specific"
            + " business sector.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 26")) {
        queryNum = 26;
        String[] colVal = null;
        String[] secVal = null;
        try {
            colVal = ti.getColumn("employment", "status");
            secVal = ti.getColumn("employment", "status");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        ComboBoxModel secoModel = new DefaultComboBoxModel(secVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Status");
        this.valueListLabel.setVisible(true);
        this.secondaryList.setModel(secoModel);
        this.secondaryList.setVisible(true);
        this.secondaryValueList.setText("Status");
        this.secondaryValueList.setVisible(true);
        this.querydisplay.setText(queryValue.toString());
        query26();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("26. Find the job profiles that have the most openings due to"
            + " lack of qualified workers. If there are many opening jobs of a"
            + " job profile but at the same time there are many qualified"
            + " jobless people. Then training cannot help fill up this type of"
            + " job. What we want to find is such a job profile that has the"
            + " largest differences between vacancies (the unfilled jobs of"
            + " this job profile) and the number of jobless that has the"
            + " largest difference between vacancies.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 27")) {
        queryNum = 27;
//        String[] colVal = null;
//        try {
//            colVal = ti.getColumn("company", comp_code);
        
//        catch (SQLException sqle) {
//            sqle.printStackTrace();
//        }
//        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
//        this.valueList.setModel(compModel);
//        this.valueList.setVisible(true);
//        this.valueListLabel.setText("");
//        this.valueListLabel.setVisible(true);
        query27();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("27. Find the courses that can help the most jobless people"
            + " find a job by training them toward the job profiles that have"
            + " the most openings due to lack of qualified workes.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    else if (chosenQuery.equals("Query 28")) {
        queryNum = 28;
        String[] colVal = null;
        String[] secVal = null;
        try {
            colVal = ti.getColumn("person_skill", "person_code");
            secVal = ti.getColumn("jp_skill", "jp_code");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        ComboBoxModel compModel = new DefaultComboBoxModel(colVal);
        ComboBoxModel secoModel = new DefaultComboBoxModel(secVal);
        this.valueList.setModel(compModel);
        this.valueList.setVisible(true);
        this.valueListLabel.setText("Person Code");
        this.valueListLabel.setVisible(true);
        this.secondaryList.setModel(secoModel);
        this.secondaryList.setVisible(true);
        this.secondaryValueList.setText("Job Profile Code");
        this.secondaryValueList.setVisible(true);
        query28();
        this.stmtdisplaypane.setVisible(true);
        this.stmtdisplay.setText("28. List all the courses, directly or indirectly required,"
            + " that a person has to take in order to be qualified for a job of"
            + " the given profile, according to his/her skills possessed and"
            + " courses taken.");
        this.stmtdisplay.setLineWrap(true);
        this.stmtdisplay.setVisible(true);
    }
    
    return queryValue;
  }
  
  private void valueListActionPerformed(ActionEvent evt) {
    if (queryNum == 1) {
        this.comp_code = (String) this.valueList.getSelectedItem();
        query1();
    }
    else if (queryNum == 2) {
        this.comp_code = (String) this.valueList.getSelectedItem();
        query2();
    }
    else if (queryNum == 4) {
      this.person_code = (String) this.valueList.getSelectedItem();
      query4();
    }
    else if (queryNum == 5) {
        this.person_code = (String) this.valueList.getSelectedItem();
        query5();
    }
    else if (queryNum == 6) {
        this.person_code = (String) this.valueList.getSelectedItem();
        query6();
    }
    else if (queryNum == 7) {
        this.job_code = (String) this.valueList.getSelectedItem();
        query7();
    }
    else if (queryNum == 8) {
        this.person_code = (String) this.valueList.getSelectedItem();
        this.job_code = (String) this.secondaryList.getSelectedItem();
        query8();
    }
    else if (queryNum == 9) {
        this.job_code = (String) this.valueList.getSelectedItem();
        this.person_code = (String) this.secondaryList.getSelectedItem();
        query9();
    }
    else if (queryNum == 10) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        this.person_code = (String) this.secondaryList.getSelectedItem();
        query10();
    }
    else if (queryNum == 11) {
        this.person_code = (String) this.valueList.getSelectedItem();
        query11();
    }
    else if (queryNum == 12) {
        this.comp_code = (String) this.valueList.getSelectedItem();
        query12();
    }
    else if (queryNum == 13) {
        this.person_code = (String) this.valueList.getSelectedItem();
        query13();
    }
    else if (queryNum == 14) {
        this.person_code = (String) this.valueList.getSelectedItem();
        query14();
    }
    else if (queryNum == 15) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        query15();
    }
    else if (queryNum == 16) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        query16();
    }
    else if (queryNum == 17) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        query17();
    }
    else if (queryNum == 18) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        query18();
    }
    else if (queryNum == 19) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        query19();
    }
    else if (queryNum == 20) {
        this.jp_code = (String) this.valueList.getSelectedItem();
        query20();
    }
    else if (queryNum == 21) {
        this.job_code = (String) this.valueList.getSelectedItem();
        query21();
    }
    else if (queryNum == 22) {
        this.status = (String) this.valueList.getSelectedItem();
        this.jp_code = (String) this.secondaryList.getSelectedItem();
        query22();
    }
    else if (queryNum == 23) {
        this.status = (String) this.valueList.getSelectedItem();
        query23();
    }
    else if (queryNum == 24) {
        this.status = (String) this.valueList.getSelectedItem();
        query24();
    }
    else if (queryNum == 25) {
        query25();
    }
    else if (queryNum == 26) {
        this.status = (String) this.valueList.getSelectedItem();
        this.status = (String) this.secondaryList.getSelectedItem();
        query26();
    }
    else if (queryNum == 27) {
        this.comp_code = (String) this.valueList.getSelectedItem();
        query27();
    }
    else if (queryNum == 28) {
        this.person_code = (String) this.valueList.getSelectedItem();
        this.jp_code = (String) this.secondaryList.getSelectedItem();
        query28();
    }
    try {
      Statement stmt = conn.createStatement();
      System.out.println(queryValue);
      rs = stmt.executeQuery(queryValue);
      tableContent = ti.resultSet2Vector(rs);
      Vector tableTitles = ti.getTitleAsVector(rs);
      TableModel tableModel = new DefaultTableModel(tableContent, tableTitles);
      table.setModel(tableModel);
      this.querydisplay.setText(queryValue.toString());
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void secondaryListActionPerformed(ActionEvent evt) {
    if (queryNum == 8) {
      this.person_code = (String) this.valueList.getSelectedItem();
      this.job_code = (String) this.secondaryList.getSelectedItem();
      query8();
    }
    else if (queryNum == 9) {
      this.job_code = (String) this.valueList.getSelectedItem();
      this.person_code = (String) this.secondaryList.getSelectedItem();
      query9();
    }
    else if (queryNum == 10) {
      this.jp_code = (String) this.valueList.getSelectedItem();
      this.person_code = (String) this.secondaryList.getSelectedItem();
      query10();
    }
    else if (queryNum == 22) {
      this.status = (String) this.valueList.getSelectedItem();
      this.jp_code = (String) this.secondaryList.getSelectedItem();
      query22();
    }
    else if (queryNum == 26) {
      this.status = (String) this.valueList.getSelectedItem();
      this.status = (String) this.secondaryList.getSelectedItem();
      query26();
    }
    else if (queryNum == 28) {
      this.person_code = (String) this.valueList.getSelectedItem();
      this.jp_code = (String) this.secondaryList.getSelectedItem();
      query28();
    }
    try {
      Statement stmt = conn.createStatement();
      System.out.println(queryValue);
      rs = stmt.executeQuery(queryValue);
      tableContent = ti.resultSet2Vector(rs);
      Vector tableTitles = ti.getTitleAsVector(rs);
      TableModel tableModel = new DefaultTableModel(tableContent, tableTitles);
      table.setModel(tableModel);
      this.querydisplay.setText(queryValue.toString());
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void query1() {
    queryValue = 
            "select last_name, first_name \n"
            + "from person inner join employment on person.person_code = \n"
            + "employment.person_code inner join job on employment.job_code = \n"
            + "job.job_code \n"
            + "where comp_code =" + comp_code + " and status = 'employed'";
  }
  
  private void query2() {
    queryValue = "select distinct last_name, first_name, pay_rate \n"
            + "from person inner join employment on person.person_code = employment.person_code \n"
            + "inner join job on employment.job_code = job.job_code \n"
            + "where comp_code = " + comp_code + " and pay_type = 'salary' \n";
  }
  
  private void query3() {
    queryValue = "with salary as ( \n"
            + "   select comp_code, sum(pay_rate) worker_salary \n"
            + "   from job inner join employment on job.job_code = employment.job_code \n"
            + "   where pay_type = 'salary' \n"
            + "   group by (job.comp_code)), \n"
            + ""
            + "wage as ( \n"
            + "   select comp_code, sum(pay_rate*1920) worker_salary \n"
            + "   from job inner join employment on job.job_code = employment.job_code \n"
            + "   where pay_type = 'wages' \n"
            + "   group by (job.comp_code)), \n"
            + ""
            + "cost as ( \n"
            + "   ((select * \n"
            + "   from salary) \n"
            + "      union \n"
            + "   (select * \n"
            + "   from wage))) \n"
            + ""
            + "select comp_code, sum(worker_salary) as total_cost \n"
            + "from cost \n"
            + "group by comp_code \n"
            + "order by total_cost desc";
  }
  
  private void query4() {
    queryValue = "select job_code \n"
            + "from employment \n"
            + "where person_code =" + person_code;
  }
  
  private void query5() {
    queryValue = "select ks_name, ks_code \n"
            + "from person_skill natural join skills \n"
            + "where person_code = \n" + person_code
            + "order by ks_name asc";
  }  
  
  private void query6() {
    queryValue = "with person_skills as ( \n"
            + "   select ks_name, ks_level, ks_code \n"
            + "   from person_skill natural join skills \n"
            + "   where person_code = " + person_code + "), \n"
            + ""
            + "person_jobs as ( \n"
            + "   select job_code \n"
            + "   from job natural join employment \n"
            + "   where person_code =" + person_code + "), \n"
            + ""
            + "job_skills as ( \n"
            + "   select ks_code "
            + "   from job_skill natural join person_jobs), \n"
            + ""
            + "skill_gap as ( \n"
            + "   (select ks_code \n"
            + "   from person_skills) \n"
            + "     minus \n"
            + "   (select ks_code \n"
            + "   from job_skills)) \n"
            + ""
            + "select distinct ks_level \n"
            + "from skills natural join skill_gap";
  }
  
  private void query7() {
    queryValue = "select ks_name, ks_code \n"
            + "from skills natural join jp_skill \n"
            + "where jp_skill.jp_code =" + jp_code;
  }
  
  private void query8() {
    queryValue = "with has_skill as ( \n"
            + "   select ks_code \n"
            + "   from person_skill \n"
            + "   where person_code =" + person_code + "), \n"
            + ""
            + "required_skill as ( \n"
            + "   select ks_code \n"
            + "   from job_skill \n"
            + "   where job_code =" + job_code + "), \n"
            + ""
            + "skill_gap as ( \n"
            + "   (select ks_code \n"
            + "   from has_skill) \n"
            + "     union \n"
            + "   (select ks_code \n"
            + "   from required_skill)) \n"
            + ""
            + "select ks_name \n"
            + "from skill_gap natural join skills";
  }
  private void query9() {
    queryValue = "with required_skills as ( \n"
            + "   select ks_code, ks_name \n"
            + "   from job_skill natural join skills \n"
            + "   where job_code =" + job_code + "), \n"
            + ""
            + "current_skills as ( \n"
            + "   select ks_code, ks_name \n"
            + "   from person_skill natural join skills \n"
            + "   where person_code =" + person_code + "), \n"
            + ""
            + "needed_course as ( \n"
            + "   (select ks_code, ks_name \n"
            + "   from required_skills) \n"
            + "     minus \n"
            + "   (select ks_code, ks_name \n"
            + "   from current_skills)), \n"
            + ""
            + "missing_skills as ( \n"
            + "   select ks_code \n"
            + "   from needed_course) \n"
            + ""
            + "select course_code, course_title \n"
            + "from missing_skills natural join course_skill natural join course";
  }

  private void query10() {
    queryValue = "with needed_skills as ( \n"
            + "   (select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + ") \n"
            + "     minus \n"
            + "   (select ks_code \n"
            + "   from person_skill \n"
            + "   where person_code =" + person_code + ")), \n"
            + ""
            + "course_skills as ( \n"
            + "   select distinct c.course_code \n"
            + "   from course c \n"
            + "   where not exists ( \n"
            + "     select ks_code \n"
            + "     from needed_skills \n"
            + "       minus \n"
            + "     select ks_code \n"
            + "     from course_skill \n"
            + "     where course_code = c.course_code)) \n"
            + ""
            + "select course_code, course_title \n"
            + "from course_skills natural join course";
  }

  private void query11() {
    queryValue = "select course_code, course_title, cost \n"
            + "from course natural join section natural join course_skill \n"
            + "where ks_code in ( \n"
            + "   select ks_code \n"
            + "   from jp_skill natural join skills natural join job \n"
            + "   where ks_code not in ( \n"
            + "     select ks_code \n"
            + "     from job_profile natural join person_skill \n"
            + "     where person_code =" + person_code + ")) \n"
            + "       and cost = ( \n"
            + "       select min(cost) \n"
            + "       from course natural join section natural join course_skill \n"
            + "       where ks_code in ( \n"
            + "         select ks_code \n"
            + "         from jp_skill natural join skills natural join job \n"
            + "         where ks_code not in ( \n"
            + "           select ks_code \n"
            + "           from job_profile natural join person_skill \n"
            + "           where person_code =" + person_code + ")))";
  }

  private void query12() {
    queryValue = "";
  }

  private void query13() {
    queryValue = "with person_skills as ( \n"
            + "select ks_code \n"
            + "from person_skill \n"
            + "where person_code =" + person_code + ") \n"
            + ""
            + "select jp_code, jp_title \n"
            + "from job_profile j \n"
            + "where not exists ( \n"
            + "   (select ks_code \n"
            + "   from jp_skill \n"
            + "   where j.jp_code = jp_skill.jp_code) \n"
            + "     minus \n"
            + "   (select ks_code \n"
            + "   from person_skills))";
  }

  private void query14() {
    queryValue = "with person_skills as ( \n"
            + "     select ks_code \n"
            + "     from person_skill \n"
            + "     where person_code =" + person_code + "), \n"
            + ""
            + "qualifications as ( \n"
            + "     select job_code \n"
            + "     from job \n"
            + "     where not exists ( \n"
            + "         (select ks_code \n"
            + "         from jp_skill \n"
            + "         where job.jp_code = jp_code) \n"
            + "             intersect \n"
            + "         (select ks_code \n"
            + "         from person_skills))), \n"
            + ""
            + "job_qualifications as ( \n"
            + "     select distinct job_code \n"
            + "     from qualifications), \n"
            + ""
            + "pay as ( "
            + "     select distinct job_code, (\n"
            + "         case \n"
            + "             when pay_type = 'salary' then pay_rate \n"
            + "             when pay_type = 'wage' then pay_rate * 1920 \n"
            + "         end) as total_earnings \n"
            + "     from job_qualifications natural join job) \n"
            + ""
            + "select job_code, total_earnings \n"
            + "from pay \n"
            + "where total_earnings = ( \n"
            + "     select max(total_earnings) \n"
            + "     from pay)";
  }

  private void query15() {
    queryValue = "with required_skills as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + ") \n"
            + ""
            + "select last_name, first_name, email, person_code \n"
            + "from person p \n"
            + "where not exists ( \n"
            + "   (select ks_code \n"
            + "   from required_skills) \n"
            + "     minus \n"
            + "   (select ks_code \n"
            + "   from person_skill \n"
            + "   where p.person_code = person_code))";
  }

  private void query16() {
    queryValue = "with skill_codes as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + "), \n"
            + ""
            + "missing_one (person_code, num_missing) as ( \n"
            + "   select person_code, count(ks_code) \n"
            + "   from person p, \n"
            + "     (select ks_code \n"
            + "     from skill_codes) sc \n"
            + "     where sc.ks_code in ( \n"
            + "       select ks_code \n"
            + "       from skill_codes \n"
            + "         minus \n"
            + "       select ks_code \n"
            + "       from person_skill \n"
            + "       where p.person_code = person_code) \n"
            + "     group by person_code) \n"
            + ""
            + "select person_code, num_missing \n"
            + "from missing_one \n"
            + "where num_missing =" + missingNum + " \n"
            + "order by num_missing desc";
  }

  private void query17() {
    queryValue = "with skill_codes as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + "), \n"
            + ""
            + "missing_one (person_code, num_missing) as ( \n"
            + "   select person_code, count(ks_code) \n"
            + "   from person p, ( \n"
            + "     select ks_code \n"
            + "     from skill_codes) sc \n"
            + "   where sc.ks_code in ( \n"
            + "     select ks_code \n"
            + "     from skill_codes \n"
            + "       minus \n"
            + "     select ks_code \n"
            + "     from person_skill \n"
            + "     where p.person_code = person_code) \n"
            + "   group by person_code), \n"
            + ""
            + "person_missing_one (person_code) as ( \n"
            + "   select person_code \n"
            + "   from missing_one \n"
            + "   where num_missing =" + missingNum + ") \n"
            + ""
            + "select ks_code, count(person_code) as num_persons_missing \n"
            + "from person_missing_one p, ( \n"
            + "   select ks_code \n"
            + "   from skill_codes) sc \n"
            + "   where sc.ks_code in ( \n"
            + "     select ks_code \n"
            + "     from skill_codes \n"
            + "       minus \n"
            + "     select ks_code \n"
            + "     from person_skill \n"
            + "     where p.person_code = person_code) \n"
            + "   group by ks_code"; 
  }

  private void query18() {
    queryValue = "with needed_skills as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + "), \n"
            + ""
            + "missing_skill (person_code, num_missing) as ( \n"
            + "   (select person_code, count(ks_code) \n"
            + "   from person p, needed_skills \n"
            + "   where ks_code in ( \n"
            + "     (select ks_code \n"
            + "     from needed_skills) \n"
            + "       minus \n"
            + "     (select ks_code \n"
            + "     from person_skill \n"
            + "     where p.person_code = person_code)) \n"
            + "   group by person_code)) \n"
            + ""
            + "select person_code, num_missing as smallest \n"
            + "from missing_skill \n"
            + "where num_missing = ( \n"
            + "   select min(num_missing) \n"
            + "   from missing_skill) \n"
            + "order by person_code asc"; 
  }

  private void query19() {
    queryValue = "with skill_list as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + "), \n"
            + ""
            + "missing_skills (person_code, num_missing) as ( \n"
            + "   select person_code, count(ks_code) \n"
            + "   from person p, ( \n"
            + "     select ks_code \n"
            + "     from skill_list) j \n"
            + "   where j.ks_code in ( \n"
            + "     select ks_code \n"
            + "     from skill_list \n"
            + "       minus \n"
            + "     select ks_code \n"
            + "     from person_skill \n"
            + "     where p.person_code = person_code) \n"
            + "     group by person_code) \n"
            + ""
            + "select person_code, num_missing \n"
            + "from missing_skills \n"
            + "where num_missing <=" + missingNum + " \n"
            + "order by num_missing desc";
  }

  private void query20() {
    queryValue = "with skill_codes as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + "), \n"
            + ""
            + "missing_skills (person_code, num_missing) as ( \n"
            + "   select person_code, count(ks_code) \n"
            + "   from person p, ( \n"
            + "     select ks_code \n"
            + "     from skill_codes) j \n"
            + "   where j.ks_code in ( \n"
            + "     select ks_code \n"
            + "     from skill_codes \n"
            + "       minus \n"
            + "     select ks_code \n"
            + "     from person_skill \n"
            + "     where p.person_code = person_code) \n"
            + "   group by person_code), \n"
            + ""
            + "missing_one (person_code) as ( \n"
            + "   select person_code \n"
            + "   from missing_skills \n"
            + "   where num_missing =" + missingNum + ") \n"
            + ""
            + "select ks_code, count(person_code) as num_missing_one \n"
            + "from missing_one m, ( \n"
            + "   select ks_code \n"
            + "   from skill_codes) s \n"
            + "where s.ks_code in ( \n"
            + "   select ks_code \n"
            + "   from skill_codes \n"
            + "     minus \n"
            + "   select ks_code \n"
            + "   from person_skill \n"
            + "   where m.person_code = person_code) \n"
            + "group by ks_code";
  }

  private void query21() {
    queryValue = "select last_name, first_name, email \n"
            + "from person inner join employment on person.person_code = employment.person_code \n"
            + "where job_code =" + job_code;
  }

  private void query22() {
    queryValue = "with unemployed as ( \n"
            + "   select person_code \n"
            + "   from person \n"
            + "     minus \n"
            + "   select distinct person_code \n"
            + "   from employment \n"
            + "   where status =" + status + ") \n"
            + ""
            + "select distinct last_name, first_name, job_code, jp_code \n"
            + "from person inner join unemployed on person.person_code = unemployed.person_code natural join job \n"
            + "where jp_code =" + jp_code + " \n"
            + "order by person.last_name asc";
  }

  private void query23() {
    queryValue = "with employer_count as ( \n"
            + "   select comp_code, count(pay_rate) as num_employees \n"
            + "   from job inner join employment on job.job_code = employment.job_code \n"
            + "   where status =" + status + " \n"
            + "   group by comp_code) \n"
            + ""
            + "select comp_code, comp_name, num_employees \n"
            + "from employer_count natural join company \n"
            + "order by (num_employees) desc";
  }

  private void query24() {
    queryValue = "with employer_count as ( \n"
            + "     select count(pay_rate) as num_employees, comp_code \n"
            + "     from job join employment using(job_code) \n"
            + "     where status =" + status + " \n"
            + "     group by comp_code), \n"
            + ""
            + "count_per_sector as ( \n"
            + "     select primary_sector, sum(num_employees) as sector_employee_count \n"
            + "     from employer_count natural join company \n"
            + "     group by primary_sector), \n"
            + ""
            + "max_count as ( \n"
            + "     select max(sector_employee_count) as max \n"
            + "     from count_per_sector) \n"
            + ""
            + "select primary_sector \n"
            + "from max_count, count_per_sector \n"
            + "where max = sector_employee_count";
  }

  private void query25() {
    queryValue = "with old_salary as ( \n"
            + "   select distinct max(pay_rate) as old_pay, person_code \n"
            + "   from employment natural join job natural join company \n"
            + "   where status = 'unemployed' and primary_sector = 'tourism' \n"
            + "   group by person_code), \n"
            + ""
            + "present_salary as ( \n"
            + "   select distinct max(pay_rate) as present_pay, person_code \n"
            + "   from employment natural join job natural join company \n"
            + "   where status = 'employed' and primary_sector = 'tourism' \n"
            + "   group by person_code), \n"
            + ""
            + "people_decrease as ( \n"
            + "   select count(person_code) as decline \n"
            + "   from old_salary natural join present_salary \n"
            + "   where present_pay < old_pay), \n"
            + ""
            + "people_increase as ( \n"
            + "   select count(person_code) as incline \n"
            + "   from old_salary natural join present_salary \n"
            + "   where present_pay > old_pay) \n"
            + ""
            + "select sum(incline) inc_ratio, sum(decline) dec_ratio \n"
            + "from people_increase natural join people_decrease";
  }

  private void query26() {
    queryValue = "with unemployed as ( \n"
            + "   select distinct person_code \n"
            + "   from employment \n"
            + "   where status =" + status + "), \n"
            + ""
            + "employed as ( \n"
            + "   select distinct person_code \n"
            + "   from employment \n"
            + "   where status =" + status + "), \n"
            + ""
            + "openings as ( \n"
            + "   select distinct job_code \n"
            + "   from ( \n"
            + "     select job_code \n"
            + "     from unemployed natural join employment \n"
            + "       minus \n"
            + "     select job_code \n"
            + "     from employed natural join employment)), \n"
            + ""
            + "num_of_profiles as ( \n"
            + "   select jp_code, count(job_code) as num_of_openings \n"
            + "   from openings natural join job \n"
            + "   group by jp_code), \n"
            + ""
            + "people_qualified as ( \n"
            + "   select jp_code, count(person_code) as num_qualified \n"
            + "   from num_of_profiles n, person p \n"
            + "   where not exists ( \n"
            + "     select ks_code \n"
            + "     from num_of_profiles natural join jp_skill \n"
            + "     where n.jp_code = jp_code \n"
            + "       minus \n"
            + "     select ks_code \n"
            + "     from num_of_profiles natural join jp_skill natural join person_skill \n"
            + "     where p.person_code = person_code) \n"
            + "   group by jp_code), \n"
            + ""
            + "missing_skill as ( \n"
            + "   select jp_code, (num_of_openings - num_qualified) as unqualified \n"
            + "   from people_qualified natural join num_of_profiles), \n"
            + ""
            + "max_missing as ( \n"
            + "   select max(unqualified) as max_unqualified \n"
            + "   from missing_skill) \n"
            + ""
            + "select jp_code \n"
            + "from missing_skill, max_missing \n"
            + "where unqualified = max_unqualified";
  }

  private void query27() {
    
  }

  private void query28() {
    queryValue = "with person_skills as ( \n"
            + "   select ks_code \n"
            + "   from person_skill \n"
            + "   where person_code =" + person_code + "), \n"
            + ""
            + "person_courses as ( \n"
            + "   select ks_code \n"
            + "   from attends natural join person_skill \n"
            + "   where person_code =" + person_code + "), \n"
            + ""
            + "skills_needed as ( \n"
            + "   select ks_code \n"
            + "   from jp_skill \n"
            + "   where jp_code =" + jp_code + " \n"
            + "     minus \n"
            + "   select ks_code \n"
            + "   from person_skills), \n"
            + ""
            + "courses_needed as ( \n"
            + "   select course_code, course_title \n"
            + "   from skills_needed natural join course_skill natural join course) \n"
            + ""
            + "select distinct course_code, course_title \n"
            + "from courses_needed c \n"
            + "where not exists ( \n"
            + "   select course_code \n"
            + "   from person_courses \n"
            + "   where c.course_code = course_code)";
  }
  public void main(String[] args) throws SQLException {
    if (args.length < 2) {
      System.out.println("usage: java TableInfo db-username db-password");
      System.exit(1);
    }
    dbaccess tc = new dbaccess();
    conn = tc.getDBConnection(args[0], args[1]);
    tu = new TableUpdate(conn);
    EditTables inst = new EditTables(tu, conn);
    inst.setVisible(true);
  }
}
