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
            [taoensso.sente.server-adapters.http-kit :refer [get-sch-adapter]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.reload :refer [wrap-reload]]
            [clj-uuid :as uuid]))

(defn create-sente-ws
  []
  (sente/make-channel-socket! (get-sch-adapter) {}))

(defn shutdown-sente-ws
  [sente]
  (let [{:keys [send-fn connected-uids]} sente]
    (doseq [uid (:any connected-uids)]
      (send-fn uid [:server/shutdown (str "Server shut down")]))))

(defstate sente
  :start (create-sente-ws)
  :stop  (shutdown-sente-ws sente))

(defn- event-loop
  []
  (let [{:keys [send-fn ch-recv]} sente]
    (go (loop [{:keys [uid ring-req event] :as data} (<! ch-recv)]
      (println (str "event loop :: " uid " / " (-> data :connected-uids deref count) " connected :: " event))
      (thread
        (when-let [result (events/handle-event event ring-req)]
          (apply send-fn result)))
      (recur (<! ch-recv))))))

(defn- args->server-config [args]
  {:port  (:port args)
   :join? false})

(defn start-server [config]
  (let [{:keys [ajax-get-or-ws-handshake-fn ajax-post-fn]} sente
        app (ring/ring-handler
              (ring/router
                ;; TODO: app routes go here :-)
                ["/chsk" {:get  {:parameters {:query {:udt int?
                                                      :client-id string?
                                                      :handshake? boolean?}}
                                 :handler ajax-get-or-ws-handshake-fn}
                          :post {:handler ajax-post-fn}
                          }]
                ;; router data effecting all routes
                {:data {:coercion reitit.coercion.spec/coercion
                        :middleware [middleware/cors
                                     session/wrap-session
                                     rmp/wrap-params
                                     rmk/wrap-keyword-params
                                     ]}})
              )]
    (when-let [server (server/run-server app (args->server-config config))]
      (event-loop)
      server)))

(defstate server
  :start (start-server config)
  :stop  (server :timeout 100))
