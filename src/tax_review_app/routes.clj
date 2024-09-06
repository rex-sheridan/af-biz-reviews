(ns tax-review-app.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [tax-review-app.views.reviews :as reviews]
            [tax-review-app.views.company-registration :as registration]))

(defn app-routes []
  (routes
   (GET "/" [] (reviews/index))
   (GET "/register" [] (registration/registration-form))
   (POST "/register" {params :params} (registration/handle-registration params))
   (route/resources "/")
   (route/not-found "Not Found")))