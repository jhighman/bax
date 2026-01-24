# Module 1: Rails Foundations for Java/C Developers

Welcome to the UCF Course Manager project! In this module, you'll build your first Ruby on Rails application while learning how Rails concepts map to what you already know from Java and C.

## Learning Objectives

By the end of this module, you will be able to:

1. Understand the Rails MVC architecture and how it compares to Java frameworks
2. Write Ruby classes and understand the syntax differences from Java/C
3. Create controllers that handle HTTP requests
4. Build views using ERB (Embedded Ruby) templates with Bootstrap styling
5. Run a Rails development server and navigate the application

## DDD Concept: Ubiquitous Language

**Domain-Driven Design** starts with a shared vocabulary between developers and domain experts. In our UCF Course Manager, we use terms that both programmers and university administrators understand:

| Domain Term | What It Means | Code Representation |
|-------------|---------------|---------------------|
| Student | A person enrolled at UCF | `Student` class |
| Student Number | Unique identifier (e.g., UCF001) | `student_number` attribute |
| Major | Field of study | `major` attribute |

This **ubiquitous language** ensures everyone speaks the same terms. When a registrar says "student," they mean the same thing as when a developer says "Student object."

---

## Ruby for Java/C Developers

### Classes: Java vs Ruby

**Java:**
```java
public class Student {
    private String firstName;
    private String lastName;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
```

**Ruby:**
```ruby
class Student
  attr_accessor :first_name, :last_name

  def initialize(first_name, last_name)
    @first_name = first_name
    @last_name = last_name
  end

  def full_name
    "#{first_name} #{last_name}"
  end
end
```

### Key Differences

| Concept | Java | Ruby |
|---------|------|------|
| Visibility | `public`, `private`, `protected` keywords | Methods are public by default |
| Getters/Setters | Write manually or use Lombok | `attr_accessor` generates them |
| Instance variables | `this.firstName` | `@first_name` (@ prefix) |
| String interpolation | `"Hello " + name` | `"Hello #{name}"` |
| No semicolons | Required | Optional (and discouraged) |
| No type declarations | `String name` | Just `name` |
| Constructor name | Same as class | Always `initialize` |

### For C Developers

If you're coming from C, think of Ruby classes as structs with functions attached:

**C struct:**
```c
typedef struct {
    char* first_name;
    char* last_name;
    char* student_number;
} Student;

char* full_name(Student* s) {
    // allocate and concatenate...
}
```

**Ruby:**
```ruby
class Student
  attr_accessor :first_name, :last_name, :student_number

  def full_name
    "#{first_name} #{last_name}"  # No memory management!
  end
end
```

Ruby handles memory automatically - no `malloc`, no `free`, no memory leaks.

---

## Rails MVC Architecture

Rails follows the **Model-View-Controller** pattern:

```
┌─────────────────────────────────────────────────────────────┐
│                      HTTP Request                            │
│                    GET /students                             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        ROUTER                                │
│              config/routes.rb                                │
│         resources :students → students#index                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      CONTROLLER                              │
│           app/controllers/students_controller.rb             │
│                                                              │
│   def index                                                  │
│     @students = Student.all    ◄─────┐                      │
│   end                                 │                      │
└───────────────────────────────────────│──────────────────────┘
                              │         │
                              │         │
                              ▼         │
┌─────────────────────────────────────────────────────────────┐
│                         MODEL                                │
│               app/models/student.rb                          │
│                                                              │
│   class Student                                              │
│     def self.all                                             │
│       @@students   # Returns all students                    │
│     end                                                      │
│   end                                                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                          VIEW                                │
│           app/views/students/index.html.erb                  │
│                                                              │
│   <% @students.each do |student| %>                         │
│     <%= student.full_name %>                                │
│   <% end %>                                                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      HTTP Response                           │
│                    HTML Page                                 │
└─────────────────────────────────────────────────────────────┘
```

### Comparison to Java Spring

