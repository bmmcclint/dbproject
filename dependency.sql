set linesize 110
tti 'REFERENTIAL INTEGRITY REPORT BY TABLE'
col table_name head 'CHILD TABLE' for a20
col column_name head 'FOREIGN KEY' for a20
col constraint_name head 'CONSTRAINT' for a20
col rtbl head 'PARENT TABLE' for a20
col rcol head 'PRIMARY KEY' for a20
break on report on table_name skip 1
spool test.lst
select c.table_name, cc.column_name, c.constraint_name,
r.table_name rtbl, r.column_name rcol
from user_constraints c,
user_cons_columns cc,
user_cons_columns r
where c.constraint_name = cc.constraint_name
and c.r_constraint_name = r.constraint_name
and c.constraint_type = 'R'
order by 1,2
/
spool off


set linesize 110
tti 'REFERENTIAL INTEGRITY REPORT BY TABLE'
col table_name head 'CHILD TABLE' for a20
col column_name head 'FOREIGN KEY' for a20
col constraint_name head 'CONSTRAINT' for a20
col rtbl head 'PARENT TABLE' for a20
col rcol head 'PRIMARY KEY' for a20
break on report on rtbl skip 1
spool test.lst
select r.table_name rtbl, r.column_name rcol,
c.table_name, cc.column_name, c.constraint_name
from user_constraints c,
user_cons_columns cc,
user_cons_columns r
where c.constraint_name = cc.constraint_name
and c.r_constraint_name = r.constraint_name
and c.constraint_type = 'R'
order by 1,2
/
spool off