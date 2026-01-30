# Module 2: Database-First Development

In this module, we connect to PostgreSQL and transform our in-memory Student class into a database-backed Active Record model. You'll learn how Rails manages database schemas through migrations and how to perform CRUD operations.

## Learning Objectives

By the end of this module, you will be able to:

1. Understand Active Record as Rails' ORM (Object-Relational Mapping)
2. Write database migrations to create and modify tables
3. Use the Rails console to interact with database records
4. Implement full CRUD operations with forms
5. Understand seeds for populating development data

## DDD Concept: Entities Have Identity

In Domain-Driven Design, an **Entity** is distinguished by its identity, not its attributes. A Student with ID `1` is the same student even if they change their name, email, or major.

```ruby
# The primary key (id) provides stable identity
student = Student.find(1)
student.first_name = "New Name"
student.email = "new.email@ucf.edu"
student.save  # Still the same student - identity preserved!

# Two students with the same name are NOT the same entity
alice1 = Student.create(first_name: "Alice", last_name: "Smith", student_number: "UCF001")
alice2 = Student.create(first_name: "Alice", last_name: "Smith", student_number: "UCF002")
alice1 == alice2  # false - different identities (different IDs)
```

This is why databases use primary keys - they provide stable identity across time.

---

## Active Record: The Bridge Between Ruby and SQL

### What is Active Record?

Active Record is Rails' implementation of the Active Record pattern - each model class corresponds to a database table, and each instance corresponds to a row.

```
┌─────────────────────────────────────────────────────────────┐
│                     Ruby World                               │
│                                                              │
│   student = Student.find(1)                                 │
│   student.first_name = "Alice"                              │
│   student.save                                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ Active Record translates
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     SQL World                                │
│                                                              │
│   SELECT * FROM students WHERE id = 1;                      │
│   UPDATE students SET first_name = 'Alice' WHERE id = 1;    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Java/C Bridge: Active Record vs JDBC/JPA

**Java JDBC (manual):**
```java
String sql = "SELECT * FROM students WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setInt(1, studentId);
ResultSet rs = stmt.executeQuery();

Student student = new Student();
student.setId(rs.getLong("id"));
student.setFirstName(rs.getString("first_name"));
student.setLastName(rs.getString("last_name"));
// ... manual mapping for every field
```

**Java JPA/Hibernate:**
```java
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    // ... annotations for every field
}

// Usage
Student student = entityManager.find(Student.class, 1L);
```

**Rails Active Record:**
```ruby
class Student < ApplicationRecord
  # That's it! No annotations, no mapping configuration
end

# Usage
student = Student.find(1)
```

Active Record figures out the column names from the database schema automatically.

---

## Migrations: Version Control for Your Database

### What are Migrations?

Migrations are Ruby files that describe changes to your database schema. They're like version control for your database structure.

```ruby
# db/migrate/20260124184511_create_students.rb
class CreateStudents < ActiveRecord::Migration[8.1]
  def change
    create_table :students do |t|
      t.string :student_number, null: false
      t.string :first_name
      t.string :last_name
      t.string :email
      t.string :major
      t.timestamps  # created_at and updated_at
    end

    add_index :students, :student_number, unique: true
    add_index :students, :email
  end
end
```

### Java/C Bridge: Migrations vs SQL Scripts

**Traditional SQL:**
```sql
-- V1__create_students.sql (Flyway)
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    student_number VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    major VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE INDEX idx_students_email ON students(email);
```

**Rails Migration:**
```ruby
create_table :students do |t|
  t.string :student_number, null: false
  t.string :first_name
  t.string :last_name
  t.string :email
  t.string :major
  t.timestamps
end
add_index :students, :student_number, unique: true
add_index :students, :email
```

Rails migrations are:
- Database-agnostic (same migration works for PostgreSQL, MySQL, SQLite)
- Reversible (can roll back changes)
- Timestamped (run in order, tracked in schema_migrations table)

---

## CRUD Operations

### Create

```ruby
# In controller
@student = Student.new(student_params)
if @student.save
  redirect_to @student, notice: "Created!"
else
  render :new  # Show form with errors
end

