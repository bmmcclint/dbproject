/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author bmcclint
 */
public class PreparedStatements {

  private final Connection conn;
  private PreparedStatement stmt;
  private ResultSet rs;

  public PreparedStatements(Connection conn) {
    this.conn = conn;
  }

  public ResultSet query1(String comp_code) throws SQLException {
    //1.
    stmt = conn.prepareStatement("select last_name, first_name"
            + "from person inner join employment on person.perosn_code = "
            + "employment.person_code inner join job on employment.job_code = "
            + "job.job_code");
    stmt.setString(1, comp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query2(String comp_code) throws SQLException {
    //2.
    stmt = conn.prepareStatement("select distinct last_name, first_name, pay_rate"
            + "from person inner join employment on person.person_code ="
            + "employment.person_code inner join job on employment.job_code = "
            + "job.job_code"
            + "where comp_code = '1001001' and pay_type = 'salary'"
            + "order by (pay_rate) desc");
    stmt.setString(1, comp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query3(String comp_code) throws SQLException {
    //3.
    stmt = conn.prepareStatement(
            "with salary as ("
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
            + "order by total_cost desc;");
    stmt.setString(1, comp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query4(String person_code) throws SQLException {
    //4.
    stmt = conn.prepareStatement(
            "select job_code"
            + "from employment natural join person"
            + "where person_code = '1018256'");
    stmt.setString(1, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query5(String person_code) throws SQLException {
    //5.
    stmt = conn.prepareStatement(
            "select ks_name"
            + "from person_skill natural join skills"
            + "where person_code = '1536512'"
            + "order by skills.ks_name asc;");
    stmt.setString(1, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query6(String person_code) throws SQLException {
    //6.
    stmt = conn.prepareStatement(
            "with person_skills as ("
            +     "select ks_code, ks_level"
            +     "from person_skill natural join skills"
            +     "where person_code = '1024701'),"
            + "person_job as ("
            +     "select job_code"
            +     "from job natural join employment"
            +     "where person_code = '1024701'),"
            + "job_skills as ("
            +     "(select ks_code"
            +     "from person_skills)"
            +        "minus"
            +     "(select ks_code"
            +     "from job_skills))"
            + "select distinct ks_level"
            + "from skills natural join skill_gap;");
    stmt.setString(1, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query7(String jp_code) throws SQLException {
    //7.
    stmt = conn.prepareStatement(
            "select ks_name"
            + "from skills inner join jp_skill on skills.ks_code = "
            + "jp_skill.ks_code"
            + "where jp_skill.jp_code = '100';");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query8(String job_code) throws SQLException {
    //8. 
    stmt = conn.prepareStatement(
            "with has_skill as ("
            +     "select ks_code"
            +     "from person_skill"
            +     "where person_code = '1024701'),"
            + "required_skill as ("
            +     "select ks_code"
            +     "from job_skill"
            +     "where job_code = '3001001'),"
            + "skill_gap as ("
            +     "(select *"
            +     "from has_skill)"
            +         "union"
            +     "(select *"
            +     "from required_skill))"
            + "select ks_name"
            + "from skill_gap natural join skills;");
    stmt.setString(1, job_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query9(String job_code, String person_code) throws SQLException {
    //9.
    stmt = conn.prepareStatement(
            "with required_skills as ("
            +     "select ks_code, ks_name"
            +     "from job_skill natural join skills"
            +     "where job_code = '3101001'),"
            + "current_skills as ("
            +     "select ks_code, ks_name"
            +     "from person_skill natural join skills"
            +     "where person_code = '1357909'),"
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
            + "from missing_skill natural join course_skill natural join course;");
    stmt.setString(1, job_code);
    stmt.setString(2, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query10(String jp_code, String person_code) throws SQLException {
    //10.
    stmt = conn.prepareStatement(
            "with needed_skills as ("
            + "   (select ks_code"
            + "   from jp_skill natural join job_profile"
            + "   where jp_code = '300')"
            + "     minus"
            + "   (select ks_code"
            + "   from person_skill"
            + "   where person_code = '6969696')),"
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
            + "from course_skills;");
    stmt.setString(1, jp_code);
    stmt.setString(2, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query11(String person_code) throws SQLException {
    //11.
    stmt = conn.prepareStatement(
            "select course_code, course_title, cost"
            + "   from course natural join section natural join course_skill"
            + "   where ks_code in ("
            + "select ks_code"
            + "from jp_skill in natural join skills natural join job"
            + "where ks_code not in ("
            + "   select ks_code"
            + "   from job_profile natural join person_skill"
            + "   where person_code = '1017145'))"
            + "     and cost = ("
            + "       select min(cost)"
            + "       from course natural join section natural join course_skill"
            + "       where ks_code in ("
            + "         select ks_code"
            + "         from jp_skill natural join skills natural join job"
            + "         where ks_code not in ("
            + "           select ks_code"
            + "           from job_profile natural join person_skill"
            + "           where person_code = '1017145')));");
    stmt.setString(1, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query12(String jp_code) throws SQLException {
    //12.
    stmt = conn.prepareStatement(
            "with set_of_jobs as ("
            + "   select ks_code, job_code"
            + "   from job_skill natural join job"
            + "   where jp_code = '666'),"
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
            + "   from count);");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query13(String person_code) throws SQLException {
    //13.
    stmt = conn.prepareStatement(
            "with person_skill as ("
            + "   select ks_code"
            + "   from person_skill"
            + "   where person_code = '2165778')"
            + "select jp_code, jp_title"
            + "from job_profile j"
            + "where not exists ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where j.jp_code = jp_skill.jp_code"
            + "     minus"
            + "   select *"
            + "   from person_skills);");
    stmt.setString(1, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query14(String person_code) throws SQLException {
    //14.
    stmt = conn.prepareStatement(
            "with person_skills as ("
            + "   select ks_code"
            + "   from person_skill"
            + "   where person_code = '6969696'"
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
            + "     from max);");
    stmt.setString(1, person_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query15(String jp_code) throws SQLException {
    //15.
    stmt = conn.prepareStatement(
            "with required_skills as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = '100')"
            + "select last_name, first_name, email, person_code"
            + "from person p"
            + "where not exists ("
            + "   select *"
            + "   from required_skills"
            + "     minus"
            + "   select ks_code"
            + "   from person_skill"
            + "   where p.person_code = person_code);");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query16(String jp_code) throws SQLException {
    //16.
    stmt = conn.prepareStatement(
            "with skill_codes as ("
            + "   select ks_code "
            + "   from jp_skill"
            + "   where jp_code = '666'),"
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
            + "order by num_missing desc;");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query17(String jp_code) throws SQLException {
    //17.
    stmt = conn.prepareStatement(
            "with skill_codes as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = '100'),"
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
            + " group by ks_code;");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query18(String jp_code) throws SQLException {
    //18.
    stmt = conn.prepareStatement(
            "with needed_skill as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code ='302'),"
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
            + "order by num_missing desc;");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query19(String jp_code) throws SQLException {
    //19.
    stmt = conn.prepareStatement(
            "with skill_list as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = '100'),"
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
            + "order by num_missing;");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query20(String jp_code) throws SQLException {
    //20.
    stmt = conn.prepareStatement(
            "with skill_codes as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = '100'),"
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
            + "   group by ks_code;");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query21(String job_code) throws SQLException {
    //21.
    stmt = conn.prepareStatement(
            "select last_name, first_name, email"
            + "from person inner join employment in person.person_code ="
            + "employment.person_code"
            + "where job_code = '9876543';");
    stmt.setString(1, job_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query22(String jp_code) throws SQLException {
    //22.
    stmt = conn.prepareStatement(
            "with unemployed as ("
            + "   select person_code"
            + "   from person"
            + "     minus"
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = 'employed')"
            + "select disting last_name, first_name, job_code, jp_code"
            + "from person inner join unemployed on person.person_code = "
            + "employment.person_code natural join job"
            + "where jp_code = '300'"
            + "order by person.last_name asc;");
    stmt.setString(1, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query23(String status) throws SQLException {
    //23.
    stmt = conn.prepareStatement(
            "with employer_count as ("
            + "   select comp_code, count(pay_rate) as num_employees"
            + "   from job inner koin employment on job.job_code = "
            + "   employment.job_code"
            + "   where status = 'employed'"
            + "   group by comp_code)"
            + "select comp_code, comp_name, num_employees"
            + "from employer_count natural join company"
            + "order by (num_employees) desc;");
    stmt.setString(1, status);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query24(String status) throws SQLException {
    //24.
    stmt = conn.prepareStatement(
            "with num_employees as ("
            + "   select count(pay_rate) as employee_count, comp_code"
            + "   from job natural join employment"
            + "   where status = 'employed'"
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
            + "where biggest_sect = sector_count;");
    stmt.setString(1, status);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query25(String status, String primary_sector) throws SQLException {
    //25.
    stmt = conn.prepareStatement(
            "with old_salary as ("
            + "   select distinct max(pay_rate) as old_pay, person_code"
            + "   from emplyment natural join job natural join company"
            + "   where status = 'unemployed'"
            + "     and primary sector = 'tourism'"
            + "   group by person_code),"
            + "present_salary as ("
            + "   select distinct max(pay_rate) as present_pay, person_code"
            + "   from employment natural join job natural join company"
            + "   where status = 'employed'"
            + "     and primary_sector = 'tourism'"
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
            + "from people_increase natural join people_decrease;");
    stmt.setString(1, status);
    stmt.setString(2, primary_sector);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query26(String status) throws SQLException {
    //26.
    stmt = conn.prepareStatement(
            "with unemployed as ("
            + "   select distinct perso_code"
            + "   from employment"
            + "   where status = 'unemployed'),"
            + "employed as ("
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = 'employed'),"
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
            + "where unqualified = max_unqualified;");
    stmt.setString(1, status);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query27(String status) throws SQLException {
    //27.
    stmt = conn.prepareStatement(
            "with unemployed as ("
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = 'unemployed'),"
            + "employed as ("
            + "   select distinct person_code"
            + "   from employment"
            + "   where status = 'employed'),"
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
            + "from course_sets_skills, max_lacking)));");
    stmt.setString(1, status);
    rs = stmt.executeQuery();
    return rs;
  }

  public ResultSet query28(String person_code, String jp_code) throws SQLException {
    //28.
    stmt = conn.prepareStatement(
            "with person_skills as ("
            + "   select ks_code"
            + "   from person_skill"
            + "   where person_code = '6969696'),"
            + "person_courses as ("
            + "   select ks_code"
            + "   from attends natural join person_skil"
            + "   where person_code = '6969696'),"
            + "skills_needed as ("
            + "   select ks_code"
            + "   from jp_skill"
            + "   where jp_code = '701'"
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
            + "   where c.course_code = course_code);");
    stmt.setString(1, person_code);
    stmt.setString(2, person_code);
    stmt.setString(3, jp_code);
    rs = stmt.executeQuery();
    return rs;
  }  
}