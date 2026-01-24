# Seeds file - populates the database with sample data
#
# Run with: bin/rails db:seed
# Reset and reseed: bin/rails db:reset (drops, creates, migrates, seeds)
#
# This file should be idempotent - safe to run multiple times.
# We use find_or_create_by! to avoid duplicates.

puts "Seeding students..."

students_data = [
  { student_number: "UCF001", first_name: "Alice", last_name: "Johnson",
    email: "alice.johnson@ucf.edu", major: "Computer Science" },
  { student_number: "UCF002", first_name: "Bob", last_name: "Smith",
    email: "bob.smith@ucf.edu", major: "Information Technology" },
  { student_number: "UCF003", first_name: "Carol", last_name: "Williams",
    email: "carol.williams@ucf.edu", major: "Computer Science" },
  { student_number: "UCF004", first_name: "David", last_name: "Brown",
    email: "david.brown@ucf.edu", major: "Software Engineering" },
  { student_number: "UCF005", first_name: "Eva", last_name: "Martinez",
    email: "eva.martinez@ucf.edu", major: "Data Science" },
  { student_number: "UCF006", first_name: "Frank", last_name: "Garcia",
    email: "frank.garcia@ucf.edu", major: "Computer Science" },
  { student_number: "UCF007", first_name: "Grace", last_name: "Lee",
    email: "grace.lee@ucf.edu", major: "Cybersecurity" },
  { student_number: "UCF008", first_name: "Henry", last_name: "Taylor",
    email: "henry.taylor@ucf.edu", major: "Information Technology" }
]

students_data.each do |attrs|
  Student.find_or_create_by!(student_number: attrs[:student_number]) do |s|
    s.first_name = attrs[:first_name]
    s.last_name = attrs[:last_name]
    s.email = attrs[:email]
    s.major = attrs[:major]
  end
end
puts "  Created #{Student.count} students"

# ============================================================================
# COURSES - New in Module 3
# ============================================================================
puts "\nSeeding courses..."

courses_data = [
  # Computer Science Core
  { course_code: "COP3223", title: "Introduction to Programming with C",
    description: "Introduction to programming concepts using the C language. Topics include data types, control structures, functions, arrays, pointers, and file I/O.",
    credits: 3, department: "Computer Science" },

  { course_code: "COP3502", title: "Computer Science I",
    description: "Problem-solving techniques, algorithms, and data structures. Introduction to object-oriented programming using Java.",
    credits: 3, department: "Computer Science" },

  { course_code: "COP3503", title: "Computer Science II",
    description: "Advanced programming concepts including recursion, linked lists, stacks, queues, trees, and algorithm analysis.",
    credits: 3, department: "Computer Science" },

  { course_code: "COP3330", title: "Object-Oriented Programming",
    description: "Object-oriented design principles, inheritance, polymorphism, exception handling, and GUI development.",
    credits: 3, department: "Computer Science" },

  { course_code: "CDA3103", title: "Computer Organization",
    description: "Computer architecture fundamentals including digital logic, assembly language, CPU design, and memory hierarchy.",
    credits: 3, department: "Computer Science" },

  # Database and Web
  { course_code: "COP4710", title: "Database Systems",
    description: "Relational database design, SQL, normalization, transaction processing, and database administration.",
    credits: 3, department: "Computer Science" },

  { course_code: "COP4813", title: "Web Application Programming",
    description: "Full-stack web development including HTML, CSS, JavaScript, server-side programming, and frameworks.",
    credits: 3, department: "Computer Science" },

  # Software Engineering
  { course_code: "COP4331", title: "Software Engineering I",
    description: "Software development lifecycle, requirements analysis, design patterns, testing, and project management.",
    credits: 3, department: "Computer Science" },

  # Math/Theory
  { course_code: "COT3100", title: "Discrete Structures",
    description: "Logic, sets, relations, functions, graphs, trees, and combinatorics with applications to computer science.",
    credits: 3, department: "Computer Science" },

  { course_code: "COP4020", title: "Programming Languages",
    description: "Programming language concepts including syntax, semantics, type systems, and paradigms.",
    credits: 3, department: "Computer Science" },

  # Graduate
  { course_code: "COP5611", title: "Operating Systems Design",
    description: "Advanced operating system concepts including process management, memory management, and distributed systems.",
    credits: 3, department: "Computer Science" }
]

courses_data.each do |attrs|
  Course.find_or_create_by!(course_code: attrs[:course_code]) do |c|
    c.title = attrs[:title]
    c.description = attrs[:description]
    c.credits = attrs[:credits]
    c.department = attrs[:department]
  end
end
puts "  Created #{Course.count} courses"

puts "\nSeeding complete!"
puts "  #{Student.count} students"
puts "  #{Course.count} courses"
