-- ============================================
-- Module 2: Challenge Query
-- Legoria ATS — Combining RBAC + Pipeline
-- ============================================
-- THE QUESTION: "Can recruiter Rachel see all applications
-- for jobs she's assigned to?"
--
-- This ties both domains together:
--   RBAC: Does she have the permission?
--   Pipeline: Which jobs is she on? What are their applicants?
--
-- In a real app, the code checks RBAC first (can she read
-- applications at all?), then scopes the query (only show
-- jobs she's assigned to). Two data model segments, one
-- business question.
-- ============================================

-- -----------------------------------------------------------------
-- Step 1: Does Rachel have the permission?
-- -----------------------------------------------------------------
-- CONCEPT: Full RBAC chain traversal — checking a specific permission
-- WHY IT MATTERS: This is the authorization check. If this returns
-- zero rows, the app would show a 403 Forbidden page. The user
-- never even gets to see the data.

SELECT p.resource, p.action
FROM users u
JOIN user_roles ur ON ur.user_id = u.id
JOIN role_permissions rp ON rp.role_id = ur.role_id
JOIN permissions p ON p.id = rp.permission_id
WHERE u.email = 'recruiter@acme.test'
  AND p.resource = 'applications' AND p.action = 'read';

-- If this returns a row → she's authorized. Proceed to Step 2.
-- If empty → 403 Forbidden. Stop here.


-- -----------------------------------------------------------------
-- Step 2: Which jobs is she recruiter for?
-- -----------------------------------------------------------------
-- CONCEPT: FK relationship — jobs.recruiter_id → users.id
-- WHY IT MATTERS: Even with permission, she only sees HER jobs.
-- This is "data scoping" — the same permission, different data
-- for different users.

SELECT j.title
FROM jobs j
JOIN users u ON u.id = j.recruiter_id
WHERE u.email = 'recruiter@acme.test';


-- -----------------------------------------------------------------
-- Step 3: The Full Picture
-- -----------------------------------------------------------------
-- CONCEPT: Combining RBAC authorization with pipeline data scoping
-- WHY IT MATTERS: This is what the application actually executes
-- (after the RBAC check passes). Notice the subquery in WHERE —
-- that's a common pattern for scoping by the current user.

SELECT j.title,
       c.first_name || ' ' || c.last_name AS candidate,
       s.name AS stage,
       a.status
FROM applications a
JOIN jobs j ON j.id = a.job_id
JOIN candidates c ON c.id = a.candidate_id
JOIN stages s ON s.id = a.current_stage_id
WHERE j.recruiter_id = (SELECT id FROM users WHERE email = 'recruiter@acme.test')
ORDER BY j.title, s.position;


-- -----------------------------------------------------------------
-- DISCUSSION QUESTIONS:
-- -----------------------------------------------------------------
-- 1. What would happen if we removed the WHERE clause in Step 3?
--    (She'd see ALL applications — that's a security bug!)
--
-- 2. What if Rachel had both Recruiter and Hiring Manager roles?
--    Would she see jobs where she's either? How would you write that?
--
-- 3. The app runs Step 1 and Step 3 separately. Could you combine
--    them into one query? Would you want to? Why or why not?
