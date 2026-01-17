# DDD Course Terminology Guide

This guide provides a cross-reference between Domain-Driven Design terminology, Information Engineering concepts, and database implementation patterns used throughout the course.

## Core Terminology Mapping

| DDD Term | IE Term | Database Concept | Rails Implementation |
|----------|---------|------------------|---------------------|
| Entity | Entity Type | Table with Primary Key | ActiveRecord Model |
| Value Object | Descriptor/Composite Descriptor | Embedded Attributes | Model Attributes/Methods |
| Aggregate | Entity Cluster | Related Tables with Constraints | Model with Associations |
| Repository | Data Access Layer | Database Interface | ActiveRecord Queries |
| Domain Service | Business Logic Service | Stored Procedures/Functions | Service Objects |
| Bounded Context | Subject Area | Database Schema | Rails Engine/Namespace |

## Information Engineering (IE) Terminology

### Entity Types
**Definition**: Things of significance about which information needs to be stored.
**Characteristics**:
- Have unique identity (primary key)
- Have lifecycle and state changes
- Can participate in relationships
- Map to database tables

**Examples**:
- Student (identified by student_id)
- Course (identified by course_id)
- Faculty (identified by faculty_id)

### Descriptors
**Definition**: Immutable data structures that describe characteristics of entities without having independent identity.
**Characteristics**:
- No independent identity
- Immutable once created
- Often embedded in entity types
- Support normalization without creating unnecessary tables

**Examples**:
- Address (street, city, state, zip)
- PersonName (first, middle, last)
- Money (amount, currency)

### Composite Descriptors
**Definition**: Descriptors composed of multiple related attributes that are treated as a unit.
**Characteristics**:
- Group related attributes together
- Maintain cohesion of related data
- Can be reused across multiple entity types
- Support validation as a unit

**Examples**:
- ContactInfo (email, phone, emergency_contact)
- CourseSchedule (days, start_time, end_time, location)
- GradeInfo (points_earned, points_possible, letter_grade)

### Structured Attributes
**Definition**: Attributes that have internal structure but are stored as components within an entity type.
**Characteristics**:
- More complex than simple attributes
- Less complex than separate entity types
- Often implemented as multiple columns
- Support business rules and validation

**Examples**:
- Address components in Student table
- Name components in Person table
- Monetary values with currency

## Database Design Patterns

### Entity Tables
Tables representing entity types with:
- Primary key for unique identification
- Attributes specific to the entity
- Foreign keys for relationships
- Appropriate indexes for performance

```sql
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Descriptor Implementation
Descriptors implemented as:
- Multiple columns in entity tables
- JSON/JSONB columns for complex structures
- Separate tables only when reuse justifies normalization

```sql
-- Address as composite descriptor in Student table
ALTER TABLE students ADD COLUMN address_street VARCHAR(100);
ALTER TABLE students ADD COLUMN address_city VARCHAR(50);
ALTER TABLE students ADD COLUMN address_state VARCHAR(2);
ALTER TABLE students ADD COLUMN address_zip VARCHAR(10);

-- Or as JSON for more complex structures
ALTER TABLE students ADD COLUMN contact_info JSONB;
```

### Relationship Tables
Tables representing relationships between entities:
- Foreign keys to related entities
- Additional attributes specific to the relationship
- Constraints to enforce business rules

```sql
CREATE TABLE enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    student_id INTEGER REFERENCES students(student_id),
    course_id INTEGER REFERENCES courses(course_id),
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'enrolled',
    grade DECIMAL(3,2),
    UNIQUE(student_id, course_id)
);
```

## Rails Implementation Patterns

### Entity Models
```ruby
class Student < ApplicationRecord
  # Identity and basic attributes
  validates :student_number, presence: true, uniqueness: true
  
  # Relationships
  has_many :enrollments
  has_many :courses, through: :enrollments
  
  # Composite descriptor methods
  def full_name
    "#{first_name} #{last_name}"
  end
  
  def address
    "#{address_street}, #{address_city}, #{address_state} #{address_zip}"
  end
end
```

### Descriptor Implementation
```ruby
# Simple descriptors as methods
def full_name
  [first_name, middle_name, last_name].compact.join(' ')
end

