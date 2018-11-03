(ns lupakartta.api.server.events
  (:require [lupakartta.api.server.session :as session]))

(defmulti handle-event
          "Multimethod used to handle events coming from the client."
          (fn [[ev-id ev-arg] ring-req] ev-id))

(defmethod handle-event :test/send
  [[_ msg] req]
  (when-let [uid (session/get-session-uid req)]
    [uid [:test/reply (clojure.string/reverse msg)]]))

(defmethod handle-event :chsk/ws-ping
  [_ req]
  (println (str "received ping from " (session/get-session-uid req)))
  nil)

(defmethod handle-event :default
  [event req]
  nil)