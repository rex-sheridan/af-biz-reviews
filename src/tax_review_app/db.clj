(ns tax-review-app.db
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [insert-into values select from]]))

(def db-spec
  {:dbtype "sqlite"
   :dbname "database/reviews.db"})

(defn create-tables! []
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS reviews
                        (id INTEGER PRIMARY KEY AUTOINCREMENT,
                         business_name TEXT,
                         rating INTEGER,
                         review_text TEXT,
                         source TEXT)"])))

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