# UCF Course Manager - Rails Example Application

This Rails application serves as the primary example throughout the DDD course, demonstrating how domain-driven design principles integrate with database-centric application development.

## Overview

The UCF Course Manager is a university management system that handles:
- Student enrollment and management
- Course scheduling and management
- Faculty assignments
- Grade tracking and reporting
- Academic program management

This application is built incrementally throughout the course modules, with each module adding new domain concepts and Rails implementations.

## Domain Model Evolution

The application evolves through the course modules:

### Module 1-2: Foundation and ER Modeling
- Basic entity types: Student, Course, Faculty
- Simple relationships and descriptors
- Initial Rails models and migrations

### Module 3-4: Bounded Contexts and Entities
- Academic context vs. Administrative context
- Identity-based entities with composite descriptors
- Address, Name, and ContactInfo as structured attributes

### Module 5-6: Aggregates and Services
- Enrollment aggregate with consistency boundaries
- Course scheduling aggregate
- Domain services for complex business logic

### Module 7-8: Context Mapping and Patterns
- Integration between academic and administrative contexts
- Implementation of Fowler's Party pattern for users
- Observation pattern for grades and assessments

### Module 9-10: Full SDLC and Advanced Topics
- Complete application with testing
- Deployment considerations
- Evolution and refactoring strategies

## Technology Stack

- **Ruby**: 3.2+
- **Rails**: 7.0+
- **Database**: PostgreSQL (production), SQLite3 (development)
- **Testing**: RSpec, FactoryBot
- **Authentication**: Devise (added in later modules)
- **Authorization**: Pundit (added in later modules)

## Getting Started

### Prerequisites
- Ruby 3.2 or higher
- Rails 7.0 or higher
- PostgreSQL (for production-like development)
- Git

### Installation

1. Clone the repository (or copy the example files)
2. Install dependencies:
   ```bash
   bundle install
   ```
3. Set up the database:
   ```bash
   rails db:create
   rails db:migrate
   rails db:seed
   ```
4. Run the application:
   ```bash
   rails server
   ```

### Running Tests
```bash
bundle exec rspec
```

## Module-Specific Branches

Each module has its own branch showing the application state at that point:
- `module-01-foundations`
- `module-02-er-modeling`
- `module-03-bounded-contexts`
- `module-04-entities-descriptors`
- `module-05-aggregates`
- `module-06-repositories-services`
- `module-07-context-mapping`
- `module-08-fowler-patterns`
- `module-09-sdlc`
- `module-10-capstone`

## Key Domain Concepts Demonstrated

### Entity Types (IE Terminology)
- **Student**: Identity-based entity with enrollment capabilities
- **Course**: Academic offering with scheduling and capacity
- **Faculty**: Teaching staff with assignment capabilities
- **Enrollment**: Association entity with business rules

### Composite Descriptors (IE Terminology)
- **Address**: Street, city, state, zip as structured attribute
- **PersonName**: First, middle, last name components
- **ContactInfo**: Email, phone, emergency contact details
- **CourseSchedule**: Days, times, location as composite

### Domain Services
- **EnrollmentService**: Handles enrollment business rules
- **SchedulingService**: Manages course scheduling conflicts
- **GradingService**: Calculates grades and GPAs
- **TranscriptService**: Generates academic transcripts

## Database Design Principles

The application demonstrates:
- **IE Notation**: Entity-relationship diagrams using Information Engineering standards
- **Normalization**: Proper normalization while avoiding over-normalization of descriptors
- **Referential Integrity**: Foreign key constraints and database-level validation
- **Performance**: Appropriate indexing and query optimization

## Learning Objectives Alignment

This example application helps students:
1. See how ER diagrams translate to Rails models and migrations
2. Understand the relationship between domain concepts and database design
3. Practice implementing business rules in both database constraints and application logic
4. Experience the evolution of a domain model over time
5. Learn to balance database normalization with application performance

## Contributing

This is an educational example. Students are encouraged to:
- Fork the repository for their own experiments
- Suggest improvements to domain modeling
- Add additional features following DDD principles
- Share alternative implementations

## License

This educational example is provided under the MIT License for use in the UCF DDD course.

---

*This application grows in complexity throughout the course. Don't worry if early modules seem simple - we're building a solid foundation for more sophisticated domain modeling.*