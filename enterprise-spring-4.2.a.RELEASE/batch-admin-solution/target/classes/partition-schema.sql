drop table T_DINING_REQUEST if exists;
drop table T_REPORT if exists;

create table T_DINING_REQUEST (ID integer identity primary key, CC_NUMBER varchar(16) not null, MERCHANT varchar(10) not null, AMOUNT double not null, DINING_DATE date not null); 
create table T_REPORT (ID integer identity primary key, TEXT varchar(255));