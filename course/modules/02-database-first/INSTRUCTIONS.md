# Module 2: Build Instructions

This document walks you through upgrading the Module 1 application to use PostgreSQL database storage. Follow along to understand each change, or skip to [Running the Completed App](#running-the-completed-app) to use the pre-built version.

## Prerequisites

- Completed Module 1
- PostgreSQL running (`brew services list | grep postgresql`)
- Ruby 3.2+ and Rails 8+

---

## Part 1: Database Migration

### Step 1.1: Generate the Migration

```bash
cd course/modules/02-database-first/app

# Generate a migration to create the students table
bin/rails generate migration CreateStudents \
  student_number:string:uniq \
  first_name:string \
  last_name:string \
  email:string \
  major:string
```

This creates a file in `db/migrate/` with a timestamp prefix.

### Step 1.2: Enhance the Migration

Edit the generated migration to add constraints:

```ruby
# db/migrate/YYYYMMDDHHMMSS_create_students.rb
class CreateStudents < ActiveRecord::Migration[8.1]
  def change
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
  end
end
```

**Key points:**
- `null: false` - database-level NOT NULL constraint
- `add_index :students, :student_number, unique: true` - ensures uniqueness at database level
- `t.timestamps` - adds `created_at` and `updated_at` columns

### Step 1.3: Run the Migration

```bash
# Create database and run migrations
bin/rails db:create
bin/rails db:migrate
```

Check the generated schema:
```bash
cat db/schema.rb
```

---

## Part 2: Convert Model to Active Record

### Step 2.1: Rewrite the Student Model

Replace the in-memory class with Active Record:

```ruby
# app/models/student.rb
class Student < ApplicationRecord
  validates :student_number, presence: true, uniqueness: true
  validates :first_name, presence: true
  validates :last_name, presence: true
  validates :email, format: { with: URI::MailTo::EMAIL_REGEXP }, allow_blank: true

  def full_name
    "#{first_name} #{last_name}"
  end
end
```

**What changed:**
- Inherits from `ApplicationRecord` instead of plain `Object`
- Removed `attr_accessor` - Active Record generates these from database columns
- Removed `@@students` and class methods - Active Record provides them
- Added validations (similar to Java Bean Validation)
- Kept the `full_name` instance method

### Step 2.2: Create Seeds

```ruby
# db/seeds.rb
puts "Seeding students..."

students_data = [
  { student_number: "UCF001", first_name: "Alice", last_name: "Johnson",
    email: "alice.johnson@ucf.edu", major: "Computer Science" },
  { student_number: "UCF002", first_name: "Bob", last_name: "Smith",
    email: "bob.smith@ucf.edu", major: "Information Technology" },
  # ... more students
]

students_data.each do |attrs|
  Student.find_or_create_by!(student_number: attrs[:student_number]) do |s|
    s.first_name = attrs[:first_name]
    s.last_name = attrs[:last_name]
    s.email = attrs[:email]
    s.major = attrs[:major]
  end
end

puts "Done! #{Student.count} students in database."
```

Run seeds:
```bash
bin/rails db:seed
```

---

## Part 3: Update Controller for CRUD

### Step 3.1: Add All CRUD Actions

```ruby
# app/controllers/students_controller.rb
class StudentsController < ApplicationController
  before_action :set_student, only: [:show, :edit, :update, :destroy]

  def index
    @students = Student.all.order(:last_name, :first_name)
  end

  def show
  end

  def new
    @student = Student.new
  end

  def create
    @student = Student.new(student_params)
    if @student.save
      redirect_to @student, notice: "Student was successfully created."
    else
      render :new, status: :unprocessable_entity
    end
  end

  def edit
  end

  def update
    if @student.update(student_params)
      redirect_to @student, notice: "Student was successfully updated."
    else
      render :edit, status: :unprocessable_entity
    end
  end

  def destroy
    @student.destroy
    redirect_to students_path, notice: "Student was successfully deleted."
  end

  private

  def set_student
    @student = Student.find(params[:id])
  end

  def student_params
    params.require(:student).permit(:student_number, :first_name, :last_name, :email, :major)
  end
end
```

### Step 3.2: Update Routes

```ruby
# config/routes.rb
Rails.application.routes.draw do
  resources :students  # All CRUD routes
  root "students#index"
end
```

---

## Part 4: Create Form Views

### Step 4.1: Create Form Partial

```erb
<%# app/views/students/_form.html.erb %>
<%= form_with model: student do |form| %>
  <% if student.errors.any? %>
    <div class="alert alert-danger">
      <h5><%= pluralize(student.errors.count, "error") %></h5>
      <ul>
        <% student.errors.full_messages.each do |msg| %>
          <li><%= msg %></li>
        <% end %>
      </ul>
    </div>
  <% end %>

  <div class="mb-3">
    <%= form.label :student_number, class: "form-label" %>
    <%= form.text_field :student_number, class: "form-control" %>
  </div>

  <%# ... more fields ... %>

  <%= form.submit class: "btn btn-primary" %>
<% end %>
```

### Step 4.2: Create New View

```erb
<%# app/views/students/new.html.erb %>
<h1>New Student</h1>
<%= render 'form', student: @student %>
<%= link_to 'Back', students_path %>
```

### Step 4.3: Create Edit View

```erb
<%# app/views/students/edit.html.erb %>
<h1>Edit <%= @student.full_name %></h1>
<%= render 'form', student: @student %>
<%= link_to 'Back', @student %>
```

### Step 4.4: Update Index View

Add "New Student" button and Edit/Delete actions to the table.

---

## Running the Completed App

If using the pre-built Module 2 app:

```bash
cd course/modules/02-database-first/app

# Install dependencies
bundle install

# Setup database
bin/rails db:create db:migrate db:seed

# Start server
bin/rails server
```

Open http://localhost:3000

---

## Verification Checklist

After completing this module, verify:

- [ ] `bin/rails server` starts without errors
- [ ] http://localhost:3000 shows students from database
- [ ] "New Student" creates a record (check in console: `Student.last`)
- [ ] Edit updates the record
- [ ] Delete removes the record
- [ ] Validation errors display when required fields are blank
- [ ] Data persists after restarting the server

---

## Troubleshooting

### "Relation students does not exist"

Migrations haven't run:
```bash
bin/rails db:migrate
```

### "PG::ConnectionBad"

PostgreSQL isn't running:
```bash
brew services start postgresql@16
```

### Validation errors not showing

Make sure the form partial checks `student.errors.any?` and renders errors.

### "Unpermitted parameter"

Add the field to strong parameters in `student_params`.

---

## Console Exploration

Try these in `bin/rails console`:

```ruby
# See all students
Student.all

# Find by ID
Student.find(1)

# Query examples
Student.where(major: "Computer Science")
Student.where("email LIKE ?", "%ucf.edu")
Student.order(last_name: :asc)

# Create
Student.create!(student_number: "NEW001", first_name: "New", last_name: "Student")

# Update
s = Student.last
s.update(major: "Changed Major")

# Delete
Student.last.destroy

# See the SQL
Student.where(major: "CS").to_sql
```

---

## Next Steps

Proceed to **Module 3: ER Modeling to Rails Models** where you'll add the Course entity and create relationships.
