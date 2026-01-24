# Module 1: Build Instructions

This document walks you through building the Module 1 application from scratch. If you want to understand how every piece was created, follow along. If you just want to run the finished app, skip to [Running the Completed App](#running-the-completed-app).

## Prerequisites

Before starting, ensure you have:

- Ruby 3.2+ installed (`ruby --version`)
- Rails 7+ installed (`rails --version`)
- PostgreSQL running (`psql --version`)
- A text editor (VS Code recommended)

---

## Part 1: Create the Rails Application

### Step 1.1: Generate a New Rails App

```bash
# Navigate to where you want to create the project
cd course/modules/01-rails-foundations

# Generate a new Rails app with PostgreSQL and Bootstrap
rails new app --database=postgresql --css=bootstrap --skip-git
```

**What this does:**
- `rails new app` creates a new Rails application in the `app/` folder
- `--database=postgresql` configures PostgreSQL instead of SQLite
- `--css=bootstrap` adds Bootstrap CSS framework
- `--skip-git` skips Git initialization (we manage Git at the course level)

**Java equivalent:** This is like running `spring init` with Spring Initializr.

### Step 1.2: Create the Database

```bash
cd app
bin/rails db:create
```

This creates two databases:
- `app_development` - for local development
- `app_test` - for running tests

---

## Part 2: Create the Student Model

### Step 2.1: Create the Model File

Create `app/app/models/student.rb`:

```ruby
class Student
  attr_accessor :id, :student_number, :first_name, :last_name, :email, :major

  @@students = []
  @@next_id = 1

  def initialize(attributes = {})
    @id = attributes[:id] || (@@next_id += 1) - 1
    @student_number = attributes[:student_number]
    @first_name = attributes[:first_name]
    @last_name = attributes[:last_name]
    @email = attributes[:email]
    @major = attributes[:major]
  end

  def full_name
    "#{first_name} #{last_name}"
  end

  def self.all
    @@students
  end

  def self.find(id)
    @@students.find { |s| s.id == id.to_i }
  end

  def self.create(attributes)
    student = new(attributes)
    @@students << student
    student
  end

  def self.count
    @@students.length
  end

  def self.seed_sample_data
    return if @@students.any?

    create(student_number: "UCF001", first_name: "Alice", last_name: "Johnson",
           email: "alice.johnson@ucf.edu", major: "Computer Science")
    create(student_number: "UCF002", first_name: "Bob", last_name: "Smith",
           email: "bob.smith@ucf.edu", major: "Information Technology")
    create(student_number: "UCF003", first_name: "Carol", last_name: "Williams",
           email: "carol.williams@ucf.edu", major: "Computer Science")
    create(student_number: "UCF004", first_name: "David", last_name: "Brown",
           email: "david.brown@ucf.edu", major: "Software Engineering")
    create(student_number: "UCF005", first_name: "Eva", last_name: "Martinez",
           email: "eva.martinez@ucf.edu", major: "Data Science")
  end
end

Student.seed_sample_data
```

**Key concepts:**
- `attr_accessor` creates getter/setter methods automatically
- `@@students` is a class variable (shared across all instances)
- `@first_name` is an instance variable (unique to each instance)
- `self.all` is a class method (like `static` in Java)

---

## Part 3: Create the Controller

### Step 3.1: Create the Controller File

Create `app/app/controllers/students_controller.rb`:

```ruby
class StudentsController < ApplicationController
  def index
    @students = Student.all
  end

  def show
    @student = Student.find(params[:id])

    if @student.nil?
      redirect_to students_path, alert: "Student not found"
    end
  end
end
```

**What this does:**
- `index` action retrieves all students and makes them available to the view
- `show` action finds a single student by ID from the URL
- `@students` and `@student` are instance variables passed to views
- `params[:id]` extracts the ID from the URL (e.g., `/students/1`)

---

## Part 4: Configure Routes

### Step 4.1: Update Routes

Edit `app/config/routes.rb`:

```ruby
Rails.application.routes.draw do
  resources :students, only: [:index, :show]
  root "students#index"
  get "up" => "rails/health#show", as: :rails_health_check
end
```

**What this creates:**
- `GET /students` → `students#index`
- `GET /students/:id` → `students#show`
- `GET /` → `students#index` (homepage)

**Verify routes:**
```bash
bin/rails routes | grep student
```

---

## Part 5: Create Views

### Step 5.1: Update the Layout

Edit `app/app/views/layouts/application.html.erb` to add Bootstrap navigation:

```erb
<!DOCTYPE html>
<html>
  <head>
    <title>UCF Course Manager</title>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <%= csrf_meta_tags %>
    <%= csp_meta_tag %>
    <%= stylesheet_link_tag :app, "data-turbo-track": "reload" %>
    <%= javascript_importmap_tags %>
  </head>

  <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
      <div class="container">
        <%= link_to "UCF Course Manager", root_path, class: "navbar-brand" %>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav">
            <li class="nav-item">
              <%= link_to "Students", students_path, class: "nav-link" %>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container">
      <%= yield %>
    </main>

    <footer class="container mt-5 py-3 border-top text-muted">
      <p class="text-center">UCF Course Manager - Module 1</p>
    </footer>
  </body>
</html>
```

### Step 5.2: Create the Index View

Create `app/app/views/students/index.html.erb`:

```erb
<h1>Students</h1>

<table class="table table-striped">
  <thead class="table-dark">
    <tr>
      <th>Student Number</th>
      <th>Name</th>
      <th>Email</th>
      <th>Major</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
    <% @students.each do |student| %>
      <tr>
        <td><%= student.student_number %></td>
        <td><%= student.full_name %></td>
        <td><%= student.email %></td>
        <td><%= student.major %></td>
        <td>
          <%= link_to "View", student_path(student.id), class: "btn btn-sm btn-primary" %>
        </td>
      </tr>
    <% end %>
  </tbody>
</table>
```

### Step 5.3: Create the Show View

Create `app/app/views/students/show.html.erb`:

```erb
<h1><%= @student.full_name %></h1>

<div class="card">
  <div class="card-body">
    <dl class="row">
      <dt class="col-sm-3">Student Number</dt>
      <dd class="col-sm-9"><%= @student.student_number %></dd>

      <dt class="col-sm-3">Email</dt>
      <dd class="col-sm-9"><%= @student.email %></dd>

      <dt class="col-sm-3">Major</dt>
      <dd class="col-sm-9"><%= @student.major %></dd>
    </dl>
  </div>
</div>

<%= link_to "Back to Students", students_path, class: "btn btn-secondary mt-3" %>
```

---

## Running the Completed App

If you're using the pre-built app:

```bash
# Navigate to the app directory
cd course/modules/01-rails-foundations/app

# Install dependencies
bundle install

# Create the database
bin/rails db:create

# Start the server
bin/rails server
```

Open http://localhost:3000 in your browser.

---

## Troubleshooting

### "Database does not exist"

```bash
bin/rails db:create
```

### "Could not find gem"

```bash
bundle install
```

### Port 3000 already in use

```bash
# Find what's using port 3000
lsof -i :3000

# Kill it, or use a different port
bin/rails server -p 3001
```

### PostgreSQL not running

```bash
# macOS with Homebrew
brew services start postgresql@16
```

---

## Verification Checklist

After completing this module, verify:

- [ ] `bin/rails server` starts without errors
- [ ] http://localhost:3000 shows the student list
- [ ] Clicking "View" shows student details
- [ ] Navigation links work
- [ ] You understand the relationship between routes → controller → view

---

## Next Steps

Proceed to **Module 2: Database-First Development** where you'll:
- Replace in-memory storage with PostgreSQL
- Write your first database migration
- Use Active Record instead of plain Ruby classes
