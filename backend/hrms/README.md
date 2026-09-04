# Enterprise Employee & HR Management System

Full-stack HRMS: Spring Boot 3.3.4 (Java 21) + PostgreSQL backend, React + Vite frontend.

## Project Layout

```
hrms-project/
├── backend/hrms/     Spring Boot API (Maven project — open in IntelliJ)
└── frontend/hrms-ui/ React + Vite SPA
```

## 1. Database Setup

Create the PostgreSQL database and user (matches `backend/hrms/src/main/resources/application.yml`):

```sql
CREATE DATABASE hrms_db;
CREATE USER hrms_user WITH PASSWORD 'hrms_password';
GRANT ALL PRIVILEGES ON DATABASE hrms_db TO hrms_user;
```

If your local Postgres uses different credentials, update `application.yml` accordingly.

## 2. Run the Backend

Open `backend/hrms` as a Maven project in IntelliJ (File → Open → select the `hrms` folder, or the `pom.xml` inside it — IntelliJ will auto-import).

From the terminal, alternatively:

```bash
cd backend/hrms
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

On first startup, `DataInitializer` seeds the four roles (ADMIN, HR, MANAGER, EMPLOYEE) and a default admin user:

- **username:** `admin`
- **password:** `Admin@123`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## 3. Run the Frontend

```bash
cd frontend/hrms-ui
npm install
npm run dev
```

The app starts on `http://localhost:5173` and proxies `/api/*` requests to `http://localhost:8080`.

Log in with the default admin account above, or register a new EMPLOYEE-level account from the Register page.

## 4. Trying the API Directly

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin@123"}'

# Use the returned token
curl http://localhost:8080/api/employees \
  -H "Authorization: Bearer <token>"
```

## Notes

- `spring.jpa.hibernate.ddl-auto=update` auto-creates/updates tables from the entities — fine for development; switch to a migration tool (Flyway/Liquibase) with `validate` for real production use.
- The JWT secret in `application.yml` is a development placeholder — replace it before deploying anywhere real.
- Role-based access rules live in `SecurityConfig.java`: ADMIN/HR manage employees, departments, payroll; MANAGER can approve leave and write reviews; EMPLOYEE has read/self-service access.
- `EmployeeProject` is a full entity (not a plain `@ManyToMany`) so it can carry `assignedDate` and `roleInProject`.
- Basic Mockito unit tests are included under `backend/hrms/src/test` covering employee creation and payroll net-salary calculation as examples — extend these as you add features.

## Common Errors

- `Connection refused` on backend startup → PostgreSQL isn't running or the port is wrong.
- `password authentication failed` → credentials in `application.yml` don't match your Postgres user.
- Frontend requests failing with CORS errors → make sure the backend is running on port 8080 (CORS is configured for `localhost:5173`/`3000` in `SecurityConfig`).
