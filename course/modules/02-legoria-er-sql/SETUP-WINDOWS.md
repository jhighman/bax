# Running the Legoria Dataset on Windows

The database is included in this module — no Ruby or Rails needed. You just need SQLite.

---

## Step 1: Install SQLite

### Option A: Download SQLite (simplest)

1. Go to [sqlite.org/download.html](https://sqlite.org/download.html)
2. Under **Precompiled Binaries for Windows**, download:
   - `sqlite-tools-win-x64-XXXXXXX.zip`
3. Extract to `C:\sqlite3`
4. Add to PATH:
   - Search "Environment Variables" in Start
   - Edit the `Path` variable → Add `C:\sqlite3`
5. Open a **new** Command Prompt and verify:
   ```cmd
   sqlite3 --version
   ```

### Option B: Via Chocolatey (if you use it)

```powershell
choco install sqlite
```

### Option C: Via WSL

If you already have WSL (Windows Subsystem for Linux):
```bash
sudo apt install sqlite3
```

---

## Step 2: Open the Database

The database file is right here in this module:

```
course/modules/02-legoria-er-sql/data/legoria.sqlite3
```

Open it:

```cmd
cd course\modules\02-legoria-er-sql\data
sqlite3 legoria.sqlite3
```

You're in. That's it.

---

## Step 3: Set Up Pretty Output

First thing — make the output readable:

```sql
.headers on
.mode column

-- Verify it works:
SELECT first_name, last_name, email FROM users;
```

You should see:

```
first_name  last_name  email
----------  ---------  --------------------------
Alice       Admin      admin@acme.test
Rachel      Recruiter  recruiter@acme.test
Henry       Manager    hiring.manager@acme.test
Ian         Viewer     interviewer@acme.test
```

---

## Step 4: Explore

### Quick orientation:

```sql
-- See all tables
.tables

-- See a table's structure
.schema users
.schema applications
.schema roles

-- Count records in key tables
SELECT 'users' AS tbl, COUNT(*) AS cnt FROM users
UNION ALL SELECT 'roles', COUNT(*) FROM roles
UNION ALL SELECT 'permissions', COUNT(*) FROM permissions
UNION ALL SELECT 'candidates', COUNT(*) FROM candidates
UNION ALL SELECT 'jobs', COUNT(*) FROM jobs
UNION ALL SELECT 'applications', COUNT(*) FROM applications
UNION ALL SELECT 'stage_transitions', COUNT(*) FROM stage_transitions;
```

### Now follow the lesson:

Open [README.md](./README.md) and start with the SQL exercises in Part 1. Run each query in your SQLite console.

---

## Useful SQLite Commands

| Command | What It Does |
|---------|-------------|
| `.tables` | List all tables |
| `.schema tablename` | Show CREATE TABLE statement |
| `.headers on` | Show column names in output |
| `.mode column` | Align output in columns |
| `.mode csv` | Output as CSV |
| `.output file.csv` | Write output to a file |
| `.output stdout` | Switch back to screen output |
| `.quit` | Exit SQLite |

---

## What's in the Database

| Table | Records | What It Is |
|-------|---------|------------|
| organizations | 1 | Acme Corporation |
| users | 4 | Admin, Recruiter, Hiring Manager, Interviewer |
| roles | 4 | Admin, Recruiter, Hiring Manager, Interviewer |
| permissions | 36 | Resource + action pairs (candidates.read, jobs.create, etc.) |
| role_permissions | ~60 | Which roles have which permissions |
| departments | 4 | Engineering, Product, Marketing, Sales |
| stages | 6 | Applied → Screening → Interview → Offer → Hired + Rejected |
| jobs | 3 | Senior Software Engineer, Product Manager, Marketing Coordinator |
| candidates | 7 | Sarah Chen (hired), Michael Johnson (rejected), and 5 more |
| applications | 7 | One per candidate, various stages and statuses |
| stage_transitions | 19 | Full pipeline history showing movement |
| interviews | 10 | Technical, cultural, panel interviews |
| hiring_decisions | 2 | Hire + reject decisions with rationale |
| offers | 1 | Sarah Chen's accepted offer |
| candidate_notes | 8 | Recruiter and interviewer notes |

Every candidate has a realistic hiring story you can trace through the tables.

---

## Also Included

| File | What It Is |
|------|-----------|
| `data/legoria.sqlite3` | The database — this is what you query |
| `data/seeds.rb` | Ruby script that created the base data (read-only reference) |
| `data/build_rich_scenario.rb` | Ruby script that created the 7 candidate narratives (read-only reference) |

The `.rb` files are for reference — you can read them to see how the data was built, but you don't need Ruby to run the exercises. Everything is already in the SQLite database.

---

## Want the Full GUI Too?

The Legoria web app (where the screenshots come from) requires Ruby on Rails. If you want to run it:

**Source:** [github.com/jhighman/legoria](https://github.com/jhighman/legoria)

See the Legoria README for full setup instructions. But for this lesson, **the SQLite database is all you need**.
