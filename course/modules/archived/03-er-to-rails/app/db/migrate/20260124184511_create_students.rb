class CreateStudents < ActiveRecord::Migration[8.1]
  def change
    create_table :students do |t|
      # student_number is required and must be unique (like a primary key for the domain)
      t.string :student_number, null: false

      # Name fields
      t.string :first_name
      t.string :last_name

      # Contact
      t.string :email

      # Academic info
      t.string :major

      # Rails automatically manages these timestamp columns
      t.timestamps
    end

    # Unique index ensures no duplicate student numbers at the database level
    # This is a constraint that protects data integrity even if application code has bugs
    add_index :students, :student_number, unique: true

    # Index on email for faster lookups (common query pattern)
    add_index :students, :email
  end
end
