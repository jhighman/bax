# Windows Development Environment Setup

This guide walks you through setting up a complete Rails development environment on Windows for the UCF Course Manager project.

## Overview

You'll install:
- **Ruby 3.2+** via RubyInstaller
- **Rails 8.x** via gem
- **PostgreSQL 16** database
- **DBeaver** for database GUI
- **Node.js** (for npm and Claude Code)
- **Claude Code** AI coding assistant
- **Git for Windows**
- **VS Code** as your editor
- **Windows Terminal** for a modern command line

---

## Step 1: Install Windows Terminal (Recommended)

Windows Terminal provides a modern, tabbed terminal experience.

1. Open **Microsoft Store**
2. Search for **Windows Terminal**
3. Click **Install**

Or download from: https://aka.ms/terminal

---

## Step 2: Install Git for Windows

Git provides version control and includes Git Bash (a Unix-like terminal).

### Download and Install

1. Go to https://git-scm.com/download/win
2. Download the **64-bit Git for Windows Setup**
3. Run the installer with these recommended options:
   - **Default editor**: Use Visual Studio Code (or your preference)
   - **PATH environment**: Git from the command line and also from 3rd-party software
   - **HTTPS transport**: Use the OpenSSL library
   - **Line ending conversions**: Checkout Windows-style, commit Unix-style
   - **Terminal emulator**: Use Windows' default console window
   - **Default behavior of `git pull`**: Fast-forward or merge
   - **Credential helper**: Git Credential Manager

### Verify Installation

Open Windows Terminal or Command Prompt:

```cmd
git --version
```

Expected output: `git version 2.x.x`

### Configure Git

