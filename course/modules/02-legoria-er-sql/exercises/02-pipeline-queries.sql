-- ============================================
-- Module 2: Pipeline SQL Exercises
-- Legoria ATS — Job → Applicant Pipeline
-- ============================================
-- Run these in the SQLite console:
--   cd /path/to/legoria && rails dbconsole
--
-- Pro tip: Run these first for readable output:
--   .headers on
--   .mode column
-- ============================================

-- -----------------------------------------------------------------
-- Exercise 1: List all open jobs with their applicant count
-- -----------------------------------------------------------------
-- CONCEPT: LEFT JOIN + GROUP BY + COUNT — the bread and butter of list pages
-- WHY IT MATTERS: This is exactly what the Jobs list page runs.
-- LEFT JOIN because we want to show jobs even if they have zero applicants.
-- If we used INNER JOIN, jobs with no applications would disappear.
-- TRY: Change LEFT JOIN to JOIN and see what happens!

SELECT j.title, j.status, j.location, COUNT(a.id) AS applicants
FROM jobs j
LEFT JOIN applications a ON a.job_id = j.id
WHERE j.status = 'open'
GROUP BY j.id, j.title, j.status, j.location
ORDER BY applicants DESC;


-- -----------------------------------------------------------------
-- Exercise 2: Who applied to Senior Software Engineer?
-- -----------------------------------------------------------------
-- CONCEPT: 4-table JOIN chain (applications → candidates + stages + jobs)
-- WHY IT MATTERS: This is what the Job Detail "applicants" tab shows.
-- Notice we're joining in three different directions from applications —
-- it's the center of a star pattern.

SELECT c.first_name, c.last_name, c.email,
       s.name AS current_stage, a.status, a.applied_at
FROM applications a
JOIN candidates c ON c.id = a.candidate_id
JOIN stages s ON s.id = a.current_stage_id
JOIN jobs j ON j.id = a.job_id
WHERE j.title = 'Senior Software Engineer'
ORDER BY a.applied_at;


-- -----------------------------------------------------------------
-- Exercise 3: Show the pipeline distribution (the dashboard!)
-- -----------------------------------------------------------------
-- CONCEPT: GROUP BY on a joined column with ORDER BY position
-- WHY IT MATTERS: This is LITERALLY the dashboard widget. You're writing
-- the exact query that generates the pipeline chart. The stages have a
-- 'position' column so they display in the right order — that's a
-- design decision in the schema.

SELECT s.name AS stage, COUNT(a.id) AS count
FROM applications a
JOIN stages s ON s.id = a.current_stage_id
WHERE a.status NOT IN ('hired', 'rejected', 'withdrawn')
GROUP BY s.name
ORDER BY s.position;


-- -----------------------------------------------------------------
-- Exercise 4: Which hiring manager has the most open jobs?
-- -----------------------------------------------------------------
-- CONCEPT: JOIN to the same table (users) via a specific FK + aggregate
-- WHY IT MATTERS: Shows the two-FK-to-same-table pattern in action.
-- jobs.hiring_manager_id and jobs.recruiter_id BOTH point to users,
-- but they mean different things. Here we follow hiring_manager_id.
-- TRY: Modify this to show recruiter instead of hiring manager!

SELECT u.first_name || ' ' || u.last_name AS hiring_manager,
       COUNT(j.id) AS open_jobs
FROM jobs j
JOIN users u ON u.id = j.hiring_manager_id
WHERE j.status = 'open'
GROUP BY u.id, u.first_name, u.last_name;
