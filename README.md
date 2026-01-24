# Introduction to Domain-Driven Design: Building Applications Around Database Concepts

A supplementary course for UCF students focusing on Domain-Driven Design principles integrated with database-centric application development.

## Course Overview

This course serves as a supplementary module to existing database concepts taught at the University of Central Florida (UCF), with a focus on conceiving applications around database design. It integrates Entity-Relationship (ER) modeling, emphasizes the Software Development Life Cycle (SDLC), uses Ruby on Rails for full-stack development examples, and draws on Martin Fowler's analysis patterns for data modeling concepts.

### Target Audience
UCF students in computer science, information systems, or related fields who have completed introductory database courses. Assumes familiarity with SQL, basic ER modeling, and introductory programming (Ruby knowledge is not required but will be introduced).

### Duration and Format
8-12 weeks as a supplementary module, with 2-3 hours per week. Format: Online/self-paced with optional in-person workshops. Includes video lectures, reading assignments, coding exercises, and a capstone project.

## Key Objectives

- Understand DDD as a framework for aligning software design with business domains, starting from database-centric perspectives
- Learn to use ER modeling to capture domain concepts, enriched with Information Engineering (IE) semantics and Fowler's patterns
- Apply DDD principles throughout the SDLC, from requirements gathering to deployment
- Build full-stack prototypes in Ruby on Rails, demonstrating how domain models integrate with databases
- Develop skills in collaborative domain modeling and iterative refinement

## Learning Outcomes

Upon completion, students will be able to:
- Create ER diagrams that reflect domain truths and evolve them into DDD-inspired models
- Implement domain logic in Rails while maintaining database integrity
- Appreciate how IE descriptors enhance relational designs without overcomplicating OO concepts

## Course Structure

The course builds the **UCF Course Manager** application incrementally across 10 modules. Each module contains a working Rails app with Bootstrap that you can run immediately.

1. **[Rails Foundations for Java/C Developers](course/modules/01-rails-foundations/)**
   - First working Rails app with Bootstrap
   - Ruby basics for Java/C developers, MVC pattern

2. **[Database-First Development](course/modules/02-database-first/)**
   - PostgreSQL connection, first migration
   - Active Record ORM, CRUD operations

3. **[ER Modeling to Rails Models](course/modules/03-er-to-rails/)**
   - Add Course entity, Mermaid ERD diagrams
   - Translate ER concepts to Rails models

4. **[The Enrollment Aggregate](course/modules/04-enrollment-aggregate/)**
   - Many-to-many relationships with business logic
   - Join models, use case documentation

5. **[Descriptors (Composite Attributes)](course/modules/05-descriptors/)**
   - Address, PersonName as structured data
   - IE terminology for Value Objects

6. **[Domain Services](course/modules/06-domain-services/)**
   - Extract business logic into service objects
   - Single Responsibility Principle

7. **[Bounded Contexts](course/modules/07-bounded-contexts/)**
   - Separate Academic from Administrative concerns
   - Sprint planning introduction

8. **[Faculty and Scheduling](course/modules/08-faculty-scheduling/)**
   - Faculty entity, course scheduling
   - Fowler's Party pattern basics

9. **[Authentication & Authorization](course/modules/09-authentication/)**
   - User accounts, role-based access
   - Devise authentication

10. **[Capstone - Complete Application](course/modules/10-capstone/)**
    - Production-ready UCF Course Manager
    - Deployment and documentation

## Repository Structure

```
├── archived/                    # Archived content (original modules, Java examples)
├── course/
│   ├── modules/                # Course modules (01-10), each with working Rails app
│   │   ├── 01-rails-foundations/
│   │   │   ├── README.md       # Learning content
│   │   │   ├── INSTRUCTIONS.md # Step-by-step build guide
│   │   │   └── app/            # Working Rails application
│   │   ├── 02-database-first/
│   │   ├── ...
│   │   └── 10-capstone/
│   ├── docs/                   # Setup guides and documentation
│   ├── tools/                  # Mermaid, VS Code, PostgreSQL guides
│   ├── blog/                   # Career inspiration articles
│   └── correspondence/         # Email templates for instructors
└── README.md                   # This file
```

## Getting Started

### For Students

1. **Set up your environment:**
   - Windows: Follow the [Windows Setup Guide](course/docs/WINDOWS_SETUP.md)
   - macOS: Install Ruby 3.2+, Rails 8+, PostgreSQL 16+
2. Clone this repository: `git clone https://github.com/jhighman/bax.git`
3. Start with [Module 1: Rails Foundations](course/modules/01-rails-foundations/)
4. Each module has a `README.md` (concepts) and `INSTRUCTIONS.md` (step-by-step)
5. Run the app in each module's `app/` folder: `bundle install && rails server`

### For Instructors

1. Review the [Course Writing Guide](course/docs/COURSE_WRITING_GUIDE.md) for content guidelines
2. See [Module Template](course/docs/MODULE_TEMPLATE.md) for creating new content
3. Email templates available in [Correspondence](course/correspondence/)

