# Module 2 (Supplementary): ER Modeling, SQL & the Application That Lives On Top

**Lesson Date:** Saturday, February 1, 2026  
**Duration:** ~90 minutes  
**Case Study:** Legoria — an Applicant Tracking System (ATS)

---

## Meet Legoria: The Only Way to Invent the Future Is to Build It

Before we touch SQL or ER diagrams, let's talk about what we're actually building — and *why*.

### What Is an ATS?

An **Applicant Tracking System** is the software that companies use to manage hiring. Every time you apply for a job online — upload your resume, answer screening questions, get an email saying "we'll be in touch" — an ATS is behind it. Companies like Greenhouse, Lever, Workday, and iCIMS make these products. It's a multi-billion dollar market because *every company that hires people needs one*.

An ATS does more than store resumes. It:
- Posts jobs to job boards and career sites
- Tracks every applicant through a hiring pipeline (Applied → Screening → Interview → Offer → Hired)
- Manages who can see what (recruiters see everything; interviewers see only their assigned candidates)
- Schedules interviews and collects feedback via scorecards
- Handles offers, approvals, and compliance (EEOC, I-9 verification)
- Produces reports so leadership can ask: "How fast are we hiring? Where are the bottlenecks?"

### Why Build One?

Here's the thing about learning software engineering: you can read about patterns, study diagrams, and memorize SQL syntax — but none of it *clicks* until you see it in a real system solving a real problem. **The only way to invent the future is to build it.**

Legoria is our build. It's a next-generation ATS designed from scratch with a specific philosophy: **hiring is not a recruiter workflow — it's a multi-actor ecosystem**. That's a concept of operations (ConOps) decision, and it shapes every table in the database.

### Legoria's Operating Philosophy

Most ATS products are built for recruiters. Legoria is designed for *everyone who touches hiring*:

| Actor | What They Need | What the System Gives Them |
|-------|---------------|---------------------------|
| **Recruiter** | Speed, coordination, pipeline visibility | Task lists, kanban boards, bulk actions, SLA alerts |
| **Hiring Manager** | Confidence, minimal time investment | Simplified views, approval queues, team feedback summaries |
| **Interviewer** | Prep materials, fast feedback submission | Interview kits, mobile scorecards, structured evaluation |
| **Executive** | 30-second health check | Dashboard KPIs, drill-down metrics, trend lines |
| **Compliance Officer** | Audit-ready records | EEOC reports, adverse action workflows, immutable audit trails |
| **Candidate** | Transparency, low friction | No-login apply, status tracking, self-scheduling |

This isn't just a feature list. It's a **data model decision**. When you design for multiple actors, you need:
- **RBAC** (Role-Based Access Control) so each actor sees only what they should
- **Audit trails** so every action is traceable and defensible
- **Stage pipelines** so candidates move through a structured process, not an email chain
- **Multi-tenant isolation** so one company's data never leaks into another's

Every one of those requirements becomes tables, columns, foreign keys, and constraints. The ConOps drives the schema. The schema drives the application. That's the chain we're going to trace today.

### Nine Principles That Shape the Data Model

Legoria was designed around nine operating principles. Each one has consequences for how data is structured:

1. **Security and privacy are the foundation, not a feature** → RBAC tables, encrypted PII columns, immutable audit logs
2. **I-9 verification is first-class, not an afterthought** → Verification workflow tables, document tracking, compliance timestamps
3. **Hiring is an ecosystem, not a recruiter workflow** → Role-specific views, actor-scoped queries, permission granularity
4. **The system drives decisions, not stores resumes** → Pipeline stages, SLA tracking, automated alerts
5. **Sourcing is measurable ROI** → Source attribution on every application, conversion tracking
6. **Interview structure is how quality scales** → Scorecard templates, competency ratings, structured feedback
7. **The hiring record must be defensible** → Who evaluated what, when, and why — all stored, all queryable
8. **Remote hiring requires assurance** → Document capture, verification workflows, exception handling
9. **Integration is the operating model** → Webhook tables, API audit logs, SSO configuration

You don't need to memorize these. But as we walk through the data model today, you'll see how these principles show up as *actual tables and relationships*. That's the lesson: **philosophy becomes schema becomes software**.

### Simulating Go-to-Market

