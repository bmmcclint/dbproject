/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author bmcclint
 */
public final class dbQueries {
  private final ArrayList<String> queries;
  private final ArrayList<String> sqlstmts;
  private final HashMap<String, String> map;
  
  public dbQueries() {
    this.queries = new ArrayList();
    this.sqlstmts = new ArrayList();
    this.map = new HashMap();
    
    addQueries();
    addsqlstmts();
    createMap();
  }
  
  public void addQueries() {
    queries.add("1. List a company's workers in descending order");
    queries.add("2. List a company's staff by salary in descending order");
    queries.add("3. List companies' labor costs (total salaries and wage rates "
            + "by 1920 hours) in descending order");
    queries.add("4. Find all the jobs a person is currently holding and worked "
            + "in the past");
    queries.add("5. List a person's knowledge/skills for a specific job profile"
            + "in a readable format.");
    queries.add("6. List the skill gap of a worker between his/her job(s) and "
            + "his/her skills.");
    queries.add("7. List the required knowledge/skill of a job profile in a "
            + "readable format");
    queries.add("8. List a person's missing knowledge/skills for a specific job"
            + " in a readable format.");
    queries.add("9. List the courses (course id and title) that each alone "
            + "teaches all the missing knowledge/skills fro a person to pursue "
            + "a specific job.");
    queries.add("10. Suppose the skill gap of a worker and the requirement of a"
            + " desired job can be covered by one course. Find the 'quickest' "
            + "solution for this worker. Show the course, section information "
            + "and the complate date");
    queries.add("11. Find the cheapest course to make up one's skill gap by "
            + "showing the course to take and the cost (of the section price)");
    queries.add("12. If query #9 returns nothing, then find the course sets "
            + "that their combination covers all the missing knowledge/skills "
            + "for a person to pursue a specific job. The considered course "
            + "sets will not include more than three courses. If multiple "
            + "course sets are found, list the course sets (with their course "
            + "IDs) in the order of the ascending order of the course sets' "
            + "total costs.");
    queries.add("13. List all the job profiles that a person is qualified for.");
    queries.add("14. Find the job with the highest pay rate for a person "
            + "according to his/her skill qualifications.");
    queries.add("15. List all the names along with the emails of the persons "
            + "who are qualified for a job profile.");
    queries.add("16. When a company cannot find any qualified person for a job,"
            + " secondary solution is to find a person who is almost qualified"
            + " to the job. Make a 'missing-one' list that lists people who"
            + " miss only one skill for a specified job profile.");
    queries.add("17. List the skillID and the number of people in the missing"
            + " one list for a given job profile in the ascending order of the"
            + " people counts.");
    queries.add("18. Suppose there is a new job profile that no one is"
            + " qualified. List the persons who miss the least number of skills"
            + " and report the 'least number'.");
    queries.add("19. For a specified job profile and a given small number k,"
            + " make a 'missing-k' list that lists the people's IDs and the"
            + " number of missing skills for the people who miss only up to k"
            + " skills in the ascending order of missing skills.");
    queries.add("20. Given a job profile and its corresponding missing-k list"
            + " specified Question 19. Find every skill that is needed by at"
            + " least one person in the given missing-k list. List each skillID"
            + " and the number of people who need it in the descending order of"
            + " the people counts.");
    queries.add("21. In a local or national crisis, we need to find all the"
            + " people who once held a job of the special job-profile"
            + " identifier.");
    queries.add("22. Find all the unemployed people who once held a job of the"
            + " given job-profile identifier.");
    queries.add("23. Find the biggest employer in terms of number of employees"
            + " or the total amount of salaries and wages paid to employees.");
    queries.add("24. Find out the job distribution among business sectors; find"
            + " out the biggest sector in terms of number of employes or the"
            + " total amount of salaries and wages paid to employees.");
    queries.add("25. Find out the ratio between the people whose earnings"
            + " increase and those whose earnings decrease; find the average"
            + " rate of earning improvement for the workers in a specific"
            + " business sector.");
    queries.add("26. Find the job profiles that have the most openings due to"
            + " lack of qualified workers. If there are many opening jobs of a"
            + " job profile but at the same time there are many qualified"
            + " jobless people. Then training cannot help fill up this type of"
            + " job. What we want to find is such a job profile that has the"
            + " largest differences between vacancies (the unfilled jobs of"
            + " this job profile) and the number of jobless that has the"
            + " largest difference between vacancies.");
    queries.add("27. Find the courses that can help the most jobless people"
            + " find a job by training them toward the job profiles that have"
            + " the most openings due to lack of qualified workes.");
    queries.add("28. List all the courses, directly or indirectly required,"
            + " that a person has to take in order to be qualified for a job of"
            + " the given profile, according to his/her skills possessed and"
            + " courses taken.");
  }
  
