# StudentsController - Handles HTTP requests related to students
#
# In Rails, controllers are the "C" in MVC (Model-View-Controller)
# This controller implements full CRUD operations:
#   C - Create (new + create actions)
#   R - Read (index + show actions)
#   U - Update (edit + update actions)
#   D - Delete (destroy action)
#
# Java equivalent: A Spring @Controller with @GetMapping, @PostMapping, etc.
# C equivalent: Functions that handle different routes/endpoints
#
class StudentsController < ApplicationController
  # before_action is a filter that runs before specified actions
  # Java equivalent: @PreAuthorize or HandlerInterceptor
  before_action :set_student, only: [:show, :edit, :update, :destroy]

  # GET /students
  # Displays a list of all students
  #
  # Java equivalent:
  #   @GetMapping("/students")
  #   public String index(Model model) {
  #     model.addAttribute("students", studentRepository.findAll());
  #     return "students/index";
  #   }
  #
  def index
    @students = Student.all.order(:last_name, :first_name)
  end

  # GET /students/:id
  # Displays a single student's details
  #
  # Java equivalent:
  #   @GetMapping("/students/{id}")
  #   public String show(@PathVariable Long id, Model model) { ... }
  #
  def show
    # @student is set by before_action :set_student
  end

  # GET /students/new
  # Displays form for creating a new student
  #
  # Java equivalent:
  #   @GetMapping("/students/new")
  #   public String newStudent(Model model) {
  #     model.addAttribute("student", new Student());
  #     return "students/new";
  #   }
  #
  def new
    @student = Student.new
  end

  # POST /students
  # Creates a new student from form data
  #
  # Java equivalent:
  #   @PostMapping("/students")
  #   public String create(@Valid @ModelAttribute Student student, BindingResult result) {
  #     if (result.hasErrors()) return "students/new";
  #     studentRepository.save(student);
  #     return "redirect:/students/" + student.getId();
  #   }
  #
  def create
    @student = Student.new(student_params)

    if @student.save
      redirect_to @student, notice: "Student was successfully created."
    else
      render :new, status: :unprocessable_entity
    end
  end

  # GET /students/:id/edit
  # Displays form for editing an existing student
  #
  def edit
    # @student is set by before_action :set_student
  end

  # PATCH/PUT /students/:id
  # Updates an existing student from form data
  #
  # Java equivalent:
  #   @PutMapping("/students/{id}")
  #   public String update(@PathVariable Long id, @Valid @ModelAttribute Student student) { ... }
  #
  def update
    if @student.update(student_params)
      redirect_to @student, notice: "Student was successfully updated."
    else
      render :edit, status: :unprocessable_entity
    end
  end

  # DELETE /students/:id
  # Removes a student from the database
  #
  # Java equivalent:
  #   @DeleteMapping("/students/{id}")
  #   public String destroy(@PathVariable Long id) {
  #     studentRepository.deleteById(id);
  #     return "redirect:/students";
  #   }
  #
  def destroy
    @student.destroy
    redirect_to students_path, notice: "Student was successfully deleted."
  end

  private

  # Find student by ID - called before show, edit, update, destroy
  # Raises ActiveRecord::RecordNotFound if not found (Rails renders 404)
  def set_student
    @student = Student.find(params[:id])
  end

  # Strong parameters - whitelist allowed attributes
  # This prevents mass assignment vulnerabilities
  #
  # Java equivalent: Using @ModelAttribute with specific fields or DTOs
  # Security: Only these fields can be set from form data
  #
  def student_params
    params.require(:student).permit(:student_number, :first_name, :last_name, :email, :major)
  end
end
