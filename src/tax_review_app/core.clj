(ns tax-review-app.core
  (:require [ring.adapter.jetty :as jetty]
            [tax-review-app.handler :as handler]))

(defn -main
  [& args]
  (jetty/run-jetty handler/app {:port 3000 :join? false})
  (println "Server running on port 3000"))