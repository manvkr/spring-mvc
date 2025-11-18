# Spring MVC User Management App (Non-Boot)

A simple Spring MVC + JPA (Hibernate) application to:
1. List bugs
2. Create (save) bug via AJAX (no page reload)
3. Filter bugs by severity (LOW /MEDIUM /HIGH) using jQuery (server param used for efficiency)

## Tech Stack
- Java 11
- Spring MVC / Spring ORM 5.3
- Hibernate / JPA
- Maven (WAR packaging)
- JSP + jQuery + AJAX
- MySQL 8
- Deployable on Tomcat 9

## Database Setup
Create database (schema) `bugtracker` with MySQL user `root` / password `root` (adjust in `applicationContext.xml` if different):
```sql
CREATE DATABASE IF NOT EXISTS bugtracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bugtracker;
-- Table auto-created by Hibernate (hibernate.hbm2ddl.auto=update). For manual creation:
-- CREATE TABLE bug (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   bug_title VARCHAR(255) NOT NULL,
--   description text,
--   status VARCHAR(10) NOT NULL,
--   severity VARCHAR(10) NOT NULL,
--   created_at DATETIME NOT NULL
-- );
```

## Build
```bash
mvn clean package
```
Generates `target/spring-mvc-bug-tracking-app.war`.

## Deploy to Tomcat 9
1. Copy `target/spring-mvc-bug-tracking-app.war` to `TOMCAT_HOME/webapps/`.
2. Start Tomcat: Ensure MySQL is running.
3. Access: `http://localhost:8080/spring-mvc-bug-tracking-app/`

If you rename the WAR, context path follows file name.

## Endpoints
- `GET /bugs` -> JSON list of bugs (optional `?severity=LOW|MEDIUM|HIGH`)
- `POST /bugs` -> Form params `bugTitle`, `description`, `severity`, `status` returns created bug JSON

## Notes
- Filtering is done via server parameter for concise dataset; jQuery manipulates the form and triggers reload.
- Switch to client-side filtering only by removing the `severity` query parameter logic in `loadBugs()` inside `index.jsp` and filtering existing rows.
- For production: move credentials to environment variables, use connection pool (HikariCP), and add validation/security.

## Possible Enhancements
- Add DTO layer & validation annotations (@Valid)
- Add pagination for large user sets
- Add login/auth
- Switch to connection pool

Enjoy! :)
