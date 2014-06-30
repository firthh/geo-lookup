(ns geo-lookup.data-loader
  (:require [geo-lookup.database :as db]
            [clojure.data.csv :as csv]))

(defn get-file [file-name] (slurp (clojure.java.io/resource file-name)))

(defn retailer-row-to-map
  [[id name street-address suburb region store-type
    can-purchase? can-top-up? can-register? can-refund? can-alter-expiry?
    accepts-cash? accepts-eftpos? accepts-credit? _ _ _ _ _ _ _ _ created modified active _ lat lng]]
  {:name name
   :street-address street-address
   :suburb suburb
   :region region
   :location [(read-string lng) (read-string lat)]})

(defn load-row
  [row]
  (-> (retailer-row-to-map row)
      db/insert-location!))

(defn load-data
  [file-name]
  (-> (get-file file-name)
      csv/read-csv
      rest
      (#(map load-row %))))

(load-data "data.csv")

;;(time (db/get-all-locations))
