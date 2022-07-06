create database bank_accounts;
use bank_accounts;

create table accounts(
    account_id char(5) NOT NULL,
    branch_id char(5) NOT NULL,
    account_type varchar(10),
    phone_number varchar(13) NOT NULL,
    balance float,
    CONSTRAINT pk_accounts PRIMARY KEY (account_id) 
);

create table bank_branch(
    branch_id char(5) NOT NULL,
    branch_city varchar(20),
    CONSTRAINT pk_bank_branch PRIMARY KEY (branch_id) 
);

create table account_owner(
    phone_number varchar(13) NOT NULL,
    owner_name varchar(20),
    owner_address varchar(20),
    CONSTRAINT pk_account_owner PRIMARY KEY (phone_number) 
);

INSERT INTO accounts VALUES
('ac001','br001','current','0123456789',1000);
-- ('ac002','br002','savings','0123456789',2000);
-- ('ac003','br001','current','1123456789',500),
-- ('ac004','br003','current','2123456789',1500),
-- ('ac005','br002','savings','3123456789',3000);

INSERT INTO bank_branch VALUES
('br001', 'Delhi'),
('br002', 'Bengaluru'),
('br003', 'Mumbai');

INSERT INTO account_owner VALUES
    ('0123456789','Abhinav','Delhi');
    -- ('1123456789','Aditya', 'Delhi'),
    -- ('2123456789', 'Adrij', 'Mumbai'),
    -- ('3123456789', 'Archit', 'Bengaluru');

alter table accounts
	add constraint fk_branch_id FOREIGN KEY (branch_id) REFERENCES bank_branch(branch_id);

alter table accounts
	add constraint fk_phone_no FOREIGN KEY (phone_number) REFERENCES account_owner(phone_number);