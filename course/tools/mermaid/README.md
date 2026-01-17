# Mermaid: Diagram as Code for Domain Modeling

Mermaid is a powerful diagramming and charting tool that uses text-based syntax to create visual diagrams. In the context of Domain-Driven Design and database modeling, Mermaid provides an excellent way to create Entity-Relationship diagrams, flowcharts, and other visual representations that can be version-controlled alongside your code.

## Why Mermaid for DDD and Database Design?

### Advantages
- **Version Control**: Diagrams are text-based and can be tracked in Git
- **Collaboration**: Easy to review and modify through pull requests
- **Integration**: Works seamlessly with GitHub, GitLab, and documentation platforms
- **Consistency**: Standardized syntax ensures uniform diagram appearance
- **Automation**: Can be generated programmatically from code or data
- **Accessibility**: Text-based format is screen-reader friendly

### Use Cases in DDD Course
- Entity-Relationship diagrams using IE notation
- Domain model visualization
- Bounded context mapping
- System architecture diagrams
- Process flows for SDLC integration
- Database schema visualization

## Installation and Setup

### Online Editor
- Visit [Mermaid Live Editor](https://mermaid.live/) for immediate use
- No installation required
- Export to PNG, SVG, or copy markdown

### VS Code Integration
```bash
# Install Mermaid Preview extension
code --install-extension bierner.markdown-mermaid
```

### Local Installation
```bash
# Install Mermaid CLI
npm install -g @mermaid-js/mermaid-cli

# Verify installation
mmdc --version
```

### GitHub Integration
Mermaid diagrams render automatically in GitHub markdown files.

## Entity-Relationship Diagrams for DDD

### Basic ER Diagram Syntax
```mermaid
erDiagram
    STUDENT {
        int student_id PK
        string student_number UK
        string first_name
        string last_name
        string email UK
        date created_at
        date updated_at
    }
    
    COURSE {
        int course_id PK
        string course_code UK
        string course_name
        int credit_hours
        string department
        date created_at
        date updated_at
    }
    
    ENROLLMENT {
        int enrollment_id PK
        int student_id FK
        int course_id FK
        date enrollment_date
        string status
        decimal grade
        date created_at
        date updated_at
    }
    
    STUDENT ||--o{ ENROLLMENT : "enrolls in"
    COURSE ||--o{ ENROLLMENT : "has"
```

### Advanced ER Features

#### Composite Descriptors
```mermaid
erDiagram
    STUDENT {
        int student_id PK
        string student_number UK
        string first_name
        string last_name
        string email UK
        string address_street
        string address_city
        string address_state
        string address_zip
        string phone_number
        string emergency_contact_name
        string emergency_contact_phone
        date created_at
        date updated_at
    }
    
    FACULTY {
        int faculty_id PK
        string employee_id UK
        string first_name
        string last_name
        string email UK
        string department
        string office_location
        string phone_number
        decimal salary
        date hire_date
        date created_at
        date updated_at
    }
    
    COURSE_SECTION {
        int section_id PK
        int course_id FK
        int faculty_id FK
        string section_number
        int capacity
        string schedule_days
        time schedule_start_time
        time schedule_end_time
        string location_building
        string location_room
        date start_date
        date end_date
        date created_at
        date updated_at
    }
    
    FACULTY ||--o{ COURSE_SECTION : "teaches"
    COURSE ||--o{ COURSE_SECTION : "offered as"
```

#### Inheritance and Subtypes
```mermaid
erDiagram
    PERSON {
        int person_id PK
        string person_type
        string first_name
        string last_name
        string email UK
        string phone_number
        date created_at
        date updated_at
    }
    
    STUDENT {
        int student_id PK
        int person_id FK
        string student_number UK
        string major
        decimal gpa
        int total_credits
        date enrollment_date
    }
    
    FACULTY {
        int faculty_id PK
        int person_id FK
        string employee_id UK
        string department
        string title
        decimal salary
        date hire_date
    }
    
    STAFF {
        int staff_id PK
        int person_id FK
        string employee_id UK
        string department
        string position
        decimal salary
        date hire_date
    }
    
    PERSON ||--|| STUDENT : "is a"
    PERSON ||--|| FACULTY : "is a"
    PERSON ||--|| STAFF : "is a"
```

## Bounded Context Diagrams

### Context Mapping
```mermaid
graph TB
    subgraph "Academic Context"
        AC[Academic Catalog]
        CS[Course Scheduling]
        EN[Enrollment Management]
        GR[Grade Recording]
    end
    
    subgraph "Student Services Context"
        SR[Student Registration]
        AD[Academic Advising]
        TR[Transcript Management]
        FA[Financial Aid]
    end
    
    subgraph "Administrative Context"
        HR[Human Resources]
        FN[Finance]
        FC[Facilities]
        IT[IT Services]
    end
    
    subgraph "External Systems"
        SIS[Student Information System]
        LMS[Learning Management System]
        PAY[Payment Gateway]
        EMAIL[Email Service]
    end
    
    AC --> SR : "Course Offerings"
    EN --> TR : "Enrollment Data"
    GR --> TR : "Grade Data"
    SR --> FA : "Enrollment Status"
    HR --> AC : "Faculty Assignments"
    
    AC -.-> SIS : "Sync Course Data"
    EN -.-> LMS : "Roster Updates"
    FA -.-> PAY : "Payment Processing"
    SR -.-> EMAIL : "Notifications"
```

### Domain Model Overview
```mermaid
graph LR
    subgraph "Core Domain"
        CD[Course Management]
        EM[Enrollment Management]
        GM[Grade Management]
    end
    
    subgraph "Supporting Domains"
        UM[User Management]
        NM[Notification Management]
        RM[Reporting Management]
    end
    
    subgraph "Generic Domains"
        AU[Authentication]
        LG[Logging]
        CF[Configuration]
    end
    
    CD --> EM
    EM --> GM
    UM --> CD
    UM --> EM
    NM --> EM
    RM --> GM
    AU --> UM
    LG --> CD
    LG --> EM
    CF --> CD
```

## Process Flow Diagrams

### Student Enrollment Process
```mermaid
flowchart TD
    A[Student Login] --> B{Prerequisites Met?}
    B -->|Yes| C[Browse Available Courses]
    B -->|No| D[Display Prerequisites]
    D --> E[Complete Prerequisites]
    E --> C
    
    C --> F[Select Course]
    F --> G{Course Available?}
    G -->|Yes| H{Capacity Available?}
    G -->|No| I[Show Alternative Sections]
    I --> F
    
    H -->|Yes| J[Add to Cart]
    H -->|No| K[Add to Waitlist]
    
    J --> L{More Courses?}
    L -->|Yes| C
    L -->|No| M[Review Cart]
    
    M --> N{Conflicts?}
    N -->|Yes| O[Resolve Conflicts]
    O --> M
    N -->|No| P[Submit Enrollment]
    
    P --> Q[Payment Processing]
    Q --> R{Payment Success?}
    R -->|Yes| S[Enrollment Confirmed]
    R -->|No| T[Payment Failed]
    T --> Q
    
    K --> U[Waitlist Notification]
    S --> V[Send Confirmation Email]
    U --> W[Monitor for Openings]
```

### Grade Processing Workflow
```mermaid
sequenceDiagram
    participant F as Faculty
    participant LMS as Learning Management System
    participant SIS as Student Information System
    participant S as Student
    participant R as Registrar
    
    F->>LMS: Enter Grades
    LMS->>LMS: Validate Grade Format
    LMS->>SIS: Submit Grades
    SIS->>SIS: Validate Business Rules
    SIS->>R: Notify Grade Submission
    R->>SIS: Approve Grades
    SIS->>S: Update Transcript
    SIS->>S: Send Grade Notification
    S->>SIS: View Updated Transcript
```

## Database Schema Visualization

### Physical Database Design
```mermaid
erDiagram
    students {
        bigserial id PK
        varchar student_number UK
        varchar first_name
        varchar last_name
        varchar email UK
        varchar address_street
        varchar address_city
        varchar address_state
        varchar address_zip
        varchar phone_number
        timestamp created_at
        timestamp updated_at
    }
    
    courses {
        bigserial id PK
        varchar course_code UK
        varchar course_name
        integer credit_hours
        varchar department
        text description
        jsonb prerequisites
        timestamp created_at
        timestamp updated_at
    }
    
    course_sections {
        bigserial id PK
        bigint course_id FK
        bigint faculty_id FK
        varchar section_number
        integer capacity
        varchar schedule_days
        time start_time
        time end_time
        varchar location
        date start_date
        date end_date
        timestamp created_at
        timestamp updated_at
    }
    
    enrollments {
        bigserial id PK
        bigint student_id FK
        bigint course_section_id FK
        date enrollment_date
        varchar status
        decimal grade
        timestamp created_at
        timestamp updated_at
    }
    
    faculty {
        bigserial id PK
        varchar employee_id UK
        varchar first_name
        varchar last_name
        varchar email UK
        varchar department
        varchar title
        timestamp created_at
        timestamp updated_at
    }
    
    students ||--o{ enrollments : "has"
    course_sections ||--o{ enrollments : "contains"
    courses ||--o{ course_sections : "offered_as"
    faculty ||--o{ course_sections : "teaches"
```

## Advanced Mermaid Features

### State Diagrams for Entity Lifecycle
```mermaid
stateDiagram-v2
    [*] --> Draft : Create Course
    Draft --> Review : Submit for Review
    Review --> Approved : Approve
    Review --> Draft : Request Changes
    Approved --> Published : Publish
    Published --> Archived : Archive
    Archived --> Published : Reactivate
    Published --> [*] : Delete
    Draft --> [*] : Delete
```

### Class Diagrams for Rails Models
```mermaid
classDiagram
    class Student {
        +String student_number
        +String first_name
        +String last_name
        +String email
        +Address address
        +enrollments() Enrollment[]
        +courses() Course[]
        +gpa() Decimal
        +full_name() String
        +active_enrollments() Enrollment[]
    }
    
    class Course {
        +String course_code
        +String course_name
        +Integer credit_hours
        +String department
        +course_sections() CourseSection[]
        +students() Student[]
        +enrollment_count() Integer
        +full_title() String
    }
    
    class Enrollment {
        +Date enrollment_date
        +String status
        +Decimal grade
        +student() Student
        +course_section() CourseSection
        +completed?() Boolean
        +letter_grade() String
    }
    
    class CourseSection {
        +String section_number
        +Integer capacity
        +Schedule schedule
        +Location location
        +course() Course
        +faculty() Faculty
        +enrollments() Enrollment[]
        +available_spots() Integer
    }
    
    Student ||--o{ Enrollment : enrolls
    CourseSection ||--o{ Enrollment : contains
    Course ||--o{ CourseSection : offered_as
    Faculty ||--o{ CourseSection : teaches
```

## Integration with Course Modules

### Module 1: Foundations
- Basic ER diagrams for Student-Course-Enrollment
- Simple relationship visualization

### Module 2: IE Semantics
- Composite descriptor representation
- Normalization visualization

### Module 3: Bounded Contexts
- Context mapping diagrams
- Domain boundaries

### Module 4: Entities and Descriptors
- Detailed entity modeling
- Descriptor composition

### Module 5: Aggregates
- Aggregate boundary visualization
- Consistency boundary mapping

### Module 6: Repositories and Services
- Service interaction diagrams
- Data flow visualization

### Module 7: Context Mapping
- Strategic design diagrams
- Integration patterns

### Module 8: Fowler Patterns
- Pattern implementation diagrams
- Analysis pattern visualization

### Module 9: SDLC Integration
- Process flow diagrams
- Workflow visualization

### Module 10: Capstone
- Complete system architecture
- Comprehensive domain model

## Best Practices

### Diagram Organization
1. **Start Simple**: Begin with basic entities and relationships
2. **Iterate**: Add complexity gradually
3. **Focus**: One concept per diagram
4. **Consistency**: Use standard notation throughout

### Naming Conventions
- Use IE terminology (Entity Types, Descriptors)
- Follow database naming conventions
- Be consistent with attribute names
- Use meaningful relationship labels

### Version Control
- Include diagrams in Git repository
- Use meaningful commit messages for diagram changes
- Review diagrams in pull requests
- Tag major diagram versions

### Documentation Integration
- Embed diagrams in module documentation
- Link diagrams to related code
- Provide context and explanations
- Keep diagrams up-to-date with code changes

## Learning Resources

### Official Documentation
- [Mermaid Documentation](https://mermaid-js.github.io/mermaid/)
- [Entity Relationship Diagrams](https://mermaid-js.github.io/mermaid/#/entityRelationshipDiagram)
- [Flowcharts](https://mermaid-js.github.io/mermaid/#/flowchart)

### Tutorials and Examples
- [Mermaid Tutorial](https://mermaid-js.github.io/mermaid/#/Tutorials)
- [GitHub Mermaid Support](https://github.blog/2022-02-14-include-diagrams-markdown-files-mermaid/)
- [VS Code Mermaid Extension](https://marketplace.visualstudio.com/items?itemName=bierner.markdown-mermaid)

### Advanced Topics
- Custom themes and styling
- Programmatic diagram generation
- Integration with documentation platforms
- Automated diagram testing and validation

---

*Mermaid provides a powerful way to create and maintain visual representations of your domain models that evolve alongside your code. Master this tool to enhance your domain modeling and communication skills.*