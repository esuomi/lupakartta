(ns lupakartta.api.core
  (:require [lupakartta.api.server.core]
            [schema.core :as s]
            [mount.core :as mount]))  ; TODO: Should do this with spec

(s/set-fn-validation! true)

(defn start
  []
  (mount/start))
