(ns geo-lookup.database
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :as op]))

(def collection "gocarddata")

(def db (mg/get-db (mg/connect) "locations"))

(defn insert-location! [loc]
  (if (not (nil? loc))
    (mc/insert db collection loc)))

(defn get-all-locations []
  (mc/find-maps db collection))

(defn get-locations [lat lng max-distance]
  (mc/find-maps db collection
                {:location
                 {op/$near [lng lat]
                  :$maxDistance max-distance}}))

(defn index-locations []
  (mc/ensure-index db collection { :location "2d"}))

;;(index-locations)

;;(mc/indexes-on db collection)

(time (get-locations -27 153 100))

(time (get-all-locations))
