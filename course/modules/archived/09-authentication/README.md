# Module 9: Authentication and Authorization

In this module, we add user accounts with Devise and role-based access control. Security becomes a first-class concern.

## Learning Objectives

By the end of this module, you will be able to:

1. Implement authentication with Devise
2. Create role-based authorization
3. Protect routes and resources
4. Handle user sessions securely
5. Test authenticated features

## DDD Concept: Identity and Access Context

Authentication and authorization form their own bounded context:

- **Identity**: Who is this user? (authentication)
- **Access**: What can they do? (authorization)

This context integrates with others but has its own rules and models.

```
┌─────────────────────────────────────┐
│    Identity & Access Context        │
│─────────────────────────────────────│
│  User                               │
│  ├── email                          │
│  ├── password (encrypted)           │
│  └── role                           │
│                                     │
│  Roles: student, faculty, admin     │
└─────────────────────────────────────┘
         │
         │ authenticates
         ▼
┌─────────────────────────────────────┐
│    Academic Context                 │
│    (sees: current_user.student)     │
└─────────────────────────────────────┘
```

## Java/C Bridge: Spring Security vs Devise

**Spring Security:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/faculty/**").hasRole("FACULTY")
                .anyRequest().authenticated()
            )
            .formLogin()
            .build();
    }
}
```

**Rails Devise:**
```ruby
# routes.rb
devise_for :users

authenticate :user, ->(u) { u.admin? } do
  namespace :admin do
    resources :users
  end
end

# Controller
class Admin::UsersController < ApplicationController
  before_action :authenticate_user!
  before_action :require_admin

  private

  def require_admin
    redirect_to root_path unless current_user.admin?
  end
end
```

## Key Components

### User Model with Roles

```ruby
class User < ApplicationRecord
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :validatable

  enum role: { student: 0, faculty: 1, admin: 2 }

  belongs_to :student, optional: true
  belongs_to :faculty, optional: true

  def display_name
    student&.full_name || faculty&.full_name || email
  end
end
```

### Authorization Helper

```ruby
# app/controllers/application_controller.rb
class ApplicationController < ActionController::Base
  before_action :authenticate_user!

  def require_role(*roles)
    unless roles.any? { |role| current_user.send("#{role}?") }
      redirect_to root_path, alert: "Not authorized"
    end
  end
end

# Usage in controller
class Admin::UsersController < ApplicationController
  before_action -> { require_role(:admin) }
end
```

## App State After This Module

- User registration and login
- Role-based navigation (students see different menu than faculty)
- Protected admin routes
- Session management
- Password reset functionality
- Bootstrap login/signup forms

## Development Practice: Security Mindset

- Never trust user input
- Use strong parameters
- Encrypt sensitive data
- Log authentication events
- Test authorization rules

---

*To be expanded with full instructions and working app.*
