(ns lupakartta.api.routing
  (:require [lupakartta.middleware :as middleware]
            [mount.core :as mount :refer [defstate]]
            [org.httpkit.server :as server]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as rrc]))


(defn args->server-config [args]
  {:port  (:port args)
   :join? false})

(defn start-server [config]
  (let [app (ring/ring-handler
              (ring/router
                ;; TODO: app routes go here :-)
                []
                ;; router data effecting all routes
                {:data {:coercion reitit.coercion.spec/coercion
                        :middleware [rrc/coerce-exceptions-middleware
                                     rrc/coerce-request-middleware
                                     rrc/coerce-response-middleware
                                     middleware/cors
                                     ]}})
              )]
    (when-let [server (server/run-server app config)]
      server)))

(defstate server
    :start (start-server (args->server-config (mount/args)))
    :stop  (server :timeout 100))
