set autocommit on;
show autocommit;
create table skills(
  ks_code char(7),
  ks_name varchar(50),
  ks_description varchar(100),
  ks_level varchar(8) not null,
  primary key (ks_code)
);
  
create table address (
  addr_code char(7),
  addr_type varchar(10),
  street varchar(50),
  zip_code char(9),
  state varchar(20),
  city varchar(50),
  primary key (addr_code)
);

create table phone_num(
  phone_num_code char(7),
  phone_type varchar(10),
  phone_num char(10),
  primary key (phone_num_code)
);

create table company(
  comp_code char(7),
  comp_name varchar(30) not null,
  website varchar(20),
  addr_code char(7),
  phone_num_code char(7),
  primary_sector varchar(30),
  primary key (comp_code),
  foreign key (addr_code) references address,
  foreign key (phone_num_code) references phone_num
);

create table required_skill(
  req_skill_code char(7),
  ks_code char(7),
  jp_code char(7),
  primary key (req_skill_code),
  foreign key (ks_code) references skills,
  foreign key (jp)code) references job_profile
);
 
create table job_profile(
  jp_code char(7),
  jp_title varchar(30) not null,
  jp_description varchar(50),
  jp_avg_pay varchar(10),
  req_skill_code char(7),
  primary key (jp_code),
  foreign key (req_skill_code) references required_skill
);
  
create table person(
  person_code char(7),
  last_name varchar(15),
  first_name varchar(15),
  gender varchar(6) not null,
  email varchar(20),
  addr_code char(7),
  phone_num_code char(7),
  primary key (person_code),
  foreign key (addr_code) references address,
  foreign key (phone_num_code) references phone_num
);
  
create table job(
  job_code char(7),
  job_description varchar(50),
  pay_rate varchar(20),
  pay_type varchar(20),
  comp_code char(7),
  jp_code char(7),
  start_date varchar(40),
  end_date varchar(40),
  primary key (job_code),
  foreign key (comp_code) references company,
  foreign key (jp_code) references job_profile
);

create table specialty(
  specialty_code char(7),
  specialty_name varchar(20),
  primary key (specialty_code)
);
  
create table company_specialty(
  specialty_code char(7),
  comp_code char(7),
  primary key (specialty_code, comp_code),
  foreign key (comp_code) references company
);
  
create table person_skill(
  person_code char(7),
  ks_code char(7),
  primary key (person_code, ks_code),
  foreign key (person_code) references person,
  foreign key (ks_code) references skills
);
  
create table format(
  format_code char(7),
  format_type varchar(20),
  format_description varchar(100),
  format_fees varchar(6),
  primary key (format_code)
);
  
create table course(
  course_code char(7),
  course_title varchar(50),
  course_description varchar(100),
  course_level varchar(8),
  course_status varchar(15),
  cost number,
  primary key (course_code)
);

create table prereq(
  prereq_code char(7),
  course_code char(7),
  primary key (prereq_code),
  foreign key (course_code) references course
);
  
create table section(
  sec_code char(7),
  course_code char(7),
  semester varchar(6),
  year number,
  complete_date varchar(40), 
  format_code char(7),
  cost varchar(10),
  primary key (sec_code),
  foreign key (course_code) references course,
  foreign key (format_code) references format
);
  
create table offers(
  sec_code char(7),
  comp_code char(7),
  year number,
  primary key (sec_code, comp_code),
  foreign key (sec_code) references section,
  foreign key (comp_code) references company
);
  
create table job_skill(
  job_code char(7),
  ks_code char(7),
  primary key (job_code, ks_code),
  foreign key (job_code) references job,
  foreign key (ks_code) references skills
);
  
create table course_skill(
  course_code char(7),
  ks_code char(7),
  primary key (course_code, ks_code),
  foreign key (course_code) references course,
  foreign key (ks_code) references skills
);
  
create table jp_skill(
  jp_code char(7),
  ks_code char(7),
  primary key (jp_code, ks_code),
  foreign key (jp_code) references job_profile,
  foreign key (ks_code) references skills
);
  
create table attends(
  sec_code char(7),
  person_code char(7),
  year number,
  primary key (sec_code, person_code, year),
  foreign key (sec_code) references section,
  foreign key (person_code) references person
);
  
create table employment(
  person_code char(7),
  job_code char(7),
  start_date varchar(12) not null,
  end_date varchar(12),
  status varchar(20),
  primary key (person_code, job_code),
  foreign key (person_code) references person,
  foreign key (job_code) references job
);