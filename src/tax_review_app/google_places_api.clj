(ns tax-review-app.google-places-api
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def api-key (if-let [key (System/getenv "GOOGLE_PLACES_API_KEY")]
               key
               "AIzaSyAUNA9dhE-Wunq4u3H9j0fA4wt_H7PQHFM"))
(def api-url "https://places.googleapis.com/v1/places:searchText")

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

(def places-details-url "https://places.googleapis.com/v1/places/")

(defn get-place-reviews [place-id]
  (let [url (str places-details-url place-id)
        response (http/get url
                           {:headers {"Content-Type" "application/json"
                                      "X-Goog-Api-Key" api-key
                                      "X-Goog-FieldMask" "id,rating,userRatingCount,displayName,reviews"}
                            :throw-exceptions false})]
    (if (= (:status response) 200)
      (let [data (json/parse-string (:body response) true)]
        {:rating (:rating data)
         :user-rating-count (:userRatingCount data)
         :display-name (get-in data [:displayName :text])
         :reviews (map (fn [review]
                         {:relative-publish-time (:relativePublishTimeDescription review)
                          :rating (:rating review)
                          :text (get-in review [:text :text])
                          :author-name (get-in review [:authorAttribution :displayName])
                          :author-uri (get-in review [:authorAttribution :uri])
                          :publish-time (:publishTime review)})
                       (:reviews data))})
      (do (println response)
          {:error "Unable to fetch place details"}))))