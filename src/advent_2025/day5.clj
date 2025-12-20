(ns advent-2025.day5
  (:require [advent.more :as more]
            [clojure.string :as str]))

(defn range-contains?
  [ranges v]
  (first (filter (fn [[low high]] (<= low v high))
                 ranges)))

(defn parse [input]
  (let [lines (str/split-lines input)
        [ranges, ids] (split-with (comp not str/blank?) lines)
        ranges (into []
                     (comp (map #(str/split % #"-"))
                           (map #(map parse-long %)))
                     ranges)
        ids (map parse-long (rest ids))]
    [ranges, ids]))

(defn solve-part-1
  ([string] (apply solve-part-1 (parse string)))

  ([ranges ids]
   (transduce
     (filter #(range-contains? ranges %))
     more/counter
     ids)))

(defn compare-interval
  "Compares intervals x and y and returns :> if x is after y, :< if x is before y, and :intersect if they intersect."
  [[a b :as x] [c d :as y]]
  (cond
    (< b c)
    :<

    (> a d)
    :>

    :else
    :intersect))

(defn cover-interval
  "Return a uniquely small interval that covers the given intervals"
  [intervals]
  (let [low (transduce (map first) min Long/MAX_VALUE intervals)
        high (transduce (map second) max Long/MIN_VALUE intervals)]
    [low high]))

(defn add-interval [intervals interval]
  (if (empty? intervals)
    [interval]

    (let [{start     :>
           end       :<
           intersect :intersect} (group-by #(compare-interval interval %)
                                           intervals)]
      (concat (conj (or start []) (cover-interval (conj intersect interval)))
              end))))

(defn count-covered-integers [intervals]
  (reduce
    (fn [sum [low high]]
      (+ sum (- high low) 1))
    0
    intervals))

(defn solve-part-2
  [input]
  (cond
    (string? input)
    (solve-part-2 (first (parse input)))

    :else
    (let [disjoint-intervals (reduce add-interval
                                     [(first input)]
                                     (rest input))]
      (count-covered-integers disjoint-intervals))))

(comment
  (solve-part-1 (slurp "resources/advent_2025/day5.txt"))
  (solve-part-2 (slurp "resources/advent_2025/day5.txt")))