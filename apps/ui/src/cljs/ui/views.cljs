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

(defn home-did-mount
  []
  ; this stuff just configures leaflet
  (let [map (.setView (.map js/L "map") #js [60.1699 24.9384] 13)]

    ;; NEED TO UPDATE with your mapID
    (.addTo (.tileLayer js/L "https://c.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)

    (.locate map (clj->js {:setView true :maxZoom 16}))

    (.on map "locationfound" (fn [e]
                               (.addTo (.marker js/L (.-latlng e))
                                       map)
                               (.addTo (.circle js/L (.-latlng e) (/ (.-accuracy e) 2))
                                       map)
                               ))

    (.on map "locationerror" (fn [e] (js/alert (str (.-message e)))))
    ))


(defn main-panel []
  (let [name (re-frame/subscribe [:name])]
    (reagent/create-class
      {:component-will-mount socket/event-loop
       :component-did-mount  home-did-mount
       :reagent-render (fn []
                         [:div {:style {:height "100%"}}
                          [:div "Hello from " @name]
                          [input]
                          [message]
                          [:div#map]])})))
