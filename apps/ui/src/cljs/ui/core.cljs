(ns ui.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [ui.events]
            [ui.subs]
            [ui.views :as views]))

(enable-console-print!)

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
