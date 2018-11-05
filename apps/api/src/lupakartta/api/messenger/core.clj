(ns lupakartta.api.messenger.core
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.exchange  :as le]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]
            [lupakartta.api.config :refer [config]]
            [mount.core :as mount :refer [defstate]]))

(def announcements-ex "lupakartta.service.announcements")
(def self "lupakartta.api")

(defn print-msg
  [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  (println (format "[consumer] Received a message: %s, delivery tag: %d, content type: %s, type: %s"
                   (String. payload "UTF-8") delivery-tag content-type type)))

(defstate connection
  :start (rmq/connect (get-in config [:rmq]))
  :end   (rmq/close connection))

(defstate rmq-ch
  :start  (lch/open connection)  ;; TODO: might return null, guard appropriately
  :end    (rmq/close rmq-ch))

(defn declare-fanout-exchanges
  "1-to-N fanout exchanges: No routing keys are involved. You bind a queue to exchange and messages sent to that exchange are delivered to all bound queues."
  []
  ; All microservices are expected to communicate with each others through the announcements topic
  (le/declare rmq-ch announcements-ex "fanout" {:durable false :auto-delete true}))

(defn subscribe-to-fanout
  [queue exchange]
  (lq/declare   rmq-ch queue {:exclusive false :auto-delete true})
  (lq/bind      rmq-ch queue exchange)
  (lc/subscribe rmq-ch queue print-msg {:auto-ack true}))

(defstate declare-exchanges :start (declare-fanout-exchanges))
(defstate start-subscriptions :start (subscribe-to-fanout self announcements-ex))

(defn publish
  [exchange msg]
  (lb/publish rmq-ch exchange "" msg {:content-type "text/plain" :type "this.is.message.type"}))
