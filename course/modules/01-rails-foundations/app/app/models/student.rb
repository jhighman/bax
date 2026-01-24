# Student - A simple Ruby class representing a student entity
#
# In Module 1, we use a Plain Old Ruby Object (PORO) to understand
# Ruby classes before introducing Active Record in Module 2.
#
# For Java developers: This is similar to a POJO (Plain Old Java Object)
# For C developers: Think of this as a struct with associated functions
#
class Student
  # attr_accessor creates getter and setter methods
  # Java equivalent: private fields with public getters/setters
  # C equivalent: struct fields accessed via pointers
  attr_accessor :id, :student_number, :first_name, :last_name, :email, :major

  # Class variable to store all students (in-memory storage)
  # In Module 2, we'll replace this with database storage
  @@students = []
  @@next_id = 1

  def initialize(attributes = {})
    @id = attributes[:id] || (@@next_id += 1) - 1
    @student_number = attributes[:student_number]
    @first_name = attributes[:first_name]
    @last_name = attributes[:last_name]
    @email = attributes[:email]
    @major = attributes[:major]
  end

  # Instance method - operates on a single student
  # Java equivalent: public String fullName() { return firstName + " " + lastName; }
  def full_name
    "#{first_name} #{last_name}"
  end

  # Class methods - operate on the collection of all students
  # Java equivalent: public static List<Student> all() { ... }
  # C equivalent: functions that operate on an array of structs

  def self.all
    @@students
  end

  def self.find(id)
    @@students.find { |s| s.id == id.to_i }
  end

  def self.create(attributes)
    student = new(attributes)
    @@students << student
    student
  end

  def self.count
    @@students.length
  end

  # Seed some sample data for demonstration
  def self.seed_sample_data
    return if @@students.any? # Don't re-seed if already populated

    create(student_number: "UCF001", first_name: "Alice", last_name: "Johnson",
           email: "alice.johnson@ucf.edu", major: "Computer Science")
    create(student_number: "UCF002", first_name: "Bob", last_name: "Smith",
           email: "bob.smith@ucf.edu", major: "Information Technology")
    create(student_number: "UCF003", first_name: "Carol", last_name: "Williams",
           email: "carol.williams@ucf.edu", major: "Computer Science")
    create(student_number: "UCF004", first_name: "David", last_name: "Brown",
           email: "david.brown@ucf.edu", major: "Software Engineering")
    create(student_number: "UCF005", first_name: "Eva", last_name: "Martinez",
           email: "eva.martinez@ucf.edu", major: "Data Science")
  end
end

# Seed data when the model is loaded
Student.seed_sample_data
