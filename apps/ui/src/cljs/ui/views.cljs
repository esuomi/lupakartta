(ns ui.views
  (:require [re-frame.core :as re-frame :refer [dispatch]]
            [reagent.core :as reagent]
            [ui.socket :as socket]))

(defn el->value
  [el]
  (-> el .-target .-value))

(defn- dispatch-value
  [event el]
  (dispatch [event (el->value el)]))

(defn input
  []
  [:input {:type "text"
           :placeholder "Write here."
           :on-change #(dispatch-value :ui/send %)}])

(defn message
  []
  (let [message (re-frame/subscribe [:message])]
    [:div
     [:span "> "]
     [:span @message]]))

(defn main-panel []
  (let [name (re-frame/subscribe [:name])]
    (reagent/create-class
      {:component-will-mount socket/event-loop
       :reagent-render (fn []
                         [:div
                          [:div "Hello from " @name]
                          [input]
                          [message]])})))
