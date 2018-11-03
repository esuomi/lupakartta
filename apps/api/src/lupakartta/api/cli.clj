(ns lupakartta.api.cli
  (:require [clojure.tools.cli :refer [parse-opts]]
            [lupakartta.api.core :refer [start]])
  (:gen-class))

(def cli-options
  [["-c" "--config CONFIG" "Path to EDN file that contains application configurations."]])

(defn load-config [path]
  (-> (or (clojure.java.io/resource path)
          (clojure.java.io/file path))
      (slurp)
      (clojure.edn/read-string)))

(defn parse-args
  "Parse application specific commandline arguments here"
  [args]
  (-> (parse-opts args cli-options)
      (get-in [:options :config])
      (load-config)))

(defn -main
  [& args]
  (-> args parse-args start))
