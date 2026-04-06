# 💰 Finance Dashboard Backend

> A role-based REST API backend for managing financial records, users, and dashboard analytics — built with **Spring Boot**, **MySQL**, and **Spring Security**.

---

## 🚀 Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 4.0.5 | Framework |
| Spring Security | 6 | Access control |
| Spring Data JPA | 4.0.5 | ORM / DB queries |
| MySQL | 8.x | Database |
| Hibernate | 6 | SQL generation |
| Jakarta Validation | 3 | Input validation |
| Lombok | latest | Reduce boilerplate |
| Maven | 3.8+ | Build tool |

---

## 📁 Project Structure

```
src/main/java/com/finance/dashboard/
│
├── FinanceDashboardApplication.java
│
├── security/
│   ├── RoleAuthFilter.java               
│   └── SecurityConfig.java              
│
├── enums/
│   ├── Role.java                         
│   ├── UserStatus.java                   
│   ├── TransactionType.java             
│   └── Category.java                    
│                                           
├── entity/
│   ├── User.java                        
│   └── FinancialRecord.java              
│
├── dto/
│   ├── request/
│   │   ├── CreateUserRequest.java
│   │   ├── UpdateUserRequest.java
│   │   ├── UserStatusRequest.java
│   │   ├── CreateRecordRequest.java
│   │   └── UpdateRecordRequest.java
│   └── response/
│       ├── UserResponse.java
│       ├── RecordResponse.java
│       ├── SummaryResponse.java
│       ├── CategoryBreakdownResponse.java
│       ├── TrendResponse.java
│       ├── WeeklyTrendResponse.java
│       └── ErrorResponse.java
│
├── mapper/
│   ├── UserMapper.java                   
│   └── FinancialRecordMapper.java
│
├── repository/
│   ├── UserRepository.java
│   └── FinancialRecordRepository.java
│
├── service/
│   ├── UserService.java                  
│   ├── FinancialRecordService.java      
│   └── DashboardService.java            
│
├── service/impl/
│   ├── UserServiceImpl.java
│   ├── FinancialRecordServiceImpl.java
│   └── DashboardServiceImpl.java
│
├── controller/
│   ├── UserController.java
│   ├── FinancialRecordController.java
│   └── DashboardController.java
│
└── exception/
    ├── ResourceNotFoundException.java
    ├── AccessDeniedException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

---

## ⚙️ Setup and Installation

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.x running locally

### Step 1 — Clone the repository

```bash
git clone https://github.com/your-username/finance-dashboard.git
cd finance-dashboard
```

### Step 2 — Create the database

```sql
CREATE DATABASE finance_db;
```

### Step 3 — Configure `application.properties`

```properties
spring.application.name=finance-dashboard

spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=your_password_here

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true

server.port=4545
spring.security.user.name=admin
spring.security.user.password=admin

```

> Tables are created automatically on first run — no SQL scripts needed.

### Step 4 — Run

```bash
mvn spring-boot:run
```

**Server:** `http://localhost:4545`

---

## 🔐 Authentication

This project uses **mock header-based authentication** — no login endpoint needed.

Add this header to **every request**:

| Header | Value |
|---|---|
| `X-Role` | `ADMIN` or `ANALYST` or `VIEWER` |

---

## 👥 Role Permissions

| Endpoint | VIEWER | ANALYST | ADMIN |
|---|---|---|---|
| `GET /api/records` | ✅ | ✅ | ✅ |
| `GET /api/records/{id}` | ✅ | ✅ | ✅ |
| `GET /api/records/all` | ✅ | ✅ | ✅ |
| `POST /api/records` | ❌ | ❌ | ✅ |
| `PUT /api/records/{id}` | ❌ | ❌ | ✅ |
| `DELETE /api/records/{id}` | ❌ | ❌ | ✅ |
| `GET /api/dashboard/**` | ✅ | ✅ | ✅ |
| `ALL /api/users/**` | ❌ | ❌ | ✅ |

---

## 📌 API Endpoints

### 👤 User Management — `ADMIN` only

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users` | Create a new user |
| `GET` | `/api/users` | Get all active users |
| `GET` | `/api/users/{id}` | Get user by ID |
| `GET` | `/api/users/by-email?email=` | Get user by email |
| `PUT` | `/api/users/{id}` | Update user details |
| `PATCH` | `/api/users/{id}/status` | Change user status |
| `DELETE` | `/api/users/{id}` | Soft delete user |

**Create user request body:**
```json
{
  "name": "Arjun Mehta",
  "email": "arjun@test.com",
  "role": "ADMIN",
  "status": "ACTIVE"
}
```

---

### 📊 Financial Records

| Method | Endpoint | Role | Description |
|---|---|---|---|
| `POST` | `/api/records` | ADMIN | Create a record |
| `GET` | `/api/records` | ALL | Filter records |
| `GET` | `/api/records/all` | ALL | Get all records |
| `GET` | `/api/records/{id}` | ALL | Get record by ID |
| `PUT` | `/api/records/{id}` | ADMIN | Update a record |
| `DELETE` | `/api/records/{id}` | ADMIN | Soft delete a record |

**Create record request body:**
```json
{
  "userId": 1,
  "amount": 75000.00,
  "transactionType": "INCOME",
  "category": "SALARY",
  "transactionDate": "2025-01-01",
  "notes": "January salary"
}
```

**Filter params for `GET /api/records`:**

| Param | Example | Description |
|---|---|---|
| `userId` | `1` | Filter by user |
| `type` | `INCOME` | `INCOME` or `EXPENSE` |
| `category` | `FOOD` | Any Category enum value |
| `startDate` | `2025-01-01` | From date (yyyy-MM-dd) |
| `endDate` | `2025-03-31` | To date (yyyy-MM-dd) |

---

### 📈 Dashboard — ALL roles

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard/summary` | Total income, expenses, net balance |
| `GET` | `/api/dashboard/category-breakdown` | Totals grouped by category |
| `GET` | `/api/dashboard/monthly-trends` | Income vs expense per month |
| `GET` | `/api/dashboard/weekly-trends` | Income vs expense per ISO week |
| `GET` | `/api/dashboard/recent-activity` | Last 5 transactions |

