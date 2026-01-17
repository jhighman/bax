# Module 1: Foundations of DDD and Database Alignment

## Introduction

This foundational module introduces Domain-Driven Design (DDD) as a framework for aligning software design with business domains, starting from database-centric perspectives. Unlike traditional object-oriented approaches that begin with behavior, we'll explore how starting with data models and Entity-Relationship diagrams can lead to more robust, maintainable applications.

This module builds on your existing database knowledge from courses like CIS 3360 and shows how ER models can drive application architecture. By the end of this module, you will understand the fundamental principles of DDD and why a database-first approach aligns well with Information Engineering methodologies.

## Learning Objectives

By the end of this module, students will be able to:
- Explain the core principles of Domain-Driven Design and how they differ from traditional development approaches
- Understand the concept of ubiquitous language and its role in domain modeling
- Justify why starting with database design can lead to better domain models
- Create simple ER diagrams using Information Engineering notation
- Identify the relationship between domain concepts and database entities

## Key Concepts

### Domain-Driven Design (DDD)
- **Definition**: A software development approach that focuses on modeling software to match a domain according to input from domain experts
- **Database Perspective**: DDD concepts map naturally to database entities, relationships, and constraints
- **DDD Integration**: Provides a framework for organizing complex business logic around data structures

### Ubiquitous Language
- **Definition**: A common language shared by developers and domain experts, reflected in code, documentation, and database schema
- **Database Perspective**: Table names, column names, and constraints should reflect domain terminology
- **DDD Integration**: Ensures consistency between business requirements and technical implementation

### Database-First Domain Modeling
- **Definition**: Starting domain analysis with data modeling and ER diagrams before defining behavior
- **Database Perspective**: Leverages existing database design skills to drive application architecture
- **DDD Integration**: Provides a concrete foundation for abstract domain concepts

## Integration with IE and Fowler

### Information Engineering (IE) Perspective
James Martin's Information Engineering methodology provides excellent terminology for DDD concepts:
- **Entity Types**: Correspond to DDD entities - things with identity and lifecycle
- **Descriptors**: Replace DDD "Value Objects" - immutable data structures without identity
- **Normalization**: Guides how to structure descriptors and avoid redundancy

### Fowler's Analysis Patterns
Martin Fowler's patterns complement database-first modeling:
- **Party Pattern**: Models people and organizations as entity types with subtypes
- **Implementation**: Shows how abstract patterns translate to concrete ER diagrams

## Rails Examples

### UCF Course Manager Example

Let's start with a simple university domain to illustrate these concepts.

#### Initial Entity-Relationship Diagram
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Student     │    │   Enrollment    │    │     Course      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ student_id (PK) │    │ student_id (FK) │    │ course_id (PK)  │
│ first_name      │────│ course_id (FK)  │────│ course_code     │
│ last_name       │    │ enrollment_date │    │ course_name     │
│ email           │    │ grade           │    │ credit_hours    │
│ student_number  │    │ status          │    │ department      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

#### Rails Migration (Foundation)
```ruby
class CreateInitialSchema < ActiveRecord::Migration[7.0]
  def change
    # Student entity type
    create_table :students do |t|
      t.string :student_number, null: false
      t.string :first_name, null: false
      t.string :last_name, null: false
      t.string :email, null: false
      
      t.timestamps
    end
    
    # Course entity type
    create_table :courses do |t|
      t.string :course_code, null: false
      t.string :course_name, null: false
      t.integer :credit_hours, null: false
      t.string :department, null: false
      
      t.timestamps
    end
    
    # Enrollment relationship entity
    create_table :enrollments do |t|
      t.references :student, null: false, foreign_key: true
      t.references :course, null: false, foreign_key: true
      t.date :enrollment_date, null: false
      t.string :status, default: 'enrolled'
      t.decimal :grade, precision: 3, scale: 2
      
      t.timestamps
    end
    
    # Indexes for performance and uniqueness
    add_index :students, :student_number, unique: true
    add_index :students, :email, unique: true
    add_index :courses, :course_code, unique: true
    add_index :enrollments, [:student_id, :course_id], unique: true
  end
end
```

#### Rails Models (Foundation)
```ruby
# app/models/student.rb
class Student < ApplicationRecord
  # Associations (representing ER relationships)
  has_many :enrollments, dependent: :destroy
  has_many :courses, through: :enrollments
  
  # Validations (enforcing domain rules)
  validates :student_number, presence: true, uniqueness: true
  validates :first_name, :last_name, presence: true
  validates :email, presence: true, uniqueness: true, format: { with: URI::MailTo::EMAIL_REGEXP }
  
  # Domain logic (simple descriptors)
  def full_name
    "#{first_name} #{last_name}"
  end
  
  def active_enrollments
    enrollments.where(status: 'enrolled')
  end
end

# app/models/course.rb
class Course < ApplicationRecord
  # Associations
  has_many :enrollments, dependent: :destroy
  has_many :students, through: :enrollments
  
  # Validations
  validates :course_code, presence: true, uniqueness: true
  validates :course_name, presence: true
  validates :credit_hours, presence: true, numericality: { greater_than: 0 }
  validates :department, presence: true
  
  # Domain logic
  def enrollment_count
    enrollments.where(status: 'enrolled').count
  end
  
  def full_title
    "#{course_code}: #{course_name}"
  end
end

# app/models/enrollment.rb
class Enrollment < ApplicationRecord
  # Associations
  belongs_to :student
  belongs_to :course
  
  # Validations
  validates :enrollment_date, presence: true
  validates :status, inclusion: { in: %w[enrolled dropped completed] }
  validates :grade, numericality: { in: 0.0..4.0 }, allow_nil: true
  
  # Ensure unique enrollment per student per course
  validates :student_id, uniqueness: { scope: :course_id }
  
  # Domain logic
  def completed?
    status == 'completed' && grade.present?
  end
  
  def letter_grade
    return nil unless grade.present?
    
    case grade
    when 3.7..4.0 then 'A'
    when 3.3..3.69 then 'A-'
    when 3.0..3.29 then 'B+'
    when 2.7..2.99 then 'B'
    when 2.3..2.69 then 'B-'
    when 2.0..2.29 then 'C+'
    when 1.7..1.99 then 'C'
    when 1.0..1.69 then 'D'
    else 'F'
    end
  end
end
```

