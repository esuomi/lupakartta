(ns ui.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (.log js/console (str "in ui: " db))
   (:name db)))
