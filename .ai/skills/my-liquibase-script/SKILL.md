---
name: my-liquibase-script
description: When asked for write or refine liquibase script this skill should be used 
---
# How to use this skill
- every liquibase script should start with numerical prefix of 4 digits, left-padded with zeros for example `00001_create_user_tables.sql`
- changeset id should start with `lwojcik:00001-create-user-table`
- keep changeset small as possible unless explicitly instructed to keep some updates in one changeset
- single file could include many logically related 'changesets' like : (1) create table, (2) populated data in that table, (3) add constraints
- no rollback needed
- every script should have a pre-condition check (If changeset createa ta bale 'blah' the pre-condition should check if table is already created)