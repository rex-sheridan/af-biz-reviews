(ns company-reviews.server
  (:require [ring.adapter.jetty :as jetty] 
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [company-reviews.routes :as routes]
            [company-reviews.db :as db]))

(defn create-tables[]
  (println "Tables created" (db/create-tables!))
  true)

(def app
  (-> (routes/app-routes)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

(defn run [s] (if (nil? s)
                (jetty/run-jetty app {:port 3000 :join? false})
                s))

(defonce server (atom nil))

(defn start! [] 
  (create-tables)
  (swap! server run))

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


