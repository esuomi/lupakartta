(ns lupakartta.api.config
  (:require [mount.core :as mount :refer [defstate]]
            [maailma.core :as m]
            [schema.core :as s]
            [lupakartta.utils :as utils]))  ; TODO: Should do this with spec

(def ^:dynamic *config-file* "config.edn")

(def Port
  (s/constrained s/Int #(< 0 % 0x10000) 'in-port-range?))

(def Config
  {:port Port
   :rmq  {s/Any s/Any}}) ;; TODO: Make this Langohr config compatible

(def defaults {:port 8080})

(defstate config
  :start (s/validate
           Config
           (utils/deep-merge
             defaults
             (m/build-config
               (m/resource *config-file*)
               (m/file *config-file*)))))
