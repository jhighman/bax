# Pre-Course Setup Email Template

**Subject:** DDD Course: Detailed Setup Instructions - Please Complete Before [DATE]

---

**Copy and paste the content below, customizing the bracketed placeholders:**

---

Dear [STUDENT_NAME],

Following up on our course invitation, here are detailed setup instructions to ensure you're ready for our Domain-Driven Design course. Please complete these steps before our first meetup on [MEETING_DATE].

## 🎯 Setup Checklist

### ✅ Step 1: Repository Access and Update

**If you already have repository access:**
```bash
cd [YOUR_LOCAL_REPO_PATH]
git pull origin main
```

**If you need initial repository access:**
```bash
git clone [REPOSITORY_URL]
cd [REPO_NAME]
```

**Verify your setup:**
- You should see folders: `course/`, `archived/`, `README.md`, `.gitignore`
- Open `README.md` and confirm you see the DDD course content

### ✅ Step 2: PostgreSQL Database Installation

**macOS (using Homebrew):**
```bash
# Install Homebrew if needed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install PostgreSQL
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Test installation
psql postgres
```

**Windows:**
1. Download PostgreSQL installer from [postgresql.org](https://www.postgresql.org/download/windows/)
2. Run installer with default settings
3. Remember the superuser password you set
4. Add PostgreSQL to your PATH during installation
5. Test: Open Command Prompt and type `psql --version`

**Linux (Ubuntu/Debian):**
```bash
# Update package list
sudo apt update

# Install PostgreSQL
sudo apt install postgresql postgresql-contrib

# Start service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Test installation
sudo -u postgres psql
```

**Troubleshooting:**
- If you encounter permission issues, see `course/tools/postgresql/README.md`
- For connection problems, ensure PostgreSQL service is running
- Contact me if you need help: [PROFESSOR_EMAIL]

### ✅ Step 3: Ruby and Rails Installation

**Check if you already have Ruby:**
```bash
ruby --version
# Should show Ruby 3.0 or higher
```

**If you need to install Ruby:**

**macOS:**
```bash
brew install ruby
gem install rails
```

**Windows:**
1. Download Ruby+Devkit from [rubyinstaller.org](https://rubyinstaller.org/)
2. Run installer and follow prompts
3. Install MSYS2 development toolchain when prompted
4. Open new command prompt and run: `gem install rails`

**Linux:**
```bash
sudo apt install ruby-full build-essential zlib1g-dev
gem install rails
```

**Verify Rails installation:**
```bash
rails --version
# Should show Rails 7.0 or higher
```

### ✅ Step 4: Visual Studio Code Setup

**Download and Install:**
1. Visit [code.visualstudio.com](https://code.visualstudio.com/)
2. Download for your operating system
3. Install with default settings

**Essential Extensions:**
Open VS Code and install these extensions (Ctrl/Cmd + Shift + X):
- Ruby LSP (Shopify.ruby-lsp)
- Rails (bung87.rails)
- PostgreSQL (ms-ossdata.vscode-postgresql)
- Mermaid Preview (bierner.markdown-mermaid)
- GitLens (eamodio.gitlens)

**Verify Setup:**
- Open the course repository folder in VS Code
- You should see syntax highlighting for Ruby files
- Mermaid diagrams should preview correctly

### ✅ Step 5: Mermaid Account and Tools

**Create Free Account:**
1. Visit [mermaid.live](https://mermaid.live/)
2. Sign up for a free account
3. Try creating a simple diagram to test functionality

**Test Mermaid Integration:**
1. In VS Code, open any `.md` file from the course
2. Look for Mermaid diagrams (they start with ```mermaid)
3. Right-click and select "Mermaid: Preview"
4. You should see the diagram rendered

### ✅ Step 6: Node.js (for Rails Asset Pipeline)

**Check if installed:**
```bash
node --version
npm --version
```

**If needed, install from:**
- [nodejs.org](https://nodejs.org/) (download LTS version)
- Or use package manager: `brew install node` (macOS)

## 🧪 Test Your Complete Setup

Run these commands to verify everything works:

```bash
# Test Ruby and Rails
ruby --version
rails --version

# Test PostgreSQL
psql --version

# Test Node.js
node --version

# Test Git
git --version

# Create a test Rails app (optional)
rails new test_app --database=postgresql
cd test_app
rails db:create
```

If all commands work without errors, you're ready!

## 📚 Pre-Reading (Optional but Recommended)

Before our first meetup, consider reading:

1. **Course Overview**: `README.md` in the repository
2. **Twitter Success Story**: `course/blog/twitter-rails-success-story.md`
3. **Getting Started Guide**: `course/docs/GETTING_STARTED.md`
4. **Module 1 Preview**: `course/modules/01-foundations-ddd-database-alignment/README.md`

## 🚨 Common Setup Issues and Solutions

**Ruby Installation Issues:**
- Permission errors: Use a Ruby version manager (rbenv or RVM)
- Old Ruby version: Update using your package manager

**PostgreSQL Connection Issues:**
- Service not running: Start PostgreSQL service
- Permission denied: Check user permissions and authentication settings

**VS Code Extension Issues:**
- Extensions not working: Restart VS Code after installation
- Ruby LSP not activating: Ensure Ruby is in your PATH

**Git Issues:**
- Authentication errors: Set up SSH keys or use personal access tokens
- Repository access: Contact me for permission issues

## 🆘 Getting Help

**If you encounter any issues:**

1. **Check the documentation**: Each tool has detailed setup instructions in `course/tools/`
2. **Search online**: Most setup issues have well-documented solutions
3. **Contact me**: Don't struggle alone!

**Contact Information:**
- Email: [PROFESSOR_EMAIL]
- Office Hours: [OFFICE_HOURS]
- Emergency Contact: [PHONE_NUMBER]
- Response Time: Within 24 hours (faster during business hours)

## 📅 What to Expect at First Meetup

**We'll cover:**
- Quick setup verification and troubleshooting
- Course philosophy and objectives discussion
- Introduction to Domain-Driven Design concepts
- Your first domain modeling exercise
- Overview of the UCF Course Manager project we'll build

**Come prepared with:**
- Your laptop with all tools installed
- Questions about any setup issues you encountered
- Ideas about domains or problems you're interested in solving
- Enthusiasm for learning skills that can change your career!

## 🎯 Final Checklist

Before the first meetup, ensure you can:
- [ ] Access the course repository and see updated content
- [ ] Connect to PostgreSQL database
- [ ] Run basic Ruby and Rails commands
- [ ] Open and edit files in VS Code with proper syntax highlighting
- [ ] View Mermaid diagrams in VS Code or mermaid.live
- [ ] Create a simple Rails application (optional test)

## 💪 You're Building Superpowers

Remember, you're not just installing software - you're setting up the tools that will give you entrepreneurial superpowers. These same tools built Twitter, GitHub, Shopify, and countless other successful platforms.

By the end of this course, you'll have the skills to turn your ideas into working prototypes and potentially into the next big thing.

Looking forward to seeing you ready and excited at our first meetup!

Best regards,

[PROFESSOR_NAME]  
[TITLE]  
[DEPARTMENT]  
[UNIVERSITY]

---

**P.S.** If you finish setup early, browse the blog section for inspiration. The stories there show how the skills you're about to learn have created billion-dollar companies and transformed careers.

---

*Complete this setup checklist and you'll be ready to dive into the exciting world of Domain-Driven Design and full-stack development!*