| Rails | Spring Boot |
|-------|-------------|
| `routes.rb` | `@RequestMapping` annotations |
| `StudentsController` | `@Controller` class |
| `@students` instance variable | `model.addAttribute()` |
| `index.html.erb` | Thymeleaf/JSP template |
| `Student` model | `@Entity` class |

---

## The Application Structure

```
app/
├── controllers/
│   ├── application_controller.rb    # Base controller (like a Java abstract class)
│   └── students_controller.rb       # Handles /students requests
├── models/
│   ├── application_record.rb        # Base model class
│   └── student.rb                   # Student entity
└── views/
    ├── layouts/
    │   └── application.html.erb     # Main layout (header, nav, footer)
    └── students/
        ├── index.html.erb           # Student list page
        └── show.html.erb            # Single student detail page
```

### Convention Over Configuration

Rails uses **naming conventions** instead of configuration files:

- Controller `StudentsController` → handles `/students` routes
- Action `index` → renders `views/students/index.html.erb`
- Action `show` → renders `views/students/show.html.erb`

In Java/Spring, you'd explicitly configure these mappings. Rails figures it out from the names.

---

## Running the Application

From the `app/` directory:

```bash
# Start the development server
bin/rails server

# Or the shorter version
bin/rails s
```

Then open http://localhost:3000 in your browser.

### Useful Commands

```bash
# See all available routes
bin/rails routes

# Open Rails console (like a REPL for your app)
bin/rails console

# In the console, try:
Student.all
Student.find(1)
Student.count
```

---

## Key Files to Study

1. **`app/models/student.rb`** - The Student class with detailed comments comparing to Java/C
2. **`app/controllers/students_controller.rb`** - Controller with Java Spring comparisons
3. **`app/views/students/index.html.erb`** - View template with ERB explanations
4. **`config/routes.rb`** - URL routing configuration

---

## Exercises

### Exercise 1: Add a New Attribute

Add a `gpa` attribute to the Student class:

1. Open `app/models/student.rb`
2. Add `gpa` to the `attr_accessor` line
3. Update the `initialize` method to accept `:gpa`
4. Update `seed_sample_data` to include GPA values
5. Update the views to display GPA

### Exercise 2: Create a New Page

Create an "About" page:

1. Add a route in `config/routes.rb`: `get "about" => "pages#about"`
2. Create `app/controllers/pages_controller.rb`
3. Create `app/views/pages/about.html.erb`
4. Add a link in the navigation

### Exercise 3: Ruby Practice

Open `bin/rails console` and try:

```ruby
# Create a student
s = Student.new(first_name: "Test", last_name: "User")
s.full_name

# Iterate with blocks (Ruby's version of lambdas/closures)
Student.all.each { |student| puts student.full_name }

# Filter with select (like Java streams)
cs_students = Student.all.select { |s| s.major == "Computer Science" }
```

---

## What's Next?

In **Module 2: Database-First Development**, we'll:

- Connect to PostgreSQL for persistent storage
- Replace our in-memory Student class with Active Record
- Write database migrations
- Learn about database seeds

The in-memory approach in this module helped you understand Ruby classes without the complexity of databases. Now you're ready to persist data!

---

## Glossary

| Term | Definition |
|------|------------|
| **MVC** | Model-View-Controller architecture pattern |
| **ERB** | Embedded Ruby - template syntax for mixing Ruby with HTML |
| **Route** | URL pattern mapped to a controller action |
| **Action** | A method in a controller that handles a request |
| **Instance Variable** | Variable prefixed with `@`, accessible in views |
| **Convention over Configuration** | Using naming patterns instead of explicit config |

---

## Resources

- [Ruby in Twenty Minutes](https://www.ruby-lang.org/en/documentation/quickstart/)
- [Rails Getting Started Guide](https://guides.rubyonrails.org/getting_started.html)
- [Ruby for Java Developers](https://www.ruby-lang.org/en/documentation/ruby-from-other-languages/to-ruby-from-java/)
