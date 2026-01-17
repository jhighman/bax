# PostgreSQL: Advanced Database Management for DDD and Rails

PostgreSQL is a powerful, open-source relational database management system that provides excellent support for Domain-Driven Design principles and Rails applications. This guide covers installation, configuration, advanced features, and best practices for the DDD course.

## Why PostgreSQL for DDD and Rails Development?

### Advantages
- **ACID Compliance**: Full transaction support for domain consistency
- **Advanced Data Types**: JSON, arrays, custom types for complex descriptors
- **Constraints**: Rich constraint system for enforcing domain rules
- **Performance**: Excellent query optimization and indexing
- **Extensibility**: Custom functions, triggers, and extensions
- **Standards Compliance**: Full SQL standard support
- **Scalability**: Handles large datasets and concurrent users

### DDD-Specific Benefits
- **Domain Integrity**: Database-level constraint enforcement
- **Complex Descriptors**: JSON/JSONB for composite descriptors
- **Audit Trails**: Built-in change tracking capabilities
- **Partitioning**: Bounded context separation at database level
- **Full-Text Search**: Advanced search capabilities for domain data
- **Concurrent Access**: Multi-user domain model access

## Installation and Setup

### macOS Installation

#### Using Homebrew (Recommended)
```bash
# Install PostgreSQL
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Create your user database
createdb $(whoami)

# Access PostgreSQL
psql postgres
```

