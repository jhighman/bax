# StudentsController - Handles HTTP requests related to students
#
# In Rails, controllers are the "C" in MVC (Model-View-Controller)
# Java equivalent: A Spring @Controller or @RestController class
# C equivalent: Functions that handle different routes/endpoints
#
class StudentsController < ApplicationController
  # GET /students
  # Displays a list of all students
  #
  # Java equivalent:
  #   @GetMapping("/students")
  #   public String index(Model model) {
  #     model.addAttribute("students", studentService.findAll());
  #     return "students/index";
  #   }
  #
  def index
    @students = Student.all
  end

  # GET /students/:id
  # Displays a single student's details
  #
  # Java equivalent:
  #   @GetMapping("/students/{id}")
  #   public String show(@PathVariable Long id, Model model) { ... }
  #
  def show
    @student = Student.find(params[:id])

    # Redirect if student not found
    if @student.nil?
      redirect_to students_path, alert: "Student not found"
    end
  end
end
