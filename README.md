# Spring MVC Bug Management App (Non-Boot)

A simple Spring MVC + JPA (Hibernate) application to:
1. List bugs
2. Create (save) bug via AJAX (no page reload)
3. Filter bugs by severity (LOW /MEDIUM /HIGH) using jQuery (server param used for efficiency)

## Tech Stack
- Java 17
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

## Prerequisite before creating build and deploying application to tomcat (without Docker)
1. Comment out the `jdbc-docker.properties` in case you want to run application on standalone Tomcat
2. Update the DB details in `jdbc.properties`
3. Build the application
```bash
mvn clean package
```
Generates `target/spring-mvc-bug-tracking-app.war`.

## Deploy to Tomcat 9
1. Copy `target/bug-tracker.war` to `TOMCAT_HOME/webapps/`.
2. Start Tomcat: Ensure MySQL is running.
3. Access: `http://localhost:8080/bug-tracker/`

If you rename the WAR, context path follows file name.

## Endpoints
- `GET /bug/list` -> JSON list of bugs (optional `?severity=LOW|MEDIUM|HIGH`)
- `POST /bug/add` -> Form params `bugTitle`, `description`, `severity`, `status` returns created bug JSON

## Notes
- Filtering is done via server parameter for concise dataset; jQuery manipulates the form and triggers reload.
- Switch to client-side filtering only by removing the `severity` query parameter logic in `loadBugs()` inside `bugDashboard.jsp` and filtering existing rows.
- For production: move credentials to environment variables, use connection pool (HikariCP), and add validation/security.

## Possible Enhancements
- Add DTO layer & validation annotations (@Valid)
- Add pagination for large user sets
- Add login/auth

## Dockerization
This project includes a multi-stage Docker build and a `docker-compose.yml` to run the app with MySQL.

### Files Added
- `Dockerfile` – Builds the WAR using Maven, then copies it into a Tomcat 9 runtime image.
- `docker-compose.yml` – Brings up MySQL 8 and the web app container.
- `jdbc-docker.properties` – Uses environment variables (`JDBC_URL`, `JDBC_USERNAME`, `JDBC_PASSWORD`) that default to connecting to the `mysql` service.
- `.dockerignore` – Reduces build context size.

### Build & Run (PowerShell)
```powershell
# Build and start containers
docker-compose build
docker-compose up -d
 OR
docker compose up -d --build

# View logs
docker logs -f bugtracker-app
docker logs -f bugtracker-mysql

# Stop
docker compose down

#To remove DB volume (fresh database):
docker-compose down -v
```

App will be available at: `http://localhost:8080/`

### Customizing Credentials
Edit environment variables in `docker-compose.yml` or supply overrides:
```powershell
docker compose up -d --build --renew-anon-volumes ^
	--env-file .env
```
Or directly:
```powershell
docker compose up -d --build -e JDBC_PASSWORD=NewPass
```

### How to open the MySQL running in Docker
```powershell
docker ps
docker exec -it <mysql-container-name> bash
mysql -u root -p 
```

### Rebuilding After Code Changes
```powershell
mvn clean package
docker compose build --no-cache app
docker compose up -d
```

### Troubleshooting
- If the app cannot connect to MySQL, check `docker compose logs mysql` for startup progress.
- Ensure the `mysql` service is healthy before the app attempts first queries (compose manages order but initial delays can happen).
- Date/time JSON issues: confirm the image was rebuilt AFTER adding `jackson-datatype-jsr310`.
- To inspect running container: `docker exec -it springapp-web /bin/bash`.

### Running Client and Server Separately
Set `APP_API_BASE_URL` to the server's base path, e.g.

PowerShell (local) or Standalone Tomcat
Set the Environment Variable
```powershell
$env:APP_ALLOWED_ORIGIN="http://10.9.8.7:8081";
$env:APP_API_BASE_URL="http://10.1.2.3:8080/bug-tracker"
```
Docker compose:
```yaml
environment:
  APP_ALLOWED_ORIGIN: "http://client.example.com"
  APP_API_BASE_URL: "http://spring-web:8080"
```
The JSP injects `API_BASE` into JavaScript; all AJAX calls use it

Enjoy! :)
