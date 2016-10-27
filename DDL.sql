create table person
  (per_id varchar(35),
   first_name varchar(35),
   last_name varchar(35),
   email varchar(35),
   gender varchar(7),
   address varchar(95),
   primary_phone_no varchar(35),
   secondary_phone_no varchar (35),
   primary key (per_id)
);

create table company
  (company_id varchar(35),
  comp_name varchar(35),
  address varchar(95),
  zip_code varchar(35),
  website varchar(35),
  primary_sector varchar(35),
  speciality varchar(35),
  primary key (company_id)
  );
  
create table job
  (job_code varchar(35),
  type varchar(15),
  pay_rate varchar(10),
  pay_type varchar(8),
  c_code char(7),
  primary key (job_code),
  foreign key (c_code) references company
  );
  
create table section
  (c_code char(7),
  sec_no char(8),
  complete_date varchar(12),
  year char(4),
  offered_by varchar(16),
  format varchar(16),
  price varchar(10),
  primary key (sec_no),
  foreign key (c_code) references course
  );
  
create table course
  (c_code char(7),
  title varchar(40),
  level varchar(10),
  description varchar(100),
  status varchar(15),
  price varchar(10),
  primary key (c_code)
  );
