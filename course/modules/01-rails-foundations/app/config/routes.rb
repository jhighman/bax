Rails.application.routes.draw do
  # Student routes - RESTful resource routing
  #
  # This single line creates multiple routes:
  #   GET    /students          -> students#index
  #   GET    /students/:id      -> students#show
  #
  # Java equivalent: @RequestMapping annotations for each action
  # C equivalent: URL pattern matching in main() routing logic
  #
  # We use 'only:' to limit routes since Module 1 doesn't have create/update/delete
  resources :students, only: [:index, :show]

  # Root path - the homepage
  # When users visit /, redirect them to the students list
  root "students#index"

  # Health check for deployment
  get "up" => "rails/health#show", as: :rails_health_check
end
