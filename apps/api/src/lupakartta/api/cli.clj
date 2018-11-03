(ns lupakartta.api.cli
  (:require [lupakartta.api.core :refer [start]])
  (:gen-class))

(defn -main
  [& args]
  (if (empty? args)
    (start)
    (throw (ex-info "Commandline arguments are not accepted! Remove them to start the application" {:type :system/fatal :args args}))))
