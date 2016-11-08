/* 1. List a company's workers by names*/
select last_name, first_name
from person inner join employment on person.person_code = employment.person_code
  inner join job on employment.job_code = job.job_code  
where comp_code = '1001001';

/* 2. List a company's staff by salary in descending order.*/
select distinct last_name, first_name, pay_rate
from person inner join employment on person.person_code = employment.person_code
  inner join job on employment.job_ode = job.job_code
where comp_code = '1001001' and pay_type = 'salary' 
order by(pay_rate) desc;

/*3. List a company's labor cost (total salaries and wage rates by 1920 hours)
in descending order. */
with salary as (
  select comp_code, sum(pay_rate) worker_salary
  from job inner join employment on job.job_code = employment.job_code
  where pay_type = 'salary'
  group by (job.comp_code)),

wage as (
  select comp_code, sum(pay_rate*1920) worker_salary
  from job inner join employment on job.job_code = employment.job_code
  where pay_type = 'wages'
  group by (job.comp_code)),

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
from skills inner join JP_SKILL on skills.ks_code = jp_skill.ks_code
where jp_skill.jp_code = '100';

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
can be covered by one course. Find the "quickest" solution for this worker. Show 
the course, section information and the completeion date.*/


/*11. Find the cheapest course to make up one's skill gap by showing the course 
to take and the cost (of the section proce).*/
select course_code, course_title, cost
from course natural join section natural join course_skill
where ks_code in (
  select ks_code
  from jp_skill natural join skills natural join job
  where ks_code not in (
    select ks_code
    from job_profile natural join person_skill
    where person_code = '1017145'))
  and cost = (
    select min(cost)
    from course natural join section natural join course_skill
    where ks_code in (
      select ks_code
      from jp_skill natural join skills natural join job
      where ks_code not in (
        select ks_code
        from job_profile natural join person_skill
        where person_code = '1017145')));

/*12. If query 9 returns nothing, find the course sets that their combination 
covers all the missing knowledge skills for a person to pursue a specific job. 
The considered course will not include more than three courses. If multiple 
course sets are found, list the course sets (with their course ifs) in the 
order of the ascenfing order of the course sets' total cost.*/


/*13. List all the job profiles that a person is qualified for.*/
with person_skills as (
  select ks_code
  from person_skill
  where person_code = '2165778')
  
select jp_code, jp_title
from job_profile J
where not exists (  
  select ks_code
  from jp_skill
  where J.jp_code = jp_skill.jp_code
    minus
  select *
  from person_skills);

/*14. Find the job with the highest pay rate for a person according to their 
skill qualifications.*/


/*15. List all the names along with the emails of the persons who are qualified 
for a job profile.*/
with required_skills as (
  select ks_code
  from jp_skill
  where jp_code = '100')

select last_name, first_name, email, PERSON_CODE 
from person P
where not exists (
  select * 
  from required_skills 
    minus
  select ks_code
  from person_skill
  where P.person_code = person_code);

/*16. When a company cannot find any qualified person for a job, a secondary 
solution is to find a person who is almost qualified to the job. Make a 
"missing-one" list that lists people who miss only one skill for a specified 
job profile.*/
with skill_codes as (
  select ks_code
  from jp_skill
  where jp_code = '666'),

missing_one (person_code, num_missing) as (
select person_code, count(ks_code)
  from person P,
    (select *
    from skill_codes) SC
    where SC.ks_code in (
      select *
      from skill_codes
        minus
      select ks_code
      from person_skill 
      where person_code = P.person_code)
    group by person_code)
    
select person_code, num_missing
from missing_one
where num_missing = 1
order by num_missing desc;

/*17. List the skillID and the number of people in the missing-one list for a 
given job profile in the ascending order of the people counts.*/
with skill_codes as (
  select ks_code
  from jp_skill
  where jp_code = '100'),
  
missing_one (person_code, num_missing) as (
  select person_code, count(ks_code)
  from person P, (
    select *
    from skill_codes) SC
  where SC.ks_code in (
    select *
    from skill_codes
      minus
    select ks_code
    from person_skill
    where person_code = P.person_code)
  group by person_code),
  
person_missing_one (person_code) as (
  select person_code
  from missing_one
  where num_missing = 1)
  
select ks_code, count(person_code) as num_persons_missing
from person_missing_one P, (
  select *
  from skill_codes) SC
  where SC.ks_code in (
    select *
    from skill_codes
      minus
    select ks_code
    from person_skill
    where person_code = P.person_code)
  group by ks_code;

/*18. Suppose there is a new job profile that has nobody qualified. list the 
persons who miss the least number of skills and reports the "least number".*/
with needed_skills as (
  select ks_code
  from jp_skill
  where jp_code = '302'),

missing_skill (person_code, num_missing) as ((
  select person_code, count(ks_code)
  from person p, needed_skills
  where ks_code in ((
    select ks_code
    from needed_skills)
      minus
    (select ks_code
    from person_skill
    where person_code = P.person_code))
  group by person_code))
  
select person_code, num_missing as smallest
from missing_skill
where num_missing = (
  select min(num_missing)
  from missing_skill)
order by person_code asc;

/*19. For a specified job profile and a given small number k, 'make a missing-k" 
that lists the people's ids and the number of missing skills for the people who 
miss only up to k skills in the ascending order of missing skils.*/
with skill_list as (
  select ks_code 
  from jp_skill
  where jp_code = '100'),
  
missing_skills (person_code, num_missing) as (
  select person_code, count(ks_code)
  from person P, (
    select *
    from skill_list) J
  where J.ks_code in (
    select *
    from skill_list
      minus
    select ks_code
    from person_skill
    where person_code = P.person_code)
    group by person_code)
    
select person_code, num_missing
from missing_skills
where num_missing <= 3
order by num_missing desc;

/*20. Given a job profile and its corresponding missing-k list specified in 
Question 19. Find every skill that is need by at least one person in the given 
missing-k list. List each skillID and the number of people who need it in the 
descending order of the people.*/
with skill_codes as (
  select ks_code
  from jp_skill
  where jp_code = '100'),
  
missing_skills (person_code, num_missing) as (
  select person_code, count(ks_code)
  from person p, (
    select *
    from skill_codes) j
  where j.ks_code in (
    select *
    from skill_codes
      minus
    select ks_code
    from person_skill
    where person_code = p.person_code)
  group by person_code),
  
missing_one (person_code) as (
  select person_code
  from missing_skills
  where num_missing = 1)
  
select ks_code, count(person_code) as num_missing_one 
from missing_one p, (
  select *
  from skill_codes) j
where j.ks_code in (
  select *
  from skill_codes
    minus
  select ks_code 
  from person_skill
  where person_code = p.person_code)
group by ks_code;

/*21. In a local or national crisis, we need to find all the people who once 
held a job of the special job-profile identifier.*/
select last_name, first_name, email
from person inner join employment on person.PERSON_CODE = employment.person_code 
where job_code = '9876543';

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

/*28. List all the courses directly or indirectly required, that person has to 
take in order to be qualified for a job of the given profile, according to his/
her skills possessed and courses taken.*/