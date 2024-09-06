(ns tax-review-app.handler
  (:require
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [tax-review-app.routes :as routes]
            [tax-review-app.db :as db]))

(def app
  (-> (routes/app-routes)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))