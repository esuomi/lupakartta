(ns ui.events
  (:require
   [re-frame.core :as re-frame]
   [ui.db :as db]))

(re-frame/reg-event-db
 :comms/connect
 (fn [_ _]
   {:name "Doot doot"}))
