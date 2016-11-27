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
    switch (chosenQuery) {
      case "Query 1":
        queryNum = 1;
        query1();
        break;
      case "Query 2":
        queryNum = 2;
        query2();
        break;
      case "Query 3":
        queryNum = 3;
        query3();
        break;
      case "Query 4":
        queryNum = 4;
        query4();
        break;
      case "Query 5":
        queryNum = 5;
        query5();
        break;
      case "Query 6":
        queryNum = 6;
        query6();
        break;
      case "Query 7":
        queryNum = 7;
        query7();
        break;
      case "Query 8":
        queryNum = 8;
        query8();
        break;
      case "Query 9":
        queryNum = 9;
        query9();
        break;
      case "Query 10":
        queryNum = 10;
        query10();
        break;
      case "Query 11":
        queryNum = 11;
        query11();
        break;
      case "Query 12":
        queryNum = 12;
        query12();
        break;
      case "Query 13":
        queryNum = 13;
        query13();
        break;
      case "Query 14":
        queryNum = 14;
        query14();
        break;
      case "Query 15":
        queryNum = 15;
        query15();
        break;
      case "Query 16":
        queryNum = 16;
        query16();
        break;
      case "Query 17":
        queryNum = 17;
        query17();
        break;
      case "Query 18":
        queryNum = 18;
        query18();
        break;
      case "Query 19":
        queryNum = 19;
        query19();
        break;
      case "Query 20":
        queryNum = 20;
        query20();
        break;
      case "Query 21":
        queryNum = 21;
        query21();
        break;
      case "Query 22":
        queryNum = 22;
        query22();
        break;
      case "Query 23":
        queryNum = 23;
        query23();
        break;
      case "Query 24":
        queryNum = 24;
        query24();
        break;
      case "Query 25":
        queryNum = 25;
        query25();
        break;
      case "Query 26":
        queryNum = 26;
        query26();
        break;
      case "Query 27":
        queryNum = 27;
        query27();
        break;
      case "Query 28":
        queryNum = 28;
        query28();
        break;
      default:
        break;
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
            "select last_name, first_name "
            + "from person inner join employment on person.person_code = employment.person_code inner join job on employment.job_code = job.job_code "
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
            + ""
            + "wage as ( "
            + "   select comp_code, sum(pay_rate*1920) worker_salary "
            + "   from job inner join employment on job.job_code = employment.job_code "
            + "   where pay_type = 'wages' "
            + "   group by (job.comp_code)), "
            + ""
            + "cost as ( "
            + "   ((select * "
            + "   from salary) "
            + "      union "
            + "   (select * "
            + "   from wage))) "
            + ""
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
            + "   select ks_name, ks_level, ks_code "
            + "   from person_skill natural join skills "
            + "   where person_code = '1024701'), "
            + ""
            + "person_jobs as ( "
            + "   select job_code "
            + "   from job natural join employment "
            + "   where person_code = '1024701'), "
            + ""
            + "job_skills as ( "
            + "   select ks_code "
            + "   from job_skill natural join person_jobs), "
            + ""
            + "skill_gap as ( "
            + "   (select ks_code "
            + "   from person_skills) "
            + "     minus "
            + "   (select ks_code "
            + "   from job_skills)) "
            + ""
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
            + "   select ks_code "
            + "   from person_skill "
            + "   where person_code = '1024701'), "
            + ""
            + "required_skill as ( "
            + "   select ks_code "
            + "   from job_skill "
            + "   where job_code = '3001001'), "
            + ""
            + "skill_gap as ( "
            + "   (select ks_code "
            + "   from has_skill) "
            + "     union "
            + "   (select ks_code "
            + "   from required_skill)) "
            + ""
            + "select ks_name "
            + "from skill_gap natural join skills";
  }
  private void query9() {
    queryValue = "with required_skills as ( "
            + "   select ks_code, ks_name "
            + "   from job_skill natural join skills "
            + "   where job_code = '3101001'), "
            + ""
            + "current_skills as ( "
            + "   select ks_code, ks_name "
            + "   from person_skill natural join skills "
            + "   where person_code = '1357909'), "
            + ""
            + "needed_course as ( "
            + "   (select ks_code, ks_name "
            + "   from required_skills) "
            + "     minus "
            + "   (select ks_code, ks_name "
            + "   from current_skills)), "
            + ""
            + "missing_skills as ( "
            + "   select ks_code "
            + "   from needed_course) "
            + ""
            + "select course_code, course_title "
            + "from missing_skills natural join course_skill natural join course";
  }

  private void query10() {
    queryValue = "with needed_skills as ( "
            + "   (select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '300') "
            + "     minus "
            + "   (select ks_code "
            + "   from person_skill "
            + "   where person_code = '6969696')), "
            + ""
            + "course_skills as ( "
            + "   select distinct c.course_code "
            + "   from course c "
            + "   where not exists ( "
            + "     select ks_code "
            + "     from needed_skills "
            + "       minus "
            + "     select ks_code "
            + "     from course_skill "
            + "     where course_code = c.course_code)) "
            + ""
            + "select course_code, course_title "
            + "from course_skills natural join course";
  }

  private void query11() {
    queryValue = "select course_code, course_title, cost "
            + "from course natural join section natural join course_skill "
            + "where ks_code in ( "
            + "   select ks_code "
            + "   from jp_skill natural join skills natural join job "
            + "   where ks_code not in ( "
            + "     select ks_code "
            + "     from job_profile natural join person_skill "
            + "     where person_code = '1017145')) "
            + "       and cost = ( "
            + "       select min(cost) "
            + "       from course natural join section natural join course_skill "
            + "       where ks_code in ( "
            + "         select ks_code "
            + "         from jp_skill natural join skills natural join job "
            + "         where ks_code not in ( "
            + "           select ks_code "
            + "           from job_profile natural join person_skill"
            + "           where person_code = '1017145')))";
  }

  private void query12() {
    queryValue = "";
  }

  private void query13() {
    queryValue = "with person_skills as ( "
            + "select ks_code "
            + "from person_skill "
            + "where person_code = '2165778') "
            + ""
            + "select jp_code, jp_title "
            + "from job_profile j"
            + "where not exists ( "
            + "   (select ks_code "
            + "   from jp_skill "
            + "   where j.jp_code = jp_skill.jp_code) "
            + "     minus "
            + "   (select ks_code "
            + "   from person_skills))";
  }

  private void query14() {
    queryValue = "with person_skills as ( "
            + "     select ks_code "
            + "     from person_skill "
            + "     where person_code = '6969696'), "
            + ""
            + "qualifications as ( "
            + "     select job_code "
            + "     from job "
            + "     where not exists ("
            + "         (select ks_code"
            + "         from jp_skill"
            + "         where job.jp_code = jp_code) "
            + "             intersect "
            + "         (select ks_code "
            + "         from person_skills))), "
            + ""
            + "job_qualifications as ( "
            + "     select distinct job_code "
            + "     from qualifications), "
            + ""
            + "pay as ( "
            + "     select distinct job_code, ("
            + "         case "
            + "             when pay_type = 'salary' then pay_rate "
            + "             when pay_type = 'wage' then pay_rate * 1920 "
            + "         end) as total_earnings "
            + "     from job_qualifications natural join job) "
            + ""
            + "select job_code, total_earnings "
            + "from pay "
            + "where total_earnings = ( "
            + "     select max(total_earnings) "
            + "     from pay) ";
  }

  private void query15() {
    queryValue = "with required_skills as ( "
            + "   select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '100') "
            + ""
            + "select last_name, first_name, email, person_code "
            + "from person p "
            + "where not exists ( "
            + "   (select ks_code "
            + "   from required_skills) "
            + "     minus "
            + "   (select ks_code "
            + "   from person_skill "
            + "   where p.person_code = person_code))";
  }

  private void query16() {
    queryValue = "with skill_codes as ( "
            + "   select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '100'), "
            + ""
            + "missing_one (person_code, num_missing) as ( "
            + "   select person_code, count(ks_code) "
            + "   from person p, "
            + "     (select ks_code "
            + "     from skill_codes) sc "
            + "     where sc.ks_code in ( "
            + "       select ks_code "
            + "       from skill_codes "
            + "         minus "
            + "       select ks_code "
            + "       from person_skill "
            + "       where p.person_code = person_code) "
            + "     group by person_code) "
            + ""
            + "select person_code, num_missing "
            + "from missing_one "
            + "where num_missing = 1 "
            + "order by num_missing desc";
  }

  private void query17() {
    queryValue = "with skill_codes as ( "
            + "   select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '100'), "
            + ""
            + "missing_one (person_code, num_missing) as ( "
            + "   select person_code, count(ks_code) "
            + "   from person p, ( "
            + "     select ks_code "
            + "     from skill_codes) sc "
            + "   where sc.ks_code in ( "
            + "     select ks_code "
            + "     from skill_codes "
            + "       minus "
            + "     select ks_code "
            + "     from person_skill "
            + "     where p.person_code = person_code) "
            + "   group by person_code), "
            + ""
            + "person_missing_one (person_code) as ( "
            + "   select person_code "
            + "   from missing_one "
            + "   where num_missing = 1) "
            + ""
            + "select ks_code, count(person_code) as num_persons_missing "
            + "from person_missing_one p, ( "
            + "   select ks_code "
            + "   from skill_codes) sc "
            + "   where sc.ks_code in ( "
            + "     select ks_code "
            + "     from skill_codes "
            + "       minus "
            + "     select ks_code "
            + "     from person_skill "
            + "     where p.person_code = person_code) "
            + "   group by ks_code"; 
  }

  private void query18() {
    queryValue = "with needed_skills as ( "
            + "   select ks_code "
            + "   from jp_skill"
            + "   where jp_code = '302'), "
            + ""
            + "missing_skill (person_code, num_missing) as ( "
            + "   (select person_code, count(ks_code) "
            + "   from person p, needed_skills "
            + "   where ks_code in ( "
            + "     (select ks_code "
            + "     from needed_skills) "
            + "       minus "
            + "     (select ks_code "
            + "     from person_skill "
            + "     where p.person_code = person_code)) "
            + "   group by person_code)) "
            + ""
            + "select person_code, num_missing as smallest "
            + "from missing_skill "
            + "where num_missing = ( "
            + "   select min(num_missing) "
            + "   from missing_skill) "
            + "order by person_code asc"; 
  }

  private void query19() {
    queryValue = "with skill_list as ( "
            + "   select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '100'), "
            + ""
            + "missing_skills (person_code, num_missing) as ( "
            + "   select person_code, count(ks_code) "
            + "   from person p, ( "
            + "     select ks_code "
            + "     from skill_list) j "
            + "   where j.ks_code in ( "
            + "     select ks_code "
            + "     from skill_list "
            + "       minus "
            + "     select ks_code "
            + "     from person_skill "
            + "     where p.person_code = person_code) "
            + "     group by person_code) "
            + ""
            + "select person_code, num_missing "
            + "from missing_skills "
            + "where num_missing <= 3 "
            + "order by num_missing desc";
  }

  private void query20() {
    queryValue = "with skill_codes as ( "
            + "   select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '100'), "
            + ""
            + "missing_skills (person_code, num_missing) as ( "
            + "   select person_code, count(ks_code) "
            + "   from person p, ( "
            + "     select ks_code "
            + "     from skill_codes) j "
            + "   where j.ks_code in ( "
            + "     select ks_code "
            + "     from skill_codes "
            + "       minus "
            + "     select ks_code "
            + "     from person_skill "
            + "     where p.person_code = person_code) "
            + "   group by person_code), "
            + ""
            + "missing_one (person_code) as ( "
            + "   select person_code "
            + "   from missing_skills "
            + "   where num_missing = 1) "
            + ""
            + "select ks_code, count(person_code) as num_missing_one "
            + "from missing_one m, ( "
            + "   select ks_code "
            + "   from skill_codes) s "
            + "where s.ks_code in ( "
            + "   select ks_code "
            + "   from skill_codes "
            + "     minus "
            + "   select ks_code "
            + "   from person_skill "
            + "   where m.person_code = person_code) "
            + "group by ks_code";
  }

  private void query21() {
    queryValue = "select last_name, first_name, email "
            + "from person inner join employment on person.person_code = employment.person_code "
            + "where job_code = '9876543'";
  }

  private void query22() {
    queryValue = "with unemployed as ( "
            + "   select person_code "
            + "   from person "
            + "     minus "
            + "   select distinct person_code "
            + "   from employment "
            + "   where status = 'employed') "
            + ""
            + "select distinct last_name, first_name, job_code, jp_code "
            + "from person inner join unemployed on person.person_code = unemployed.person_code natural join job "
            + "where jp_code = '300' "
            + "order by person.last_name asc";
  }

  private void query23() {
    queryValue = "with employer_count as ( "
            + "   select comp_code, count(pay_rate) as num_employees "
            + "   from job inner join employment on job.job_code = employment.job_code "
            + "   where status = 'employed' "
            + "   group by comp_code) "
            + ""
            + "select comp_code, comp_name, num_employees "
            + "from employer_count natural join company "
            + "order by (num_employees) desc";
  }

  private void query24() {
    queryValue = "with employer_count as ( "
            + "     select count(pay_rate) as num_employees, comp_code "
            + "     from job join employment using(job_code) "
            + "     where status = 'employed' "
            + "     group by comp_code), "
            + ""
            + "count_per_sector as ( "
            + "     select primary_sector, sum(num_employees) as sector_employee_count "
            + "     from employer_count natural join company "
            + "     group by primary_sector), "
            + ""
            + "max_count as ( "
            + "     select max(sector_employee_count) as max "
            + "     from count_per_sector) "
            + ""
            + "select primary_sector "
            + "from max_count, count_per_sector "
            + "where max = sector_employee_count";
  }

  private void query25() {
    queryValue = "with old_salary as ( "
            + "   select distinct max(pay_rate) as old_pay, person_code "
            + "   from employment natural join job natural join company "
            + "   where status = 'unemployed' and primary_sector = 'tourism' "
            + "   group by person_code), "
            + ""
            + "present_salary as ( "
            + "   select distinct max(pay_rate) as present_pay, person_code "
            + "   from employment natural join job natural join company "
            + "   where status = 'employed' and primary_sector = 'tourism' "
            + "   group by person_code), "
            + ""
            + "people_decrease as ( "
            + "   select count(person_code) as decline "
            + "   from old_salary natural join present_salary "
            + "   where present_pay < old_pay), "
            + ""
            + "people_increase as ( "
            + "   select count(person_code) as incline "
            + "   from old_salary natural join present_salary "
            + "   where present_pay > old_pay) "
            + ""
            + "select sum(incline) inc_ratio, sum(decline) dec_ratio "
            + "from people_increase natural join people_decrease";
  }

  private void query26() {
    queryValue = "with unemployed as ( "
            + "   select distinct person_code"
            + "   from employment "
            + "   where status = 'unemployed'), "
            + ""
            + "employed as ( "
            + "   select distinct person_code "
            + "   from employment "
            + "   where status = 'employed'), "
            + ""
            + "openings as ( "
            + "   select distinct job_code "
            + "   from ( "
            + "     select job_code "
            + "     from unemployed natural join employment "
            + "       minus "
            + "     select job_code "
            + "     from employed natural join employment)), "
            + ""
            + "num_of_profiles as ( "
            + "   select jp_code, count(job_code) as num_of_openings "
            + "   from openings natural join job "
            + "   group by jp_code), "
            + ""
            + "people_qualified as ( "
            + "   select jp_code, count(person_code) as num_qualified "
            + "   from num_of_profiles n, person p"
            + "   where not exists ( "
            + "     select ks_code "
            + "     from num_of_profiles natural join jp_skill "
            + "     where n.jp_code = jp_code "
            + "       minus "
            + "     select ks_code "
            + "     from num_of_profiles natural join jp_skill natural join person_skill "
            + "     where p.person_code = person_code) "
            + "   group by jp_code), "
            + ""
            + "missing_skill as ( "
            + "   select jp_code, (num_of_openings - num_qualified) as unqualified "
            + "   from people_qualified natural join num_of_profiles), "
            + ""
            + "max_missing as ( "
            + "   select max(unqualified) as max_unqualified "
            + "   from missing_skill) "
            + ""
            + "select jp_code "
            + "from missing_skill, max_missing "
            + "where unqualified = max_unqualified";
  }

  private void query27() {
    
  }

  private void query28() {
    queryValue = "with person_skills as ( "
            + "   select ks_code "
            + "   from person_skill "
            + "   where person_code = '6969696'), "
            + ""
            + "person_courses as ( "
            + "   select ks_code "
            + "   from attends natural join person_skill "
            + "   where person_code = '6969696'), "
            + ""
            + "skills_needed as ( "
            + "   select ks_code "
            + "   from jp_skill "
            + "   where jp_code = '701' "
            + "     minus "
            + "   select ks_code "
            + "   from person_skills), "
            + ""
            + "courses_needed as ( "
            + "   select course_code, course_title "
            + "   from skills_needed natural join course_skill natural join course) "
            + ""
            + "select distinct course_code, course_title "
            + "from courses_needed c "
            + "where not exists ( "
            + "   select course_code "
            + "   from person_courses "
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