## Key Terminology

This course uses Information Engineering (IE) terminology to maintain alignment with database concepts:

- **Entity Types** instead of DDD Entities
- **Descriptors** or **Composite Descriptors** instead of Value Objects
- **Structured Attributes** for immutable, attribute-based structures
- **Attribute Clusters** for grouped related attributes

## Prerequisites

- Completion of introductory database course (e.g., CIS 3360 at UCF)
- Basic understanding of SQL and ER diagrams
- Introductory programming experience (any language)
- Familiarity with basic software development concepts

## Technology Stack

- **Backend Development**: Ruby on Rails 8+
- **Database**: PostgreSQL 16+
- **Frontend**: Bootstrap 5
- **Database Modeling**: Mermaid ERD diagrams
- **Version Control**: Git
- **AI Assistant**: Claude Code
- **Testing**: RSpec (introduced in Module 4)
- **IDE**: VS Code with Ruby/Rails extensions

## Development Tools

The course utilizes three primary development tools that can be studied as independent learning domains:

### 🎨 [Mermaid: Diagram as Code](course/tools/mermaid/)
Create Entity-Relationship diagrams, flowcharts, and system architecture diagrams using text-based syntax. Essential for visual domain modeling and documentation that evolves with your code.

**Key Features:**
- Version-controlled diagrams
- ER diagrams with IE notation
- Bounded context mapping
- Integration with GitHub and VS Code

### 💻 [Visual Studio Code: IDE for DDD Development](course/tools/vscode/)
Comprehensive development environment with excellent Ruby on Rails support, database integration, and collaborative features for domain modeling sessions.

**Key Features:**
- Ruby and Rails IntelliSense
- Database query and management tools
- Git integration and Live Share
- Extensive extension ecosystem

### 🗄️ [PostgreSQL: Advanced Database Management](course/tools/postgresql/)
Production-grade database system with advanced features that align perfectly with Domain-Driven Design principles and Information Engineering methodology.

**Key Features:**
- JSON/JSONB for complex descriptors
- Custom data types and constraints
- Triggers for domain rule enforcement
- Advanced indexing and performance optimization

**[📚 Complete Tools Guide](course/tools/)** - Comprehensive documentation for all development tools with setup instructions, best practices, and advanced usage patterns.

## Professor's Blog: Career Inspiration and Real-World Impact

Beyond technical skills, this course is about understanding the power you wield as a full-stack domain expert. The blog section features inspirational articles about how these skills translate into career opportunities and entrepreneurial success.

### 🚀 Featured Articles

**[From Dorm Room to Global Platform: The Twitter Story](course/blog/twitter-rails-success-story.md)**
How a simple Rails application built on solid domain principles became one of the world's most influential platforms. Learn why Twitter's domain-first approach and full-stack thinking enabled rapid iteration and massive scale.

**[The Entrepreneur's Secret Weapon: Full-Stack Domain Mastery](course/blog/full-stack-entrepreneur-advantage.md)**
Why understanding the complete technology stack gives you superpowers in the startup world. Discover how mastering both domain modeling and technical implementation allows you to validate ideas quickly and communicate effectively with all stakeholders.

### 💡 Why This Matters for Your Career

When you master Domain-Driven Design, Rails, and PostgreSQL, you're not just learning to code—you're developing the ability to:

- **Think like an entrepreneur**: Understanding domains means understanding business problems
- **Build like an architect**: Database design skills create scalable foundations
- **Execute like a full-stack developer**: Rails proficiency means rapid prototyping and deployment
- **Communicate like a business analyst**: Domain modeling bridges technical and business teams

You become more than a specialist—you become a **digital polymath** capable of taking an idea from conception to production.

**[📝 Read All Blog Articles](course/blog/)** - Explore stories of how DDD and Rails skills have launched careers, created startups, and solved real-world problems.

## Email Templates for Course Communication

Streamline your course communication with ready-to-use email templates that you can copy, customize, and send to students.

### 📧 [Course Correspondence Templates](course/correspondence/)

**[Course Invitation Email](course/correspondence/course-invitation.md)**
Perfect for students with Git access who need to pull repository updates and prepare for the first meetup. Includes setup reminders and course overview.

**[Pre-Course Setup Email](course/correspondence/pre-course-setup.md)**
Detailed technical setup instructions for PostgreSQL, Ruby/Rails, VS Code, and Mermaid. Includes troubleshooting tips and verification steps.

**[Weekly Module Announcements](course/correspondence/weekly-module.md)**
Template for announcing new modules, assignments, and weekly progress. Maintains student engagement and provides clear expectations.

### ✨ Key Features
- **Copy-and-paste ready**: Just customize the bracketed placeholders
- **Professional tone**: Maintains academic standards while inspiring students
- **Technical accuracy**: Includes correct setup instructions and commands
- **Career focus**: Emphasizes entrepreneurial opportunities and real-world applications
- **Troubleshooting support**: Anticipates common issues and provides solutions

---

*This course builds on your existing database knowledge by showing how ER models drive application architecture and domain-driven design principles.*