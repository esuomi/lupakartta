(ns lupakartta.user
  (:require [clojure.tools.namespace.repl :as tnr]
            [lupakartta.api.core :as core]
            [mount.core :as mount]))

; nothing special yet :-)

(defn start []
  (alter-var-root (var lupakartta.api.config/*config-file*) (fn [x] "config-dev.edn"))
  (core/start))

(defn reset []
  (mount/stop)
  (tnr/refresh :after 'lupakartta.user/start))
