# Getting Started with the DDD Course

This guide will help you set up your development environment and get started with the Domain-Driven Design course materials.

## Prerequisites

Before beginning this course, ensure you have:

### Academic Prerequisites
- Completed an introductory database course (e.g., CIS 3360 at UCF)
- Basic understanding of SQL and Entity-Relationship diagrams
- Introductory programming experience (any language)
- Familiarity with basic software development concepts

### Technical Prerequisites
- Computer with macOS, Windows, or Linux
- Reliable internet connection for downloading tools and resources
- Text editor or IDE (VS Code recommended)
- Git for version control

## Development Environment Setup

### 1. Install Ruby

#### macOS (using Homebrew)
```bash
# Install Homebrew if not already installed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Ruby
brew install ruby
```

#### Windows (using RubyInstaller)
1. Download Ruby+Devkit from [RubyInstaller.org](https://rubyinstaller.org/)
2. Run the installer and follow the prompts
3. Install the MSYS2 development toolchain when prompted

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install ruby-full build-essential zlib1g-dev
```

#### Verify Installation
```bash
ruby --version
# Should show Ruby 3.0 or higher
```

### 2. Install Rails

```bash
gem install rails
rails --version
# Should show Rails 7.0 or higher
```

### 3. Install Database Systems

#### PostgreSQL (Recommended for production-like development)
- **macOS**: `brew install postgresql`
- **Windows**: Download from [PostgreSQL.org](https://www.postgresql.org/download/windows/)
- **Linux**: `sudo apt install postgresql postgresql-contrib`

#### SQLite3 (For development and testing)
Usually included with Ruby installation. Verify with:
```bash
sqlite3 --version
```

### 4. Install Git
- **macOS**: `brew install git` or use Xcode Command Line Tools
- **Windows**: Download from [git-scm.com](https://git-scm.com/download/win)
- **Linux**: `sudo apt install git`

### 5. Install Node.js (for Rails asset pipeline)
- Download from [nodejs.org](https://nodejs.org/) or use package manager
- Verify: `node --version` and `npm --version`

### 6. Code Editor Setup

#### VS Code (Recommended)
1. Download from [code.visualstudio.com](https://code.visualstudio.com/)
2. Install recommended extensions:
   - Ruby
   - Rails
   - SQLite Viewer
   - GitLens
   - Markdown All in One

## Course Repository Setup

### 1. Clone or Download Course Materials
```bash
# If using Git (recommended)
git clone [repository-url]
cd bax

# Or download and extract ZIP file
```

### 2. Explore Repository Structure
```
bax/
├── archived/                    # Previous Java content (preserved)
├── course/
│   ├── modules/                # Course modules (01-10)
│   │   ├── 01-foundations-ddd-database-alignment/
│   │   ├── 02-er-modeling-ie-semantics/
│   │   └── ... (additional modules)
│   ├── docs/                   # Course documentation
│   │   ├── COURSE_WRITING_GUIDE.md
│   │   ├── MODULE_TEMPLATE.md
│   │   └── GETTING_STARTED.md (this file)
│   ├── examples/               # Code examples
│   │   └── ucf-course-manager/ # Main Rails example
│   ├── resources/              # Additional resources
│   │   └── TERMINOLOGY_GUIDE.md
│   └── assessments/            # Quizzes and assignments
├── README.md                   # Course overview
└── .gitignore                  # Git ignore rules
```

### 3. Set Up Rails Example Application

Navigate to the examples directory:
```bash
cd course/examples/ucf-course-manager
```

Create a new Rails application (this will be done in Module 1):
```bash
rails new . --database=postgresql --skip-git
# Or for SQLite: rails new . --database=sqlite3 --skip-git
```

## Learning Path

### Week 1: Foundations
1. Read the [Course Overview](../README.md)
2. Review the [Terminology Guide](../resources/TERMINOLOGY_GUIDE.md)
3. Complete [Module 1: Foundations of DDD and Database Alignment](../modules/01-foundations-ddd-database-alignment/)

### Week 2: ER Modeling
1. Complete Module 2: ER Modeling with IE Semantics
2. Practice with ER diagram tools (Lucidchart, Draw.io)
3. Begin Rails application setup

### Weeks 3-10: Progressive Learning
Follow the module sequence, building knowledge incrementally:
- Each module builds on previous concepts
- Complete exercises before moving to the next module
- Participate in discussion forums
- Work on the capstone project throughout the course

## Study Tips

### 1. Hands-On Practice
- Don't just read the materials - implement the examples
- Experiment with variations of the provided code
- Create your own domain models for practice

### 2. Use the Terminology Guide
- Refer to the [Terminology Guide](../resources/TERMINOLOGY_GUIDE.md) frequently
- Practice using IE terminology instead of traditional OO terms
- Build your domain vocabulary gradually

### 3. Connect to Database Knowledge
- Always think about how concepts map to database structures
- Draw ER diagrams before writing code
- Consider normalization and performance implications

### 4. Collaborate and Discuss
- Join study groups for domain modeling sessions
- Discuss concepts with classmates
- Ask questions in course forums

### 5. Build Incrementally
- Start with simple models and add complexity gradually
- Test your understanding with small examples
- Don't try to build everything at once

## Common Setup Issues and Solutions

### Ruby Installation Issues
**Problem**: Permission errors when installing gems
**Solution**: Use a Ruby version manager (rbenv or RVM) instead of system Ruby

**Problem**: Old Ruby version
**Solution**: Update Ruby using your package manager or version manager

### Rails Installation Issues
**Problem**: Rails command not found
**Solution**: Ensure Ruby's bin directory is in your PATH

**Problem**: Bundle install fails
**Solution**: Install development tools (build-essential on Linux, Xcode tools on macOS)

### Database Connection Issues
**Problem**: PostgreSQL connection refused
**Solution**: Start PostgreSQL service and create database user

**Problem**: SQLite3 gem installation fails
**Solution**: Install SQLite3 development headers

### Git Issues
**Problem**: Authentication errors
**Solution**: Set up SSH keys or use personal access tokens

## Getting Help

### Course Resources
- Review module materials and examples
- Check the [Terminology Guide](../resources/TERMINOLOGY_GUIDE.md)
- Refer to the [Course Writing Guide](COURSE_WRITING_GUIDE.md) for context

### External Resources
- [Rails Guides](https://guides.rubyonrails.org/) - Official Rails documentation
- [Ruby Documentation](https://ruby-doc.org/) - Ruby language reference
- [PostgreSQL Documentation](https://www.postgresql.org/docs/) - Database reference

### Community Support
- Course discussion forums
- Study group sessions
- Office hours with instructors
- UCF Computer Science tutoring resources

## Assessment Overview

### Module Assessments (80% of grade)
- Knowledge check quizzes (20%)
- Practical exercises (40%)
- Discussion participation (20%)

### Capstone Project (20% of grade)
- Domain analysis and ER modeling
- Rails application implementation
- Documentation and presentation

### Grading Scale
- A: 90-100%
- B: 80-89%
- C: 70-79%
- D: 60-69%
- F: Below 60%

## Next Steps

1. Complete the development environment setup
2. Read through the [Course Overview](../README.md)
3. Begin [Module 1](../modules/01-foundations-ddd-database-alignment/)
4. Join the course discussion forum
5. Schedule time for regular study and practice

## Course Schedule Template

| Week | Module | Topics | Deliverables |
|------|--------|--------|--------------|
| 1 | Module 1 | DDD Foundations, Database Alignment | Quiz, ER Diagram Exercise |
| 2 | Module 2 | ER Modeling with IE Semantics | Rails Models, Domain Glossary |
| 3 | Module 3 | Bounded Contexts | Context Mapping Exercise |
| 4 | Module 4 | Entities and Descriptors | Composite Descriptor Implementation |
| 5 | Module 5 | Aggregates and Consistency | Aggregate Design Exercise |
| 6 | Module 6 | Repositories and Services | Service Layer Implementation |
| 7 | Module 7 | Context Mapping | Strategic Design Exercise |
| 8 | Module 8 | Fowler's Patterns | Pattern Implementation |
| 9 | Module 9 | DDD in SDLC | Full Cycle Exercise |
| 10 | Module 10 | Capstone | Final Project Presentation |

---

*Welcome to the Domain-Driven Design course! Remember that learning DDD is a journey - focus on understanding the principles and how they apply to database-driven application development.*