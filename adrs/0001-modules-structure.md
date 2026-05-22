# Author
@lwojcik

# Decision
Application is composed of modules that are placed in root package.
There are few 'special' modules (accessible from every module, potentially be published as a JAR in artifactory when microservices comes)
- `shared` - Reusable objects/structures like ID's, Exceptions, globally understood enums etc.
- `infrastructure` - code specyfic to the Database, Messaging, Runtime environment etc.

Known functional modules are:
- `consents` - related to consents definition and agreements
- `users` - CRUD module for storing users record