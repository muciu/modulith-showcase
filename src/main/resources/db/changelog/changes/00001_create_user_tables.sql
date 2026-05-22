-- liquibase formatted sql

-- changeset lwojcik:00001-create-user-table
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_SCHEMA) = 'PUBLIC' AND UPPER(TABLE_NAME) = 'USERS'
CREATE TABLE users (
    id UUID NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- changeset lwojcik:00002-create-user-email-table1
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_SCHEMA) = 'PUBLIC' AND UPPER(TABLE_NAME) = 'USER_EMAILS'
CREATE TABLE user_emails (
    id UUID NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL,
    email VARCHAR(320) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    validation_status VARCHAR(64) NOT NULL,
    verification_date TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_user_emails_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);