  public void addsqlstmts() {
    //1.
    sqlstmts.add("select last_name, first_name"
            + "from person inner join employment on person.perosn_code = "
            + "employment.person_code inner join job on employment.job_code = "
            + "job.job_code");
    //2.
    sqlstmts.add("select distinct last_name, first_name, pay_rate"
            + "from person inner join employment on person.person_code ="
            + "employment.person_code inner join job on employment.job_code = "
            + "job.job_code"
            + "where comp_code = '1001001' and pay_type = 'salary'"
            + "order by (pay_rate) desc");
    //3.
    sqlstmts.add("with salary as ("
            + "select comp_code, sum(pay_rate) worker_salary"
            + "from job inner join employment on job.job_code = employment.job_code"
            + "where pay_type = 'salary'"
            + "group by (job.comp_code)),"
            + "wage as ("
            + "select comp_code, sum(pay_rate*1920) worker_salary"
            + "from job inner join employment on job.job_code = employment.job_code"
            + "where pay_type = 'wages'"
            + "group by (job.bomp_code)),"
            + "cost as ("
            + "((select * "
            + "from salary)"
            + "union"
            + "(select * "
            + "from wage))"
            + "select comp_code, sum(worker_salary) as total_cost"
            + "from cost"
            + "group by comp_code"
            + "order by total_cost desc;");
    //4.
    sqlstmts.add("select job_code"
            + "from employment natural join person"
            + "where person_code = '1018256'");
    //5.
    sqlstmts.add("select ks_name"
            + "from person_skill natural join skills"
            + "where person_code = '1536512'"
            + "order by skills.ks_name asc;");
    //6.
    sqlstmts.add("with person_skills as ("
            + "select ks_code, ks_level"
            + "from person_skill natural join skills"
            + "where person_code = '1024701'),"
            + "person_job as ("
            + "select job_code"
            + "from job natural join employment"
            + "where person_code = '1024701'),"
            + "job_skills as ("
            + "(select ks_code"
            + "from person_skills)"
            + "minus"
            + "(select ks_code"
            + "from job_skills))"
            + "select distinct ks_level"
            + "from skills natural join skill_gap;");
    //7.
    sqlstmts.add("select ks_name"
            + "from skills innere join jp_skill on skills.ks_code = "
            + "jp_skill.ks_code"
            + "where jp_skill.jp_code = '100';");
    //8. 
    sqlstmts.add("with has_skill as ("
            + "select ks_code"
            + "from person_skill"
            + "where person_code = '1024701'),"
            + "required_skill as ("
            + "select ks_code"
            + "from job_skill"
            + "where job_code = '3001001'),"
            + "skill_gap as ("
            + "(select *"
            + "from has_skill)"
            + "union"
            + "(select *"
            + "from required_skill))"
            + "select ks_name"
            + "from skill_gap natural join skills;");
    //9.
    sqlstmts.add("with required_skills as ("
            + "select ks_code, ks_name"
            + "from job_skill natural join skills"
            + "where job_code = '3101001'),"
            + "current_skills as ("
            + "select ks_code, ks_name"
            + "from person_skill natural join skills"
            + "where person_code = '1357909'),"
            + "needed_course as ("
            + "(select *"
            + "from required_skills)"
            + "minus"
            + "(select *"
            + "from current_skills)),"
            + "missing_skill as ("
            + "select ks_code"
            + "from needed_course)"
            + "select course_code, course_title"
            + "from missing_skill natural join course_skill natural join course;");
    //10.
    sqlstmts.add("with needed_skills as ("
            + "(select ks_code"
            + "from jp_skill natural join job_profile"
            + "where jp_code = '300')"
            + "minus"
            + "(select ks_code"
            + "from person_skill"
            + "where person_code = '6969696')),"
            + "course_skills as ("
            + "select distinct c.course_code"
            + "from course c"
            + "where not exists ("
            + "select ks_code"
            + "from needed_skills"
            + "minus"
            + "select ks_code"
            + "from course_skill natural join course"
            + "where course_code = c.course_code))"
            + "select course_code"
            + "from course_skills;");
    //11.
    sqlstmts.add("select course_code, course_title, cost"
            + "from course natural join section natural join course_skill"
            + "where ks_code in ("
            + "elect ks_code"
            + "from jp_skill in natural join skills natural join job"
            + "where ks_code not in ("
            + "select ks_code"
            + "from job_profile natural join person_skill"
            + "where person_code = '1017145'))"
            + "and cost = ("
            + "select min(cost)"
            + "from course natural join section natural join course_skill"
            + "where ks_code in ("
            + "select ks_code"
            + "from jp_skill natural join skills natural join job"
            + "where ks_code not in ("
            + "select ks_code"
            + "from job_profile natural join person_skill"
            + "where person_code = '1017145')));");
    //12.
    sqlstmts.add("with set_of_jobs as ("
            + "select ks_code, job_code"
            + "from job_skill natural join job"
            + "where jp_code = '666'),"
            + "course_sets as ("
            + "select course_code as, null as b, null as c"
            + "from course"
            + "union"
            + "select a.course_code as a, b.course_code as b, null as c"
            + "from course a, course b"
            + "where a.course_code = b.course_code"
            + "union"
            + "select a.course_code as a, b.course_code b, course_code as c"
            + "from course a, course b, course c"
            + "where a.course_code < b.course_code"
            + "and b.course_code < c.course_code),"
            + "course_sets_skills as ("
            + "select a, b, c, ks_code"
            + "from course_skill, course_sets"
            + "where course_sets.a = course_skill.course_code"
            + "or course_sets.b = course_skill.course_code"
            + "or course_sets.c = course_skill.course_code),"
            + "good_set as ("
            + "select *"
            + "from ("
            + "(select a, b, c"
            + "from course_sets_skill)"
            + "minus"
            + "(select a, b, c"
            + "from ("
            + "select a, b, c, ks_code"
            + "from course_sets_skills),"
            + "(select *"
            + "from set_of_jobs)"
            + "minus"
            + "(select a, b, c, ks_code"
            + "from course_sets_skills)))),"
            + "count as ("
            + "select a, b, c,"
            + "case"
            + "when b is null then 1"
            + "when c is null then 2"
            + "else 3"
            + "end as num_courses"
            + "from good_set)"
            + "select a, b, c, num_courses"
            + "from count"
            + "where num_courses = ("
            + "select min(num_courses)"
            + "from count);");
    //13.
    sqlstmts.add("with person_skill as ("
            + "select ks_code"
            + "from person_skill"
            + "where person_code = '2165778')"
            + "select jp_code, jp_title"
            + "from job_profile j"
            + "where not exists ("
            + "select ks_code"
            + "from jp_skill"
            + "where j.jp_code = jp_skill.jp_code"
            + "minus"
            + "select *"
            + "from person_skills);");
    //14.
    sqlstmts.add("with person_skills as ("
            + "select ks_code"
            + "from person_skill"
            + "where person_code = '6969696'"
            + "qualification as ("
            + "select jp_ode, jp_title"
            + "from job_profile j"
            + "where not exists ("
            + "select ks_code"
            + "from jp_skill"
            + "where j.jp_code = jp_code"
            + "minus"
            + "select ks_code"
            + "from person_skills)),"
            + "job_qualifications as ("
            + "select didtinct job_code"
            + "from qualifications natural join job),"
            + "pay as ("
            + "select distinct job_code, ("
            + "case"
            + "when pay_type = 'salary' then pay_rate"
            + "when pay_type = 'wage' then pay_rate"
            + "else null"
            + "end) as max_pay"
            + "from job_qualifications natural join job),"
            + "max as ("
            + "select min(max_pay)"
            + "from pay"
            + "where ax_pay = ("
            + "select * "
            + "from max);");
    //15.
    sqlstmts.add("with required_skills as ("
            + "select ks_code"
            + "from jp_skill"
            + "where jp_code = '100')"
            + "select last_name, first_name, email, person_code"
            + "from person p"
            + "where not exists ("
            + "select *"
            + "from required_skills"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code);");
    //16.
    sqlstmts.add("with skill_codes as ("
            + "selec ks_code "
            + "from jp_skill"
            + "where jp_code = '666'),"
            + "missing_one (person_code, num_missing) as ("
            + "select person_code, count(ks_code)"
            + "from person p,"
            + "(select *"
            + "from skill_codes) sc"
            + "where sc.ks_code in ("
            + "select *"
            + "from skill_codes"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by person_code)"
            + "select person_code, num_missing"
            + "from missing_one"
            + "where num_missing = 1"
            + "order by num_missing desc;");
    //17.
    sqlstmts.add("with skill_codes as ("
            + "select ks_code"
            + "from jp_skill"
            + "where jp_code = '100'),"
            + "missing_one (person_code, num_missing) as ("
            + "select person_code, count(ks_code)"
            + "from person p, ("
            + "select *"
            + "from skill_codes) sc"
            + "where sc.ks_code in ("
            + "select *"
            + "from skill_codes"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by person_code),"
            + "person_missing_one (person_code) as ("
            + "select person_code"
            + "from missing_one"
            + "where num_missing = 1)"
            + "select ks_code, count(person_code) as num_persons_missing"
            + "from person_missing_one p, ("
            + "select *"
            + "from skill_codes) sc"
            + "where sc.skill_code in ("
            + "select *"
            + "from skilll_codes"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by ks_code;");
    //18.
    sqlstmts.add("with needed_skill as ("
            + "select ks_code"
            + "from jp_skill"
            + "where jp_code ='32'),"
            + "missing_skill (person_code, num_missing) as (("
            + "select person_code, count(ks_code)"
            + "from person p, needed_skills"
            + "where ks_code in (("
            + "select ks_code"
            + "from needed_skills)"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by person_code)"
            + "select person_code, num_missing"
            + "from missing_skills"
            + "where num_missing == 3"
            + "order by num_missing desc;");
    //19.
    sqlstmts.add("with skill_list as ("
            + "select ks_code"
            + "from jp_skill"
            + "where jp_code = '100'),"
            + "missing_skill (person_code, num_missing) as ("
            + "select peron_code, count(ks_code)"
            + "from person p, ("
            + "select *"
            + "from skill_codes) j"
            + "where j.ks_code in ("
            + "select *"
            + "from skill_codes"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by person_code)"
            + "select person_code, num_missing"
            + "from missing_skill"
            + "where num_missing <= 3"
            + "order by num_missing;");
    //20.
    sqlstmts.add("with skill_codes as ("
            + "select ks_code"
            + "from jp_skill"
            + "where jp_code = '100'),"
            + "missing_skills (person_code, num_missing) as ("
            + "select person_code, count(ks_code)"
            + "from person p, "
            + "(select *"
            + "from skill_codes) j"
            + "where j.ks_code in ("
            + "select *"
            + "from skill_codes"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by person_code),"
            + "missing_one (person_code) as ("
            + "select person_code"
            + "from missing_skills"
            + "where num_missing = 1)"
            + "select ks_code, count(person_code) as num_missing_one"
            + "from missing_one p, ("
            + "select *"
            + "from skill_codes) j"
            + "where j.ks_code in ("
            + "select *"
            + "from skill_codes"
            + "minus"
            + "select ks_code"
            + "from person_skill"
            + "where p.person_code = person_code)"
            + "group by ks_code;");
    //21.
    sqlstmts.add("select last_name, first_name, email"
            + "from person inner join employment in person.person_code ="
            + "employment.person_code"
            + "where job_code = '9876543';");
    //22.
    sqlstmts.add("with unemployed as ("
            + "select person_code"
            + "from person"
            + "minus"
            + "select distinct person_code"
            + "from employment"
            + "where status = 'employed')"
            + "select disting last_name, first_name, job_code, jp_code"
            + "from person inner join unemployed on person.person_code = "
            + "employment.person_code natural join job"
            + "where jp_code = '300'"
            + "order by person.last_name asc;");
    //23.
    sqlstmts.add("with employer_count as ("
            + "select comp_code, count(pay_rate) as num_employees"
            + "from job inner koin employment on job.job_code = "
            + "employment.job_code"
            + "where status = 'employed'"
            + "group by comp_code)"
            + "select comp_code, comp_name, num_employees"
            + "from employer_count natural join company"
            + "order by (num_employees) desc;");
    //24.
    sqlstmts.add("with num_employees as ("
            + "select count(pay_rate) as employee_count, comp_code"
            + "from job natural join employment"
            + "where status = 'employed'"
            + "group by comp_code),"
            + "employees_per_sector as ("
            + "select primary_sector, sum(employee_count) as sector_count"
            + "from num_employees natural join company"
            + "group by primary_sector),"
            + "majority_count as ("
            + "select max(sector_count) as biggest_sector"
            + "from employees_per_sector)"
            + "select primary_sector"
            + "from majority_count, employees_per_sector"
            + "where biggest_sect = sector_count;");
    //25.
    sqlstmts.add("with old_salary as ("
            + "select distinct max(pay_rate) as old_pay, person_code"
            + "from emplyment natural join job natural join company"
            + "where status = 'unemployed'"
            + "and primary sector ='tourism'"
            + "group by person_code),"
            + "present_salary as ("
            + "select distinct max(pay_rate) as present_pay, person_code"
            + "from employment natural join job natural join company"
            + "where status = 'employed'"
            + "and primary_sector = 'tourism'"
            + "group by person_code),"
            + "people_decrease as ("
            + "select count(person_code) as decline"
            + "from old_salary natural join present_salary"
            + "where present_pay < old_pay),"
            + "people_increase as ("
            + "select count(person_code) as incline "
            + "from old_salary natural join present_salary"
            + "where present_pay > old_pay)"
            + "select sum(incline) inc_ration, sum(decline) dec_ration"
            + "from people_increase natural join people_decrease;");
    //26.
    sqlstmts.add("with unemployed as ("
            + "select distinct perso_code"
            + "from employment"
            + "where status = 'unemployed'),"
            + "employed as ("
            + "select distinct person_code"
            + "from employment"
            + "where status = 'employed'),"
            + "openings as ( "
            + "select distinct job_code"
            + "from ("
            + "select job_code"
            + "from unemployed natural join employment"
            + "minus"
            + "select job_code"
            + "fromo employed natural join employment)),"
            + "num_of_profiles as ("
            + "select jp_code, count(job_code) as num_of_openings"
            + "from openings natural join job"
            + "group by jp_code),"
            + "people_qualified as ("
            + "select jp_code, count?(person_code) as num_qualified"
            + "from num_of_profiles j, peron p"
            + "where not exists ("
            + "select ks_code"
            + "from num_of_profiles natural join jp_skill"
            + "where j.jp_code = jp_code"
            + "minus"
            + "select distinct ks_code"
            + "from num_of_profiles natural join jp_skill natural join "
            + "person_skill"
            + "where p.person_code = person_code)"
            + "group by jp_code),"
            + "missing_skill as ("
            + "select jp_code, (num_of_openings - num_qualifies) as unqualified"
            + "from people_qualified natural join num_of_profiles),"
            + "max_missing as ("
            + "select max(unqualified) as max_unqualified"
            + "from missing_skill)"
            + "select jp_code"
            + "from missing_skill, max_missing"
            + "where unqualified = max_unqualified;");
    //27.
    sqlstmts.add("with unemployed as ("
            + "select distinct person_code"
            + "from employment"
            + "where status = 'unemployed'),"
            + "employed as ("
            + "select distinct person_code"
            + "from employment"
            + "where status = 'employed'),"
            + "openings as ("
            + "select distinct job_code"
            + "from ("
            + "select job_code"
            + "from unemployed natural join employment"
            + "minus"
            + "select job_code"
            + "from employed natural join emplyoment)),"
            + "num_of_profiles as ("
            + "select jp_code, count(job_code) as num_openings"
            + "from openings natural join job"
            + "group by jp_code),"
            + "qualified as ("
            + "select jp_code, count(person_code) as num_qualified"
            + "from num_of_profiles j, person p"
            + "where not exists ("
            + "select ks_code"
            + "from num_of_profiles natural join jp_skill"
            + "where j.jp_code = jp_code"
            + "minus"
            + "select distinct ks_code"
            + "from num_of_profiles natural join jp_skill natural join "
            + "person_skill"
            + "where p.person_code = person_code)"
            + "group by jp_code),"
            + "lacking as ("
            + "select jp_code, (num_openings - num_qualified) as "
            + "lacking_qualifications"
            + "from qualified natural join num_of_profiles),"
            + "max_lacking as ("
            + "select max(lacking_qualifications) as max_lackings"
            + "from lacking),"
            + "max_lacking_ as ("
            + "select jp_code"
            + "from lacking, max_lacking"
            + "where lacking_qualkifications = max_lackings),"
            + "course_sets as ("
            + "select course_code as a, null as b, null as c"
            + "from course"
            + "union"
            + "select a.course_code as a, b.course_code as b, null as c"
            + "from course, course b"
            + "where a.course_code < b.course_code"
            + "union"
            + "select a.course_code as a, b.course_code as b, c.course_code as c"
            + "from course a, course b, course c"
            + "where a.course_code < b.course_code"
            + "and b.course_code < c.course_code),"
            + "course_sets_skills as ("
            + "select a, b, c, ks_code"
            + "from course_skill, course_sets"
            + "where course_sets.a = course_skill.course_code"
            + "or course_sets.b = course_skill.course_code"
            + "or course_sets.c = course_skill.course_code)"
            + "select *"
            + "from (("
            + "select a, b, c, jp_code"
            + "from course_sets_skills, max_lacking_)"
            + "minus"
            + "(select a, b, c, jp_code"
            + "from ("
            + "select a, b, c, jp_code, ks_code"
            + "from ("
            + "select a, b, c"
            + "from course_sets_skills),"
            + "(select jp_code, ks_code"
            + "from max_lacking_ natural join jp_skill)"
            + "minus"
            + "select a, b, c, jp_code, ks_code"
            + "from course_sets_skills, max_lacking)));");
    //28.
    sqlstmts.add("with person_skills as ("
            + "select ks_code"
            + "from person_skill"
            + "wehre person_code = '6969696'),"
            + "person_courses as ("
            + "select ks_code"
            + "from attends natural join person_skil"
            + "where person_code = '6969696'),"
            + "skills_needed as ("
            + "select ks_code"
            + "from jp_skill"
            + "where jp_code = '701'"
            + "minus"
            + "select ks_code"
            + "from person_skills),"
            + "courses_needed as ("
            + "select course_code"
            + "from skills_needed natural join course_skill)"
            + "select distinct course_code"
            + "from courses_needed c"
            + "where not exists ("
            + "select course_code"
            + "from person_courses "
            + "where c.course_code = course_code);");
  }
  
  public void createMap() {
    for (int i = 0; i < queries.size(); i++) {
      this.map.put(queries.get(i), sqlstmts.get(i));
    }
  }
  
  public String[] getQueryList() {
    String[] array = new String [1];
    array = (String[]) this.queries.toArray(array);
    return array;
  }
  
  public String getSQLStmts(String queries) {
    return (String)map.get(queries);
  }
}