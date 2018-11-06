(ns lupakartta.api.core
  (:require [lupakartta.api.messenger.core :as messenger]
            [lupakartta.api.server.core]
            [schema.core :as s]  ; TODO: Should do this with spec
            [mount.core :as mount]))

(s/set-fn-validation! true)

(defn start
  []
  (println (mount/start))
  (messenger/publish messenger/announcements-ex "Hello!"))
