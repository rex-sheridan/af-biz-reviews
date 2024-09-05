(ns tax-review-app.handler
  (:require
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [tax-review-app.routes :as routes]))

(def app
  (-> (routes/app-routes)
      (wrap-defaults site-defaults)))