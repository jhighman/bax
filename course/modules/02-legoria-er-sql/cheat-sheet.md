# Legoria Cheat Sheet 📋

## Quick Login

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@acme.test` | `password123` |
| Recruiter | `recruiter@acme.test` | `password123` |
| Hiring Mgr | `hiring.manager@acme.test` | `password123` |
| Interviewer | `interviewer@acme.test` | `password123` |

**URL:** http://localhost:3000  
**DB Console:** `cd /path/to/legoria && rails dbconsole`

---

## Key Tables & Columns

### RBAC Domain

| Table | Key Columns | Purpose |
|-------|-------------|---------|
| `users` | id, org_id, email, first_name, last_name, active | People who log in |
| `roles` | id, organization_id, name, description, system_role | Permission groups |
| `user_roles` | user_id, role_id, granted_at, granted_by_id | Who has which role (join table w/ audit) |
| `permissions` | id, resource, action, description | Atomic access rights (e.g., candidates + read) |
| `role_permissions` | role_id, permission_id, conditions | Which role gets which permission |

### Pipeline Domain

| Table | Key Columns | Purpose |
|-------|-------------|---------|
| `organizations` | id, name, subdomain | Tenant (company) |
| `jobs` | id, org_id, title, status, hiring_manager_id, recruiter_id, department_id | Job postings |
| `candidates` | id, org_id, first_name, last_name, email | People applying |
| `applications` | id, org_id, job_id, candidate_id, current_stage_id, status, source_type, applied_at | The bridge between jobs & candidates |
| `stages` | id, name, position | Pipeline stages (Applied, Screening, Interview, etc.) |
| `departments` | id, name, organization_id | Org departments |

---

## Application State Machine

```
new → screening → interviewing → offer → hired
  \       \            \           \
   └───────└────────────└───────────└──→ rejected
                                         withdrawn
```

**Status values:** `new`, `screening`, `interviewing`, `offer`, `hired`, `rejected`, `withdrawn`

---

## Common JOIN Patterns

### User → Roles (RBAC lookup)
```sql
SELECT u.*, r.name AS role
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id;
```

### Role → Permissions (what can this role do?)
```sql
SELECT r.name, p.resource, p.action
FROM roles r
JOIN role_permissions rp ON rp.role_id = r.id
JOIN permissions p ON p.id = rp.permission_id;
```

### Job → Applications → Candidates (who applied?)
```sql
SELECT j.title, c.first_name, c.last_name, s.name AS stage
FROM jobs j
JOIN applications a ON a.job_id = j.id
JOIN candidates c ON c.id = a.candidate_id
JOIN stages s ON s.id = a.current_stage_id;
```

### Job → Hiring Manager & Recruiter (two FKs to users)
```sql
SELECT j.title,
       hm.first_name || ' ' || hm.last_name AS hiring_manager,
       r.first_name || ' ' || r.last_name AS recruiter
FROM jobs j
JOIN users hm ON hm.id = j.hiring_manager_id
JOIN users r ON r.id = j.recruiter_id;
```

---

## Key Counts (Seeded Data)

| What | Approx Count |
|------|-------------|
| Organizations | 1 |
| Users | 4 |
| Roles | 4 |
| Permissions | ~36 |
| Candidates | 7 |
| Jobs | 4-5 |
| Applications | ~10 |
| Stages | 6-7 |

---

## SQLite Quick Reference

```
.tables              -- list all tables
.schema tablename    -- show CREATE TABLE
.headers on          -- show column names
.mode column         -- pretty-print
.quit                -- exit
```
