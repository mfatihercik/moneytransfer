-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: C:/Users/fatih.ercik/git/moneytransfer/moneytransfer/src/main/resources/liquibase.yaml
-- Ran at: 15.01.2019 12:15
-- Against: @jdbc:h2:mem:testdb
-- Liquibase version: 3.6.2
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE DATABASECHANGELOGLOCK (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM DATABASECHANGELOGLOCK;

INSERT INTO DATABASECHANGELOGLOCK (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE DATABASECHANGELOGLOCK SET LOCKED = TRUE, LOCKEDBY = 'OBSS-IM-581 (10.0.75.1)', LOCKGRANTED = '2019-01-15 12:15:47.537' WHERE ID = 1 AND LOCKED = FALSE;

-- Create Database Change Log Table
CREATE TABLE DATABASECHANGELOG (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255), DEPLOYMENT_ID VARCHAR(10));

-- Changeset C:/Users/fatih.ercik/git/moneytransfer/moneytransfer/src/main/resources/liquibase.yaml::1::fatih.ercik
CREATE TABLE user (id INT AUTO_INCREMENT NOT NULL, name VARCHAR(50), created_date date, modified_date date, created_by INT, modifiedBy INT, CONSTRAINT PK_USER PRIMARY KEY (id));

CREATE TABLE account (account_id INT AUTO_INCREMENT NOT NULL, balance DECIMAL(16, 2) NOT NULL, owner INT NOT NULL, currency VARCHAR(3) NOT NULL, status VARCHAR(10) NOT NULL, created_date date NOT NULL, modified_date date, created_by INT, modifiedBy INT, CONSTRAINT PK_ACCOUNT PRIMARY KEY (account_id));

CREATE TABLE account_operation (id INT AUTO_INCREMENT NOT NULL, account_id INT NOT NULL, transaction_id VARCHAR(50) NOT NULL, amount DECIMAL(16, 2) NOT NULL, type VARCHAR(10) NOT NULL, description VARCHAR(255), created_date date, modified_date date, created_by INT, modifiedBy INT, CONSTRAINT PK_ACCOUNT_OPERATION PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1', 'fatih.ercik', 'C:/Users/fatih.ercik/git/moneytransfer/moneytransfer/src/main/resources/liquibase.yaml', NOW(), 1, '8:5da34ee3b39f8703805e69651f606b9f', 'createTable tableName=user; createTable tableName=account; createTable tableName=account_operation', '', 'EXECUTED', NULL, NULL, '3.6.2', '7547348293');

-- Changeset C:/Users/fatih.ercik/git/moneytransfer/moneytransfer/src/main/resources/liquibase.yaml::init_user::fatih.ercik
insert into user (id,created_date,name) values(1,sysdate,'mfatihercik'); insert into user (id,created_date,name) values(2,sysdate,'fatih'); insert into user (id,created_date,name) values(3,sysdate,'foo'); insert into user (id,created_date,name) values(4,sysdate,'mfe'); insert into user (id,created_date,name) values(5,sysdate,'mehmet'); insert into user (id,created_date,name) values(6,sysdate,'istanbul');
insert into account (balance,owner,currency,status, created_date) values(100,1,'TRY','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,1,'USD','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,2,'TRY','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,2,'USD','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,3,'TRY','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,3,'USD','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,4,'TRY','ACTIVE',sysdate); insert into account (balance,owner,currency,status, created_date) values(100,5,'USD','ACTIVE',sysdate);
insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(1,1,'1',100,'DEBIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(2,2,'1',100,'CREDIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(3,1,'2',130,'DEBIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(4,3,'2',130,'CREDIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(5,3,'4',150,'DEBIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(6,1,'5',180,'CREDIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(7,3,'6',170,'DEBIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(8,1,'7',150,'DEBIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(9,2,'8',120,'CREDIT','debit fooo',sysdate); insert into account_operation (id,account_id,transaction_id,amount,type,description,created_date) values(10,4,'9',140,'DEBIT','debit fooo',sysdate);
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init_user', 'fatih.ercik', 'C:/Users/fatih.ercik/git/moneytransfer/moneytransfer/src/main/resources/liquibase.yaml', NOW(), 2, '8:4754065946aebade9ee61191d12fbc7d', 'sql', '', 'EXECUTED', NULL, NULL, '3.6.2', '7547348293');

-- Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