# What happens:
# 1. Student.new creates object in memory
# 2. Validations run
# 3. If valid, INSERT INTO students (...) VALUES (...)
# 4. student.id is populated from database
```

### Read

```ruby
# Find by primary key
Student.find(1)           # Raises error if not found
Student.find_by(id: 1)    # Returns nil if not found

# Query methods
Student.all                                    # All students
Student.where(major: "Computer Science")       # Filter
Student.order(:last_name)                      # Sort
Student.limit(10)                              # Limit results
Student.where(major: "CS").order(:last_name)   # Chain methods!
```

### Update

```ruby
@student = Student.find(params[:id])
if @student.update(student_params)
  redirect_to @student, notice: "Updated!"
else
  render :edit
end

# SQL: UPDATE students SET ... WHERE id = ?
```

### Delete

```ruby
@student = Student.find(params[:id])
@student.destroy
redirect_to students_path, notice: "Deleted!"

# SQL: DELETE FROM students WHERE id = ?
```

---

## Seeds: Sample Data for Development

The `db/seeds.rb` file populates your database with initial data:

```ruby
# db/seeds.rb
students_data = [
  { student_number: "UCF001", first_name: "Alice", last_name: "Johnson", ... },
  { student_number: "UCF002", first_name: "Bob", last_name: "Smith", ... },
]

students_data.each do |attrs|
  Student.find_or_create_by!(student_number: attrs[:student_number]) do |s|
    s.first_name = attrs[:first_name]
    s.last_name = attrs[:last_name]
    # ...
  end
end
```

Run with: `bin/rails db:seed`

---

## Strong Parameters: Security

Rails requires you to whitelist form parameters to prevent mass assignment attacks:

```ruby
# In controller
def student_params
  params.require(:student).permit(:student_number, :first_name, :last_name, :email, :major)
end

# Only these fields can be set from form data
# If someone tries to inject admin: true, it's ignored
```

**Java equivalent:** Using DTOs or `@ModelAttribute` with specific fields

---

## App State After This Module

- PostgreSQL stores student data persistently
- Full CRUD: Create, Read, Update, Delete operations
- Bootstrap forms with validation error display
- Flash messages for user feedback
- Data survives server restarts

---

## Exercises

### Exercise 1: Rails Console Exploration

```bash
bin/rails console
```

Try these commands:
```ruby
# Create
Student.create(student_number: "TEST001", first_name: "Test", last_name: "User")

# Read
Student.all
Student.find(1)
Student.where(major: "Computer Science")
Student.count

# Update
s = Student.last
s.update(major: "Data Science")

# Delete
Student.last.destroy
```

### Exercise 2: Add a New Field

Add a `phone_number` field to Student:

1. Generate migration: `bin/rails generate migration AddPhoneNumberToStudents phone_number:string`
2. Run migration: `bin/rails db:migrate`
3. Update the form partial to include the new field
4. Update strong parameters in the controller
5. Update views to display the phone number

### Exercise 3: Query Challenges

Using the Rails console, write queries to:
1. Find all students majoring in "Computer Science"
2. Count students by major
3. Find students whose email contains "ucf.edu"
4. List students alphabetically by last name

---

## What's Next?

In **Module 3: ER Modeling to Rails Models**, we'll:
- Add the Course entity
- Create relationships between Students and Courses
- Build your first ER diagram with Mermaid
- Learn about `has_many` and `belongs_to` associations

---

## Glossary

| Term | Definition |
|------|------------|
| **Active Record** | Rails' ORM that maps classes to database tables |
| **Migration** | Ruby file describing a database schema change |
| **CRUD** | Create, Read, Update, Delete - basic data operations |
| **Seeds** | Sample data loaded into the database for development |
| **Strong Parameters** | Security feature that whitelists form fields |
| **Primary Key** | Unique identifier for a database row (usually `id`) |

---

## Commands Reference

```bash
# Database commands
bin/rails db:create      # Create database
bin/rails db:migrate     # Run pending migrations
bin/rails db:rollback    # Undo last migration
bin/rails db:seed        # Load seed data
bin/rails db:reset       # Drop, create, migrate, seed

# Generate migration
bin/rails generate migration CreateStudents name:string email:string
bin/rails generate migration AddPhoneToStudents phone:string

# Console
bin/rails console        # Interactive Ruby with your app loaded
```
