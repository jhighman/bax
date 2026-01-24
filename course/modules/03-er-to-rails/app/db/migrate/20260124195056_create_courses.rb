class CreateCourses < ActiveRecord::Migration[8.1]
  def change
    create_table :courses do |t|
      # Course code is the business identifier (like "COP3502")
      # This maps to the domain concept of a unique course identifier
      t.string :course_code, null: false

      # Course title (like "Programming Fundamentals I")
      t.string :title, null: false

      # Full description of the course content
      t.text :description

      # Credit hours (typically 1-4 for undergrad courses)
      t.integer :credits, default: 3

      # Academic department offering the course
      t.string :department

      t.timestamps
    end

    # Unique index on course_code ensures no duplicate courses
    # This is a database-level constraint that matches our domain rule
    add_index :courses, :course_code, unique: true

    # Index on department for faster filtering
    add_index :courses, :department
  end
end