In the real world, software products don't start with code. They start with a problem, a vision, and a plan. Then:

1. **ConOps** (Concept of Operations) — who are the actors, what do they need, how does the system serve them?
2. **Data Model** — what entities exist, how do they relate, what are the constraints?
3. **Build** — code the application against the model
4. **Seed** — populate with realistic data to test and demonstrate
5. **Go to Market** — show it to users, get feedback, iterate

Today, Legoria is at step 4. It's built, seeded with realistic hiring scenarios (7 candidates, 4 roles, 36 permissions, a full pipeline), and running locally. We're going to explore it like a product team doing a walkthrough before launch. You'll see the ER diagrams, run SQL queries against real data, and map everything back to the screens.

This is what building a software product feels like from the inside.

---

## A Brief History: How We Got Here

Before we dive into the hands-on work, let's talk about *why* relational databases exist and why ER diagrams still matter 50 years after they were invented.

### The Relational Revolution (1970s)

In 1970, an IBM researcher named **Edgar F. Codd** published a paper called *"A Relational Model of Data for Large Shared Data Banks."* It was a bombshell. Before Codd, data was stored in hierarchical or network databases — rigid tree structures where navigating from one record to another meant following physical pointers. If you wanted to ask a new question of your data, you often had to restructure the entire database.

Codd's insight was radical: **separate the logical structure of data from its physical storage.** Store data in tables (relations). Let people query it with a declarative language (what became SQL). The database engine figures out *how* to get the data — you just say *what* you want.

This was more than a technical advance. It was a **business revolution:**

- **1979 — Oracle** ships the first commercial relational database. Suddenly enterprises can store and query structured data without an army of programmers maintaining pointer chains.
- **1983 — IBM's DB2** brings relational databases to mainframes. Banks, airlines, and governments adopt them wholesale.
- **1986 — SQL becomes an ANSI standard.** One query language to rule them all. Skills become portable across vendors.
- **1989 — Microsoft SQL Server** launches. Relational databases move from mainframes to departmental servers.
- **1995 — MySQL and PostgreSQL** emerge as open-source alternatives. Now startups and universities can use relational databases for free. The web explosion runs on them.

By the late 1990s, relational databases were *the* way to store business data. Every ERP, CRM, HR system, banking platform, and e-commerce site ran on SQL. The reason? **Relational databases model the real world well.** Customers have orders. Orders have line items. Employees have departments. These are *relationships* — and that's exactly what relational databases were designed for.

### The ER Diagram: A Shared Language (1976)

In 1976, **Peter Chen** published his paper on Entity-Relationship modeling. The ER diagram gave people a visual language to talk about data *before* writing any SQL or code. For the first time, a business analyst and a database engineer could look at the same picture and agree on what the system needed to store.

This matters more than it sounds. The hardest part of building software isn't writing code — it's **making sure everyone agrees on what the system should do.** ER diagrams became the bridge between business thinking and technical implementation. They still are.

### ER Diagrams and Object-Oriented Design

If you've studied OO programming (and at UCF, you have), ER diagrams should feel familiar. That's not a coincidence.

| ER Concept | OO Concept |
|-----------|-----------|
| Entity | Class |
| Attribute | Property / Field |
| Relationship | Association / Reference |
| Primary Key | Object Identity |
| Foreign Key | Object Reference |
| Join Table (many-to-many) | Collection of associated objects |
| Inheritance (is-a) | Class inheritance |

When you draw an ER diagram, you're doing something very close to **object-oriented analysis and design** — you're identifying the key *things* in your domain, their *attributes*, and how they *relate* to each other. The ER diagram is a data-centric view; a UML class diagram is a behavior-centric view. But they're modeling the same reality.

In practice, tools like **Active Record** (Rails), **Hibernate** (Java), and **Entity Framework** (.NET) exist precisely because the mapping between ER models and object models is so natural. An entity becomes a class. A row becomes an object. A foreign key becomes a reference. This is called **Object-Relational Mapping (ORM)** — and it works because ER and OO are two lenses on the same truth.

### The NoSQL Era: Different Shape, Same Thinking (2000s–Now)

In the 2000s, companies like Google, Amazon, and Facebook hit problems that relational databases struggled with: billions of rows, petabytes of data, globally distributed systems. The response was **NoSQL** — a family of databases that trade SQL's strict structure for flexibility and scale:

