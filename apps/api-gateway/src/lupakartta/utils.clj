(ns lupakartta.utils
  (:require [clojure.set]))

(declare deep-merge)
(defn- merge-values
  [x y]
  (cond
    (map? x)             (deep-merge x y)
    (or (sequential? x)) (concat x y)
    (set? x)             (clojure.set/union x y)
    :else                y))
; original from https://stackoverflow.com/a/17328219
(defn deep-merge
  ([] {})  ;; this is for compatibility with reduce for no values
  ([a b] (merge-with merge-values a b)))
