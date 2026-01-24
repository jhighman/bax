Rails.application.routes.draw do
  # Student routes - RESTful resource routing
  #
  # This single line creates ALL CRUD routes:
  #   GET    /students          -> students#index   (list all)
  #   GET    /students/new      -> students#new     (form for new)
  #   POST   /students          -> students#create  (save new)
  #   GET    /students/:id      -> students#show    (display one)
  #   GET    /students/:id/edit -> students#edit    (form for edit)
  #   PATCH  /students/:id      -> students#update  (save changes)
  #   DELETE /students/:id      -> students#destroy (remove)
  #
  # Java equivalent: All @RequestMapping annotations for a resource
  # C equivalent: Full URL pattern matching for CRUD operations
  #
  # In Module 2, we enable all actions (removed 'only:' restriction)
  resources :students

  # Root path - the homepage
  root "students#index"

  # Health check for deployment
  get "up" => "rails/health#show", as: :rails_health_check
end
