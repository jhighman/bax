# Course - An Active Record model representing an academic course
#
# In ER modeling terms, Course is an ENTITY TYPE with:
# - Identity: course_code (business key) and id (surrogate key)
# - Attributes: title, description, credits, department
#
# For Java developers: Similar to a JPA @Entity class
# For C developers: A struct with database persistence
#
class Course < ApplicationRecord
  # Validations ensure data integrity
  # These map to domain constraints identified in ER modeling
  validates :course_code, presence: true,
                          uniqueness: { case_sensitive: false },
                          format: { with: /\A[A-Z]{3}\d{4}[A-Z]?\z/,
                                    message: "must be like 'COP3502' or 'COP3502C'" }
  validates :title, presence: true
  validates :credits, numericality: { only_integer: true,
                                       greater_than: 0,
                                       less_than_or_equal_to: 6 }

  # Scopes - reusable query fragments
  # Java equivalent: @Query methods in Spring Data repositories
  scope :by_department, ->(dept) { where(department: dept) }
  scope :undergraduate, -> { where("course_code LIKE ?", "___3%").or(where("course_code LIKE ?", "___4%")) }
  scope :graduate, -> { where("course_code LIKE ?", "___5%").or(where("course_code LIKE ?", "___6%")) }

  # Instance methods for domain logic
  def display_name
    "#{course_code}: #{title}"
  end

  def credit_label
    credits == 1 ? "1 credit" : "#{credits} credits"
  end

  # Determine course level from course code
  # UCF convention: 3xxx = junior, 4xxx = senior, 5xxx+ = graduate
  def level
    return nil unless course_code.present?

    level_digit = course_code[3]&.to_i
    case level_digit
    when 1, 2 then "Freshman/Sophomore"
    when 3 then "Junior"
    when 4 then "Senior"
    when 5, 6 then "Graduate"
    else "Unknown"
    end
  end
end