- **Document stores** (MongoDB) — store JSON-like documents instead of rows
- **Key-value stores** (Redis, DynamoDB) — ultra-fast lookups by key
- **Column-family stores** (Cassandra) — optimized for write-heavy, distributed workloads
- **Graph databases** (Neo4j) — model relationships as first-class citizens

Here's the thing most people miss: **NoSQL didn't replace relational thinking. It absorbed it.**

A MongoDB collection of "users" with embedded "orders" is still entities with relationships — just denormalized into one document. A DynamoDB table with partition keys and sort keys is still modeling access patterns that map to ER concepts. When teams design a NoSQL schema, they almost always start by drawing... an ER diagram. They just implement it differently.

Even graph databases — which seem the furthest from tables — map beautifully to ER diagrams. Nodes are entities. Edges are relationships. Properties are attributes. The ER diagram is the *thinking tool*; the database is the *implementation choice*.

### Why This Still Matters in 2026

The tech stack changes. SQL, NoSQL, NewSQL, vector databases, whatever comes next. But the **skill of looking at a business domain and identifying entities, attributes, and relationships** — that's permanent. That's what ER modeling teaches you. It's not about drawing boxes and lines. It's about *seeing the structure in the chaos*.

Today, you'll practice that skill on a real application.

---

## What This Lesson Is About

You know SQL. You can write a SELECT, a JOIN, a GROUP BY. But have you ever looked at an application — like LinkedIn or Workday — and thought: *"What's the data model underneath this?"*

That's what today is about. We're going to take a real ATS called **Legoria** and walk between three views of the same truth:

1. **ER Diagram** — the shape of the data
2. **SQL** — how you ask questions of it
3. **GUI** — how users interact with it

Every screen you see in a web app is a query. Every form field is a column. Every dropdown is a foreign key. Every list is a `SELECT` with `JOIN`s. Once you see this, you can't unsee it.

We'll cover two domains:
- **Part 1: RBAC** — Role-Based Access Control (Who can do what?)
- **Part 2: Job → Applicant Pipeline** — The core hiring workflow
- **Part 3: The Bridge Moment** — Connecting ER → SQL → GUI → API
- **Part 4: Challenge Query** — Tying both domains together

> 📋 **Setup:** See [INSTRUCTIONS.md](./INSTRUCTIONS.md) to get Legoria running. **On Windows?** See [SETUP-WINDOWS.md](./SETUP-WINDOWS.md) for the full walkthrough.  
> 📎 **Quick ref:** Keep [cheat-sheet.md](./cheat-sheet.md) open in a second tab.

---

## Part 1: RBAC — Who Can Do What? (~30 min)

### The Big Picture

Every real application has to answer one question over and over: **"Is this user allowed to do this thing?"** That's access control. Legoria uses RBAC — Role-Based Access Control — which means permissions aren't assigned directly to users. Instead:

- Users get **roles** (like "Recruiter" or "Admin")
- Roles get **permissions** (like "can read candidates" or "can delete jobs")
- To check if a user can do something, you follow the chain: User → Role → Permission

This is a classic pattern you'll see everywhere — AWS IAM, GitHub organizations, any SaaS product.

### The ER Shape

```mermaid
erDiagram
    users {
        int id PK
        int org_id FK
        string email
        string first_name
        string last_name
        boolean active
    }
    user_roles {
        int id PK
        int user_id FK
        int role_id FK
        datetime granted_at
        int granted_by_id FK
    }
    roles {
        int id PK
        int organization_id FK
        string name
        string description
        boolean system_role
    }
    role_permissions {
        int id PK
        int role_id FK
        int permission_id FK
        json conditions
    }
    permissions {
        int id PK
        string resource
        string action
        string description
    }

    users ||--o{ user_roles : "has"
    roles ||--o{ user_roles : "assigned to"
    roles ||--o{ role_permissions : "grants"
    permissions ||--o{ role_permissions : "granted by"
```

> 🔗 See the full Mermaid ERD: [artifacts/erd/rbac-model.md](./artifacts/erd/rbac-model.md)

### Teaching Points — What to Notice

1. **Two join tables in one pattern.** `user_roles` connects users to roles. `role_permissions` connects roles to permissions. Same pattern, twice. The user is two JOINs away from their permissions.

