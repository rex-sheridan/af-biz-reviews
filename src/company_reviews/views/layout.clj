(ns company-reviews.views.layout
  (:require [selmer.parser :as selmer]
            [ring.util.response :as response]
            [clojure.java.io :as io]))

(selmer/set-resource-path! "templates/")

(defn render [template & [params]]
  (-> (selmer/render-file template params)
      (response/response)
      (response/content-type "text/html; charset=utf-8")))

(defn application [content]
  (render "index.html" {:content content}))