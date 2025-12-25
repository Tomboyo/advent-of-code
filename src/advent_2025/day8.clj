(ns advent-2025.day8
  (:require [clojure.math :as math]
            [clojure.string :as str]
            [clojure.set :as set]))

; The input contains 1000 lines, each line with the 3D integer coordinates of a junction box. We need to connect
; together the 1000 nearest neighbors by Euclidian distance, forming one or more independent circuits of junction boxes.
; Once done, we need to multiply the sizes of the 3 largest circuits, which is the answer to the puzzle.

; There are 10^6 distinct pairs of junction boxes.
; Euclidian distance takes roughly ~42ns (42x10^-9) on my machine
; Getting the distance between all pairs should take milliseconds (10^-3).
;
; It appears that for any pair of junction boxes p1 and p2, the distance between the boxes of p1 is different from the
; distance between the junction boxes of p2. This is at least true of the example and my problem input.
;
; Solution:
; - Make a list of the 10^6 distinct pairs. Sort it by the Euclidian distance function.
; - Take the first 1000 entries of the sorted list.
; - Perform reduction:
;     If neither junction box is already in a circuit set, add both to a new set. If just one is already
;     in a circuit set, add both to the existing set. If both are in sets and those sets are the same, do nothing. If
;     those sets are instead different, combine those sets together. The reduction state should be a set of circuit
;     sets.
;

(defn sqr [x] (math/pow x 2))

(defn distance [[x y z] [x' y' z']]
  (math/sqrt (+ (sqr (- x' x)) (sqr (- y' y)) (sqr (- z' z)))))

(defn cartesian-product [col]
  (into #{} (for [x col y col :when (not= x y)]
              #{x y})))

(defn parse-coordinates
  "Returns a lazy seq of coordinates vectors"
  [input]
  (->> (str/split-lines input)
       (map #(re-seq #"\d+" %))
       (map #(map parse-long %))
       (map vec)))

(defn sorted-connections
  "Returns a sorted sequence of pairs of points, (#{p1 p2} #{p3 p4}...)"
  [points]
  (sort-by
    (memoize #(apply distance %))
    (cartesian-product points)))

(defn merge-circuits
  "Merges a circuit into a set of disjoint circuits, returning a new set of disjoint circuits."
  [circuits circuit]
  (let [a (first circuit)
        b (second circuit)
        intersections (filter
                        #(or (contains? % a) (contains? % b))
                        circuits)]
    (case (count intersections)
      1 circuits                                            ; circuit is a subset, so there's nothing to do.
      2 (conj (apply disj circuits intersections)
              (apply set/union intersections)))))

(defn solve-part-1
  [num-connections num-circuits input]
  (let [boxes (parse-coordinates input)
        to-connect (take num-connections (sorted-connections boxes))
        circuits (reduce
                   merge-circuits
                   (into #{} (map (fn [x] #{x})) boxes)
                   to-connect)
        top-k-circuits (take num-circuits (reverse (sort-by count circuits)))]
    (transduce
      (map count)
      *
      1
      top-k-circuits)))

(defn solve-part-2
  [input]
  (let [boxes (parse-coordinates input)
        to-connect (sorted-connections boxes)]
    (reduce
      (fn [circuits circuit]
        (let [[a _ _] (first circuit)                       ; x coordinate of junction box
              [b _ _] (second circuit)
              new-circuits (merge-circuits circuits circuit)]
          (if (= 1 (count new-circuits))
            (reduced (* a b))
            new-circuits)))
      (into #{} (map (fn [x] #{x})) boxes)
      to-connect)))

(comment
  (solve-part-1 1000 3 (slurp "resources/advent_2025/day8.txt"))
  (solve-part-2 (slurp "resources/advent_2025/day8.txt"))

  (use 'criterium.core)
  (with-progress-reporting
    (quick-bench
      (distance [21 15 8] [33 105 42]))))