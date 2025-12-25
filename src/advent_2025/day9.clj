(ns advent-2025.day9
  (:require [advent.more :as more]
            [clojure.string :as str]))

(defn parse [input]
  (->> (str/split-lines input)
       (map #(re-seq #"\d+" %))
       (flatten)
       (map parse-long)
       (partition 2)))

(defn rect-area [rect]
  (let [[x1 y1] (first rect)
        [x2 y2] (second rect)]
    (* (inc (abs (- y2 y1)))
       (inc (abs (- x2 x1))))))

(defn solve-part-1 [input]
  (rect-area (->> (parse input)
                  (more/unordered-cartesian-product)
                  (sort-by rect-area)
                  (reverse)
                  (first))))

(comment
  (solve-part-1 (slurp "resources/advent_2025/day9.txt")))
