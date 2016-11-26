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
  
  private JTable table;
  private JButton jbutton1;
  private JButton jbutton2;
  
  private JComboBox queryCombo;
  
  private JScrollPane jScrollPane1;
  
  private Vector tableContent;
  private ResultSet rs;
  private Vector newRow = null;
  
  private final TableInfo ti;
  private TableUpdate tu;
  private Connection conn;
  private JComboBox valueList = new JComboBox();
  private JComboBox secondaryList = new JComboBox();
  private final String person_code = null;
  private String comp_code = null;
  private final String jp_code = null;
  private final String job_code = null;
  private String queryValue = null;
  private final String pay_type = null;
  private final String status = null;
  private final String primary_sector = null;
  private int queryNum = 0;
  private final String missingNum = "0";
  
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
        this.table = new JTable(new String[][] {{" ", " "}}, 
                new String[] {"Column 1", "Column 2"});
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
      
      query1();
    }
    else if (chosenQuery.equals("Query 2")) {
      queryNum = 2;
      
      query2();
    }
    else if (chosenQuery.equals("Query 3")) {
      queryNum = 3;
      
      query3();
    }
    else if (chosenQuery.equals("Query 4")) {
      queryNum = 4;
      query4();
    }
    else if (chosenQuery.equals("Query 5")) {
      queryNum = 5;
      
      query5();
    }
    else if (chosenQuery.equals("Query 6")) {
      queryNum = 6;
      
      query6();
    }
    else if (chosenQuery.equals("Query 7")) {
      queryNum = 7;
      
      query7();
    }
    else if (chosenQuery.equals("Query 8")) {
      queryNum = 8;
      
      query8();
    }
    else if (chosenQuery.equals("Query 9")) {
      queryNum = 9;
      
      query9();
    }
    else if (chosenQuery.equals("Query 10")) {
      queryNum = 10;
      
      query10();
    }
    else if (chosenQuery.equals("Query 11")) {
      queryNum = 11;
      
      query11();
    }
    else if (chosenQuery.equals("Query 12")) {
      queryNum = 12;
      
      query12();
    }
    else if (chosenQuery.equals("Query 13")) {
      queryNum = 13;
      
      query13();
    }
    else if (chosenQuery.equals("Query 14")) {
      queryNum = 14;
      
      query14();
    }
    else if (chosenQuery.equals("Query 15")) {
      queryNum = 15;
      
      query15();
    }
    else if (chosenQuery.equals("Query 16")) {
      queryNum = 16;
      
      query16();
    }
    else if (chosenQuery.equals("Query 17")) {
      queryNum = 17;
      
      query17();
    }
    else if (chosenQuery.equals("Query 18")) {
      queryNum = 18;
      
      query18();
    }
    else if (chosenQuery.equals("Query 19")) {
      queryNum = 19;
      
      query19();
    }
    else if (chosenQuery.equals("Query 20")) {
      queryNum = 20;
      
      query20();
    }
    else if (chosenQuery.equals("Query 21")) {
      queryNum = 21;
      
      query21();
    }
    else if (chosenQuery.equals("Query 22")) {
      queryNum = 22;
      
      query22();
    }
    else if (chosenQuery.equals("Query 23")) {
      queryNum = 23;
      
      query23();
    }
    else if (chosenQuery.equals("Query 24")) {
      queryNum = 24;
      
      query24();
    }
    else if (chosenQuery.equals("Query 25")) {
      queryNum = 25;
      
      query25();
    }
    else if (chosenQuery.equals("Query 26")) {
      queryNum = 26;
      
      query26();
    }
    else if (chosenQuery.equals("Query 27")) {
      queryNum = 27;
      
      query27();
    }
    else if (chosenQuery.equals("Query 28")) {
      queryNum = 28;
      
      query28();
    }
    return queryValue;
  }
  
  private void valueListActionPerformed(ActionEvent evt) {
    if (queryNum == 1) {
      comp_code = (String) valueList.getSelectedItem();
      
      query1();
    }
    else if (queryNum == 2) {
      comp_code = (String) this.valueList.getSelectedItem();
    }
  }
  
  private void secondaryListActionPerformed(ActionEvent evt) {
    
  }
  
  private void jbutton1ActionPerformed(ActionEvent evt) {
    
  }

  private void query1() {
    queryValue = 
            "select last_name, first_name"
            + "from person inner join employment on person.person_code = "
            + "   employment.person_code inner join job on employment.job_code = "
            + "   job.job_code"
            + "where comp_code ='1001001'";
  }
  
  private void query2() {
    queryValue = "select distinct last_name, first_name, pay_rate "
            + "from person inner join employment on person.person_code = employment.person_code "
            + "inner join job on employment.job_code = job.job_code "
            + "where comp_code = '1001001' and pay_type = 'salary' ";
  }
  
  private void query3() {
    queryValue = "with salary as ( "
            + "   select comp_code, sum(pay_rate) worker_salary "
            + "   from job inner join employment on job.job_code = employment.job_code "
            + "   where pay_type = 'salary' "
            + "   group by (job.comp_code)), "
            + "wage as ( "
            + "   select comp_code, sum(pay_rate*1920) worker_salary "
            + "   from job inner join employment on job.job_code = employment.job_code "
            + "   where pay_type = 'wages' "
            + "   group by (job.comp_code)), "
            + "cost as ( "
            + "   ((select * "
            + "   from salary) "
            + "      union "
            + "   (select * "
            + "   from wage))) "
            + "select comp_code, sum(worker_salary) as total_cost "
            + "from cost "
            + "group by comp_code "
            + "order by total_cost desc";
  }
  
  private void query4() {
    queryValue = "select job_code "
            + "from employment "
            + "where person_code = '1018256'";
  }
  
  private void query5() {
    queryValue = "select ks_name, ks_code "
            + "from person_skill natural join skills "
            + "where person_code = '1536512' "
            + "order by ks_name asc";
  }  
  
  private void query6() {
    queryValue = "with person_skills as ( "
            + "select ks_name, ks_level, ks_code "
            + "from person_skill natural join skills "
            + "where person_code = '1024701'), "
            + "person_jobs as ( "
            + "select job_code "
            + "from job natural join employment "
            + "where person_code = '1024701'), "
            + "job_skills as ( "
            + "select ks_code "
            + "from job_skill natural join person_jobs), "
            + "skill_gap as ( "
            + "(select ks_code "
            + "from person_skills) "
            + "minus "
            + "(select ks_code "
            + "from job_skills)) "
            + "select distinct ks_level "
            + "from skills natural join skill_gap";
  }
  
  private void query7() {
    queryValue = "select ks_name, ks_code "
            + "from skills natural join jp_skill "
            + "where jp_skill.jp_code = '100'";
  }
  
  private void query8() {
    queryValue = "with has_skill as ( "
            + "select ks_code "
            + "from person_skill "
            + "where person_code = '1024701'), "
            + "required_skill as ( "
            + "select ks_code "
            + "from job_skill "
            + "where job_code = '3001001'), "
            + "skill_gap as ( "
            + "(select ks_code "
            + "from has_skill) "
            + "union "
            + "(select ks_code "
            + "from required_code)) "
            + "select ks_name "
            + "from skill_gap natural join skills";
  }
  private void query9() {
    queryValue = "with required_skills as ( "
            + "select ks_code, ks_name "
            + "from job_skill natural join skills "
            + "where job_code = '3101001'), "
            + "current_skills as ( "
            + "select ks_code, ks_name "
            + "from person_skill natural join skills "
            + "where person_code = '1357909'), "
            + "needed_course as ( "
            + "(select ks_code, ks_name "
            + "from required_skills) "
            + "minus "
            + "(select ks_code, ks_name "
            + "from current_skills), "
            + "missing_skills as ( "
            + "select ks_code "
            + "from needed_course) "
            + "select course_code, course_title "
            + "from missing_skill natural join course_skill natural join course";
  }

  private void query10() {
    queryValue = "with needed_skills as ( "
            + "(select ks_code "
            + "from jp_skill natural join job_profile "
            + "where jp_code = '300') "
            + "minus "
            + "(select ks_code "
            + "from person_skill "
            + "where person_code = '6969696')), "
            + "course_skills as ( "
            + "select distinct c.course_code "
            + "from course c "
            + "where not exists ( "
            + "select ks_code "
            + "from needed_skills "
            + "minus "
            + "select ks_code "
            + "from course_skill natural join course "
            + "where course_code = c.course_code)) "
            + "select course_code "
            + "from course_skills";
  }

  private void query11() {
    queryValue = "select course_code, course_title, cost "
            + "from course natural join section natural join course_skill "
            + "where ks_code in ( "
            + "select ks_code "
            + "from jp_skill natural join skill natural join job "
            + "where ks_code not in ( "
            + "select ks_code "
            + "from job_profile natural join person_skill "
            + "where person_code = '1017145')) "
            + "and cost = ( "
            + "select min(cost) "
            + "from course natural join section natural join course_skill "
            + "where ks_code in ( "
            + "select ks_code "
            + "from jp_skill natural join skills natural join job "
            + "where ks_code not in ( "
            + "select ks_code "
            + "from job_profile natural join person_skill"
            + "where person_code = '1017145')))";
  }

  private void query12() {
    queryValue = "";
  }

  private void query13() {
    queryValue = "with person_skills as ( "
            + "select ks_code "
            + "from person_skill "
            + "where person_code = '2165778') "
            + "select jp_code, jp_title "
            + "from job_profile j"
            + "where not exists ( "
            + "select ks_code "
            + "from jp_skill "
            + "where j.jp_code = jp_skill.jp_code "
            + "minus "
            + "select ks_code "
            + "from person_skills)";
  }

  private void query14() {
    
  }

  private void query15() {
    
  }

  private void query16() {
    
  }

  private void query17() {
    
  }

  private void query18() {
    
  }

  private void query19() {
    
  }

  private void query20() {
    
  }

  private void query21() {
    
  }

  private void query22() {
    
  }

  private void query23() {
    
  }

  private void query24() {
    
  }

  private void query25() {
    
  }

  private void query26() {
    
  }

  private void query27() {
    
  }

  private void query28() {
    
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
