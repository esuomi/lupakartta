(ns ui.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [register-sub]]))

(re-frame/reg-sub
   :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :message
  (fn [db]
    (:message db)))
