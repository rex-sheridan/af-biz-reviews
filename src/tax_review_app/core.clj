(ns tax-review-app.core
  (:require [ring.adapter.jetty :as jetty]
            [tax-review-app.handler :as handler]
            [tax-review-app.db :as db]))

(defn create-tables[]
  (println "Tables created" (db/create-tables!))
  true)

(defn run [s] (if (nil? s)
                (jetty/run-jetty handler/app {:port 3000 :join? false})
                s))

(defonce server (atom nil))

(defn start! [] (swap! server run))

(defn stop! [] (swap! server
                      (fn [a] (when-some [s a]
                                (.stop s)
                                (println "Stopped")))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn restart-server! []
  (stop!)
  (start!))

(defn -main []
  (start!)
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop!))
  (println "Started"))

#_((restart-server!))


