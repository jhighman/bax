-- ============================================
-- Module 2: RBAC SQL Exercises
-- Legoria ATS — Role-Based Access Control
-- ============================================
-- Run these in the SQLite console:
--   cd /path/to/legoria && rails dbconsole
--
-- Pro tip: Run these first for readable output:
--   .headers on
--   .mode column
-- ============================================

-- -----------------------------------------------------------------
-- Exercise 1: What roles does Alice Admin have?
-- -----------------------------------------------------------------
-- CONCEPT: Basic 3-table JOIN chain (user → user_roles → roles)
-- WHY IT MATTERS: This is the fundamental RBAC lookup. Every time
-- a user logs in, the app runs a query like this to figure out
-- what they're allowed to do.
-- EXPECTED: Alice should have the 'admin' role

SELECT u.first_name, u.last_name, r.name AS role_name, ur.granted_at
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id
WHERE u.email = 'admin@acme.test';


-- -----------------------------------------------------------------
-- Exercise 2: What can the Recruiter role do?
-- -----------------------------------------------------------------
-- CONCEPT: Walking the other side of the chain (role → role_permissions → permissions)
-- WHY IT MATTERS: Shows how granular permissions are — each one is a
-- specific resource + action pair. "Can read candidates" and "can delete
-- candidates" are two separate permissions.
-- LOOK FOR: Notice the pattern — every permission is a (resource, action) tuple

SELECT r.name AS role, p.resource, p.action, p.description
FROM roles r
JOIN role_permissions rp ON rp.role_id = r.id
JOIN permissions p ON p.id = rp.permission_id
WHERE r.name = 'recruiter'
ORDER BY p.resource, p.action;


-- -----------------------------------------------------------------
-- Exercise 3: Can Ian Interviewer edit candidates?
-- -----------------------------------------------------------------
-- CONCEPT: Full RBAC chain with LEFT JOIN — checking for the ABSENCE
-- of a permission, not just the presence of one
-- WHY IT MATTERS: This is what the application does on every page load.
-- The LEFT JOIN is key — if the permission doesn't exist, we get NULL
-- instead of no rows. That lets us distinguish "no permission" from
-- "user doesn't exist."
-- EXPECTED: Ian should NOT have candidates.update permission

SELECT u.first_name, p.resource, p.action,
       CASE WHEN p.id IS NOT NULL THEN 'YES' ELSE 'NO' END AS has_access
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id
LEFT JOIN role_permissions rp ON rp.role_id = r.id
LEFT JOIN permissions p ON p.id = rp.permission_id
  AND p.resource = 'candidates' AND p.action = 'update'
WHERE u.email = 'interviewer@acme.test';


-- -----------------------------------------------------------------
-- Exercise 4: Compare permissions across roles
-- -----------------------------------------------------------------
-- CONCEPT: GROUP BY with COUNT on a join table — aggregate analysis
-- WHY IT MATTERS: Quick audit view. Which role is the most powerful?
-- Which has the fewest permissions? This is what an admin dashboard shows.
-- EXPECTED: Admin should have the most, Interviewer the fewest

SELECT r.name AS role, COUNT(rp.id) AS permission_count
FROM roles r
LEFT JOIN role_permissions rp ON rp.role_id = r.id
GROUP BY r.name
ORDER BY permission_count DESC;