2. **Join tables aren't always just two FKs.** Look at `user_roles` — it has `granted_at` and `granted_by_id`. That's **audit data on the relationship itself**. When was this role given? Who gave it? That's not about the user or the role — it's about the *assignment*.

3. **Permissions are resource + action pairs.** The `permissions` table doesn't say "can do stuff." It says `resource = 'candidates'` and `action = 'read'`. That's the atomic unit of access. Think of it like a Unix permission: the resource is the file, the action is read/write/execute.

4. **Everything is scoped to `organization_id`.** This is **multi-tenant design**. Acme Corp's roles can't see Globex Corp's data. One database, many organizations, all isolated by that FK.

### See It in the GUI

![Users with Role Badges](./screenshots/05-users-roles.jpg)
> **Screenshot: Users Admin Page.** See the Roles column? Those badges ("Admin", "Recruiter") are the `user_roles` join table rendered visually. Each badge = one row in `user_roles`. The GUI is literally showing you the JOIN result.

![Roles Cards](./screenshots/06-roles.jpg)
> **Screenshot: Roles Page.** Four system roles, each showing a user count. Behind each card: `role_permissions` rows linking that role to specific `permissions`. Click one and you'd see the permission list — that's `role_permissions JOIN permissions`.

### SQL Exercises

Open your SQLite console and try these. Each one teaches a different concept.

```sql
-- Exercise 1: What roles does Alice Admin have?
-- CONCEPT: Basic 3-table JOIN chain (user → user_roles → roles)
-- WHY IT MATTERS: This is the fundamental RBAC lookup
SELECT u.first_name, u.last_name, r.name AS role_name, ur.granted_at
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id
WHERE u.email = 'admin@acme.test';
```

```sql
-- Exercise 2: What can the Recruiter role do?
-- CONCEPT: Walking the other side of the chain (role → role_permissions → permissions)
-- WHY IT MATTERS: Shows how granular permissions are (resource + action)
SELECT r.name AS role, p.resource, p.action, p.description
FROM roles r
JOIN role_permissions rp ON rp.role_id = r.id
JOIN permissions p ON p.id = rp.permission_id
WHERE r.name = 'recruiter'
ORDER BY p.resource, p.action;
```

```sql
-- Exercise 3: THE BIG ONE — Can Ian Interviewer edit candidates?
-- CONCEPT: Full RBAC chain with LEFT JOIN (user → role → permission check)
-- WHY IT MATTERS: This is what the application does on EVERY page load
SELECT u.first_name, p.resource, p.action,
       CASE WHEN p.id IS NOT NULL THEN 'YES' ELSE 'NO' END AS has_access
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id
LEFT JOIN role_permissions rp ON rp.role_id = r.id
LEFT JOIN permissions p ON p.id = rp.permission_id
  AND p.resource = 'candidates' AND p.action = 'update'
WHERE u.email = 'interviewer@acme.test';
```

> 💡 **Notice the LEFT JOIN.** If Ian doesn't have the permission, we still get a row back — but `p.id` is NULL. That's how you check for the *absence* of something in SQL. An INNER JOIN would return nothing, and you couldn't tell the difference between "no permission" and "user doesn't exist."

```sql
-- Exercise 4: Compare permissions across roles
-- CONCEPT: GROUP BY with COUNT on a join table
-- WHY IT MATTERS: Quick audit — which role is most powerful?
SELECT r.name AS role, COUNT(rp.id) AS permission_count
FROM roles r
LEFT JOIN role_permissions rp ON rp.role_id = r.id
GROUP BY r.name
ORDER BY permission_count DESC;
```

### The Bridge: ER → SQL → GUI → API

Let's connect all four views of the same data:

| View | What It Looks Like |
|------|-------------------|
| **ER** | `users` ←→ `roles` through `user_roles` (many-to-many with audit columns) |
| **SQL** | `SELECT ... FROM users JOIN user_roles JOIN roles` — the 3-table chain |
| **GUI** | The Users page with role badges, the Roles page with permission cards |
| **API** | `GET /api/v1/users/:id` returns `{ roles: [{ name: "admin", permissions: [...] }] }` |
| **Form** | Edit User page has a role dropdown — that's a FK rendered as a `<select>` element |

---

