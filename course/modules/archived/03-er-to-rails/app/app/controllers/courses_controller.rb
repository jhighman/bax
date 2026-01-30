# CoursesController - Handles HTTP requests related to courses
#
# This controller mirrors StudentsController, demonstrating that
# Rails' RESTful conventions apply consistently across all resources.
#
# In ER modeling, Course is a separate ENTITY TYPE from Student.
# In Module 4, we'll connect them with an Enrollment relationship.
#
class CoursesController < ApplicationController
  before_action :set_course, only: [:show, :edit, :update, :destroy]

  # GET /courses
  def index
    @courses = Course.all.order(:course_code)

    # Optional filtering by department
    if params[:department].present?
      @courses = @courses.by_department(params[:department])
    end
  end

  # GET /courses/:id
  def show
  end

  # GET /courses/new
  def new
    @course = Course.new
  end

  # POST /courses
  def create
    @course = Course.new(course_params)

    if @course.save
      redirect_to @course, notice: "Course was successfully created."
    else
      render :new, status: :unprocessable_entity
    end
  end

  # GET /courses/:id/edit
  def edit
  end

  # PATCH/PUT /courses/:id
  def update
    if @course.update(course_params)
      redirect_to @course, notice: "Course was successfully updated."
    else
      render :edit, status: :unprocessable_entity
    end
  end

  # DELETE /courses/:id
  def destroy
    @course.destroy
    redirect_to courses_path, notice: "Course was successfully deleted."
  end

  private

  def set_course
    @course = Course.find(params[:id])
  end

  def course_params
    params.require(:course).permit(:course_code, :title, :description, :credits, :department)
  end
end
