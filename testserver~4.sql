drop table address cascade constraints;
drop table company cascade constraints;
drop table company_specialty cascade constraints;
drop TABLE course cascade constraints;
drop table course_skill  cascade constraints;
drop table employment cascade constraints;
drop table format cascade constraints;
drop table job cascade constraints;
drop table job_profile cascade constraints;
drop table jp_skill  cascade constraints;
drop table knowledge_skills  cascade constraints;
drop table person  cascade constraints;
drop table person_ks  cascade constraints;
drop table person_phone  cascade constraints;
drop table phone_num  cascade constraints;
drop table specialty  cascade constraints;
drop table attends cascade constraints;
drop table job_skill cascade constraints;
drop table offers cascade constraints;
drop table section cascade constraints;
drop table comp_addr cascade constraints;
drop table comp_phone cascade constraints;
drop table person_addr cascade constraints;
drop table pays cascade constraints;

select * from job;

select last_name, first_name
from person natural join employment natural join job
where comp_code = '1001001';

select * from job;

select first_name, last_name,  person_code
from person;

select last_name, first_name
from employment natural join job natural join person
where comp_code = '1001001';

select * from employment;

select last_name, first_name
from person natural join employment natural join job
where comp_code = '1001001' and start_date < CURRENT_DATE and (end_date > CURRENT_DATE or end_date is null)
order by person.last_name;

select distinct last_name, jp_code
from person natural join job_profile
order by person.LAST_NAME;

select person_code
from employment natural join job
where job_code= '1001001';

select person_code 
from employment;

select * from company;

select last_name 
from person natural join employment natural join job
where comp_code = '1001001';