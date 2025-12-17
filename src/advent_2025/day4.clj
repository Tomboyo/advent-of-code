(ns advent-2025.day4
  (:require [clojure.string :as str]))

; the input is a 2D matrix of spaces that are either occupied or unoccupied by a roll of paper.
; a roll of paper is "accessible" if there are < 4 rolls in the adjacent 8 spaces.
;
; The input is 141x141 = 19,881 or ~20k elements. Checking each space is 8 comparisons, coming out to ~30k comparisons.
; That kind of thing is nanosecond-scale (10^-9), so 30k of them is microsecond 30x10(^-6) scale. Brute-force should
; work.
;
; There aren't any rolls of paper outside the matrix, so e.g. a roll of paper in a corner can only have 3 neighbors.
;
; Things that might change in part 2: I think the most likely thing is changing the definition of "accessible." That
; would not be hard to accommodate.
;

; Take the input file, read it into a vec of strings. This is a 2D matrix. For i,j within the bounds of the matrix,
; which we know to be 140 by 140, count the number of \@ characters in the surrounding 8 positions. Any position that
; falls outside the bounds is considered \. rather than \@. For each i,j where that sum is < 4, increment the counter.
; This is a reduction, as all problems apparently are :P
(defn solve-part-1 [input]
  (cond
    (string? input)
    (solve-part-1 (vec (str/split-lines input)))

    (vector? input)
    (let [d (count input)
          d' (dec d)]
      (->>
        (for [y (range d) x (range d)]
          (when (= \@ (nth (nth input y) x))
            (->>
              [[(dec y) (dec x)] [y (dec x)] [(inc y) (dec x)]
               [(dec y) x] [(inc y) x]
               [(dec y) (inc x)] [y (inc x)] [(inc y) (inc x)]]
              (filter (fn [[y x]] (and (<= 0 x d') (<= 0 y d'))))
              (filter (fn [[y x]] (= \@ (nth (nth input y) x))))
              (count))))
        ; Find only accessible locations
        (filter some?)
        (filter #(< % 4))
        (count)
        ))

    :else
    (throw (IllegalArgumentException. "Unexpected type of input -- should be string or vector"))
    )
  )

(comment
  (solve-part-1 ["..@@.@@@@."
                 "@@@.@.@.@@"
                 "@@@@@.@.@@"
                 "@.@@@@..@."
                 "@@.@@@@.@@"
                 ".@@@@@@@.@"
                 ".@.@.@.@@@"
                 "@.@@@.@@@@"
                 ".@@@@@@@@."
                 "@.@.@@@.@."])
  (solve-part-1 (slurp "resources/advent_2025/day4.part1.txt")))