```cmd
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

---

## Step 3: Install Ruby via RubyInstaller

RubyInstaller is the easiest way to get Ruby on Windows. It includes the MSYS2 development toolkit needed to compile native gems.

### Download and Install

1. Go to https://rubyinstaller.org/downloads/
2. Download **Ruby+Devkit 3.2.x (x64)** - choose the latest 3.2 version
   - Make sure to get the **WITH DEVKIT** version
3. Run the installer:
   - Check **Add Ruby executables to your PATH**
   - Check **Associate .rb and .rbw files with this Ruby installation**
   - Click Install

4. At the end, keep **Run 'ridk install'** checked and click Finish

5. In the MSYS2 installer window that opens:
   - Press **Enter** to select option 3 (MSYS2 and MINGW development toolchain)
   - Wait for installation to complete
   - Press **Enter** again when done

### Verify Installation

Open a **new** terminal window (important - PATH needs to refresh):

```cmd
ruby --version
```

Expected output: `ruby 3.2.x`

```cmd
gem --version
```

Expected output: `3.x.x`

---

## Step 4: Install Rails

With Ruby installed, install Rails via RubyGems.

```cmd
gem install rails
```

This takes a few minutes. When complete, verify:

```cmd
rails --version
```

Expected output: `Rails 8.x.x`

---

## Step 5: Install PostgreSQL

### Download and Install

1. Go to https://www.postgresql.org/download/windows/
2. Click **Download the installer** (EnterpriseDB)
3. Download **PostgreSQL 16.x** for Windows x86-64
4. Run the installer:
   - **Installation Directory**: Keep default (`C:\Program Files\PostgreSQL\16`)
   - **Components**: Select all (especially **pgAdmin 4** and **Command Line Tools**)
   - **Data Directory**: Keep default
   - **Password**: Set a password for the `postgres` superuser - **remember this!**
   - **Port**: Keep default `5432`
   - **Locale**: Keep default
5. Complete the installation (you can skip Stack Builder at the end)

### Add PostgreSQL to PATH

1. Open **System Properties** → **Advanced** → **Environment Variables**
2. Under **System variables**, find **Path** and click **Edit**
3. Click **New** and add: `C:\Program Files\PostgreSQL\16\bin`
4. Click **OK** to save

### Verify Installation

Open a **new** terminal:

```cmd
psql --version
```

Expected output: `psql (PostgreSQL) 16.x`

### Test Connection

```cmd
psql -U postgres -c "SELECT version();"
```

Enter your password when prompted. You should see PostgreSQL version info.

---

## Step 6: Install DBeaver (Database GUI)

DBeaver is a free, modern database tool that works with PostgreSQL and many other databases. It's faster and more intuitive than pgAdmin.

### Download and Install

1. Go to https://dbeaver.io/download/
2. Download **DBeaver Community** for Windows (Windows Installer 64-bit)
3. Run the installer with default options
4. Launch DBeaver

### Connect to PostgreSQL

1. Click **Database** → **New Database Connection** (or the plug icon with a +)
2. Select **PostgreSQL** and click **Next**
3. Enter connection details:
   - Host: `localhost`
   - Port: `5432`
   - Database: `postgres`
   - Username: `postgres`
   - Password: (your PostgreSQL password from Step 5)
4. Click **Test Connection**
   - If prompted to download the PostgreSQL driver, click **Download**
5. Click **Finish**

### Verify Connection

In the Database Navigator panel on the left:
1. Expand your connection → Databases → postgres → Schemas → public → Tables
2. You should see the tables (empty for now, until you run migrations)

### Useful DBeaver Features

- **SQL Editor**: Right-click connection → SQL Editor → New SQL Script
- **View Table Data**: Double-click any table to see its contents
- **ER Diagrams**: Right-click schema → View Diagram (great for visualizing relationships)
- **Export Data**: Right-click table → Export Data (CSV, SQL, Excel, etc.)

---

## Step 7: Install Visual Studio Code

VS Code is an excellent editor for Rails development.

### Download and Install

1. Go to https://code.visualstudio.com/
2. Download for Windows
3. Run installer with default options
   - Check **Add to PATH** option

### Recommended Extensions

Open VS Code and install these extensions (Ctrl+Shift+X):

### Essential: Ruby & Rails

| Extension | Publisher | Why You Need It |
|-----------|-----------|-----------------|
| **Ruby LSP** | Shopify | Code completion, go-to-definition, hover docs, diagnostics. The modern Ruby language server. |
| **Rails** | bung87 | Rails-specific snippets, navigation helpers, and syntax support |
| **ERB Formatter/Beautify** | Ali Ariff | Format `.html.erb` templates consistently |
| **ruby-rubocop** | misogi | Ruby linter integration (optional but recommended) |

### Essential: ERD & Mermaid Diagrams

These are critical for the DDD/ERD portions of this course:

| Extension | Publisher | Why You Need It |
|-----------|-----------|-----------------|
| **Markdown Preview Mermaid Support** | Matt Bierner | Preview Mermaid diagrams (ERDs) directly in VS Code |
| **Mermaid Markdown Syntax Highlighting** | Brian Maendler | Syntax highlighting for Mermaid code blocks |

### Database

| Extension | Publisher | Why You Need It |
|-----------|-----------|-----------------|
| **PostgreSQL** | Chris Kolkman | Query PostgreSQL directly from VS Code, view tables |

### Git & Collaboration

| Extension | Publisher | Why You Need It |
|-----------|-----------|-----------------|
| **GitLens** | GitKraken | See who changed what, line-by-line git blame, history |
| **Git Graph** | mhutchie | Visual git history and branch visualization |

### HTML & Frontend

| Extension | Publisher | Why You Need It |
|-----------|-----------|-----------------|
| **Auto Rename Tag** | Jun Han | Rename paired HTML tags automatically |
| **Auto Close Tag** | Jun Han | Automatically close HTML tags |
| **HTML CSS Support** | ecmel | CSS class autocomplete in HTML/ERB |

### Productivity

| Extension | Publisher | Why You Need It |
|-----------|-----------|-----------------|
| **Path Intellisense** | Christian Kohler | Autocomplete file paths |
| **Bracket Pair Colorizer 2** | CoenraadS | Color-code matching brackets (helps with Ruby blocks) |
| **indent-rainbow** | oderwat | Visualize indentation levels with colors |

### Quick Install Command

You can install all essential extensions at once. Open a terminal and run:

```cmd
code --install-extension Shopify.ruby-lsp
code --install-extension bung87.rails
code --install-extension aliariff.erb-formatter
code --install-extension bierner.markdown-mermaid
code --install-extension bpruitt-goddard.mermaid-markdown-syntax-highlighting
code --install-extension ckolkman.vscode-postgres
code --install-extension eamodio.gitlens
code --install-extension mhutchie.git-graph
code --install-extension formulahendry.auto-rename-tag
code --install-extension formulahendry.auto-close-tag
code --install-extension ecmel.vscode-html-css
code --install-extension christian-kohler.path-intellisense
code --install-extension oderwat.indent-rainbow
```

### Configure VS Code for Ruby/Rails

Create/edit `.vscode/settings.json` in your project:

```json
{
  "editor.formatOnSave": true,
  "editor.tabSize": 2,
  "files.trimTrailingWhitespace": true,
  "files.insertFinalNewline": true,
  "[ruby]": {
    "editor.defaultFormatter": "Shopify.ruby-lsp",
    "editor.tabSize": 2
  },
  "[erb]": {
    "editor.defaultFormatter": "aliariff.erb-formatter",
    "editor.tabSize": 2
  },
  "emmet.includeLanguages": {
    "erb": "html"
  },
  "files.associations": {
    "*.html.erb": "erb"
  }
}
```

### Recommended Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl+P` | Quick open file by name |
| `Ctrl+Shift+P` | Command palette |
| `Ctrl+`` ` | Toggle integrated terminal |
| `Ctrl+B` | Toggle sidebar |
| `Ctrl+Shift+E` | File explorer |
| `Ctrl+Shift+F` | Search across all files |
| `Ctrl+Shift+G` | Git panel |
| `F12` | Go to definition |
| `Ctrl+Click` | Go to definition (alternative) |
| `Shift+F12` | Find all references |
| `Ctrl+Shift+O` | Go to symbol in file |