## Part 2: Job → Applicant Pipeline (~30 min)

### The Big Picture

This is the core of an ATS: companies post **jobs**, **candidates** apply, and their **applications** move through a pipeline (screening → interview → offer → hired). It's a classic master-detail pattern with a twist — the application entity has its own lifecycle.

### The ER Shape

```mermaid
erDiagram
    organizations {
        int id PK
        string name
        string subdomain
    }
    jobs {
        int id PK
        int org_id FK
        int department_id FK
        string title
        string status
        int hiring_manager_id FK
        int recruiter_id FK
    }
    candidates {
        int id PK
        int org_id FK
        string first_name
        string last_name
        string email
    }
    applications {
        int id PK
        int organization_id FK
        int job_id FK
        int candidate_id FK
        int current_stage_id FK
        string status
        string source_type
        datetime applied_at
        datetime hired_at
        datetime rejected_at
    }
    stages {
        int id PK
        string name
        int position
    }
    users {
        int id PK
        string email
        string first_name
        string last_name
    }

    organizations ||--o{ jobs : "posts"
    organizations ||--o{ candidates : "tracks"
    jobs ||--o{ applications : "receives"
    candidates ||--o{ applications : "submits"
    stages ||--o{ applications : "current stage"
    users ||--o{ jobs : "hiring_manager"
    users ||--o{ jobs : "recruiter"
```

> 🔗 See the full Mermaid ERD: [artifacts/erd/pipeline-model.md](./artifacts/erd/pipeline-model.md)

### Teaching Points — What to Notice

1. **Master-detail chain:** `organizations` → `jobs` → `applications`. An org has many jobs, a job has many applications. Classic hierarchical data.

2. **`applications` is a join entity, not a join table.** It sits between `jobs` and `candidates` — but it's not just two foreign keys. It has its own lifecycle (`status`), its own timestamps (`applied_at`, `hired_at`), its own attributes (`source_type`, `rating`). When a join table grows its own identity, we call it a **join entity** or **associative entity**. That's a key modeling concept.

3. **Two FKs to the same table.** Look at `jobs`: both `hiring_manager_id` and `recruiter_id` point to `users`. Same table, different relationships. In SQL, you'd alias the joins: `JOIN users AS hm ON ...` and `JOIN users AS r ON ...`. This is super common in real systems.

4. **State machine on `status`.** Applications don't just exist — they move through states: `new → screening → interviewing → offer → hired` (or `rejected`/`withdrawn` at any point). The `status` column IS the state machine. The `current_stage_id` FK points to the `stages` table for the pipeline position.

5. **Temporal columns.** `applied_at`, `hired_at`, `rejected_at` — these model *when things happened*. Not just "is this person hired?" but "when were they hired?" Time is data.

### See It in the GUI

![Dashboard](./screenshots/01-dashboard.jpg)
> **Screenshot: Dashboard.** This is where everything starts. The Pipeline Summary shows the distribution of applications across stages. Under the hood, this is a `GROUP BY stages.name` with a `COUNT`. Every stat on this page is a SQL aggregate.

![Jobs List](./screenshots/02-jobs-list.jpg)
> **Screenshot: Jobs List.** Each row is a record in the `jobs` table. Status badges (Open/Draft/Closed), department, location — all columns. The filter dropdowns at the top map directly to `WHERE` clauses. When you select "Open" from the status filter, the app adds `WHERE status = 'open'` to the query.

![Job Detail](./screenshots/03-job-detail.jpg)
> **Screenshot: Job Detail.** Click into a job and you see a single record from `jobs`. Title, description, requirements — all columns. The Team section shows `hiring_manager_id` and `recruiter_id` resolved to user names via JOIN. Salary range is two columns: `salary_min` and `salary_max`.

![Candidates List](./screenshots/04-candidates-list.jpg)
> **Screenshot: Candidates List.** Each row is a record in `candidates`. The Applications column shows which job they applied to — that's the `applications` join table rendered inline. Notice phone numbers are masked (***-***-1355) — the DB stores `encrypted_phone`. PII protection at the column level.

![Candidate Detail](./screenshots/07-candidate-detail.jpg)
> **Screenshot: Sarah Chen's Profile.** Contact info from `candidates`. The Applications section shows her application to Senior Software Engineer with status and current stage. The Activity Timeline is built from `stage_transitions` and audit logs — every row in that timeline is a record somewhere.

