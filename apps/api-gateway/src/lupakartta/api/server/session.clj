(ns lupakartta.api.server.session
  (:require [clj-uuid :as uuid]))

(defn get-session-uid
  "Convenient to extract from the request the UID that Sente needs."
  [req]
  (get-in req [:session :uid]))

; TODO #1: use namespaced UUIDs for generating microservice specific identity UUIDs; this is for backtracking and rudimentary validation
(defn unique-id
  "Return a unique ID (for an unsecured session ID)."
  []
  (uuid/v1))

(defn wrap-session
  [app]
  (fn [req]
    (if (get-session-uid req)
      (do  (println "uid ok")
           (app req))
      (app (assoc-in req [:session :uid] (unique-id))))))
