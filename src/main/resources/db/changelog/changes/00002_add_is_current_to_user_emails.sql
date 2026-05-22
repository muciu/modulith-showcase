-- liquibase formatted sql

-- changeset lwojcik:00003-add-is-current-to-user-emails
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_SCHEMA) = 'PUBLIC' AND UPPER(TABLE_NAME) = 'USER_EMAILS'
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE UPPER(TABLE_SCHEMA) = 'PUBLIC' AND UPPER(TABLE_NAME) = 'USER_EMAILS' AND UPPER(COLUMN_NAME) = 'IS_CURRENT'
ALTER TABLE user_emails
    ADD COLUMN is_current BOOLEAN NOT NULL DEFAULT FALSE;