**Optional params on all dashboard endpoints:** `userId`, `startDate`, `endDate`

**Summary response example:**
```json
{
  "totalIncome": 162000.00,
  "totalExpenses": 29800.00,
  "netBalance": 132200.00,
  "periodFrom": "2025-01-01",
  "periodTo": "2025-03-31"
}
```

---

## ❌ Error Response Format

All errors return consistent JSON:

```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Role not permitted to POST /api/records"
}
```

| Scenario | Status |
|---|---|
| Missing `X-Role` header | `403` |
| Invalid `X-Role` value | `403` |
| Role not allowed for route | `403` |
| Resource not found | `404` |
| Validation failure | `400` |
| Duplicate email | `400` |
| Update/delete already deleted resource | `400` |
| Server error | `500` |

---


## 🗑️ Soft Delete

Nothing is ever physically deleted.

| Resource | How |
|---|---|
| User deleted | `status = INACTIVE` |
| Record deleted | `is_deleted = true` |

All read queries automatically filter out soft-deleted data.

---

## 🏗️ Architecture

```
Request
   ↓
RoleAuthFilter       ← reads X-Role, sets Spring Security Authentication
   ↓
SecurityConfig       ← checks hasRole() per route → 403 if not allowed
   ↓
Controller           ← receives request, calls service
   ↓
Service              ← business logic, validates rules
   ↓
Mapper               ← converts entity ↔ DTO
   ↓
Repository           ← Spring Data JPA queries
   ↓
MySQL
```

---

## 📝 Assumptions and Design Decisions

- **Mock auth via `X-Role` header** — No JWT. In production, replace `RoleAuthFilter` with JWT validation.
- **Default role is `VIEWER`** — Safest default when role is not provided on user creation.
- **VIEWER can access dashboard** — The requirement states *"Viewer: Can only view dashboard data"*, so all `/api/dashboard/**` routes are open to VIEWER.
- **Service interface + impl pattern** — Follows Dependency Inversion Principle, makes testing easier.
- **Static mappers** — `UserMapper` and `FinancialRecordMapper` use static methods, no Spring injection needed.
- **Java stream aggregation** — Dashboard data is aggregated in Java streams for clarity and simplicity.
- **Soft delete everywhere** — No hard deletes. Data is always preserved for audit purposes.

---

# Finance Dashboard — System Design

---

```
                  Postman / Client
                          │
                          │  HTTP Request + X-Role header
                          ▼
┌─────────────────────────────────────────────────────┐
│                  Spring Boot App                    │
│                  Port: 4545                         │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │           Security Layer                     │   │
│  │                                              │   │
│  │  RoleAuthFilter                              │   │
│  │  → reads X-Role header                       │   │
│  │  → validates against Role enum               │   │
│  │  → sets Authentication in SecurityContext    │   │
│  │                                              │   │
│  │  SecurityConfig                              │   │
│  │  → checks hasRole() per route                │   │
│  │  → allows or returns 403                     │   │
│  └──────────────────────────────────────────────┘   │
│                       │                             │
│                       ▼                             │
│  ┌──────────────────────────────────────────────┐   │
│  │           Controller Layer                   │   │
│  │  UserController                              │   │
│  │  FinancialRecordController                   │   │
│  │  DashboardController                         │   │
│  └──────────────────────────────────────────────┘   │
│                       │                             │
│                       ▼                             │
│  ┌──────────────────────────────────────────────┐   │
│  │           Service Layer (Interface + Impl)   │   │
│  │  UserService / UserServiceImpl               │   │
│  │  FinancialRecordService / Impl               │   │
│  │  DashboardService / DashboardServiceImpl     │   │
│  └──────────────────────────────────────────────┘   │
│                       │                             │
│                  ┌────┴────┐                        │
│                  │         │                        │
│                  ▼         ▼                        │
│        ┌───────────┐      ┌─────────────────┐       │
│        │  Mapper   │      │   Repository    │       │
│        │  (static) │      │   (JPA)         │       │
│        └───────────┘      └─────────────────┘       │
│                         n│                          │
└──────────────────────────┼──────────────────────────┘
                           │
                           ▼
                    ┌─────────────┐
                    │    MySQL    │
                    │  finance_db │
                    └─────────────┘
 
```

 
- GitHub: [@MadhanBandi](https://github.com/MadhanBandi25)
- LinkedIn: [Madhan_B](https://linkedin.com/in/madhanbandi25)
