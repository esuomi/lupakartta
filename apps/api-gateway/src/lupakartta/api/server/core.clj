(ns lupakartta.api.server.core
  (:require [lupakartta.middleware :as middleware]
            [lupakartta.api.config :refer [config]]
            [lupakartta.api.server.events :as events]
            [lupakartta.api.server.session :as session]
            [mount.core :as mount :refer [defstate]]
            [org.httpkit.server :as server]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as rrc]
            [ring.middleware.keyword-params :as rmk]
            [ring.middleware.params :as rmp]
            [clojure.core.async :as async :refer [<! <!! chan go go-loop thread]]
            [clojure.core.cache :as cache]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (sente-web-server-adapter)]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.reload :refer [wrap-reload]]
            [clj-uuid :as uuid]))

(defn create-sente-ws
  []
  (defonce sente-socket (sente/make-channel-socket! sente-web-server-adapter {}))

  (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn connected-uids]} sente-socket]
    (def ring-ajax-post                ajax-post-fn)
    (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
    (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
    (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
    (def connected-uids                connected-uids) ; Watchable, read-only atom
    ))

(defstate sente
  :start (create-sente-ws)
  )  ; TODO create graceful shutdown which sends shutdown events to all connected uids and tears down the server proper

(defn- event-loop
  []
  (go (loop [{:keys [client-uuid ring-req event] :as data} (<! ch-chsk)]
    (println (str "event loop :: " client-uuid " :: " event))
    (thread
      (when-let [result (events/handle-event event ring-req)]
        (apply chsk-send! result)))
    (recur (<! ch-chsk)))))

(defn- args->server-config [args]
  {:port  (:port args)
   :join? false})

(defn start-server [config]
  (let [app (ring/ring-handler
              (ring/router
                ;; TODO: app routes go here :-)
                ["/chsk" {:get  {:parameters {:query {:udt int?
                                                      :client-id string?
                                                      :handshake? boolean?}}
                                 :handler ring-ajax-get-or-ws-handshake}
                          :post {:handler ring-ajax-post}
                          }]
                ;; router data effecting all routes
                {:data {:coercion reitit.coercion.spec/coercion
                        :middleware [middleware/cors
                                     session/wrap-session
                                     rmp/wrap-params
                                     rmk/wrap-keyword-params
                                     ]}})
              )]
    (when-let [server (server/run-server app config)]
      (event-loop)
      server)))

(defstate server
  :start (start-server config)
  :stop  (server :timeout 100))
