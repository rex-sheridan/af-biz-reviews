(ns tax-review-app.views.company-registration
  (:require [tax-review-app.views.layout :as layout]
            [selmer.parser :as selmer]
            [tax-review-app.db :as db]))

(defn registration-form []
  (layout/application
   (selmer/render-file "company_registration.html" {})))

(defn handle-registration [params]
  ;; TODO: Implement registration logic
  (db/add-company! params)
  (layout/application
   (str "Registration successful for " (params :name))))