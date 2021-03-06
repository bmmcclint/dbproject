-- CSCI425 Fall 2016
-- DB Project
-- Updated/Fixed Queries
-- Authors: Brandon McClinton, Phil Reason

/* 1. List a company's workers by names*/
select last_name, first_name
from person inner join employment on person.person_code =
   employment.person_code inner join job on employment.job_code = job.job_code
where comp_code  + comp_code  and status = 'employed';

/* 2. List a company's staff by salary in descending order.*/
select distinct last_name, first_name, pay_rate
from person inner join employment on person.person_code = employment.person_code
   inner join job on employment.job_code = job.job_code
where comp_code =  + comp_code  and pay_type = 'salary';

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

cost as (
   ((select *
   from salary)
      union
   (select *
   from wage)))

select comp_code, sum(worker_salary) as total_cost
    from cost
    group by comp_code
    order by total_cost desc;

/*4. Find all the jobs a person is currently holding and worked in the past.*/
select job_code
    from employment
    where person_code  + person_code;

/*5. List a person's knowledge skills in a readable format.*/
select ks_name, ks_code
    from person_skill natural join skills
    where person_code  + person_code
    order by ks_name asc;

/*6. list the skill gap of a worker between his/her job(s) and his/her skill(s).*/
with person_skills as (
   select ks_name, ks_level, ks_code
   from person_skill natural join skills
   where person_code =  + person_code ),

person_jobs as (
   select job_code
   from job natural join employment
   where person_code  + person_code ),

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
select ks_name, ks_code
    from skills natural join jp_skill
    where jp_code  + jp_code;

/*8. List a person's missing knowledge skills for a specific job in a readable
format.*/
with has_skill as (
   select ks_code
   from person_skill
   where person_code  + person_code ),

required_skill as (
   select ks_code
   from job_skill
   where job_code  + job_code ),

skill_gap as (
   (select ks_code
   from has_skill)
     union
   (select ks_code
   from required_skill))

select ks_name
from skill_gap natural join skills;

/*9. List the course (course id and title) that each alone teaches all
the missing knowledge skill for a person to pursue a specicific job.*/
with required_skills as (
   select ks_code, ks_name
   from job_skill natural join skills
   where job_code  + job_code ),

current_skills as (
   select ks_code, ks_name
   from person_skill natural join skills
   where person_code  + person_code ),

needed_course as (
   (select ks_code, ks_name
   from required_skills)
     minus
   (select ks_code, ks_name
   from current_skills)),

missing_skills as (
   select ks_code
   from needed_course)

select course_code, course_title
from missing_skills natural join course_skill natural join course;

/*10. Suppose the skill gap for a  worker and the requirement of a desired job
can be covered by one course. Find the "quickest" solution for this worker. Show
the course, section information and the completeion date.*/
with needed_skills as (
   (select ks_code
   from jp_skill
   where jp_code  + jp_code )
     minus
   (select ks_code
   from person_skill
   where person_code  + person_code )),

course_skills as (
   select distinct c.course_code
   from course c
   where not exists (
     select ks_code
     from course_skill
     where course_code = c.course_code
       minus
     select ks_code
     from needed_skills))

select course_code, course_title
from course_skills join course using(course_code);

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
     where person_code  + person_code ))
       and cost = (
       select min(cost)
       from course natural join section natural join course_skill
       where ks_code in (
         select ks_code
         from jp_skill natural join skills natural join job
         where ks_code not in (
           select ks_code
           from job_profile natural join person_skill
           where person_code  + person_code )));

/*13. List all the job profiles that a person is qualified for.*/
with person_skills as (
select ks_code
from person_skill
where person_code  + person_code )

select jp_code, jp_title
from job_profile j
where not exists (
   (select ks_code
   from jp_skill
   where j.jp_code = jp_code)
     minus
   (select ks_code
   from person_skills));

