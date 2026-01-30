# Module 4: The Enrollment Aggregate

In this module, we implement the Enrollment entity - not just a join table, but a domain object with its own behavior. This is where we introduce automated testing with RSpec and begin documenting use cases.

## Learning Objectives

By the end of this module, you will be able to:

1. Understand aggregates as consistency boundaries
2. Create join models with business logic
3. Write RSpec tests for models
4. Document use cases for domain operations
5. Implement validations and state management

## DDD Concept: Aggregates

An **Aggregate** is a cluster of domain objects treated as a single unit. The Enrollment aggregate includes:

- Enrollment (the root)
- Associated Student reference
- Associated Course reference
- Status transitions (enrolled → completed/dropped)
- Grade assignment

Changes to the aggregate go through the root (Enrollment), ensuring consistency.

## Java/C Bridge: Join Tables with Behavior

**Java (typical):**
```java
// Often just a join table, logic scattered elsewhere
@Entity
@Table(name = "student_courses")
public class StudentCourse {
    @Id private Long id;
    @ManyToOne private Student student;
    @ManyToOne private Course course;
}
```

**Rails (domain-rich):**
```ruby
class Enrollment < ApplicationRecord
  belongs_to :student
  belongs_to :course

  validates :status, inclusion: { in: %w[enrolled completed dropped] }
  validates :student_id, uniqueness: { scope: :course_id }

  def complete!(grade:)
    update!(status: 'completed', grade: grade)
  end

  def drop!
    update!(status: 'dropped')
  end
end
```

## Key Artifacts Introduced

### Use Cases (`artifacts/use-cases/`)

**enroll-student.md:**
```markdown
# Use Case: Enroll Student in Course

**Actor:** Registrar
**Preconditions:** Student exists, Course exists, Course not full
**Main Flow:**
1. Registrar selects student
2. Registrar selects course
3. System validates enrollment is allowed
4. System creates enrollment with status "enrolled"
**Postconditions:** Student appears in course roster
```

### Testing

```ruby
# spec/models/enrollment_spec.rb
RSpec.describe Enrollment do
  it "prevents duplicate enrollments" do
    enrollment = create(:enrollment)
    duplicate = build(:enrollment,
      student: enrollment.student,
      course: enrollment.course)

    expect(duplicate).not_to be_valid
  end
end
```

## App State After This Module

- Students can enroll in courses
- Enrollment status tracking (enrolled/completed/dropped)
- Grade assignment for completed enrollments
- RSpec tests for enrollment logic
- Use case documentation

## Development Practice: Test-Driven Development

From this module forward:
1. Write a failing test
2. Write code to make it pass
3. Refactor

---

*To be expanded with full instructions and working app.*
