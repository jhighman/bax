# Combined Legoria Entity-Relationship Diagram

## Mermaid ERD

```mermaid
erDiagram
    organizations {
        int id PK
        string name
        string subdomain
    }

    users {
        int id PK
        int organization_id FK
        string email
        string first_name
        string last_name
        boolean active
    }

    roles {
        int id PK
        int organization_id FK
        string name
        string description
        boolean system_role
    }

    user_roles {
        int id PK
        int user_id FK
        int role_id FK
        datetime granted_at
        int granted_by_id FK
    }

    permissions {
        int id PK
        string resource
        string action
        string description
    }

    role_permissions {
        int id PK
        int role_id FK
        int permission_id FK
        json conditions
    }

    departments {
        int id PK
        string name
        int organization_id FK
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

    stage_transitions {
        int id PK
        int application_id FK
        int from_stage_id FK
        int to_stage_id FK
        datetime created_at
    }

    %% RBAC relationships
    organizations ||--o{ users : "employs"
    organizations ||--o{ roles : "defines"
    users ||--o{ user_roles : "has"
    roles ||--o{ user_roles : "assigned to"
    roles ||--o{ role_permissions : "grants"
    permissions ||--o{ role_permissions : "granted by"

    %% Pipeline relationships
    organizations ||--o{ departments : "has"
    organizations ||--o{ jobs : "posts"
    organizations ||--o{ candidates : "tracks"
    departments ||--o{ jobs : "categorizes"
    jobs ||--o{ applications : "receives"
    candidates ||--o{ applications : "submits"
    stages ||--o{ applications : "current stage"
    applications ||--o{ stage_transitions : "tracked by"
    stages ||--o{ stage_transitions : "from"
    stages ||--o{ stage_transitions : "to"

    %% Cross-domain relationships
    users ||--o{ jobs : "hiring_manager"
    users ||--o{ jobs : "recruiter"
```

## Two Domains, One Database

This diagram shows both domains together:

### RBAC Domain (left side)
- `users` → `user_roles` → `roles` → `role_permissions` → `permissions`
- Answers: "Who can do what?"

### Pipeline Domain (right side)
- `organizations` → `jobs` → `applications` ← `candidates`
- `applications` → `stages` → `stage_transitions`
- Answers: "Who applied where and how's it going?"

### The Bridge
- `users` connects both domains: they're RBAC subjects AND job team members (hiring manager, recruiter)
- `organizations` scopes everything — both RBAC and pipeline data

### Key Modeling Patterns
1. **Join table** (`user_roles`) — mostly just FKs, some audit columns
2. **Join entity** (`applications`) — FKs plus its own lifecycle, status, timestamps
3. **Two FKs to same table** — `jobs.hiring_manager_id` and `jobs.recruiter_id` both → `users`
4. **State machine** — `applications.status` tracks lifecycle
5. **Audit trail** — `stage_transitions` records every pipeline change
6. **Multi-tenancy** — `organization_id` on nearly every table