### Preview Mermaid ERD Diagrams

To preview the ERD diagrams in this course:

1. Open any `.md` file containing a Mermaid diagram (e.g., `artifacts/erd/domain-model.md`)
2. Press `Ctrl+Shift+V` to open Markdown preview
3. The Mermaid diagram will render as a visual ERD

You can also use `Ctrl+K V` to open preview side-by-side with the source.

---

## Step 8: Install Node.js

Node.js is required for Claude Code and provides npm (Node Package Manager).

### Download and Install

1. Go to https://nodejs.org/
2. Download the **LTS** version (Long Term Support) - currently 20.x or higher
3. Run the installer:
   - Accept the license agreement
   - Keep default installation path
   - Keep default components
   - **Important**: Check **Automatically install the necessary tools** if prompted
4. Complete the installation

### Verify Installation

Open a **new** terminal window:

```cmd
node --version
```

Expected output: `v20.x.x` or higher

```cmd
npm --version
```

Expected output: `10.x.x` or higher

---

## Step 9: Install Claude Code

Claude Code is an AI-powered coding assistant that runs in your terminal. It can help you understand code, write features, debug issues, and learn Rails concepts.

### Install via npm

```cmd
npm install -g @anthropic-ai/claude-code
```

### Authenticate

After installation, run Claude Code to authenticate:

```cmd
claude
```

This opens a browser window to sign in with your Anthropic account. Follow the prompts to authorize.

### Verify Installation

```cmd
claude --version
```

### Using Claude Code

Navigate to your project directory and start Claude:

```cmd
cd %USERPROFILE%\Documents\bax
claude
```

**Useful commands inside Claude Code:**
- Just type your question or request naturally
- `/help` - Show available commands
- `/clear` - Clear conversation history
- `Ctrl+C` - Exit Claude Code

**Example prompts for the course:**
- "Explain how the Student model works"
- "What does the `has_many :through` association do?"
- "Help me understand this migration file"
- "Why is my validation failing?"
- "Show me how to add a new field to the Course model"

Claude Code can read your project files and provide context-aware assistance, making it an excellent learning companion.

