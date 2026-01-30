# Building Applications Around Database Concepts

A hands-on course for UCF students: learn to build real software by understanding the data models underneath it.

## What This Course Is

You know SQL. You can write queries. But can you look at an application — LinkedIn, Workday, any SaaS product — and *see* the data model underneath it? Can you design one from scratch?

That's what this course teaches. We use **Legoria**, a real Applicant Tracking System (ATS), as our case study. Each week we go deeper into the data model, write SQL against real data, and connect what we see on screen to the schema underneath.

**Approach:** Database-first. Every screen is a query. Every form field is a column. Every dropdown is a foreign key. Once you see this, you can't unsee it.

## Current Modules

| Module | Topic | Description |
|--------|-------|-------------|
| **[1. Rails Foundations](course/modules/01-rails-foundations/)** | Getting Started | Entity concepts, attributes, model classes, in-memory data |
| **[2. ER Modeling & SQL with Legoria](course/modules/02-legoria-er-sql/)** | ER Diagrams, SQL, RBAC, Pipelines | Relational DB history, ER modeling, RBAC and hiring pipeline domains, hands-on SQL exercises against a real ATS database |

*New modules added weekly.*

## Getting Started

### 1. Clone this repo

```bash
git clone https://github.com/jhighman/bax.git
cd bax
```

### 2. Start with Module 2

Module 2 includes a bundled SQLite database — no server setup needed:

```bash
cd course/modules/02-legoria-er-sql/data
sqlite3 legoria.sqlite3
```

On Windows? See the [Windows setup guide](course/modules/02-legoria-er-sql/SETUP-WINDOWS.md).

### 3. Follow the lesson

Open the [Module 2 README](course/modules/02-legoria-er-sql/README.md) and work through Parts 1-4.

## Repository Structure

```
├── course/
│   ├── modules/
│   │   ├── 01-rails-foundations/    # Module 1: Rails basics
│   │   └── 02-legoria-er-sql/      # Module 2: ER modeling & SQL with Legoria
│   │       ├── README.md            # The lesson (~90 min of content)
│   │       ├── INSTRUCTIONS.md      # Setup guide
│   │       ├── SETUP-WINDOWS.md     # Windows-specific setup
│   │       ├── cheat-sheet.md       # Quick reference card
│   │       ├── data/                # Bundled SQLite database + seed scripts
│   │       ├── exercises/           # SQL exercise files
│   │       ├── artifacts/erd/       # Mermaid ER diagrams
│   │       └── screenshots/         # Application screenshots
│   ├── resources/                   # Terminology guide
│   └── tools/                       # Tool-specific guides
├── archived/                        # Previous course content
└── README.md                        # This file
```

## Case Study: Legoria ATS

Legoria is a next-generation Applicant Tracking System designed from scratch. It's our teaching vehicle because it has:

- **RBAC** (Role-Based Access Control) — users, roles, permissions with join tables
- **Hiring Pipeline** — jobs, candidates, applications, stages, transitions
- **Multi-tenant design** — organization scoping on every table
- **Real-world patterns** — state machines, audit trails, encrypted PII, structured interviews

The full Legoria source is at [github.com/jhighman/legoria](https://github.com/jhighman/legoria).

## Tools

- **SQLite** — bundled database, no server needed
- **Mermaid** — ER diagrams that render on GitHub
- **Ruby on Rails** (optional) — for running the full Legoria GUI

## Archived Content

Previous module drafts (UCF Course Manager, DDD modules 3-10) are in the `archived/` directory and `course/modules/archived/` for reference.
