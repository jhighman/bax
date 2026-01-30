# Seeds file - populates the database with sample data
#
# Run with: bin/rails db:seed
# Reset and reseed: bin/rails db:reset (drops, creates, migrates, seeds)
#
# This file should be idempotent - safe to run multiple times.
# We use find_or_create_by! to avoid duplicates.
#
# Java equivalent: data.sql or Flyway migrations with INSERT statements
# C equivalent: initialization code that populates data structures

puts "Seeding students..."

students_data = [
  {
    student_number: "UCF001",
    first_name: "Alice",
    last_name: "Johnson",
    email: "alice.johnson@ucf.edu",
    major: "Computer Science"
  },
  {
    student_number: "UCF002",
    first_name: "Bob",
    last_name: "Smith",
    email: "bob.smith@ucf.edu",
    major: "Information Technology"
  },
  {
    student_number: "UCF003",
    first_name: "Carol",
    last_name: "Williams",
    email: "carol.williams@ucf.edu",
    major: "Computer Science"
  },
  {
    student_number: "UCF004",
    first_name: "David",
    last_name: "Brown",
    email: "david.brown@ucf.edu",
    major: "Software Engineering"
  },
  {
    student_number: "UCF005",
    first_name: "Eva",
    last_name: "Martinez",
    email: "eva.martinez@ucf.edu",
    major: "Data Science"
  },
  {
    student_number: "UCF006",
    first_name: "Frank",
    last_name: "Garcia",
    email: "frank.garcia@ucf.edu",
    major: "Computer Science"
  },
  {
    student_number: "UCF007",
    first_name: "Grace",
    last_name: "Lee",
    email: "grace.lee@ucf.edu",
    major: "Cybersecurity"
  },
  {
    student_number: "UCF008",
    first_name: "Henry",
    last_name: "Taylor",
    email: "henry.taylor@ucf.edu",
    major: "Information Technology"
  }
]

students_data.each do |student_attrs|
  # find_or_create_by! finds existing record or creates new one
  # The ! means it will raise an error if validation fails
  student = Student.find_or_create_by!(student_number: student_attrs[:student_number]) do |s|
    s.first_name = student_attrs[:first_name]
    s.last_name = student_attrs[:last_name]
    s.email = student_attrs[:email]
    s.major = student_attrs[:major]
  end
  puts "  #{student.persisted? ? 'Created' : 'Found'}: #{student.full_name} (#{student.student_number})"
end

puts "Done! #{Student.count} students in database."
