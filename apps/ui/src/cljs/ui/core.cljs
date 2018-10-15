(ns ui.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [ui.events :as events]
   [ui.views :as views]
   [ui.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:comms/connect])
  (dev-setup)
  (mount-root))
