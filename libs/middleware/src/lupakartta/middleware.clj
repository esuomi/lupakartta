(ns lupakartta.middleware)

(def cors-headers
  "Generic CORS headers"
  {"Access-Control-Allow-Origin"  "*"
   "Access-Control-Allow-Headers" "Content-Type,Content-Disposition,Authorization,X-Requested-With"
   "Access-Control-Expose-Headers" "Content-Disposition"
   "Access-Control-Allow-Methods" "GET, PUT, POST, DELETE, OPTIONS"})

(defn- preflight?
  "Returns true if the request is a preflight request"
  [request]
  (= (request :request-method) :options))

;(defn known-route? [request]
;    {:request-method (or (some-> (get-in request [:headers "access-control-request-method"]) s/lower-case keyword)
;                         (:request-method request))}))
(defn known-route? [request] true)  ; TODO: make Reitit impl

(defn cors
  "Allow requests from all origins - also check preflight"
  [handler]
  (fn [request]
    (if (preflight? request)
      (do
        {:status 200
         :headers cors-headers
         :body "preflight complete"})
      (if (known-route? request)
        (do
          (update-in (handler request) [:headers] merge cors-headers))
        (throw (ex-info "Unknown route" {:type           :routing/unknown-route
                                         :request-method (get-in request [:request-method])
                                         :request-path   (get-in request [:uri])}))))))
