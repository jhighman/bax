# Setup Instructions

## The Database Is Included

The Legoria SQLite database is bundled right here in this module — no servers to run, no Ruby to install.

```
data/legoria.sqlite3    ← the database (all you need)
data/seeds.rb           ← how the base data was created (reference)
data/build_rich_scenario.rb  ← how the 7 candidates were created (reference)
```

## Getting Started

### 1. Open the database

```bash
cd course/modules/02-legoria-er-sql/data
sqlite3 legoria.sqlite3
```

> 🪟 **On Windows?** See [SETUP-WINDOWS.md](./SETUP-WINDOWS.md) for how to install SQLite.  
> 🍎 **On Mac?** SQLite is pre-installed. Just run the command above.  
> 🐧 **On Linux?** `sudo apt install sqlite3` if you don't have it.

### 2. Set up pretty output

```sql
.headers on
.mode column
```

### 3. Verify it works

```sql
SELECT first_name, last_name, email FROM users;
```

You should see 4 users: Alice Admin, Rachel Recruiter, Henry Manager, and Ian Viewer.

### 4. Follow the lesson

Open [README.md](./README.md) and start with Part 1. Run each SQL exercise in your SQLite console.

---

## Login Credentials (for the GUI, if running the full app)

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@acme.test` | `password123` |
| **Recruiter** | `recruiter@acme.test` | `password123` |
| **Hiring Manager** | `hiring.manager@acme.test` | `password123` |
| **Interviewer** | `interviewer@acme.test` | `password123` |

> For this lesson, you only need the SQLite console. The GUI is optional — see [github.com/jhighman/legoria](https://github.com/jhighman/legoria) if you want to run the full web app.

---

## What's in the Database

The database is pre-loaded with realistic hiring data for **Acme Corporation**:

- **4 users** with distinct roles (Admin, Recruiter, Hiring Manager, Interviewer)
- **4 roles** with graduated permissions (Admin has everything, Interviewer has read-only)
- **36 permissions** as resource + action pairs (candidates.read, jobs.create, etc.)
- **7 candidates** — each with a unique hiring story:
  - **Sarah Chen** → Full pipeline, HIRED (6 stage transitions, 3 interviews, offer accepted)
  - **Michael Johnson** → REJECTED after technical interview (documented decision)
  - **Jennifer Davis** → IN PROGRESS, hot candidate with competing offer
  - **James Wilson** → JUST APPLIED, fresh application, no activity yet
  - **David Brown** → WITHDRAWN (took Salesforce offer, added to talent pool)
  - **Emily Williams** → STUCK IN SCREENING for 19 days (bottleneck scenario)
  - **Robert Miller** → Strong PM candidate, ready for onsite interview
- **19 stage transitions** tracking every pipeline movement
- **10 interviews** (technical, cultural, panel)
- **2 hiring decisions** with documented rationale
- **1 offer** (Sarah Chen, accepted)
- **8 candidate notes** from recruiters and interviewers

This is not lorem ipsum. Every candidate has a story you can trace through the tables.

---

## Useful SQLite Commands

| Command | What It Does |
|---------|-------------|
| `.tables` | List all tables |
| `.schema tablename` | Show CREATE TABLE statement |
| `.headers on` | Show column names in output |
| `.mode column` | Align output in columns |
| `.quit` | Exit SQLite |

> 🎯 **Pro tip:** Run `.headers on` and `.mode column` first thing every time. It makes the output way easier to read.

---

## Troubleshooting

- **"unable to open database"** — Make sure you're in the `data/` directory, or use the full path to `legoria.sqlite3`
- **"no such table"** — Run `.tables` to see what's available. Table names are lowercase and pluralized (e.g., `users`, `applications`, `stage_transitions`)
- **Output is ugly** — Run `.headers on` and `.mode column`
- **Want to reset?** The database file is read-only from git. If you mess it up: `git checkout -- data/legoria.sqlite3`
