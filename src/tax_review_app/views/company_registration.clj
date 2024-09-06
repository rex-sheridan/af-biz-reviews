(ns tax-review-app.views.company-registration
  (:require [tax-review-app.views.layout :as layout]
            [tax-review-app.db :as db]
            [tax-review-app.google-places-api :as google-api]
            [selmer.parser :as selmer]))

(defn registration-form []
  (layout/application
   (selmer/render-file "company_registration.html" {})))

(defn handle-registration [params]
  (let [company {:name (:name params)
                 :street (:street params)
                 :city (:city params)
                 :state (:state params)
                 :zipcode (:zipcode params)
                 :country (:country params)
                 :phone (:phone params)}
        company-id (db/add-company! company)
        _ (println "Created company " company-id)
        search-results (google-api/search-place company)]
    (layout/application
     (selmer/render-file "google_places_results.html"
                         {:company company
                          :company-id company-id
                          :results (:places search-results)
                          :error (:error search-results)}))))

(defn handle-place-selection [params]
  (let [company-id (Integer/parseInt (:company-id params))
        place-id (:place-id params)]
    (when (and company-id place-id)
      (db/update-company-google-place-id! company-id place-id))
    (if (= place-id "none")
      (layout/application "No matching business selected")
      (let [place-details (google-api/get-place-details place-id)]
        (layout/application
         (selmer/render-file "google_place_reviews.html"
                             {:place-details place-details}))))))