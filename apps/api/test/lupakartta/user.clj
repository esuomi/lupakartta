(ns lupakartta.user
  (:require [clojure.tools.namespace.repl :as tnr]
            [lupakartta.api.core :as core]
            [mount.core :as mount]))

; nothing special yet :-)

(defn start []
  (core/start))

(defn reset []
  (mount/stop)
  (tnr/refresh :after 'lupakartta.user/start))
