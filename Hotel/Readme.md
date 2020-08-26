# Hotel web site

This is simple web service which allows you to make order in hotel and do other things for admin users. This service developed on `Java` with using next technologies: `Spring`, `Spring boot`, `Spring data`, `Hibernate`, `Keycloak` and `Liquibase`.

# How to deploy application

You`ll should do next things for deploy this applcation:

  - Create database `hotel` in your DBMS (there uses postgresql by default)
  - Open file `application.properties` which located in ___Hotel/src/main/resources___ and change value of the next fields:
    - `spring.datasource.url` to your database url;
    - `spring.datasource.username` to yourd databese username;
    - `spring.datasource.password` to your database password.
  - Run `keycloak` server and create _realm_, _realm-app_. Then add roles __USER__ and __ADMIN__. After that change next values in `application.properties`:
    - `keycloak.realm`to created real name;
    - `keycloak.resource` to created realm app;
    - `keycloak.principal-attribute` to your preffered username.
  - Deploy application with the help of `maven` command : ___mvn clean spring-boot:run___
 
Now you can visit website by typing address `localhost:8080` in your favourite web browser.

