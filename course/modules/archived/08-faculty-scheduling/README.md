# Module 8: Faculty and Scheduling

In this module, we add the Faculty entity and course scheduling. We'll explore Fowler's Party pattern and continue sprint-based development.

## Learning Objectives

By the end of this module, you will be able to:

1. Implement the Party pattern for people (Students, Faculty)
2. Create scheduling models with time-based constraints
3. Handle many-to-many relationships with attributes
4. Build dashboard views for different user types
5. Execute a full sprint cycle

## DDD Concept: Fowler's Party Pattern

The **Party pattern** recognizes that Students and Faculty are both "parties" (people or organizations) that share common attributes:

```
         ┌──────────┐
         │  Party   │
         │----------|
         │ name     │
         │ email    │
         │ phone    │
         └────┬─────┘
              │
     ┌────────┴────────┐
     │                 │
┌────┴────┐      ┌─────┴─────┐
│ Student │      │  Faculty  │
│---------|      │-----------|
│ major   │      │ department│
│ gpa     │      │ office    │
└─────────┘      └───────────┘
```

This isn't always needed (YAGNI), but understanding the pattern helps when requirements evolve.

## Java/C Bridge: Inheritance vs Composition

**Java inheritance:**
```java
public abstract class Party {
    protected String name;
    protected String email;
}

public class Student extends Party {
    private String major;
}

public class Faculty extends Party {
    private String department;
}
```

**Rails (composition approach):**
```ruby
# Often simpler in Rails to use concerns or just similar columns
class Student < ApplicationRecord
  include Contactable  # shared behavior
end

class Faculty < ApplicationRecord
  include Contactable
end

# Or Single Table Inheritance if truly polymorphic
class Party < ApplicationRecord
end
class Student < Party
end
class Faculty < Party
end
```

## Key Models

### Faculty

```ruby
class Faculty < ApplicationRecord
  has_many :course_sections
  has_many :courses, through: :course_sections

  validates :employee_id, presence: true, uniqueness: true
end
```

### CourseSection (Scheduling)

```ruby
class CourseSection < ApplicationRecord
  belongs_to :course
  belongs_to :faculty

  validates :section_number, presence: true
  validates :days, presence: true  # e.g., "MWF"
  validates :start_time, presence: true
  validates :end_time, presence: true
  validates :room, presence: true
  validates :capacity, numericality: { greater_than: 0 }

  def schedule_display
    "#{days} #{start_time.strftime('%I:%M %p')} - #{end_time.strftime('%I:%M %p')}"
  end

  def conflicts_with?(other)
    return false if days_overlap(other.days).empty?
    time_overlaps?(other)
  end
end
```

## App State After This Module

- Faculty profiles and dashboard
- Course sections with schedules
- Conflict detection for scheduling
- Faculty can view their assigned courses
- Calendar-style schedule display

## Development Practice: User Stories

Format: "As a [role], I need [capability] so that [benefit]"

Examples:
- As a faculty member, I need to see my course schedule so I can plan my week
- As a registrar, I need to assign faculty to sections so courses are staffed
- As a student, I need to see section times so I can avoid conflicts

---

*To be expanded with full instructions and working app.*