![Sarah Chen Full Story](./screenshots/08-sarah-chen-full-story.jpg)
> **Screenshot: Sarah Chen's Full Story.** This is the complete view — every piece of data about one candidate, pulled from multiple tables via JOINs. This single page touches `candidates`, `applications`, `jobs`, `stages`, `stage_transitions`, and more.

### SQL Exercises

```sql
-- Exercise 1: List all open jobs with their applicant count
-- CONCEPT: LEFT JOIN + GROUP BY + COUNT — the bread and butter of list pages
-- WHY IT MATTERS: This is exactly what the Jobs list page runs
SELECT j.title, j.status, j.location, COUNT(a.id) AS applicants
FROM jobs j
LEFT JOIN applications a ON a.job_id = j.id
WHERE j.status = 'open'
GROUP BY j.id, j.title, j.status, j.location
ORDER BY applicants DESC;
```

```sql
-- Exercise 2: Who applied to Senior Software Engineer and what stage are they in?
-- CONCEPT: 4-table JOIN chain (applications → candidates + stages + jobs)
-- WHY IT MATTERS: This is what the Job Detail "applicants" tab shows
SELECT c.first_name, c.last_name, c.email,
       s.name AS current_stage, a.status, a.applied_at
FROM applications a
JOIN candidates c ON c.id = a.candidate_id
JOIN stages s ON s.id = a.current_stage_id
JOIN jobs j ON j.id = a.job_id
WHERE j.title = 'Senior Software Engineer'
ORDER BY a.applied_at;
```

```sql
-- Exercise 3: Show the pipeline distribution (what the dashboard shows!)
-- CONCEPT: GROUP BY on a joined column with ORDER BY position
-- WHY IT MATTERS: This is literally the dashboard widget. You're writing the query behind the chart.
SELECT s.name AS stage, COUNT(a.id) AS count
FROM applications a
JOIN stages s ON s.id = a.current_stage_id
WHERE a.status NOT IN ('hired', 'rejected', 'withdrawn')
GROUP BY s.name
ORDER BY s.position;
```

```sql
-- Exercise 4: Which hiring manager has the most open jobs?
-- CONCEPT: JOIN to the same table (users) via a specific FK + aggregate
-- WHY IT MATTERS: Shows the two-FK-to-same-table pattern in action
SELECT u.first_name || ' ' || u.last_name AS hiring_manager,
       COUNT(j.id) AS open_jobs
FROM jobs j
JOIN users u ON u.id = j.hiring_manager_id
WHERE j.status = 'open'
GROUP BY u.id, u.first_name, u.last_name;
```

---

## Part 3: The Bridge Moment (~15 min)

Let's pick **`applications`** — the richest entity in the system — and see it through all four lenses at once.

| View | What It Looks Like |
|------|-------------------|
| **ER** | `applications` sits between `jobs` and `candidates`, with FKs to both plus `stages` |
| **SQL** | `SELECT ... FROM applications JOIN candidates JOIN jobs JOIN stages` |
| **API** | `GET /api/v1/jobs/:id/applications` returns JSON with nested candidate and stage |
| **Form** | The candidate detail page shows applications as cards with stage badges and source info |

### The Key Insight

> **The data model IS the application.** Everything you see on screen is a query. Every form field is a column. Every dropdown is a foreign key to a lookup table. Every list is a SELECT with JOINs.

This is why ER modeling matters. When you design the data model well, the application almost writes itself. When you design it badly, every feature is a fight against the schema.

Think about it:
- The **Dashboard** is `GROUP BY` + `COUNT`
- The **Jobs list** is `SELECT * FROM jobs WHERE ...`
- The **Job detail** is `SELECT ... FROM jobs JOIN users` (resolving the hiring manager FK)
- The **Candidates list** is `SELECT ... FROM candidates LEFT JOIN applications`
- The **Candidate profile** is `SELECT ... FROM candidates JOIN applications JOIN jobs JOIN stages`

Every. Single. Page. Is. A. Query.

---

## Part 4: Challenge Query (~15 min)

Now let's tie both domains together. This is the real-world question:

> **"Can recruiter Rachel see all applications for jobs she's assigned to?"**

