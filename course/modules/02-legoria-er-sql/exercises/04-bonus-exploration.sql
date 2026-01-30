-- ============================================
-- Module 2: Bonus Exploration
-- Legoria ATS — Extra Exercises
-- ============================================
-- Finished early? These go deeper into the data model.
-- Each one explores a different SQL concept.
-- ============================================

-- -----------------------------------------------------------------
-- Bonus 1: Find candidates with NO applications
-- -----------------------------------------------------------------
-- CONCEPT: LEFT JOIN + IS NULL — finding the absence of a relationship
-- WHY IT MATTERS: In an ATS, candidates with no applications might be
-- sourced leads who haven't applied yet, or data cleanup targets.
-- This is a classic "find orphans" query.

SELECT c.first_name, c.last_name, c.email
FROM candidates c
LEFT JOIN applications a ON a.candidate_id = c.id
WHERE a.id IS NULL;


-- -----------------------------------------------------------------
-- Bonus 2: Show the full history of stage transitions for Sarah Chen
-- -----------------------------------------------------------------
-- CONCEPT: Joining through multiple tables to build a timeline
-- WHY IT MATTERS: The Activity Timeline on Sarah's profile page is
-- built from this data. Each row is a transition — from one stage
-- to the next, with a timestamp. This is how you audit a process.

SELECT c.first_name, c.last_name,
       j.title AS job,
       st.from_stage_id,
       st.to_stage_id,
       s_from.name AS from_stage,
       s_to.name AS to_stage,
       st.created_at AS transitioned_at
FROM stage_transitions st
JOIN applications a ON a.id = st.application_id
JOIN candidates c ON c.id = a.candidate_id
JOIN jobs j ON j.id = a.job_id
LEFT JOIN stages s_from ON s_from.id = st.from_stage_id
JOIN stages s_to ON s_to.id = st.to_stage_id
WHERE c.first_name = 'Sarah' AND c.last_name = 'Chen'
ORDER BY st.created_at;


-- -----------------------------------------------------------------
-- Bonus 3: Count applications by source type
-- -----------------------------------------------------------------
-- CONCEPT: GROUP BY on a categorical column
-- WHY IT MATTERS: Recruiters need to know WHERE candidates come from.
-- This is a common analytics query — which source brings the most
-- applicants? (Career site? Referrals? LinkedIn?)

SELECT a.source_type, COUNT(*) AS application_count
FROM applications a
GROUP BY a.source_type
ORDER BY application_count DESC;


-- -----------------------------------------------------------------
-- Bonus 4: Find jobs with NO applicants
-- -----------------------------------------------------------------
-- CONCEPT: Same LEFT JOIN + IS NULL pattern as Bonus 1, different tables
-- WHY IT MATTERS: Jobs with zero applicants might need better
-- descriptions, higher salary, or more promotion. This is an
-- actionable business metric.

SELECT j.title, j.status, j.location, j.created_at
FROM jobs j
LEFT JOIN applications a ON a.job_id = j.id
WHERE a.id IS NULL
ORDER BY j.created_at;


-- -----------------------------------------------------------------
-- Bonus 5: Show which roles can do what with candidates (matrix style)
-- -----------------------------------------------------------------
-- CONCEPT: Conditional aggregation (CASE WHEN inside GROUP BY)
-- WHY IT MATTERS: This produces a permission matrix — the kind of
-- thing you'd see in an admin settings page. It's a pivot table
-- built with pure SQL. Pretty advanced!

SELECT r.name AS role,
       MAX(CASE WHEN p.action = 'create' THEN '✓' ELSE '—' END) AS can_create,
       MAX(CASE WHEN p.action = 'read'   THEN '✓' ELSE '—' END) AS can_read,
       MAX(CASE WHEN p.action = 'update' THEN '✓' ELSE '—' END) AS can_update,
       MAX(CASE WHEN p.action = 'delete' THEN '✓' ELSE '—' END) AS can_delete
FROM roles r
LEFT JOIN role_permissions rp ON rp.role_id = r.id
LEFT JOIN permissions p ON p.id = rp.permission_id AND p.resource = 'candidates'
GROUP BY r.name
ORDER BY r.name;
