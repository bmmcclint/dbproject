/* 1. List a company's workers by names*/
select last_name, first_name
from person inner join employment on person.PERSON_CODE = employment.PERSON_CODE inner join job on employment.JOB_CODE = job.JOB_CODE  
where comp_code = '1001001';

/* 2. List a company's staff by salary in descending order.*/
select distinct last_name, first_name, pay_rate
from person inner join employment on person.PERSON_CODE = employment.PERSON_CODE inner join job on employment.JOB_CODE = job.JOB_CODE
where comp_code = '1001001' and pay_type = 'salary' 
order by(pay_rate) desc;

/*3. List a company's labor cost (total salaries and wage rates by 1920 hours)
in descending order. */
with salary as (
  select comp_code, sum(pay_rate) worker_salary
  from job inner join employment on job.JOB_CODE = employment.JOB_CODE
  where pay_type = 'salary'
  group by (job.COMP_CODE)),

wage as (
  select comp_code, sum(pay_rate*1920) worker_salary
  from job inner join employment on job.JOB_CODE = employment.JOB_CODE
  where pay_type = 'wages'
  group by (job.COMP_CODE)),

cost as 
  ((select * from salary) union (select * from wage))
  
select comp_code, sum(worker_salary) as total_cost
from cost
group by comp_code
order by total_cost desc;

/*4. Find all the jobs a person is currently holding and worked in the past.*/
select job_code
from employment natural join person
where person_code = '1018256';

/*5. List a person's knowledge skills in a readable format.*/
select ks_name
from person_skill natural join skills
where person_code = '1536512'
order by skills.KS_NAME asc;

/*6. list the skill gap of a worker between his/her job(s) and his/her skill(s).*/
with person_skills as (
  select ks_code, ks_level
  from person_skill natural join skills
  where person_code = '1024701'),

person_jobs as (
  select job_code
  from job natural join employment
  where person_code = '1024701'),
  
job_skills as (
  select ks_code
  from job_skill natural join person_jobs),
  
skill_gap as (
  (select ks_code
  from person_skills)
    minus
  (select ks_code
  from job_skills))
  
select distinct ks_level
from skills natural join skill_gap;

/*7. List the required knowledge skills of a job profile in a readable format.*/
select ks_name
from skills inner join JP_SKILL on skills.KS_CODE = JP_SKILL.KS_CODE
where JP_SKILL.JP_CODE = '100';

/*8. List a person's missing knowledge skills for a specific job in a readable
format.*/
with has_skill as (
  select ks_code
  from person_skill
  where person_code = '1024701'),

required_skill as (
  select ks_code
  from job_skill
  where job_code = '3001001'),
  
skill_gap as (
  (select * from has_skill) union (select * from required_skill))
  
select ks_name
from skill_gap natural join skills;

/*9. List the course (course id and title) that each alone teaches all 
the missing knowledge skill for a person to pursue a specicific job.*/
with required_skills as (
  select ks_code, ks_name
  from job_skill natural join skills
  where job_code = '3101001'),

current_skills as (
  select ks_code, ks_name
  from person_skill natural join skills
  where person_code = '1357909'),

needed_course as (
  (select * from required_skills) minus (select * from current_skills)),
  
missing_skill as (  
  select ks_code
  from needed_course)
  
select course_code, course_title
from missing_skill natural join course_skill natural join course;

/*10. Suppose the skill gap for a  worker and the requirement of a desired job 
can be covered byone course. Find the "quickest" solution for this worker. Show 
the course, section information and the completeion date.*/


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


/*21. In a local or national crisis, we need to find all the people who once 
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