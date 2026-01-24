# Student - An Active Record model representing a student entity
#
# In Module 2, we've upgraded from a plain Ruby class to Active Record.
# Active Record is Rails' ORM (Object-Relational Mapping) that handles
# database interactions automatically.
#
# For Java developers: This is similar to JPA/Hibernate entities
# For C developers: Think of this as a struct that automatically persists to a database
#
class Student < ApplicationRecord
  # Validations ensure data integrity at the application level
  # These complement database constraints (belt AND suspenders)
  #
  # Java equivalent: @NotNull, @Size, @Email annotations with Bean Validation
  validates :student_number, presence: true, uniqueness: true
  validates :first_name, presence: true
  validates :last_name, presence: true
  validates :email, format: { with: URI::MailTo::EMAIL_REGEXP }, allow_blank: true

  # Instance method - works on a single student record
  # Same as Module 1, but now the data comes from the database
  def full_name
    "#{first_name} #{last_name}"
  end

  # Active Record provides these class methods automatically:
  #
  #   Student.all        - SELECT * FROM students
  #   Student.find(id)   - SELECT * FROM students WHERE id = ?
  #   Student.where(...) - SELECT * FROM students WHERE ...
  #   Student.count      - SELECT COUNT(*) FROM students
  #   Student.first      - SELECT * FROM students ORDER BY id LIMIT 1
  #   Student.last       - SELECT * FROM students ORDER BY id DESC LIMIT 1
  #
  # Java equivalent:
  #   studentRepository.findAll()
  #   studentRepository.findById(id)
  #   studentRepository.findByMajor(major)
  #
  # No need to write SQL or implement these methods - Active Record does it!
end
