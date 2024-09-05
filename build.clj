(ns build
  (:require [clojure.tools.build.api :as b]))

(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file "target/tax-review-app.jar")

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main 'tax-review-app.core}))

(defn tailwind [_]
  (b/process {:command-args ["npx" "tailwindcss"
                             "-i" "./resources/public/css/input.css"
                             "-o" "./resources/public/css/output.css"]}))

(defn run [_]
  (b/process {:command-args ["java" "-jar" uber-file]}))

