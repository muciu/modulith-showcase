# Project description
This is a showcase project that some good practices and architectural style

# Tech stack
- Spring Boot 4.x
- H2 in memory database 
- JUnit
- Java 25

# Architecture 
- modular monolith where modules lives in com.muciociosan.theproject
- every module communicate with other modules via well-established interface, for example `com.muciociosan.theproject.RegistrationFacade`
- root module (com.muciociosan.theproject) has access to all sub-modules

# Coding tips
## Liquibase tips
- every liquibase script should start with numerical prefix of 4 digits, left-padded with zeros for example `00001_create_user_tables.sql`
- changeset id should start with `lwojcik:00001-create-user-table`
- keep changeset small as possible unless explicitly instructed to keep some updates in one changeset
- single file could include many logically related 'changesets' like : (1) create table, (2) populated data in that table, (3) add constraints
- no rollback needed
- every script should have a pre-condition check (If changeset createa ta bale 'blah' the pre-condition should check if table is already created)

# Additional SKILLs
Include the content of @.ai/skills directory of this project to find the suitable skills to use; example skill is @.ai/skills/my-unit-test 