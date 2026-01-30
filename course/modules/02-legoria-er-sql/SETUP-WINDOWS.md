# Running Legoria on Windows

This guide gets Legoria running on a Windows machine so you can follow along with the Module 2 lesson.

## What You're Setting Up

Legoria is a Ruby on Rails application with a SQLite database. You'll:
1. Install Ruby and Rails
2. Clone the repo
3. Set up the database with realistic seed data
4. Run the app and explore it in your browser

**Time estimate:** 20-30 minutes for first-time setup.

---

## Step 1: Install Prerequisites

### Option A: WSL (Recommended — Linux on Windows)

WSL (Windows Subsystem for Linux) is the easiest path for Ruby/Rails on Windows. It gives you a real Linux environment.

```powershell
# Open PowerShell as Administrator
wsl --install
```

Restart your computer, then open **Ubuntu** from the Start menu. Then follow the Linux steps:

```bash
# Update packages
sudo apt update && sudo apt upgrade -y

# Install Ruby dependencies
sudo apt install -y build-essential libssl-dev libreadline-dev zlib1g-dev libsqlite3-dev sqlite3 libyaml-dev git curl

# Install rbenv (Ruby version manager)
curl -fsSL https://github.com/rbenv/rbenv-installer/raw/HEAD/bin/rbenv-installer | bash

# Add rbenv to your shell
echo 'eval "$(~/.rbenv/bin/rbenv init - bash)"' >> ~/.bashrc
source ~/.bashrc

# Install Ruby 3.4.5
rbenv install 3.4.5
rbenv global 3.4.5

# Verify
ruby --version   # Should show 3.4.5
gem install bundler
```

### Option B: Native Windows (RubyInstaller)

If you prefer native Windows without WSL:

1. **Download RubyInstaller** from [rubyinstaller.org](https://rubyinstaller.org/downloads/)
   - Get **Ruby+Devkit 3.3.x (x64)** (3.4.5 may not be available yet — 3.3.x works fine)
   - Run the installer, check "Add Ruby executables to your PATH"
   - At the end, run the MSYS2 setup (option 3 — full install)

2. **Install SQLite3:**
   - Download from [sqlite.org/download.html](https://sqlite.org/download.html)
   - Get `sqlite-tools-win-x64` and `sqlite-dll-win-x64`
   - Extract both to `C:\sqlite3`
   - Add `C:\sqlite3` to your PATH:
     - Search "Environment Variables" in Start
     - Edit `Path` → Add `C:\sqlite3`

3. **Install Git** (if not already):
   - Download from [git-scm.com](https://git-scm.com/download/win)
   - Use defaults during installation

4. **Verify in a new Command Prompt:**
   ```cmd
   ruby --version
   gem --version
   sqlite3 --version
   git --version
   ```

---

## Step 2: Clone and Set Up Legoria

Open your terminal (Ubuntu on WSL, or Command Prompt/PowerShell on native Windows):

```bash
# Clone the repo
git clone https://github.com/jhighman/legoria.git
cd legoria

# Install Ruby gems
bundle install
```

**If `bundle install` fails on native Windows:**
- `sqlite3` gem issue? Try: `gem install sqlite3 --platform ruby`
- `attr_encrypted` issue? Try: `gem install attr_encrypted`
- Still stuck? The WSL path (Option A) avoids most of these issues.

---

## Step 3: Set Up the Database

```bash
# Create the database and run all migrations (89 migration files!)
bin/rails db:create
bin/rails db:migrate

# Seed with base data (organization, users, roles, permissions, stages, etc.)
bin/rails db:seed

# Run the rich scenario script (adds 7 candidates with realistic hiring narratives)
bin/rails runner script/build_rich_scenario.rb
```

**On native Windows**, use `ruby bin/rails` instead of `bin/rails` if you get permission errors.

You should see output like:
```
Seeding database...
Creating organization...
Creating departments...
Creating stages...
Creating permissions...
Creating roles...
Creating users...
Done!
```

And from the rich scenario script:
```
Building rich scenario data...
Creating Sarah Chen → HIRED (full pipeline)
Creating Michael Johnson → REJECTED (technical fail)
...
Done! 7 candidates, 19 stage transitions, 10 interviews
```

---

## Step 4: Start the Server

```bash
bin/rails server
```

Open your browser to **http://localhost:3000**

### Login Credentials

| User | Email | Password | Role |
|------|-------|----------|------|
| Alice Admin | admin@acme.test | password123 | Admin (full access) |
| Rachel Recruiter | recruiter@acme.test | password123 | Recruiter |
| Henry Hiring Manager | hiring.manager@acme.test | password123 | Hiring Manager |
| Ian Interviewer | interviewer@acme.test | password123 | Interviewer |

**Start with admin@acme.test** — you'll see everything.

---

## Step 5: Access the SQLite Console

This is where you'll run the SQL exercises from the lesson.

### From the Rails project directory:

```bash
# Rails way (recommended)
bin/rails dbconsole

# Or directly with sqlite3
sqlite3 storage/development.sqlite3
```

### Useful SQLite commands:

```sql
-- Show all tables
.tables

-- Show a table's columns
.schema users
.schema applications

-- Pretty-print output
.mode column
.headers on

-- Now try a query from the lesson!
SELECT u.first_name, u.last_name, r.name AS role_name
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id;
```

### On native Windows:

If `bin/rails dbconsole` doesn't work, use SQLite directly:

```cmd
sqlite3 storage\development.sqlite3
```

---

## Step 6: Explore!

You're now set up to follow the full Module 2 lesson. Open these side by side:

1. **Browser** → http://localhost:3000 (logged in as admin)
2. **SQLite console** → `bin/rails dbconsole`
3. **The lesson** → [README.md](./README.md) in this module

As you go through each exercise, try the SQL query first, then find the same data in the GUI. That's the whole point: **seeing the same truth through different lenses**.

---

## Troubleshooting

### "Could not find gem" errors during `bundle install`

```bash
# Update bundler
gem install bundler
bundle update --bundler
bundle install
```

### "sqlite3.h is missing" on Windows

Native Windows needs the SQLite development headers:
```cmd
ridk exec pacman -S mingw-w64-x86_64-sqlite3
gem install sqlite3 --platform ruby
bundle install
```

### "Migrations are pending"

```bash
bin/rails db:migrate
```

### "Address already in use" when starting server

Another process is on port 3000:
```bash
# Change port
bin/rails server -p 3001
```
Then open http://localhost:3001

### "Permission denied" on `bin/rails` (Windows native)

Use `ruby bin/rails` instead of `bin/rails`:
```cmd
ruby bin\rails server
ruby bin\rails dbconsole
```

### WSL: Browser won't open localhost

WSL2 should forward localhost automatically. If not:
```bash
# Find your WSL IP
hostname -I
```
Then open `http://<that-ip>:3000` in your Windows browser.

### Need to reset everything?

```bash
bin/rails db:drop db:create db:migrate db:seed
bin/rails runner script/build_rich_scenario.rb
```

---

## What's in the Database After Setup

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

This is realistic ATS data — not lorem ipsum. Every candidate has a story you can trace through the tables.