/*14. Find the job with the highest pay rate for a person according to their
skill qualifications.*/
with person_skills as (
     select ks_code
     from person_skill
     where person_code  + person_code ),

qualifications as (
     select job_code
     from job
     where not exists (
         (select ks_code
         from jp_skill
         where job.jp_code = jp_code)
             intersect
         (select ks_code
         from person_skills))),

job_qualifications as (
     select distinct job_code
     from qualifications),

pay as (
     select distinct job_code, (
         case
             when pay_type = 'salary' then pay_rate
             when pay_type = 'wage' then pay_rate * 1920
         end) as total_earnings
     from job_qualifications natural join job)

select distinct job_code, jp_title, total_earnings
from pay natural join job_profile
where total_earnings = (
     select max(total_earnings)
     from pay);

 /*15. List all the names along with the emails of the persons who are qualified
 for a job profile.*/
with required_skills as (
   select ks_code
   from jp_skill
   where jp_code  + jp_code )

select last_name, first_name, email, person_code
from person p
where not exists (
   (select ks_code
   from required_skills)
     minus
   (select ks_code
   from person_skill
   where p.person_code = person_code));

 /*16. When a company cannot find any qualified person for a job, a secondary
 solution is to find a person who is almost qualified to the job. Make a
 "missing-one" list that lists people who miss only one skill for a specified
 job profile.*/
with skill_codes as (
   select ks_code
   from jp_skill
   where jp_code  + jp_code ),

missing_one (person_code, num_missing) as (
   select person_code, count(ks_code)
   from person p,
     (select ks_code
     from skill_codes) sc
     where sc.ks_code in (
       select ks_code
       from skill_codes
         minus
       select ks_code
       from person_skill
       where p.person_code = person_code)
     group by person_code)

select person_code, num_missing
from missing_one
where num_missing  + missingNum
order by num_missing desc;

/*17. List the skillID and the number of people in the missing-one list for a
given job profile in the ascending order of the people counts.*/
with skill_codes as (
   select ks_code
   from jp_skill
   where jp_code  + jp_code ),

missing_one (person_code, num_missing) as (
   select person_code, count(ks_code)
   from person p, (
     select ks_code
     from skill_codes) sc
   where sc.ks_code in (
     select ks_code
     from skill_codes
       minus
     select ks_code
     from person_skill
     where p.person_code = person_code)
   group by person_code),

person_missing_one (person_code) as (
   select person_code
   from missing_one
   where num_missing  + missingNum )

select ks_code, count(person_code) as num_persons_missing
from person_missing_one p, (
   select ks_code
   from skill_codes) sc
   where sc.ks_code in (
     select ks_code
     from skill_codes
       minus
     select ks_code
     from person_skill
     where p.person_code = person_code)
   group by ks_code;

 /*18. Suppose there is a new job profile that has nobody qualified. list the
 persons who miss the least number of skills and reports the "least number".*/
with needed_skills as (
   select ks_code
   from jp_skill
   where jp_code  + jp_code ),

missing_skill (person_code, num_missing) as (
   (select person_code, count(ks_code)
   from person p, needed_skills
   where ks_code in (
     (select ks_code
     from needed_skills)
       minus
     (select ks_code
     from person_skill
     where p.person_code = person_code))
   group by person_code))

select person_code, last_name, first_name, num_missing as smallest
from missing_skill natural join person
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
   where jp_code  + jp_code ),

missing_skills (person_code, num_missing) as (
   select person_code, count(ks_code)
   from person p, (
     select ks_code
     from skill_list) j
   where j.ks_code in (
     select ks_code
     from skill_list
       minus
     select ks_code
     from person_skill
     where p.person_code = person_code)
     group by person_code)

select person_code, last_name, first_name, num_missing
from missing_skills natural join person
where num_missing < + missingNum
order by num_missing desc;


