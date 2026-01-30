Rails.application.routes.draw do
  # Module 3: Two independent entity types
  #
  # In ER modeling terms:
  # - Student is an ENTITY TYPE with its own routes
  # - Course is an ENTITY TYPE with its own routes
  # - No relationship between them yet (that's Module 4)
  #
  # Each 'resources' line creates 7 RESTful routes for CRUD operations

  resources :students
  resources :courses

  # Root path - the homepage
  root "students#index"

  # Health check for deployment
  get "up" => "rails/health#show", as: :rails_health_check
end
