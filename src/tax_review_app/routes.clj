(ns tax-review-app.routes
  (:require [compojure.core :refer [routes GET]]
            [compojure.route :as route]
            [tax-review-app.views.reviews :as reviews]))

(defn app-routes []
  (routes
   (GET "/" [] (reviews/index))
   (route/resources "/")
   (route/not-found "Not Found")))