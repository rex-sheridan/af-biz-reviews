(ns company-reviews.google-places-api
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def api-key (if-let [key (System/getenv "GOOGLE_PLACES_API_KEY")]
               key
               "AIzaSyAUNA9dhE-Wunq4u3H9j0fA4wt_H7PQHFM"))
(def api-url "https://places.googleapis.com/v1/places:searchText")
(def details-url "https://places.googleapis.com/v1/places/")


(defn search-place [company]
  (let [query (str (:name company) ", " (:street company) ", " (:city company) ", " (:state company) " " (:zipcode company))
        body (json/generate-string {:textQuery query})
        _ (println body)
        response (http/post api-url
                            {:body body
                             :headers {"Content-Type" "application/json"
                                       "X-Goog-Api-Key" api-key
                                       "X-Goog-FieldMask" "places.id,places.displayName,places.formattedAddress"}
                             :throw-exceptions false})]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (do (println response)
       {:error "Google search results are currently unavailable"}))))


(defn get-place-details [place-id]
  (let [response (http/get (str details-url place-id)
                           {:headers {"Content-Type" "application/json"
                                      "X-Goog-Api-Key" api-key
                                      "X-Goog-FieldMask" "id,displayName,rating,userRatingCount,reviews"}
                            :throw-exceptions false})]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (do (println response)
       {:error "Unable to fetch place details"}))))
