(ns lupakartta.api.core
  (:require [lupakartta.utils :as utils]
            [lupakartta.api.routing]
            [mount.core :as mount :refer [defstate]]
            [schema.core :as s]))  ; TODO: Should do this with spec

(def Port
  (s/constrained s/Int #(< 0 % 0x10000) 'in-port-range?))

(def Config
  {:port Port})

(def defaults {:port 8080})

(s/set-fn-validation! true)

(s/defn ^:always-validate start :- s/Any
  [args :- Config]
  (mount/start-with-args (utils/deep-merge defaults args)))
