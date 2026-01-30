# Setup Instructions

## Getting Legoria Running

Legoria is a Rails 8 app on GitHub. You need to clone it and set it up locally.

**Source code:** [github.com/jhighman/legoria](https://github.com/jhighman/legoria)

### 🪟 On Windows?

See the full step-by-step guide: **[SETUP-WINDOWS.md](./SETUP-WINDOWS.md)**

It covers WSL vs. native Ruby, installing dependencies, and all the Windows-specific gotchas.

### Quick Setup (Mac/Linux)

```bash
# Prerequisites: Ruby 3.4.5, Bundler, SQLite3
git clone https://github.com/jhighman/legoria.git
cd legoria
bundle install
bin/rails db:create db:migrate db:seed
bin/rails runner script/build_rich_scenario.rb
bin/rails server
```

Open http://localhost:3000 — you're in.

## Connecting to the Application

1. **Open the web app:** [http://localhost:3000](http://localhost:3000)
2. **Log in with one of these accounts:**

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@acme.test` | `password123` |
| **Recruiter** | `recruiter@acme.test` | `password123` |
| **Hiring Manager** | `hiring.manager@acme.test` | `password123` |
| **Interviewer** | `interviewer@acme.test` | `password123` |

> 💡 Start with **Admin** — it has full access to everything. Later we'll explore what other roles can and can't see.

## Accessing the SQLite Console

This is where you'll run all the SQL exercises:

```bash
cd /path/to/legoria
rails dbconsole
```

This drops you into the SQLite shell. You can run any SQL query directly.

**Useful SQLite commands:**
```
.tables              -- List all tables
.schema users        -- Show CREATE TABLE for users
.headers on          -- Show column headers in output
.mode column         -- Pretty-print output in columns
.quit                -- Exit
```

> 🎯 **Pro tip:** Run `.headers on` and `.mode column` first. It makes the output way easier to read.

## What's in the Seeded Database

The database comes pre-loaded with realistic test data:

| Entity | Count | Notes |
|--------|-------|-------|
| Organizations | 1 | Acme Corporation |
| Users | 4 | Admin, Recruiter, Hiring Manager, Interviewer |
| Roles | 4 | admin, recruiter, hiring_manager, interviewer |
| Permissions | ~36 | Resource + action pairs (candidates.read, jobs.create, etc.) |
| Candidates | 7 | Including Sarah Chen (our main example) |
| Jobs | 4-5 | Various open/draft/closed positions |
| Applications | ~10 | Candidates applied to jobs, various stages |
| Stages | 6-7 | Applied → Screening → Interview → Offer → Hired (+ Rejected) |
| Departments | 3-4 | Engineering, Marketing, etc. |

## Lesson Flow

1. **Start here:** Open the web app and the SQLite console side by side
2. **Part 1 (RBAC):** Log in as Admin, explore Users and Roles pages while running RBAC queries
3. **Part 2 (Pipeline):** Explore Dashboard, Jobs, Candidates while running pipeline queries
4. **Part 3 (Bridge):** Pick a page and trace it back to the SQL
5. **Part 4 (Challenge):** Combine both domains in one query

## Troubleshooting

- **App won't load?** Make sure the Rails server is running: `cd /path/to/legoria && rails server`
- **SQLite error?** Make sure you're in the Legoria directory before running `rails dbconsole`
- **Table not found?** Run `.tables` to see what's available — table names might be slightly different than expected