## Activities and Exercises

### Exercise 1: ER Diagram Analysis
**Objective**: Analyze an existing ER diagram and identify domain concepts

**Instructions**:
1. Review the Student-Course-Enrollment diagram above
2. Identify the entity types and their attributes
3. Describe the relationships and their cardinalities
4. Suggest additional attributes that might be needed for a real university system

**Deliverable**: Written analysis (1-2 pages) with annotated diagram

### Exercise 2: Ubiquitous Language Development
**Objective**: Create a glossary of domain terms

**Instructions**:
1. Interview a classmate about their understanding of university enrollment
2. Create a list of 10-15 domain terms with definitions
3. Map these terms to database concepts (entities, attributes, relationships)
4. Identify any terminology conflicts or ambiguities

**Deliverable**: Domain glossary with database mappings

### Exercise 3: Rails Model Implementation
**Objective**: Implement basic Rails models following the examples

**Instructions**:
1. Create a new Rails application
2. Implement the migration and models shown above
3. Add seed data for testing
4. Write simple tests to verify model behavior

**Deliverable**: Working Rails application with basic models

## Readings and Resources

### Required Readings
- Evans, Eric. *Domain-Driven Design*. Chapter 1: "Crunching Knowledge"
- Martin, James. *Information Engineering*. Chapter 3: "Entity Types and Attributes"
- Rails Guides: "Active Record Basics" (sections 1-3)

### Supplementary Resources
- Fowler, Martin. *Analysis Patterns*. Introduction
- [Rails Database Migrations Guide](https://guides.rubyonrails.org/active_record_migrations.html)
- [Entity-Relationship Modeling Tutorial](https://www.lucidchart.com/pages/er-diagrams)

### Code Examples Repository
- [Module 1 Branch](course/examples/ucf-course-manager) - Basic models and migrations

## Assessment

### Knowledge Check Quiz

1. **Question**: What is the primary benefit of starting domain modeling with ER diagrams?
   - a) It's faster than object-oriented design
   - b) It provides a concrete foundation for abstract concepts
   - c) It eliminates the need for business analysis
   - d) It automatically generates all application code
   
   **Answer**: b) It provides a concrete foundation for abstract concepts

2. **Question**: In Information Engineering terminology, what do we call immutable data structures without identity?
   - a) Entity Types
   - b) Value Objects
   - c) Descriptors
   - d) Aggregates
   
   **Answer**: c) Descriptors

3. **Question**: What is ubiquitous language in DDD?
   - a) A programming language used for domain modeling
   - b) A common language shared by developers and domain experts
   - c) A database query language
   - d) A documentation standard
   
   **Answer**: b) A common language shared by developers and domain experts

### Discussion Prompts
1. How does starting with database design change your approach to software development?
2. What challenges might arise when trying to establish ubiquitous language in a team?
3. How do you balance database normalization with application performance needs?

### Mini-Assignment
**Title**: University Domain Analysis
**Points**: 100
**Due Date**: End of Week 1

**Description**: Choose a different university process (e.g., library system, dining services, parking) and create an initial domain model using ER diagrams and Rails models.

**Rubric**:
- **Domain Understanding (40%)**: Clear identification of entities, relationships, and business rules
- **Technical Implementation (40%)**: Correct ER diagram notation and Rails model implementation
- **Documentation (20%)**: Clear explanations and proper use of domain terminology

## SDLC Integration

### How This Module Fits the Software Development Life Cycle

**Requirements Analysis**: Domain modeling helps bridge the gap between business requirements and technical specifications. ER diagrams provide a visual tool for validating understanding with stakeholders.

**Design Phase**: Database-first design ensures that the application architecture aligns with data relationships and constraints from the beginning.

**Implementation**: Rails models generated from ER diagrams provide a solid foundation for application logic.

**Testing**: Domain models with clear business rules are easier to test because the expected behavior is well-defined.

**Deployment**: Applications built on solid data foundations tend to be more stable and performant in production.

**Maintenance**: Clear domain models make it easier to understand and modify applications as business requirements evolve.

## Connection to UCF Curriculum

This module builds on concepts from:
- **CIS 3360 (Database Systems)**: ER modeling, normalization, and SQL fundamentals
- **COP 3330 (Object-Oriented Programming)**: Class design and relationships
- **CIS 4301 (Information and Database Systems)**: Advanced database design concepts

## Next Steps

In the next module, we will dive deeper into ER modeling with Information Engineering semantics, exploring how to properly model descriptors and composite descriptors while maintaining database integrity and performance.

---

## Module Checklist

Before moving to the next module, ensure you can:
- [ ] Explain the core principles of Domain-Driven Design
- [ ] Create basic ER diagrams using proper notation
- [ ] Implement Rails models with appropriate validations and associations
- [ ] Use domain terminology consistently in code and documentation
- [ ] Complete the knowledge check quiz with 80% or higher
- [ ] Successfully implement the Rails examples
- [ ] Participate meaningfully in discussion forums

---

*Remember: Domain modeling is an iterative process. The goal of this module is to establish a foundation - we'll build complexity gradually throughout the course.*