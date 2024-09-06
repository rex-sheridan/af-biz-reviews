(ns tax-review-app.db
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [insert-into select from values]]
            [clojure.java.io :as io]))


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
                         phone TEXT)"])
    (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS reviews
                        (id INTEGER PRIMARY KEY AUTOINCREMENT,
                         company_id INTEGER,
                         rating INTEGER,
                         review_text TEXT,
                         source TEXT,
                         FOREIGN KEY(company_id) REFERENCES companies(id))"])))

(defn add-company! [company]
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/execute! ds (-> (insert-into :companies)
                          (values [company])
                          sql/format))))

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