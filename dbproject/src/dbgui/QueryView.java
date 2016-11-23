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
  private String comp_code = null;
  private String jp_code = null;
  private String job_code = null;
  private String queryValue = null;
  private String pay_type = null;
  private String status = null;
  private String primary_sector = null;
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
    return chosenQuery;
  }
  
  private void valueListActionPerformed(ActionEvent evt) {
    if (queryNum == 1) {
      comp_code = (String) valueList.getSelectedItem();
      
      query1();
    }    
  }
  
  private void secondaryListActionPerformed(ActionEvent evt) {
    
  }
  
  private void jbutton1ActionPerformed(ActionEvent evt) {
    
  }

  private void query1() {
    queryValue = "select last_name, first_name"
            + "from person inner join employment on person.perosn_code = "
            + "employment.person_code inner join job on employment.job_code = "
            + "job.job_code";
  }
  
  private void query2() {
    queryValue = "select distinct last_name, first_name, pay_rate"
            + "from person inner join employment on person.person_code ="
            + "employment.person_code inner join job on employment.job_code = "
            + "job.job_code"
            + "where comp_code = " + comp_code + " and pay_type =" + pay_type 
            + "order by (pay_rate) desc";
  }
  
  private void query3() {
    queryValue = "with salary as ("
            + "   select comp_code, sum(pay_rate) worker_salary"
            + "   from job inner join employment on job.job_code = employment.job_code"
            + "   where pay_type = 'salary'"
            + "   group by (job.comp_code)),"
            + "wage as ("
            + "   select comp_code, sum(pay_rate*1920) worker_salary"
            + "   from job inner join employment on job.job_code = employment.job_code"
            + "   where pay_type = 'wages'"
            + "   group by (job.bomp_code)),"
            + "cost as ("
            + "   ((select * "
            + "   from salary)"
            + "      union"
            + "   (select * "
            + "   from wage))"
            + "select comp_code, sum(worker_salary) as total_cost"
            + "from cost"
            + "group by comp_code"
            + "order by total_cost desc;";
  }
  
  private void query4() {
    queryValue = "select job_code"
            + "from employment natural join person"
            + "where person_code = " + comp_code;
  }
  
  private void query5() {
    queryValue = "select ks_name"
            + "from person_skill natural join skills"
            + "where person_code =" + person_code
            + "order by skills.ks_name asc;";
  }
  
  private void query6() {
    queryValue = "with person_skills as ("
            +     "select ks_code, ks_level"
            +     "from person_skill natural join skills"
            +     "where person_code = " + person_code + "),"
            + "person_job as ("
            +     "select job_code"
            +     "from job natural join employment"
            +     "where person_code = " + person_code + "),"
            + "job_skills as ("
            +     "(select ks_code"
            +     "from person_skills)"
            +        "minus"
            +     "(select ks_code"
            +     "from job_skills))"
            + "select distinct ks_level"
            + "from skills natural join skill_gap;";
  }
  
  private void query7() {
    queryValue = "select ks_name"
            + "from skills inner join jp_skill on skills.ks_code = "
            + "jp_skill.ks_code"
            + "where jp_skill.jp_code = " + jp_code + ";";
  }
  
  private void query8() {
    queryValue = "with has_skill as ("
            +     "select ks_code"
            +     "from person_skill"
            +     "where person_code = " + person_code + "),"
            + "required_skill as ("
            +     "select ks_code"
            +     "from job_skill"
            +     "where job_code = " + job_code + "),"
            + "skill_gap as ("
            +     "(select *"
            +     "from has_skill)"
            +         "union"
            +     "(select *"
            +     "from required_skill))"
            + "select ks_name"
            + "from skill_gap natural join skills;";
  }
  
  private void query9() {
    queryValue = "with required_skills as ("
            +     "select ks_code, ks_name"
            +     "from job_skill natural join skills"
            +     "where job_code = " + job_code + "),"
            + "current_skills as ("
            +     "select ks_code, ks_name"
            +     "from person_skill natural join skills"
            +     "where person_code = " + person_code + "),"
            + "needed_course as ("
            +     "(select *"
            +     "from required_skills)"
            +         "minus"
            +     "(select *"
            +     "from current_skills)),"
            +   "missing_skill as ("
            + "select ks_code"
            + "from needed_course)"
            + "select course_code, course_title"
            + "from missing_skill natural join course_skill natural join course;";
  }
  
  private void query10() {
    queryValue = "with needed_skills as ("
            + "   (select ks_code"
            + "   from jp_skill natural join job_profile"
            + "   where jp_code = " + jp_code + ")"
            + "     minus"
            + "   (select ks_code"
            + "   from person_skill"
            + "   where person_code = " + person_code + ")),"
            + "course_skills as ("
            + "   select distinct c.course_code"
            + "   from course c"
            + "   where not exists ("
            + "     select ks_code"
            + "     from needed_skills"
            + "       minus"
            + "     select ks_code"
            + "     from course_skill natural join course"
            + "     where course_code = c.course_code))"
            + "select course_code"
            + "from course_skills;";
  }
  
  private void query11() {
    queryValue = "select course_code, course_title, cost"
            + "   from course natural join section natural join course_skill"
            + "   where ks_code in ("
            + "select ks_code"
            + "from jp_skill in natural join skills natural join job"
            + "where ks_code not in ("
            + "   select ks_code"
            + "   from job_profile natural join person_skill"
            + "   where person_code = " + person_code + "))"
            + "     and cost = ("
            + "       select min(cost)"
            + "       from course natural join section natural join course_skill"
            + "       where ks_code in ("
            + "         select ks_code"
            + "         from jp_skill natural join skills natural join job"
            + "         where ks_code not in ("
            + "           select ks_code"
            + "           from job_profile natural join person_skill"
            + "           where person_code = " + person_code + ")));";
  }
  
  private void query12() {
    queryValue = "with set_of_jobs as ("
            + "   select ks_code, job_code"
            + "   from job_skill natural join job"
            + "   where jp_code = " + jp_code + "),"
            + "course_sets as ("
            + "   select course_code as, null as b, null as c"
            + "   from course"
            + "     union"
            + "   select a.course_code as a, b.course_code as b, null as c"
            + "   from course a, course b"
            + "   where a.course_code = b.course_code"
            + "     union"
            + "   select a.course_code as a, b.course_code b, course_code as c"
            + "   from course a, course b, course c"
            + "   where a.course_code < b.course_code"
            + "     and b.course_code < c.course_code),"
            + "course_sets_skills as ("
            + "   select a, b, c, ks_code"
            + "   from course_skill, course_sets"
            + "   where course_sets.a = course_skill.course_code"
            + "     or course_sets.b = course_skill.course_code"
            + "     or course_sets.c = course_skill.course_code),"
            + "good_set as ("
            + "   select *"
            + "   from ("
            + "     (select a, b, c"
            + "     from course_sets_skill)"
            + "       minus"
            + "     (select a, b, c"
            + "     from ("
            + "       select a, b, c, ks_code"
            + "       from course_sets_skills),"
            + "(select *"
            + "from set_of_jobs)"
            + "   minus"
            + "(select a, b, c, ks_code"
            + "from course_sets_skills)))),"
            + "count as ("
            + "   select a, b, c,"
            + "     case"
            + "       when b is null then 1"
            + "       when c is null then 2"
            + "       else 3"
            + "     end as num_courses"
            + "   from good_set)"
            + "select a, b, c, num_courses"
            + "from count"
            + "where num_courses = ("
            + "   select min(num_courses)"
            + "   from count);";
  }
  
  private void query13() {
    queryValue = "with person_skill as ("
            + "   select ks_code"
            + "   from person_skill"
            + "   where person_code = " + person_code + ")"
            + "select jp_code, jp_title"
            + "from job_profile j"
            + "where not exists ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where j.jp_code = jp_skill.jp_code"
            + "     minus"
            + "   select *"
            + "   from person_skills);";
  }
  
  private void query14() {
    queryValue = "with person_skills as ("
            + "   select ks_code"
            + "   from person_skill"
            + "   where person_code = " + person_code + "),"
            + "qualification as ("
            + "   select jp_ode, jp_title"
            + "   from job_profile j"
            + "   where not exists ("
            + "     select ks_code"
            + "     from jp_skill"
            + "     where j.jp_code = jp_code"
            + "       minus"
            + "     select ks_code"
            + "     from person_skills)),"
            + "job_qualifications as ("
            + "   select didtinct job_code"
            + "   from qualifications natural join job),"
            + "pay as ("
            + "   select distinct job_code, ("
            + "     case"
            + "       when pay_type = 'salary' then pay_rate"
            + "       when pay_type = 'wage' then pay_rate"
            + "       else null"
            + "     end) as max_pay"
            + "   from job_qualifications natural join job),"
            + "max as ("
            + "   select min(max_pay)"
            + "   from pay"
            + "   where ax_pay = ("
            + "     select * "
            + "     from max);";
  }
  
  private void query15() {
    queryValue = "with required_skills as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = " + jp_code + ")"
            + "select last_name, first_name, email, person_code"
            + "from person p"
            + "where not exists ("
            + "   select *"
            + "   from required_skills"
            + "     minus"
            + "   select ks_code"
            + "   from person_skill"
            + "   where p.person_code = person_code);";
  }
  
  private void query16() {
    queryValue = "with skill_codes as ("
            + "   select ks_code "
            + "   from jp_skill"
            + "   where jp_code = " + jp_code + "),"
            + "missing_one (person_code, num_missing) as ("
            + "   select person_code, count(ks_code)"
            + "   from person p,"
            + "     (select *"
            + "     from skill_codes) sc"
            + "     where sc.ks_code in ("
            + "       select *"
            + "       from skill_codes"
            + "         minus"
            + "       select ks_code"
            + "       from person_skill"
            + "       where p.person_code = person_code)"
            + "   group by person_code)"
            + "select person_code, num_missing"
            + "from missing_one"
            + "where num_missing = 1"
            + "order by num_missing desc;";
  }
  
  private void query17() {
    queryValue = "with skill_codes as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = " + jp_code + "),"
            + "missing_one (person_code, num_missing) as ("
            + "   select person_code, count(ks_code)"
            + "   from person p, ("
            + "     select *"
            + "     from skill_codes) sc"
            + "     where sc.ks_code in ("
            + "       select *"
            + "       from skill_codes"
            + "         minus"
            + "       select ks_code"
            + "       from person_skill"
            + "       where p.person_code = person_code)"
            + "     group by person_code),"
            + "person_missing_one (person_code) as ("
            + "   select person_code"
            + "   from missing_one"
            + "   where num_missing = 1)"
            + "select ks_code, count(person_code) as num_persons_missing"
            + "from person_missing_one p, ("
            + "   select *"
            + "   from skill_codes) sc"
            + "   where sc.skill_code in ("
            + "     select *"
            + "     from skilll_codes"
            + "       minus"
            + "     select ks_code"
            + "     from person_skill"
            + "     where p.person_code = person_code)"
            + " group by ks_code;";
  }
  
  private void query18() {
    queryValue = "with needed_skill as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = " + jp_code + "),"
            + "missing_skill (person_code, num_missing) as (("
            + "   select person_code, count(ks_code)"
            + "   from person p, needed_skills"
            + "   where ks_code in (("
            + "     select ks_code"
            + "     from needed_skills)"
            + "       minus"
            + "     select ks_code"
            + "     from person_skill"
            + "     where p.person_code = person_code)"
            + "   group by person_code)"
            + "select person_code, num_missing"
            + "from missing_skills"
            + "where num_missing == 3"
            + "order by num_missing desc;";
  }
  
  private void query19() {
    queryValue = "with skill_list as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = " + jp_code + "),"
            + "missing_skill (person_code, num_missing) as ("
            + "   select peron_code, count(ks_code)"
            + "   from person p, ("
            + "     select *"
            + "     from skill_codes) j"
            + "     where j.ks_code in ("
            + "       select *"
            + "     from skill_codes"
            + "       minus"
            + "     select ks_code"
            + "     from person_skill"
            + "     where p.person_code = person_code)"
            + "   group by person_code)"
            + "select person_code, num_missing"
            + "from missing_skill"
            + "where num_missing <= 3"
            + "order by num_missing;";
  }
  
  private void query20() {
    queryValue = "with skill_codes as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = " + jp_code + "),"
            + "missing_skills (person_code, num_missing) as ("
            + "   select person_code, count(ks_code)"
            + "   from person p, "
            + "     (select *"
            + "     from skill_codes) j"
            + "     where j.ks_code in ("
            + "       select *"
            + "     from skill_codes"
            + "       minus"
            + "     select ks_code"
            + "     from person_skill"
            + "     where p.person_code = person_code)"
            + "     group by person_code),"
            + "missing_one (person_code) as ("
            + "   select person_code"
            + "   from missing_skills"
            + "   where num_missing = 1)"
            + "select ks_code, count(person_code) as num_missing_one"
            + "from missing_one p, ("
            + "   select *"
            + "   from skill_codes) j"
            + "   where j.ks_code in ("
            + "     select *"
            + "     from skill_codes"
            + "       minus"
            + "     select ks_code"
            + "     from person_skill"
            + "     where p.person_code = person_code)"
            + "   group by ks_code;";
  }
  
  private void query21() {
    queryValue = "select last_name, first_name, email"
            + "from person inner join employment in person.person_code ="
            + "employment.person_code"
            + "where job_code = " + job_code + ";";
  }
  
  private void query22() {
    queryValue = "with unemployed as ("
            + "   select person_code"
            + "   from person"
            + "     minus"
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = 'employed')"
            + "select disting last_name, first_name, job_code, jp_code"
            + "from person inner join unemployed on person.person_code = "
            + "employment.person_code natural join job"
            + "where jp_code = " + jp_code 
            + "order by person.last_name asc;";
  }
  
  private void query23() {
    queryValue = "with employer_count as ("
            + "   select comp_code, count(pay_rate) as num_employees"
            + "   from job inner koin employment on job.job_code = "
            + "   employment.job_code"
            + "   where status = " + status
            + "   group by comp_code)"
            + "select comp_code, comp_name, num_employees"
            + "from employer_count natural join company"
            + "order by (num_employees) desc;";
  }
  
  private void query24() {
    queryValue = "with num_employees as ("
            + "   select count(pay_rate) as employee_count, comp_code"
            + "   from job natural join employment"
            + "   where status = " + status
            + "   group by comp_code),"
            + "employees_per_sector as ("
            + "   select primary_sector, sum(employee_count) as sector_count"
            + "   from num_employees natural join company"
            + "   group by primary_sector),"
            + "majority_count as ("
            + "   select max(sector_count) as biggest_sector"
            + "   from employees_per_sector)"
            + "   select primary_sector"
            + "from majority_count, employees_per_sector"
            + "where biggest_sect = sector_count;";
  }
  
  private void query25() {
    queryValue = "with old_salary as ("
            + "   select distinct max(pay_rate) as old_pay, person_code"
            + "   from emplyment natural join job natural join company"
            + "   where status = " + status
            + "     and primary sector = " + primary_sector
            + "   group by person_code),"
            + "present_salary as ("
            + "   select distinct max(pay_rate) as present_pay, person_code"
            + "   from employment natural join job natural join company"
            + "   where status = " + status
            + "     and primary_sector = " + primary_sector
            + "   group by person_code),"
            + "people_decrease as ("
            + "   select count(person_code) as decline"
            + "   from old_salary natural join present_salary"
            + "   where present_pay < old_pay),"
            + "people_increase as ("
            + "   select count(person_code) as incline "
            + "   from old_salary natural join present_salary"
            + "   where present_pay > old_pay)"
            + "select sum(incline) inc_ration, sum(decline) dec_ration"
            + "from people_increase natural join people_decrease;";
  }
  
  private void query26() {
    queryValue = "with unemployed as ("
            + "   select distinct perso_code"
            + "   from employment"
            + "   where status = " + status + "),"
            + "employed as ("
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = " + status + "),"
            + "openings as ( "
            + "   select distinct job_code"
            + "   from ("
            + "     select job_code"
            + "     from unemployed natural join employment"
            + "       minus"
            + "     select job_code"
            + "     from employed natural join employment)),"
            + "num_of_profiles as ("
            + "   select jp_code, count(job_code) as num_of_openings"
            + "   from openings natural join job"
            + "   group by jp_code),"
            + "people_qualified as ("
            + "   select jp_code, count?(person_code) as num_qualified"
            + "   from num_of_profiles j, peron p"
            + "   where not exists ("
            + "     select ks_code"
            + "     from num_of_profiles natural join jp_skill"
            + "     where j.jp_code = jp_code"
            + "       minus"
            + "     select distinct ks_code"
            + "     from num_of_profiles natural join jp_skill natural join "
            + "       person_skill"
            + "     where p.person_code = person_code)"
            + "     group by jp_code),"
            + "missing_skill as ("
            + "   select jp_code, (num_of_openings - num_qualifies) as unqualified"
            + "   from people_qualified natural join num_of_profiles),"
            + "max_missing as ("
            + "   select max(unqualified) as max_unqualified"
            + "   from missing_skill)"
            + "select jp_code"
            + "from missing_skill, max_missing"
            + "where unqualified = max_unqualified;";
  }
  
  private void query27() {
    queryValue = "with unemployed as ("
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = " + status + "),"
            + "employed as ("
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = " + status + "),"
            + "openings as ("
            + "   select distinct job_code"
            + "   from ("
            + "     select job_code"
            + "     from unemployed natural join employment"
            +         "minus"
            + "     select job_code"
            + "     from employed natural join emplyoment)),"
            + "num_of_profiles as ("
            + "   select jp_code, count(job_code) as num_openings"
            + "   from openings natural join job"
            + "   group by jp_code),"
            + "qualified as ("
            + "   select jp_code, count(person_code) as num_qualified"
            + "   from num_of_profiles j, person p"
            + "   where not exists ("
            + "     select ks_code"
            + "     from num_of_profiles natural join jp_skill"
            + "     where j.jp_code = jp_code"
            + "       minus"
            + "     select distinct ks_code"
            + "     from num_of_profiles natural join jp_skill natural join "
            + "       person_skill"
            + "     where p.person_code = person_code)"
            + "     group by jp_code),"
            + "lacking as ("
            + "   select jp_code, (num_openings - num_qualified) as "
            + "     lacking_qualifications"
            + "   from qualified natural join num_of_profiles),"
            + "max_lacking as ("
            + "   select max(lacking_qualifications) as max_lackings"
            + "   from lacking),"
            + "max_lacking_ as ("
            + "   select jp_code"
            + "   from lacking, max_lacking"
            + "   where lacking_qualkifications = max_lackings),"
            + "course_sets as ("
            + "   select course_code as a, null as b, null as c"
            + "   from course"
            + "     union"
            + "   select a.course_code as a, b.course_code as b, null as c"
            + "   from course, course b"
            + "   where a.course_code < b.course_code"
            + "     union"
            + "   select a.course_code as a, b.course_code as b, c.course_code as c"
            + "   from course a, course b, course c"
            + "   where a.course_code < b.course_code"
            + "     and b.course_code < c.course_code),"
            + "course_sets_skills as ("
            + "   select a, b, c, ks_code"
            + "   from course_skill, course_sets"
            + "   where course_sets.a = course_skill.course_code"
            + "     or course_sets.b = course_skill.course_code"
            + "     or course_sets.c = course_skill.course_code)"
            + "select *"
            + "from (("
            + "   select a, b, c, jp_code"
            + "   from course_sets_skills, max_lacking_)"
            + "     minus"
            + "   (select a, b, c, jp_code"
            + "   from ("
            + "     select a, b, c, jp_code, ks_code"
            + "     from ("
            + "       select a, b, c"
            + "       from course_sets_skills),"
            + "(select jp_code, ks_code"
            + "from max_lacking_ natural join jp_skill)"
            + "   minus"
            + "select a, b, c, jp_code, ks_code"
            + "from course_sets_skills, max_lacking)));";
  }
  
  private void query28() {
    queryValue = "with person_skills as ("
            + "   select ks_code"
            + "   from person_skill"
            + "   where person_code = " + person_code + "),"
            + "person_courses as ("
            + "   select ks_code"
            + "   from attends natural join person_skil"
            + "   where person_code = " + person_code + "),"
            + "skills_needed as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = " + jp_code
            + "     minus"
            + "   select ks_code"
            + "   from person_skills),"
            + "courses_needed as ("
            + "   select course_code"
            + "   from skills_needed natural join course_skill)"
            + "select distinct course_code"
            + "from courses_needed c"
            + "where not exists ("
            + "   select course_code"
            + "   from person_courses "
            + "   where c.course_code = course_code);";
  }
  
  public void main(String[] args) throws SQLException {
    if (args.length < 2) {
      System.out.println("usage: java TableInfo db-username db-password");
      System.exit(1);
    }
    dbaccess tc = new dbaccess();
    Connection conn = tc.getDBConnection(args[0], args[1]);
    TableUpdate tu = new TableUpdate(conn);
    EditTables inst = new EditTables(tu, conn);
    inst.setVisible(true);
  }
}
