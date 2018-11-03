(ns lupakartta.api.config
  (:require [mount.core :as mount :refer [defstate]]
            [maailma.core :as m]
            [schema.core :as s]
            [lupakartta.utils :as utils]))  ; TODO: Should do this with spec


(def Port
  (s/constrained s/Int #(< 0 % 0x10000) 'in-port-range?))

(def Config
  {:port Port})

(def defaults {:port 8080})

(defstate config
  :start (s/validate
           Config
           (utils/deep-merge
             defaults
             (m/build-config
               (m/resource "config.edn")
               (m/file     "config-dev.edn")))))