#### Using Postgres.app
1. Download from [postgresapp.com](https://postgresapp.com/)
2. Drag to Applications folder
3. Launch and initialize
4. Add to PATH: `export PATH="/Applications/Postgres.app/Contents/Versions/latest/bin:$PATH"`

### Windows Installation

#### Using PostgreSQL Installer
1. Download from [postgresql.org](https://www.postgresql.org/download/windows/)
2. Run installer with default settings
3. Remember the superuser password
4. Add to PATH during installation

#### Using WSL2 (Windows Subsystem for Linux)
```bash
# Update package list
sudo apt update

# Install PostgreSQL
sudo apt install postgresql postgresql-contrib

# Start PostgreSQL service
sudo service postgresql start

# Switch to postgres user
sudo -u postgres psql
```

### Linux Installation (Ubuntu/Debian)
```bash
# Update package list
sudo apt update

# Install PostgreSQL and additional modules
sudo apt install postgresql postgresql-contrib postgresql-client

# Start and enable PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Switch to postgres user
sudo -u postgres psql
```

### Initial Configuration

#### Create Development User
```sql
-- Connect as postgres superuser
psql postgres

-- Create development user
CREATE USER your_username WITH PASSWORD 'your_password';
ALTER USER your_username CREATEDB;
ALTER USER your_username WITH SUPERUSER;

-- Exit
\q
```

#### Configure Authentication
```bash
# Edit pg_hba.conf (location varies by system)
# macOS Homebrew: /opt/homebrew/var/postgresql@15/pg_hba.conf
# Linux: /etc/postgresql/15/main/pg_hba.conf

# Add line for local development (trust method for development only)
local   all             your_username                           trust
host    all             your_username       127.0.0.1/32        trust
```

#### Restart PostgreSQL
```bash
# macOS Homebrew
brew services restart postgresql@15

# Linux
sudo systemctl restart postgresql

# Windows
# Use Services app or restart PostgreSQL service
```

## Database Design for DDD

### Creating Course Database
```sql
-- Connect to PostgreSQL
psql postgres

-- Create development database
CREATE DATABASE ucf_course_manager_development;

-- Create test database
CREATE DATABASE ucf_course_manager_test;

-- Create production database (if needed)
CREATE DATABASE ucf_course_manager_production;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE ucf_course_manager_development TO your_username;
GRANT ALL PRIVILEGES ON DATABASE ucf_course_manager_test TO your_username;

-- Connect to development database
\c ucf_course_manager_development
```

### Entity Tables with IE Principles

#### Students Entity Type
```sql
-- Students table with composite descriptors
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    
    -- Name descriptor components
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    
    -- Contact descriptor components
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    
    -- Address descriptor components
    address_street VARCHAR(100),
    address_city VARCHAR(50),
    address_state VARCHAR(2),
    address_zip VARCHAR(10),
    address_country VARCHAR(50) DEFAULT 'USA',
    
    -- Emergency contact descriptor
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(50),
    
    -- Academic descriptor
    major VARCHAR(100),
    gpa DECIMAL(3,2) CHECK (gpa >= 0.0 AND gpa <= 4.0),
    total_credits INTEGER DEFAULT 0 CHECK (total_credits >= 0),
    enrollment_date DATE NOT NULL,
    graduation_date DATE,
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'graduated', 'withdrawn')),
    
    -- Audit fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_students_student_number ON students(student_number);
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_students_last_name ON students(last_name);
CREATE INDEX idx_students_status ON students(status);
CREATE INDEX idx_students_major ON students(major);

-- Partial index for active students
CREATE INDEX idx_students_active ON students(id) WHERE status = 'active';

-- Composite index for name searches
CREATE INDEX idx_students_full_name ON students(last_name, first_name);
```

#### Courses Entity Type
```sql
-- Courses table with academic descriptors
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(200) NOT NULL,
    
    -- Academic descriptor
    credit_hours INTEGER NOT NULL CHECK (credit_hours > 0 AND credit_hours <= 6),
    department VARCHAR(100) NOT NULL,
    course_level VARCHAR(20) CHECK (course_level IN ('undergraduate', 'graduate')),
    
    -- Description and requirements
    description TEXT,
    prerequisites JSONB, -- Array of prerequisite course codes
    corequisites JSONB,  -- Array of corequisite course codes
    
    -- Offering descriptor
    offered_fall BOOLEAN DEFAULT true,
    offered_spring BOOLEAN DEFAULT true,
    offered_summer BOOLEAN DEFAULT false,
    
    -- Status and audit
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'archived')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_courses_course_code ON courses(course_code);
CREATE INDEX idx_courses_department ON courses(department);
CREATE INDEX idx_courses_status ON courses(status);
CREATE INDEX idx_courses_level ON courses(course_level);

-- GIN index for JSON prerequisite searches
CREATE INDEX idx_courses_prerequisites ON courses USING GIN (prerequisites);
```

#### Faculty Entity Type
```sql
-- Faculty table with employment descriptors
CREATE TABLE faculty (
    id BIGSERIAL PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE NOT NULL,
    
    -- Name descriptor
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    title VARCHAR(10) CHECK (title IN ('Dr.', 'Prof.', 'Mr.', 'Ms.', 'Mrs.')),
    
    -- Contact descriptor
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    office_location VARCHAR(100),
    
    -- Employment descriptor
    department VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    employment_type VARCHAR(20) CHECK (employment_type IN ('full-time', 'part-time', 'adjunct')),
    tenure_status VARCHAR(20) CHECK (tenure_status IN ('tenured', 'tenure-track', 'non-tenure')),
    
    -- Compensation descriptor (if needed for domain)
    salary DECIMAL(10,2),
    hire_date DATE NOT NULL,
    
    -- Academic credentials
    highest_degree VARCHAR(50),
    specializations JSONB, -- Array of specialization areas
    
    -- Status and audit
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'retired')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_faculty_employee_id ON faculty(employee_id);
CREATE INDEX idx_faculty_email ON faculty(email);
CREATE INDEX idx_faculty_department ON faculty(department);
CREATE INDEX idx_faculty_status ON faculty(status);
CREATE INDEX idx_faculty_last_name ON faculty(last_name);
```

### Relationship Tables

#### Course Sections (Aggregate Root)
```sql
-- Course sections with scheduling descriptors
CREATE TABLE course_sections (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    faculty_id BIGINT REFERENCES faculty(id) ON DELETE SET NULL,
    
    -- Section descriptor
    section_number VARCHAR(10) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    year INTEGER NOT NULL,
    
    -- Capacity descriptor
    capacity INTEGER NOT NULL CHECK (capacity > 0),
    enrolled_count INTEGER DEFAULT 0 CHECK (enrolled_count >= 0),
    waitlist_count INTEGER DEFAULT 0 CHECK (waitlist_count >= 0),
    
    -- Schedule descriptor
    schedule_days VARCHAR(10), -- e.g., 'MWF', 'TR'
    start_time TIME,
    end_time TIME,
    
    -- Location descriptor
    building VARCHAR(50),
    room VARCHAR(20),
    
    -- Date descriptor
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    
    -- Status and audit
    status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'active', 'completed', 'cancelled')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    UNIQUE(course_id, section_number, semester, year),
    CHECK (enrolled_count <= capacity),
    CHECK (end_time > start_time),
    CHECK (end_date > start_date)
);

-- Indexes
CREATE INDEX idx_course_sections_course_id ON course_sections(course_id);
CREATE INDEX idx_course_sections_faculty_id ON course_sections(faculty_id);
CREATE INDEX idx_course_sections_semester_year ON course_sections(semester, year);
CREATE INDEX idx_course_sections_schedule ON course_sections(schedule_days, start_time);
```

#### Enrollments (Association Entity)
```sql
-- Enrollments with academic progress descriptors
CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_section_id BIGINT NOT NULL REFERENCES course_sections(id) ON DELETE CASCADE,
    
    -- Enrollment descriptor
    enrollment_date DATE NOT NULL,
    enrollment_type VARCHAR(20) DEFAULT 'regular' CHECK (enrollment_type IN ('regular', 'audit', 'pass-fail')),
    
    -- Academic progress descriptor
    status VARCHAR(20) DEFAULT 'enrolled' CHECK (status IN ('enrolled', 'dropped', 'withdrawn', 'completed')),
    drop_date DATE,
    
    -- Grade descriptor
    grade_points DECIMAL(3,2) CHECK (grade_points >= 0.0 AND grade_points <= 4.0),
    letter_grade VARCHAR(2),
    credits_earned DECIMAL(3,1) DEFAULT 0.0,
    
    -- Attendance tracking
    attendance_percentage DECIMAL(5,2) CHECK (attendance_percentage >= 0.0 AND attendance_percentage <= 100.0),
    
    -- Status and audit
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    UNIQUE(student_id, course_section_id),
    CHECK ((status = 'completed' AND grade_points IS NOT NULL) OR status != 'completed'),
    CHECK ((status IN ('dropped', 'withdrawn') AND drop_date IS NOT NULL) OR status NOT IN ('dropped', 'withdrawn'))
);

-- Indexes
CREATE INDEX idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX idx_enrollments_course_section_id ON enrollments(course_section_id);
CREATE INDEX idx_enrollments_status ON enrollments(status);
CREATE INDEX idx_enrollments_enrollment_date ON enrollments(enrollment_date);

-- Composite indexes for common queries
CREATE INDEX idx_enrollments_student_status ON enrollments(student_id, status);
CREATE INDEX idx_enrollments_section_status ON enrollments(course_section_id, status);
```

## Advanced PostgreSQL Features for DDD

### JSON/JSONB for Complex Descriptors

#### Course Prerequisites as JSON
```sql
-- Update courses table with JSON prerequisites
UPDATE courses 
SET prerequisites = '[
    {"course_code": "CIS 3020", "minimum_grade": "C"},
    {"course_code": "MAT 2401", "minimum_grade": "C"}
]'::jsonb
WHERE course_code = 'CIS 3360';

-- Query courses with specific prerequisites
SELECT course_code, course_name, prerequisites
FROM courses
WHERE prerequisites @> '[{"course_code": "CIS 3020"}]';

-- Extract prerequisite course codes
SELECT course_code, 
       jsonb_array_elements(prerequisites)->>'course_code' as prerequisite
FROM courses
WHERE prerequisites IS NOT NULL;
```

#### Student Metadata as JSON
```sql
-- Add metadata column for flexible student data
ALTER TABLE students ADD COLUMN metadata JSONB;

-- Store complex student information
UPDATE students 
SET metadata = '{
    "academic_interests": ["Computer Science", "Data Science"],
    "extracurricular": ["Programming Club", "Math Tutoring"],
    "accommodations": {
        "extended_time": true,
        "note_taking_assistance": false
    },
    "emergency_contacts": [
        {
            "name": "John Doe Sr.",
            "relationship": "Father",
            "phone": "555-0123",
            "primary": true
        }
    ]
}'::jsonb
WHERE student_number = 'UCF12345678';

-- Query students with specific interests
SELECT student_number, first_name, last_name
FROM students
WHERE metadata->'academic_interests' ? 'Data Science';
```

### Custom Data Types for Domain Concepts

#### Grade Type
```sql
-- Create custom grade type
CREATE TYPE grade_info AS (
    points DECIMAL(3,2),
    letter VARCHAR(2),
    credits DECIMAL(3,1)
);

-- Create function to calculate letter grade
CREATE OR REPLACE FUNCTION calculate_letter_grade(points DECIMAL)
RETURNS VARCHAR(2) AS $$
BEGIN
    CASE
        WHEN points >= 3.7 THEN RETURN 'A';
        WHEN points >= 3.3 THEN RETURN 'A-';
        WHEN points >= 3.0 THEN RETURN 'B+';
        WHEN points >= 2.7 THEN RETURN 'B';
        WHEN points >= 2.3 THEN RETURN 'B-';
        WHEN points >= 2.0 THEN RETURN 'C+';
        WHEN points >= 1.7 THEN RETURN 'C';
        WHEN points >= 1.0 THEN RETURN 'D';
        ELSE RETURN 'F';
    END CASE;
END;
$$ LANGUAGE plpgsql;
```

#### Address Type
```sql
-- Create address composite type
CREATE TYPE address_type AS (
    street VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(2),
    zip VARCHAR(10),
    country VARCHAR(50)
);

-- Function to format address
CREATE OR REPLACE FUNCTION format_address(addr address_type)
RETURNS TEXT AS $$
BEGIN
    RETURN CONCAT(
        addr.street, ', ',
        addr.city, ', ',
        addr.state, ' ',
        addr.zip
    );
END;
$$ LANGUAGE plpgsql;
```

### Triggers for Domain Rules

#### Automatic GPA Calculation
```sql
-- Function to calculate student GPA
CREATE OR REPLACE FUNCTION calculate_student_gpa()
RETURNS TRIGGER AS $$
DECLARE
    student_gpa DECIMAL(3,2);
BEGIN
    -- Calculate GPA for the affected student
    SELECT COALESCE(
        SUM(e.grade_points * cs.credit_hours) / NULLIF(SUM(cs.credit_hours), 0),
        0.0
    ) INTO student_gpa
    FROM enrollments e
    JOIN course_sections cs_sec ON e.course_section_id = cs_sec.id
    JOIN courses c ON cs_sec.course_id = c.id
    WHERE e.student_id = COALESCE(NEW.student_id, OLD.student_id)
      AND e.status = 'completed'
      AND e.grade_points IS NOT NULL;
    
    -- Update student's GPA
    UPDATE students 
    SET gpa = student_gpa,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = COALESCE(NEW.student_id, OLD.student_id);
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Create trigger
CREATE TRIGGER trigger_calculate_gpa
    AFTER INSERT OR UPDATE OR DELETE ON enrollments
    FOR EACH ROW
    EXECUTE FUNCTION calculate_student_gpa();
```

#### Enrollment Capacity Management
```sql
-- Function to manage enrollment counts
CREATE OR REPLACE FUNCTION manage_enrollment_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Increase enrolled count
        UPDATE course_sections 
        SET enrolled_count = enrolled_count + 1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = NEW.course_section_id;
        
        -- Check capacity
        IF (SELECT enrolled_count FROM course_sections WHERE id = NEW.course_section_id) > 
           (SELECT capacity FROM course_sections WHERE id = NEW.course_section_id) THEN
            RAISE EXCEPTION 'Course section is at capacity';
        END IF;
        
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        -- Decrease enrolled count
        UPDATE course_sections 
        SET enrolled_count = enrolled_count - 1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = OLD.course_section_id;
        
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        -- Handle status changes
        IF OLD.status = 'enrolled' AND NEW.status != 'enrolled' THEN
            UPDATE course_sections 
            SET enrolled_count = enrolled_count - 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = OLD.course_section_id;
        ELSIF OLD.status != 'enrolled' AND NEW.status = 'enrolled' THEN
            UPDATE course_sections 
            SET enrolled_count = enrolled_count + 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = NEW.course_section_id;
        END IF;
        
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Create trigger
CREATE TRIGGER trigger_manage_enrollment_count
    AFTER INSERT OR UPDATE OR DELETE ON enrollments
    FOR EACH ROW
    EXECUTE FUNCTION manage_enrollment_count();
```

### Views for Domain Queries

#### Student Academic Summary
```sql
-- View for student academic summary
CREATE VIEW student_academic_summary AS
SELECT 
    s.id,
    s.student_number,
    s.first_name || ' ' || s.last_name AS full_name,
    s.major,
    s.gpa,
    s.total_credits,
    COUNT(e.id) FILTER (WHERE e.status = 'enrolled') AS current_enrollments,
    COUNT(e.id) FILTER (WHERE e.status = 'completed') AS completed_courses,
    AVG(e.grade_points) FILTER (WHERE e.status = 'completed') AS average_grade,
    s.enrollment_date,
    s.status
FROM students s
LEFT JOIN enrollments e ON s.id = e.student_id
GROUP BY s.id, s.student_number, s.first_name, s.last_name, 
         s.major, s.gpa, s.total_credits, s.enrollment_date, s.status;
```

#### Course Enrollment Statistics
```sql
-- View for course enrollment statistics
CREATE VIEW course_enrollment_stats AS
SELECT 
    c.course_code,
    c.course_name,
    c.department,
    cs.semester,
    cs.year,
    cs.section_number,
    f.first_name || ' ' || f.last_name AS instructor_name,
    cs.capacity,
    cs.enrolled_count,
    ROUND((cs.enrolled_count::DECIMAL / cs.capacity) * 100, 2) AS enrollment_percentage,
    COUNT(e.id) FILTER (WHERE e.status = 'completed') AS completed_count,
    AVG(e.grade_points) FILTER (WHERE e.status = 'completed') AS average_grade
FROM courses c
JOIN course_sections cs ON c.id = cs.course_id
LEFT JOIN faculty f ON cs.faculty_id = f.id
LEFT JOIN enrollments e ON cs.id = e.course_section_id
GROUP BY c.id, c.course_code, c.course_name, c.department,
         cs.id, cs.semester, cs.year, cs.section_number,
         f.first_name, f.last_name, cs.capacity, cs.enrolled_count;
```

## Performance Optimization

### Indexing Strategies

#### Composite Indexes for Common Queries
```sql
-- Index for student course history queries
CREATE INDEX idx_enrollments_student_semester 
ON enrollments(student_id, course_section_id) 
INCLUDE (status, grade_points, enrollment_date);

-- Index for course scheduling queries
CREATE INDEX idx_course_sections_schedule_lookup
ON course_sections(semester, year, schedule_days, start_time)
WHERE status = 'active';

-- Partial index for active enrollments
CREATE INDEX idx_enrollments_active
ON enrollments(student_id, course_section_id)
WHERE status = 'enrolled';
```

#### Full-Text Search
```sql
-- Add full-text search for courses
ALTER TABLE courses ADD COLUMN search_vector tsvector;

-- Update search vector
UPDATE courses 
SET search_vector = to_tsvector('english', 
    course_code || ' ' || course_name || ' ' || COALESCE(description, '')
);

-- Create index for full-text search
CREATE INDEX idx_courses_search ON courses USING GIN(search_vector);

-- Create trigger to maintain search vector
CREATE OR REPLACE FUNCTION update_course_search_vector()
RETURNS TRIGGER AS $$
BEGIN
    NEW.search_vector = to_tsvector('english',
        NEW.course_code || ' ' || NEW.course_name || ' ' || COALESCE(NEW.description, '')
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_course_search
    BEFORE INSERT OR UPDATE ON courses
    FOR EACH ROW
    EXECUTE FUNCTION update_course_search_vector();
```

### Query Optimization

#### Efficient Pagination
```sql
-- Efficient pagination for large student lists
SELECT s.*, COUNT(*) OVER() as total_count
FROM students s
WHERE s.status = 'active'
ORDER BY s.last_name, s.first_name
LIMIT 25 OFFSET 0;

-- Using cursor-based pagination for better performance
SELECT s.*
FROM students s
WHERE s.status = 'active'
  AND (s.last_name, s.first_name, s.id) > ('Smith', 'John', 12345)
ORDER BY s.last_name, s.first_name, s.id
LIMIT 25;
```

#### Optimized Aggregation Queries
```sql
-- Efficient department statistics
WITH department_stats AS (
    SELECT 
        c.department,
        COUNT(DISTINCT c.id) as course_count,
        COUNT(DISTINCT cs.id) as section_count,
        COUNT(DISTINCT e.student_id) as unique_students,
        SUM(cs.enrolled_count) as total_enrollments
    FROM courses c
    LEFT JOIN course_sections cs ON c.id = cs.course_id
    LEFT JOIN enrollments e ON cs.id = e.course_section_id
    WHERE cs.semester = 'Fall' AND cs.year = 2024
    GROUP BY c.department
)
SELECT 
    department,
    course_count,
    section_count,
    unique_students,
    total_enrollments,
    ROUND(total_enrollments::DECIMAL / section_count, 2) as avg_enrollment_per_section
FROM department_stats
ORDER BY total_enrollments DESC;
```

## Backup and Recovery

### Automated Backups
```bash
#!/bin/bash
# backup_script.sh

# Configuration
DB_NAME="ucf_course_manager_development"
BACKUP_DIR="/path/to/backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_${DATE}.sql"

# Create backup directory if it doesn't exist
mkdir -p $BACKUP_DIR

# Create backup
pg_dump $DB_NAME > $BACKUP_FILE

# Compress backup
gzip $BACKUP_FILE

# Remove backups older than 30 days
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete

echo "Backup completed: ${BACKUP_FILE}.gz"
```

### Point-in-Time Recovery Setup
```sql
-- Enable WAL archiving (in postgresql.conf)
-- wal_level = replica
-- archive_mode = on
-- archive_command = 'cp %p /path/to/archive/%f'

-- Create base backup
SELECT pg_start_backup('base_backup');
-- Copy data directory
SELECT pg_stop_backup();
```

## Monitoring and Maintenance

### Performance Monitoring Queries
```sql
-- Check slow queries
SELECT 
    query,
    calls,
    total_time,
    mean_time,
    rows
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- Check index usage
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;

-- Check table sizes
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

### Maintenance Tasks
```sql
-- Update table statistics
ANALYZE;

-- Rebuild indexes if needed
REINDEX INDEX CONCURRENTLY idx_students_student_number;

-- Clean up dead tuples
VACUUM ANALYZE students;

-- Check for bloat
SELECT 
    schemaname,
    tablename,
    n_dead_tup,
    n_live_tup,
    ROUND(n_dead_tup::DECIMAL / NULLIF(n_live_tup, 0) * 100, 2) as dead_percentage
FROM pg_stat_user_tables
WHERE n_dead_tup > 0
ORDER BY dead_percentage DESC;
```

## Security Best Practices

### User Management
```sql
-- Create application user with limited privileges
CREATE USER app_user WITH PASSWORD 'secure_password';

-- Grant specific permissions
GRANT CONNECT ON DATABASE ucf_course_manager_development TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO app_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO app_user;

-- Create read-only user for reporting
CREATE USER report_user WITH PASSWORD 'report_password';
GRANT CONNECT ON DATABASE ucf_course_manager_development TO report_user;
GRANT USAGE ON SCHEMA public TO report_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO report_user;
```

### Row-Level Security
```sql
-- Enable row-level security for sensitive data
ALTER TABLE students ENABLE ROW LEVEL SECURITY;

-- Create policy for student data access
CREATE POLICY student_access_policy ON students
    FOR ALL
    TO app_user
    USING (status = 'active');

-- Create policy for faculty to see their students
CREATE POLICY faculty_student_access ON enrollments
    FOR SELECT
    TO faculty_user
    USING (
        course_section_id IN (
            SELECT id FROM course_sections 
            WHERE faculty_id = current_setting('app.current_faculty_id')::bigint
        )
    );
```

## Integration with Rails

### Database Configuration
```yaml
# config/database.yml
development:
  adapter: postgresql
  encoding: unicode
  database: ucf_course_manager_development
  pool: <%= ENV.fetch("RAILS_MAX_THREADS") { 5 } %>
  username: your_username
  password: your_password
  host: localhost
  port: 5432

test:
  adapter: postgresql
  encoding: unicode
  database: ucf_course_manager_test
  pool: <%= ENV.fetch("RAILS_MAX_THREADS") { 5 } %>
  username: your_username
  password: your_password
  host: localhost
  port: 5432

production:
  adapter: postgresql
  encoding: unicode
  database: ucf_course_manager_production
  pool: <%= ENV.fetch("RAILS_MAX_THREADS") { 5 } %>
  username: <%= ENV['DATABASE_USERNAME'] %>
  password: <%= ENV['DATABASE_PASSWORD'] %>
  host: <%= ENV['DATABASE_HOST'] %>
  port: <%= ENV['DATABASE_PORT'] %>
```

### Rails Migration Best Practices
```ruby
# Use PostgreSQL-specific features in migrations
class AddJsonPrerequisitesToCourses < ActiveRecord::Migration[7.0]
  def change
    add_column :courses, :prerequisites, :jsonb
    add_index :courses, :prerequisites, using: :gin
  end
end

# Add check constraints
class AddGradeConstraints < ActiveRecord::Migration[7.0]
  def change
    add_check_constraint :enrollments, 
      'grade_points >= 0.0 AND grade_points <= 4.0', 
      name: 'valid_grade_range'
  end
end

# Create custom functions
class CreateGpaCalculationFunction < ActiveRecord::Migration[7.0]
  def up
    execute <<-SQL
      CREATE OR REPLACE FUNCTION calculate_gpa(student_id BIGINT)
      RETURNS DECIMAL(3,2) AS $$
      DECLARE
        gpa_value DECIMAL(3,2);
      BEGIN
        SELECT COALESCE(
          SUM(e.grade_points * c.credit_hours) / NULLIF(SUM(c.credit_hours), 0),
          0.0
        ) INTO gpa_value
        FROM enrollments e
        JOIN course_sections cs ON e.course_section_id = cs.id
        JOIN courses c ON cs.course_id = c.id
        WHERE e.student_id = $1
          AND e.