/*20. Given a job profile and its corresponding missing-k list specified in
Question 19. Find every skill that is need by at least one person in the given
missing-k list. List each skillID and the number of people who need it in the
descending order of the people.*/
with skill_codes as (
   select ks_code
   from jp_skill
   where jp_code  + jp_code ),

missing_skills (person_code, num_missing) as (
   select person_code, count(ks_code)
   from person p, (
     select ks_code
     from skill_codes) j
   where j.ks_code in (
     select ks_code
     from skill_codes
       minus
     select ks_code
     from person_skill
     where p.person_code = person_code)
   group by person_code),

missing_one (person_code) as (
   select person_code
   from missing_skills
   where num_missing  + missingNum )

select ks_code, count(person_code) as num_missing_one
from missing_one m, (
   select ks_code
   from skill_codes) s
where s.ks_code in (
   select ks_code
   from skill_codes
     minus
   select ks_code
   from person_skill
   where m.person_code = person_code)
group by ks_code;

/*21. In a local or national crisis, we need to find all the people who once
held a job of the special job-profile identifier.*/
select last_name, first_name, email
from person inner join employment on person.person_code = employment.person_code
where job_code  + job_code;

/*22. Find all the unemployed people who have once held a job of the given job-
profile identifier.*/
with unemployed as (
   select person_code
   from person
     minus
   select distinct person_code
   from employment
   where status ='unemployed')

select distinct last_name, first_name, job_code, jp_code
from person inner join unemployed on person.person_code = unemployed.person_code natural join job
where jp_code  + jp_code
order by person.last_name asc;

/*23. Find out the biggest employer in terms of number of employees ir the total
amount of salaries and wages paid to the emloyees.*/
with employer_count as (
   select comp_code, count(pay_rate) as num_employees
   from job inner join employment on job.job_code = employment.job_code
   where status ='employed'
   group by comp_code)

select comp_code, comp_name, num_employees
from employer_count natural join company
order by (num_employees) desc;

/*24. Find out the job distribution among business sectors; find out the biggest
sector in terms of number of employees or the total amount of salaries and wages
paid to employees.*/
with employer_count as (
     select count(pay_rate) as num_employees, comp_code
     from job join employment using(job_code)
     where status ='employed'
     group by comp_code),

count_per_sector as (
     select primary_sector, sum(num_employees) as sector_employee_count
     from employer_count natural join company
     group by primary_sector),

max_count as (
     select max(sector_employee_count) as max
     from count_per_sector)

select primary_sector
from max_count, count_per_sector
where max = sector_employee_count;

/*25. Find out the ratio between the people whose earnings increase and those
whose earning decrease; find the average rate of earning improvement for the
workers in a specific business sector.*/
with old_salary as (
   select distinct max(pay_rate) as old_pay, person_code
   from employment natural join job natural join company
   where status = 'unemployed' and primary_sector = 'tourism'
   group by person_code),

present_salary as (
   select distinct max(pay_rate) as present_pay, person_code
   from employment natural join job natural join company
   where status = 'employed' and primary_sector = 'tourism'
   group by person_code),

people_decrease as (
   select count(person_code) as decline
   from old_salary natural join present_salary
   where present_pay < old_pay),

people_increase as (
   select count(person_code) as incline
   from old_salary natural join present_salary
   where present_pay > old_pay)

select sum(incline) inc_ratio, sum(decline) dec_ratio
from people_increase natural join people_decrease;

/*26. Find the job profiles that have the most openings due to lack of qualified
workers. If there are many opening jobs of a job profile but at the same time
there are many qualified jobless people. Then trainiing connot help fill up this
type of job. What we want to find is such a job profile that has the largest
difference between vacancies and the number of jobless people who are wualified
for this job profile.*/
with unemployed as (
   select distinct person_code
   from employment
   where status = 'unemployed'),

employed as (
   select distinct person_code
   from employment
   where status = 'employed'),

