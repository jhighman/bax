# RBAC Entity-Relationship Diagram

## Mermaid ERD

```mermaid
erDiagram
    users {
        int id PK
        int org_id FK
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

    users ||--o{ user_roles : "has"
    roles ||--o{ user_roles : "assigned to"
    roles ||--o{ role_permissions : "grants"
    permissions ||--o{ role_permissions : "granted by"
    users ||--o{ user_roles : "granted_by"
```

## Reading the Diagram

- **Users** connect to **Roles** through the **user_roles** join table
- **Roles** connect to **Permissions** through the **role_permissions** join table
- Two degrees of separation: User → Role → Permission
- `user_roles` has audit columns (`granted_at`, `granted_by_id`) — it's not just two FKs
- `permissions` uses a resource + action pattern (e.g., `candidates` + `read`)
- Everything scoped by `organization_id` for multi-tenancy