This requires traversing **both** the RBAC model (does she have permission?) **and** the pipeline model (which jobs is she on?). Two data model segments, one business question.

### Step 1: Does Rachel have the permission?

```sql
-- Check RBAC: Does Rachel have 'applications.read'?
-- CONCEPT: Full RBAC chain traversal
SELECT p.resource, p.action
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN role_permissions rp ON rp.role_id = ur.role_id
JOIN permissions p ON p.id = rp.permission_id
WHERE u.email = 'recruiter@acme.test'
  AND p.resource = 'applications' AND p.action = 'read';
```

> If this returns a row, she has permission. If empty, she's blocked at the door.

### Step 2: Which jobs is she recruiter for?

```sql
-- Check pipeline: Which jobs have Rachel as recruiter?
-- CONCEPT: FK relationship — jobs.recruiter_id → users.id
SELECT j.title
FROM jobs j
JOIN users u ON u.id = j.recruiter_id
WHERE u.email = 'recruiter@acme.test';
```

### Step 3: Full picture — her jobs with their applicants

```sql
-- The complete query: Rachel's assigned jobs + their applicants + pipeline stage
-- CONCEPT: This is what the application actually runs (after the RBAC check passes)
SELECT j.title, c.first_name || ' ' || c.last_name AS candidate,
       s.name AS stage, a.status
FROM applications a
JOIN jobs j ON j.id = a.job_id
JOIN candidates c ON c.id = a.candidate_id
JOIN stages s ON s.id = a.current_stage_id
WHERE j.recruiter_id = (SELECT id FROM users WHERE email = 'recruiter@acme.test')
ORDER BY j.title, s.position;
```

### The Discussion

In a real application, the code does this in two steps:
1. **RBAC check** — Can Rachel read applications *at all*? (Step 1)
2. **Scope the query** — Only show her the jobs she's assigned to (Step 3)

Two data model segments working together. The RBAC model says "you're allowed." The pipeline model says "here's what you can see." Neither works alone.

This is what real software architecture looks like — not one giant table, but interconnected models that each handle one concern.

---

## What We Learned

1. **ER diagrams are blueprints.** They show you the shape of the data before you write a single query.
2. **Join tables vs. join entities.** `user_roles` is a join table (mostly just FKs). `applications` is a join entity (has its own lifecycle and attributes). The difference matters.
3. **Every GUI element maps to a data concept.** Badges = join tables. Dropdowns = FK lookups. Lists = SELECT queries. Forms = INSERT/UPDATE.
4. **RBAC is a pattern, not a product.** Users → Roles → Permissions. Two join tables. You'll see this in every enterprise app you ever work on.
5. **State machines live in columns.** The `status` field on `applications` is a state machine. The database enforces which states are valid.

---

## Next Steps

- Try the [bonus exercises](./exercises/04-bonus-exploration.sql) if you want to go deeper
- Look at the [combined ERD](./artifacts/erd/combined-model.md) to see how everything connects
- Think about: what would you add to this schema? (Interviews table? Email templates? Scorecards?)

---

## Files in This Module

| File | What It Is |
|------|-----------|
| [INSTRUCTIONS.md](./INSTRUCTIONS.md) | Setup guide — how to connect |
| [SETUP-WINDOWS.md](./SETUP-WINDOWS.md) | Full Windows setup (WSL + native Ruby) |
| [cheat-sheet.md](./cheat-sheet.md) | Quick reference card |
| [exercises/01-rbac-queries.sql](./exercises/01-rbac-queries.sql) | RBAC SQL exercises |
| [exercises/02-pipeline-queries.sql](./exercises/02-pipeline-queries.sql) | Pipeline SQL exercises |
| [exercises/03-challenge-query.sql](./exercises/03-challenge-query.sql) | Combined challenge query |
| [exercises/04-bonus-exploration.sql](./exercises/04-bonus-exploration.sql) | Bonus exercises |
| [artifacts/erd/rbac-model.md](./artifacts/erd/rbac-model.md) | RBAC Mermaid ERD |
| [artifacts/erd/pipeline-model.md](./artifacts/erd/pipeline-model.md) | Pipeline Mermaid ERD |
| [artifacts/erd/combined-model.md](./artifacts/erd/combined-model.md) | Full Legoria ERD |