# Complex descriptors as value objects (when needed)
class Address
  include ActiveModel::Model
  
  attr_accessor :street, :city, :state, :zip
  
  validates :street, :city, :state, :zip, presence: true
  validates :state, length: { is: 2 }
  validates :zip, format: { with: /\A\d{5}(-\d{4})?\z/ }
  
  def to_s
    "#{street}, #{city}, #{state} #{zip}"
  end
end
```

### Domain Services
```ruby
class EnrollmentService
  def initialize(student, course)
    @student = student
    @course = course
  end
  
  def enroll
    return false if enrollment_exists?
    return false unless prerequisites_met?
    return false if course_full?
    
    create_enrollment
  end
  
  private
  
  def enrollment_exists?
    @student.enrollments.exists?(course: @course)
  end
  
  def prerequisites_met?
    # Business logic for prerequisite checking
  end
  
  def course_full?
    @course.enrollment_count >= @course.capacity
  end
  
  def create_enrollment
    @student.enrollments.create!(
      course: @course,
      enrollment_date: Date.current,
      status: 'enrolled'
    )
  end
end
```

## Common Anti-Patterns to Avoid

### Over-Normalization of Descriptors
**Problem**: Creating separate tables for every descriptor
**Solution**: Use composite descriptors embedded in entity tables

```ruby
# Anti-pattern: Separate table for addresses
class Address < ApplicationRecord
  belongs_to :student
end

# Better: Address as composite descriptor
class Student < ApplicationRecord
  def address
    Address.new(
      street: address_street,
      city: address_city,
      state: address_state,
      zip: address_zip
    )
  end
end
```

### Anemic Domain Models
**Problem**: Models with only getters/setters, no business logic
**Solution**: Include domain logic in entity models

```ruby
# Anti-pattern: Logic in controllers
def calculate_gpa
  total_points = 0
  total_hours = 0
  enrollments.completed.each do |enrollment|
    total_points += enrollment.grade * enrollment.course.credit_hours
    total_hours += enrollment.course.credit_hours
  end
  total_points / total_hours
end

# Better: Logic in domain model
class Student < ApplicationRecord
  def gpa
    completed_enrollments = enrollments.completed.includes(:course)
    return 0.0 if completed_enrollments.empty?
    
    total_points = completed_enrollments.sum { |e| e.grade * e.course.credit_hours }
    total_hours = completed_enrollments.sum { |e| e.course.credit_hours }
    
    total_points / total_hours
  end
end
```

### Ignoring Database Constraints
**Problem**: Relying only on application-level validation
**Solution**: Use database constraints for critical business rules

```ruby
# Migration with proper constraints
class CreateEnrollments < ActiveRecord::Migration[7.0]
  def change
    create_table :enrollments do |t|
      t.references :student, null: false, foreign_key: true
      t.references :course, null: false, foreign_key: true
      t.date :enrollment_date, null: false
      t.string :status, null: false, default: 'enrolled'
      t.decimal :grade, precision: 3, scale: 2
      
      t.timestamps
    end
    
    # Business rule: One enrollment per student per course
    add_index :enrollments, [:student_id, :course_id], unique: true
    
    # Business rule: Valid grade range
    add_check_constraint :enrollments, 'grade >= 0.0 AND grade <= 4.0', name: 'valid_grade_range'
    
    # Business rule: Valid status values
    add_check_constraint :enrollments, "status IN ('enrolled', 'dropped', 'completed')", name: 'valid_status'
  end
end
```

## Glossary

**Aggregate**: A cluster of associated entities and descriptors treated as a unit for data changes.

**Bounded Context**: A specific area of the domain with its own ubiquitous language and model boundaries.

**Composite Descriptor**: A descriptor made up of multiple related attributes treated as a cohesive unit.

**Descriptor**: An immutable data structure that describes characteristics without independent identity.

**Domain Service**: A service that encapsulates business logic that doesn't naturally belong to any entity.

**Entity Type**: A thing of significance with unique identity and lifecycle.

**Repository**: An abstraction for accessing and persisting entities.

**Structured Attribute**: An attribute with internal structure, more complex than simple attributes but simpler than entities.

**Ubiquitous Language**: A common vocabulary shared by developers and domain experts, reflected in code and documentation.

---

*This terminology guide should be referenced throughout the course to maintain consistency in language and implementation patterns.*