openings as (
   select distinct job_code
   from (
     select job_code
     from unemployed natural join employment
       minus
     select job_code
     from employed natural join employment)),

num_of_profiles as (
   select jp_code, count(job_code) as num_of_openings
   from openings natural join job
   group by jp_code),

people_qualified as (
   select jp_code, count(person_code) as num_qualified
   from num_of_profiles n, person p
   where not exists (
     select ks_code
     from num_of_profiles natural join jp_skill
     where n.jp_code = jp_code
       minus
     select ks_code
     from num_of_profiles natural join jp_skill natural join person_skill
     where p.person_code = person_code)
   group by jp_code),

missing_skill as (
   select jp_code, (num_of_openings - num_qualified) as unqualified
   from people_qualified natural join num_of_profiles),

max_missing as (
   select max(unqualified) as max_unqualified
   from missing_skill)

select jp_code
from missing_skill, max_missing
where unqualified = max_unqualified;

/*27. Find the courses that can help most jobless people find a job by training
them toward the job profile that have the most openings due to lack of qualified
workers.*/
with unemployed as (
  select distinct person_code
  from employment
  where status = 'unemployed'),

employed as (
  select distinct person_code
  from employment
  where status = 'employed'),

openings as (
  select distinct job_code
  from (
    select job_code
    from unemployed natural join employment
      minus
    select job_code
    from employed natural join employment)),

num_of_profiles as (
  select jp_code, count(job_code) as num_openings
  from openings natural join job
  group by jp_code),

qualified as (
  select jp_code, count(person_code) as num_qualified
  from num_of_profiles j, person p
  where not exists (
    select ks_code
    from num_of_profiles natural join jp_skill
    where j.jp_code = jp_code
      minus
    select distinct ks_code
    from num_of_profiles natural join jp_skill natural join person_skill
    where p.person_code = person_code)
  group by jp_code),

lacking as (
  select jp_code, (num_openings - num_qualified) as lacking_qualification
  from qualified natural join num_of_profiles),

max_lacking as (
  select max(lacking_qualification) as max_lackings
  from lacking),

max_lacking_ as (
  select jp_code
  from lacking, max_lacking
  where lacking_qualification = max_lackings),

course_sets as (
  select course_code as a, null as b, null as c
  from course
    union
  select a.course_code as a, b.course_code as b, null as c
  from course a, course b
  where a.course_code < b.course_code
    union
  select a.course_code as a, b.course_code b, c.course_code as c
  from course a, course b, course c
  where a.course_code < b.course_code
    and b.course_code < c.course_code),

course_sets_skills as (
  select a, b, c, ks_code
  from course_skill, course_sets
  where course_sets.a = course_skill.course_code
    or course_sets.b = course_skill.course_code
    or course_sets.c = course_skill.course_code)

select a, b, c, jp_code
from (
  (select a, b, c, jp_code
  from course_sets_skills, max_lacking_)
    minus
  (select a, b, c, jp_code
  from (
    select a, b, c, jp_code, ks_code
    from (
      select a, b, c
      from course_sets_skills),
    (select jp_code, ks_code
    from max_lacking_ natural join jp_skill)
      minus
    select a, b, c, jp_code, ks_code
    from course_sets_skills, max_lacking_)));

  /*28. List all the courses directly or indirectly required, that person has to
  take in order to be qualified for a job of the given profile, according to his/
  her skills possessed and courses taken.*/
with person_skills as (
   select ks_code
   from person_skill
   where person_code  + person_code ),

person_courses as (
   select ks_code
   from attends natural join person_skill
   where person_code  + person_code ),

skills_needed as (
   select ks_code
   from jp_skill
   where jp_code  + jp_code
     minus
   select ks_code
   from person_skills),

courses_needed as (
   select course_code, course_title
   from skills_needed natural join course_skill natural join course)

select distinct course_code, course_title
from courses_needed c
where not exists (
   select course_code
   from person_courses
   where c.course_code = course_code);
