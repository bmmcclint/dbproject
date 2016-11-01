/* 1. List a company's workers by names*/
/*EMPLOYMENT BROKEN*/
select last_name, first_name
from PERSON natural join EMPLOYMENT natural join JOB
where comp_code = '1017145';

/* 2. List a company's staff by salary in descending order.*/
/*EMPLOYMENT IS BROKEBNT*/
select person_code, last_name, first_name, pay_rate
from person natural join employment natural join job
where comp_code = '1001001' and pay_type = 'salary' and
      start_date < CURRENT_DATE and
      (end_date > CURRENT_DATE or end_date is null)
order by(pay_rate) desc;

/*3. List a company's labor cost (total salaries and wage rates by 1920 hours)
in descending order. */
with total_salary as (
  select sum(pay_rate) as t_salary, comp_code from job natural join company
  where pay_type = 'salary' and
  (start_date < CURRENT_DATE) and
  (end_date > CURRENT_DATE or end_date is null) group by comp_code),
total_wages as (
  select sum(pay_rate* 1920) as t_wages, comp_code from job natural join company
  where pay_type = 'wages' and
  (start_date < CURRENT_DATE) and
  (end_date > CURRENT_DATE or end_date is null)
  group by comp_code)
select (t_salary + t_wages) as total_cost, comp_code from total_salary natural join total_wages
order by total_cost desc;

/*4. Find all the jobs a person is currently holding and worked in the past.*/
select job_code
from employment
where person_code = '1047996' and start_date < CURRENT_DATE;

/*5. List a person's knowledge skills in a readable format.*/
select ks_name
from person_ks natural join KNOWLEDGE_SKILLS where person_code = '1047996';

/*6. list the skill gap of a worker between his/her job(s) and skill(s).*/
/*BROKENT*/
with person_current_jobs as (
select job_code, jp_code
from employment NATURAL JOIN job
where person_code = '1047996' and
start_date < CURRENT_DATE and
(end_date > CURRENT_DATE or end_date is null) ) (select ks_code
from person_current_jobs natural join job_skill union
select ks_code
from person_current_jobs natural join jp_skill) minus
select ks_code
from person_ks
where person_code = '1047996';

/*7. List the required knowledge skills of a job profile in a readable format.*/
select ks_name
from job_profile natural join KNOWLEDGE_SKILLS
where jp_code = '100';

/*8. List a person's missing knowledge skills for a specific job in a readable
format.*/
select ks_name
from knowledge_skills natural join
  ((select ks_code
    from knowledge_skills natural join job
    where job_code = '2001001')
    union
    select ks_code
    from jp_skill natural join job
    where job_code = '2001001')
    minus
   (select ks_code
    from person_ks
    where person_code = '1014890');

/*9. List the course (course id and title) that each alone teaches all 
the missing knowledge skill for a person to pursue a specicific job.*/
/*BROKEN*/
with needed_skills as (
  select ks_code
  from jp_skill natural join job_skill where jp_code = '100')
select course_code
from COURSE_SKILL C where not exists 
  (select *
  from needed_skills
minus
  select ks_code
  from course_skill
  where course_skill.course_code = C.course_code);

/*10. Suppose the skill gap for a  worker and the requirement of a desired job 
can be covered byone course. Find the "quickest" solution for this worker. Show 
the course, section information and the completeion dat.*/


/*11. Find the cheapest course to make up one's skill gap by showing the course 
to take and the cost (of the section proce).*/


/*12. If query 9 returns nothing, find the course sets that their combination 
covers all the missing knowledge skills for a person to pursue a specific job. 
The considered course will not include more than three courses. If multiple 
course sets are found, list the course sets (with their course ifs) in the 
order of the ascenfing order of the course sets' total cost.*/


/*13. List all the job profiles that a person is qualified for.*/


/*14. Find the job with the highest pay rate for a person according to their 
skill qualifications.*/


/*15. List all the names along with the emails of the persons who are qualified 
for a job profile.*/


/*16. When a company cannot find any qualified person for a job, a secondary 
solution is to find a person who is almost qualified to the job. Make a 
"missing-one" list that lists people who miss only one skill for a specified 
job profile.*/


/*17. List the skillID and the number of people in the missing-one list for a 
given job profile in the ascendingorder of the people counts.*/


/*18. Suppose there is a new job profile that has nobody qualified. list the 
persons who miss the least number of skills and reports the "least number".*/


/*19. For a specified job profile and a given small number k, 'make a missing-k" 
that lists the people's ids and the number of missing skills for the people who 
miss only up to k skills in the ascending order of missing skils.*/


/*21. In an local or national crisis, we need to find all the people who once 
held a job of the special job-profile identifier.*/


/*22. Find all the unemployed people who have once held a job of the given job- 
profile identifier.*/


/*23. Find out the biggest employer in terms of number of employees ir the total 
amount of salaries and wages paid to the emloyees.*/


/*24. Find out the job distribution among business sectors; find out the biggest 
sector in terms of number of employees or the total amount of salaries and wages 
paid to employees.*/


/*25. Find out the ratio between the people whose earnings increase and those 
whose earning decrease; find the average rate of earning improvement for the 
workers in a specific business sector.*/


/*26. Find the job profiles that have the most openings due to lack of qualified 
workers. If there are many opening jobs of a job profile but at the same time 
there are many qualified jobless people. Then trainiing connot help fill up this 
type of job. What we want to find is such a job profile that has the largest 
difference between vacancies and the number of jobless people who are wualified 
for this job profile.*/


/*27. Find the courses that can help most jobless people find a job by training 
them toward the job profile that have the most openings due to lack of qualified 
workers.*/