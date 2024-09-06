(ns company-reviews.views.reviews
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [company-reviews.views.layout :as layout]))

(defn index []
  (layout/application 
                      [:div#content
                       [:h1 "Tax & Accounting Business Reviews"]
                       [:div#reviews
                        [:h2 "Reviews will be loaded here using HTMX"]]]))