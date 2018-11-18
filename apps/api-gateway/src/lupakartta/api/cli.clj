(ns lupakartta.api.cli
  (:require [clojure.tools.cli :refer [parse-opts]]
            [lupakartta.api.core :refer [start]])
  (:gen-class))

(def cli-options
  [["-c" "--config CONFIG" "Path to EDN file that contains application configurations."]])


(defn parse-args
  "Parse application specific commandline arguments here"
  [args]
  (let [options (:options (parse-opts args cli-options))]
    (set! lupakartta.api.config/*config-file* (:config options))))

(defn -main
  [& args]
  (when (empty? args)
    (throw (ex-info "No arguments given. Provide -c/--config PATH_TO_EDN argument to start up the application fron CLI" {:type :system/fatal :args args}))
    (parse-args args))
  (start))
