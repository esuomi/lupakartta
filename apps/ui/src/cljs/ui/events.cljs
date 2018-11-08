(ns ui.events
  (:require [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame.core :as re-frame :refer [register-handler]]
            [ui.socket :refer [chsk-send!]]
            [ui.db :as db]))

(re-frame/reg-event-db
  :initialize-db
  (fn-traced  [_ _]
    db/default-db))

(re-frame/reg-event-db
  :ui/reply
  (fn-traced [db msg]
    (assoc db :message msg)))

(re-frame/reg-event-db
  :ui/send
  (fn-traced [db [_ msg]]
    (chsk-send! [:ui/send msg])
    db))
