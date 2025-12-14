(ns advent-2025.day2
  (:require [clojure.string :as str]))

(defn solver [pattern line]
  (->> (str/split line #",")
       (map (fn [range] (str/split range #"-")))
       (map (fn [[low high]]
              (let [lower-bound (parse-long low)
                    upper-bound (parse-long high)]
                (->> (range lower-bound (inc upper-bound))
                     (map str)
                     (filter #(re-matches pattern %))
                     (map parse-long)
                     ))))
       (flatten)
       (reduce +)
       ))

(defn solve-part-1 [line]
  (solver #"^(.+)\1$" line))

(defn solve-part-2 [line]
  (solver #"^(.+)(\1)+$" line))
