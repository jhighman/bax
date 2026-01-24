# Module 6: Domain Services

In this module, we extract complex business logic into service objects. When an operation doesn't naturally belong to any single entity, it belongs in a service.

## Learning Objectives

By the end of this module, you will be able to:

1. Identify when to use service objects
2. Create service classes following Rails conventions
3. Handle complex multi-step operations
4. Implement proper error handling
5. Keep models thin and focused

## DDD Concept: Domain Services

A **Domain Service** encapsulates logic that:
- Involves multiple entities
- Doesn't naturally belong to any single entity
- Represents a significant domain operation

Examples:
- `EnrollmentService` - enrolls student, checks prerequisites, capacity
- `GradeCalculationService` - calculates GPA across enrollments
- `TranscriptService` - generates academic transcript

## Java/C Bridge: Service Layer

**Java Spring:**
```java
@Service
public class EnrollmentService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private CourseRepository courseRepo;

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        Student student = studentRepo.findById(studentId).orElseThrow();
        Course course = courseRepo.findById(courseId).orElseThrow();

        if (course.isFull()) {
            throw new CourseFullException();
        }

        return enrollmentRepo.save(new Enrollment(student, course));
    }
}
```

**Rails:**
```ruby
class EnrollmentService
  def initialize(student:, course:)
    @student = student
    @course = course
  end

  def call
    validate_enrollment!
    create_enrollment
  end

  private

  def validate_enrollment!
    raise CourseFull if @course.full?
    raise AlreadyEnrolled if already_enrolled?
    raise PrerequisitesNotMet unless prerequisites_met?
  end

  def create_enrollment
    Enrollment.create!(
      student: @student,
      course: @course,
      status: 'enrolled'
    )
  end
end

# Usage
EnrollmentService.new(student: student, course: course).call
```

## Key Concepts

### Service Object Pattern

```ruby
# app/services/enrollment_service.rb
class EnrollmentService
  Result = Struct.new(:success?, :enrollment, :error, keyword_init: true)

  def initialize(student:, course:)
    @student = student
    @course = course
  end

  def call
    return failure("Course is full") if @course.full?
    return failure("Already enrolled") if already_enrolled?

    enrollment = Enrollment.create!(student: @student, course: @course, status: 'enrolled')
    success(enrollment)
  rescue ActiveRecord::RecordInvalid => e
    failure(e.message)
  end

  private

  def success(enrollment)
    Result.new(success?: true, enrollment: enrollment)
  end

  def failure(error)
    Result.new(success?: false, error: error)
  end

  def already_enrolled?
    Enrollment.exists?(student: @student, course: @course, status: 'enrolled')
  end
end
```

### When NOT to Use Services

Don't use services for:
- Simple CRUD operations (use the model)
- Single-entity validations (use model validations)
- View formatting (use helpers or presenters)

## App State After This Module

- `EnrollmentService` handles enrollment logic
- `GradeCalculationService` computes GPA
- Proper error handling with flash messages
- Controllers are thin, services are focused
- Tests for service objects

## Development Practice: Single Responsibility

Each class should have one reason to change:
- Models: data validation, persistence
- Services: business operations
- Controllers: request handling
- Views: presentation

---

*To be expanded with full instructions and working app.*
