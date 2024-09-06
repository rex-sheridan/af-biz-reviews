(ns company-reviews.db
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [insert-into select from values where update set returning]]
            [clojure.java.io :as io])
  (:refer-clojure :exclude [update set]))


(def db-file "database/reviews.db")

(def db-spec
  {:dbtype "sqlite"
   :dbname db-file})

(defn ensure-db-exists []
  (let [db-dir (io/file "database")]
    (when-not (.exists db-dir)
      (.mkdirs db-dir))
    (when-not (.exists (io/file db-file))
      (jdbc/get-connection db-spec))))

(defn create-tables! []
  (ensure-db-exists)
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS companies
                        (id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT,
                         street TEXT,
                         city TEXT,
                         state TEXT,
                         zipcode TEXT,
                         country TEXT,
                         phone TEXT,
                         Google_Place_ID TEXT,
                         Google_Place_ID_Last_Updated TEXT)"])
    (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS reviews
                        (id INTEGER PRIMARY KEY AUTOINCREMENT,
                         company_id INTEGER,
                         rating INTEGER,
                         review_text TEXT,
                         source TEXT,
                         FOREIGN KEY(company_id) REFERENCES companies(id))"])))

(defn update-company-google-place-id! [company-id place-id]
  (let [ds (jdbc/get-datasource db-spec)
        formatted-sql (-> (update :companies)
                          (set {:Google_Place_ID place-id
                                :Google_Place_ID_Last_Updated (str (java.time.LocalDateTime/now))})
                          (where [:= :id company-id])
                          sql/format)]
    (jdbc/execute! ds formatted-sql)))

(defn add-company! [company]
  (let [ds (jdbc/get-datasource db-spec)
        formatted-sql (-> (insert-into :companies)
                          (values [company])
                          (returning :*)
                          sql/format)
        _ (println formatted-sql)
        result (jdbc/execute-one! ds formatted-sql)
        _ (println result)]
    (result :companies/id)))
(defn get-companies []
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/execute! ds (-> (select :*)
                          (from :companies)
                          sql/format))))

(defn add-review! [review]
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/execute! ds (-> (insert-into :reviews)
                          (values [review])
                          sql/format))))

(defn get-reviews []
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/execute! ds (-> (select :*)
                          (from :reviews)
                          sql/format))))