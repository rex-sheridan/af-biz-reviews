(ns company-reviews.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [company-reviews.views.reviews :as reviews]
            [company-reviews.views.company-registration :as registration]))

(defn app-routes []
  (routes
   (GET "/" [] (reviews/index))
   (GET "/register" [] (registration/registration-form))
   (POST "/register" {params :params} (registration/handle-registration params))
   (POST "/select-place" {params :params} (registration/handle-place-selection params))
   (route/resources "/")
   (route/not-found "Not Found")))