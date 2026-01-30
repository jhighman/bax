# Pipeline Entity-Relationship Diagram

## Mermaid ERD

```mermaid
erDiagram
    organizations {
        int id PK
        string name
        string subdomain
    }

    jobs {
        int id PK
        int organization_id FK
        int department_id FK
        string title
        string status
        int hiring_manager_id FK
        int recruiter_id FK
        string location
        int salary_min
        int salary_max
    }

    candidates {
        int id PK
        int organization_id FK
        string first_name
        string last_name
        string email
        string encrypted_phone
    }

    applications {
        int id PK
        int organization_id FK
        int job_id FK
        int candidate_id FK
        int current_stage_id FK
        string status
        string source_type
        datetime applied_at
        datetime hired_at
        datetime rejected_at
    }

    stages {
        int id PK
        string name
        int position
    }

    departments {
        int id PK
        string name
        int organization_id FK
    }

    users {
        int id PK
        string email
        string first_name
        string last_name
    }

    organizations ||--o{ jobs : "posts"
    organizations ||--o{ candidates : "tracks"
    organizations ||--o{ departments : "has"
    departments ||--o{ jobs : "categorizes"
    jobs ||--o{ applications : "receives"
    candidates ||--o{ applications : "submits"
    stages ||--o{ applications : "current stage"
    users ||--o{ jobs : "hiring_manager"
    users ||--o{ jobs : "recruiter"
```

## Reading the Diagram

- **Organizations** own everything (multi-tenant)
- **Jobs** belong to an org and a department, with two FK references to **Users** (hiring manager & recruiter)
- **Candidates** belong to an org
- **Applications** is the bridge entity between Jobs and Candidates — with its own lifecycle
- **Stages** define the pipeline positions (Applied → Screening → Interview → Offer → Hired)
- `applications.status` is a state machine: `new → screening → interviewing → offer → hired/rejected/withdrawn`
