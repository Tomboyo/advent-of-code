(ns advent-2025.day6
  (:require [clojure.string :as str]))

(defn parse [input]
  (let [matrix (->> input
                    (str/split-lines)
                    (map #(re-seq #"[\+\*\d]+" %))
                    (map #(into {} (map-indexed (fn [i v] {i v}) %)))
                    (into {} (map-indexed (fn [i v] {i v})))
                    )
        max-y (apply max (keys matrix))
        max-x (apply max (keys (get matrix 0)))]
    (for [x (range (inc max-x))]
      (reverse
        (for [y (range (inc max-y))]
          (if (< y max-y)
            (parse-long (get-in matrix [y x]))
            (keyword (get-in matrix [y x]))))))))

(defn evaluate [[operation & operands]]
  (case operation
    :* (apply * operands)
    :+ (apply + operands)))

(defn solve-part-1 [input]
  (->>
    (parse input)
    (map evaluate)
    (reduce +))
  )

(comment (solve-part-1 (slurp "resources/advent_2025/day6.txt")))
