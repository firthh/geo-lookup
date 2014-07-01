(ns geo-lookup.database
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :as op])
  (:use [criterium.core]))

(def collection "gocarddata")

(def db (mg/get-db (mg/connect) "locations"))

(defn insert-location! [loc]
  (if (not (nil? loc))
    (mc/insert db collection loc)))

(defn get-all-locations []
  (mc/find-maps db collection))

(def get-all-memoized (memoize get-all-locations))

(defn get-locations
  ([point-1 point-2]
     (mc/find-maps db collection
                    {:location
                     {op/$geoWithin
                      {:$box [point-1
                              point-2]}}}))
  ([lat lng max-distance]
      (mc/find-maps db collection
                    {:location
                     {op/$near [lng lat]
                      :$maxDistance max-distance}})))

(defn index-locations []
  (mc/ensure-index db collection { :location "2d"}))

;;(index-locations)

;;(mc/indexes-on db collection)

;;(get-locations [0 0] [180 -180])

;;(bench(get-locations -27 153 100))

;;(bench (get-all-locations))

;;(bench (get-all-memoized))
