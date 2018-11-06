(ns ui.events
  (:require [re-frame.core :as re-frame :refer [register-handler]]
            [ui.socket :refer [chsk-send!]]
            [ui.db :as db]))

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    db/default-db))

(re-frame/reg-event-db
  :ui/reply
  (fn [db msg]
    (assoc db :message msg)))

(re-frame/reg-event-db
  :ui/send
  (fn [db [_ msg]]
    (chsk-send! [:ui/send msg])
    db))
