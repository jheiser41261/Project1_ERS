CREATE TABLE ERS_REIMBURSEMENT(
	REIMB_ID serial PRIMARY KEY,
	REIMB_AMOUNT double precision NOT NULL,
	REIMB_SUBMITTED timestamp DEFAULT now(),
	REIMB_RESOLVED timestamp,
	REIMB_DESCRIPTION varchar(250) NOT null,
	REIMB_AUTHOR int REFERENCES ERS_USERS(ERS_USER_ID) ON DELETE CASCADE NOT NULL,
	REIMB_RESOLVER int REFERENCES ERS_USERS(ERS_USER_ID) ON DELETE CASCADE,
	REIMB_STATUS_ID int REFERENCES ERS_REIMBURSEMENT_STATUS(REIMB_STATUS_ID) ON DELETE CASCADE DEFAULT 1,
	REIMB_TYPE_ID int REFERENCES ERS_REIMBURSEMENT_TYPE(REIMB_TYPE_ID) ON DELETE CASCADE NOT NULL
);

CREATE TABLE ERS_REIMBURSEMENT_STATUS(
	REIMB_STATUS_ID serial PRIMARY KEY,
	REIMB_STATUS varchar(10) NOT NULL
);

CREATE TABLE ERS_REIMBURSEMENT_TYPE(
	REIMB_TYPE_ID serial PRIMARY KEY,
	REIMB_TYPE varchar(10) NOT NULL
);

CREATE TABLE ERS_USERS(
	ERS_USER_ID serial PRIMARY KEY,
	ERS_USERNAME varchar(50) UNIQUE NOT NULL,
	ERS_PASSWORD varchar(50) NOT NULL,
	USER_FIRST_NAME varchar(100) NOT NULL,
	USER_LAST_NAME varchar(100) NOT NULL,
	USER_EMAIL varchar(150) UNIQUE NOT NULL,
	USER_ROLE_ID int REFERENCES ERS_USER_ROLES(ERS_USER_ROLE_ID) ON DELETE CASCADE NOT NULL
);

CREATE TABLE ERS_USER_ROLES(
	ERS_USER_ROLE_ID serial PRIMARY KEY,
	USER_ROLE varchar(10) NOT NULL
);



-- Hard-Coded Values
INSERT INTO ers_user_roles VALUES (DEFAULT, 'Employee');
INSERT INTO ers_user_roles VALUES (DEFAULT, 'Manager');

INSERT INTO ers_reimbursement_type VALUES (DEFAULT, 'Lodging');
INSERT INTO ers_reimbursement_type VALUES (DEFAULT, 'Food');
INSERT INTO ers_reimbursement_type VALUES (DEFAULT, 'Travel');

INSERT INTO ers_reimbursement_status VALUES (DEFAULT, 'Pending');
INSERT INTO ers_reimbursement_status VALUES (DEFAULT, 'Approved');
INSERT INTO ers_reimbursement_status VALUES (DEFAULT, 'Denied');