---

## Step 10: Clone and Setup the Course

### Clone the Repository

```cmd
cd %USERPROFILE%\Documents
git clone https://github.com/jhighman/bax.git
cd bax
```

### Install Bundler

```cmd
gem install bundler
```

### Setup Module 1 (Test Your Environment)

```cmd
cd course\modules\01-rails-foundations\app
bundle install
rails server
```

Open http://localhost:3000 in your browser. You should see the student list.

### Setup Module 2+ (With Database)

```cmd
cd course\modules\02-database-first\app
bundle install
rails db:create db:migrate db:seed
rails server
```

---

## Troubleshooting

### "ruby is not recognized as an internal or external command"

Close and reopen your terminal. If still failing, verify Ruby is in your PATH:

```cmd
echo %PATH%
```

Look for a Ruby path like `C:\Ruby32-x64\bin`

### "pg" gem fails to install

The pg gem needs PostgreSQL development files. Ensure PostgreSQL bin is in PATH:

```cmd
set PATH=%PATH%;C:\Program Files\PostgreSQL\16\bin
gem install pg
```

### "Permission denied" errors

Run terminal as Administrator, or use a directory you own (like Documents).

### Rails server shows "Address already in use"

Another process is using port 3000. Either:
- Stop the other process
- Use a different port: `rails server -p 3001`

### Database connection refused

1. Check PostgreSQL is running:
   - Open **Services** (search in Start menu)
   - Find **postgresql-x64-16**
   - Ensure status is **Running**

2. Verify connection in `config/database.yml`:
   ```yaml
   default: &default
     adapter: postgresql
     encoding: unicode
     pool: 5
     username: postgres
     password: YOUR_PASSWORD
     host: localhost
   ```

### SSL certificate errors with Git

```cmd
git config --global http.sslBackend schannel
```

### "node is not recognized as an internal or external command"

Close and reopen your terminal. If still failing:
1. Reinstall Node.js and ensure **Add to PATH** is checked
2. Or manually add to PATH: `C:\Program Files\nodejs`

### Claude Code authentication issues

If `claude` fails to authenticate:

1. Clear existing credentials:
   ```cmd
   claude logout
   ```

2. Re-authenticate:
   ```cmd
   claude
   ```

3. If browser doesn't open, copy the URL shown in terminal and paste in browser manually

### npm permission errors

If you see EACCES or permission errors with npm:

```cmd
npm config set prefix %APPDATA%\npm
```

Then add `%APPDATA%\npm` to your PATH.

---

## Environment Summary

After completing this guide, you should have:

| Tool | Version | Verify Command |
|------|---------|----------------|
| Git | 2.x | `git --version` |
| Ruby | 3.2.x | `ruby --version` |
| Rails | 8.x | `rails --version` |
| PostgreSQL | 16.x | `psql --version` |
| Bundler | 2.x | `bundler --version` |
| Node.js | 20.x | `node --version` |
| npm | 10.x | `npm --version` |
| Claude Code | latest | `claude --version` |

---

## Quick Reference Commands

```cmd
# Start PostgreSQL service (if stopped)
net start postgresql-x64-16

# Connect to PostgreSQL
psql -U postgres

# Create a new Rails app
rails new myapp --database=postgresql

# Bundle install dependencies
bundle install

# Database commands
rails db:create
rails db:migrate
rails db:seed

# Start Rails server
rails server

# Rails console
rails console

# Start Claude Code in your project
cd path\to\project
claude

# Update Claude Code
npm update -g @anthropic-ai/claude-code
```

---

## Next Steps

1. Read through **Module 1: Rails Foundations** in `course/modules/01-rails-foundations/`
2. Follow the INSTRUCTIONS.md to understand the app structure
3. Start Claude Code in the project folder: `claude`
4. Ask Claude to explain code as you explore
5. Make changes, see results live

**Pro tip**: Keep Claude Code running in a separate terminal while you work. It can explain Rails concepts, debug errors, and help you understand the codebase as you progress through the modules.

Welcome to Rails development!
