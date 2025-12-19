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


; Iteratively applying the brute-force algorithm to find rolls, remove them, and repeat until nothing changes will
; multiply the 30x10^-6 cost by anywhere from a few hundred iterations to a few thousand. In the worst case we remove
; one roll per iteration, taking ~20k iterations. That's 20x10^3 * 30x10^-6 = 600x10^-3 = 6x10^-1, around a second.
; That's fine.
(defn remove-rolls [input]
  (let [d (count input)
        d' (dec d)]
    (->>
      (for [y (range d) x (range d)]
        (when (= \@ (nth (nth input y) x))
          {:y y :x x :n
           (->>
             [[(dec y) (dec x)] [y (dec x)] [(inc y) (dec x)]
              [(dec y) x] [(inc y) x]
              [(dec y) (inc x)] [y (inc x)] [(inc y) (inc x)]]
             (filter (fn [[y x]] (and (<= 0 x d') (<= 0 y d'))))
             (filter (fn [[y x]] (= \@ (nth (nth input y) x))))
             (count))}))
      ; Find only accessible locations
      (filter some?)
      (filter #(< (:n %) 4))
      (reduce
        (fn [[result removed] {:keys [y x n]}]
          (let [row (nth result y)]
            [(assoc result y (assoc row x \x))
             (inc removed)]))
        [input 0])
      ))
  )

(defn solve-part-2 [input]
  (cond
    (string? input)
    ; 2D character vector. Strings support nth access, but not writing with (assoc). Hence, we use a vector.
    (solve-part-2 (->> (str/split-lines input)
                       (map #(vec (.toCharArray %)))
                       (vec)))

    (vector? input)
    (loop [current input
           removed 0]
      (let [[next r] (remove-rolls current)]
        (if (> r 0)
          (recur next (+ removed r))
          removed)))

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
  (solve-part-1 (slurp "resources/advent_2025/day4.part1.txt"))
  (remove-rolls (vec (map #(vec (.toCharArray %)) ["..@@.@@@@."
                                                   "@@@.@.@.@@"
                                                   "@@@@@.@.@@"
                                                   "@.@@@@..@."
                                                   "@@.@@@@.@@"
                                                   ".@@@@@@@.@"
                                                   ".@.@.@.@@@"
                                                   "@.@@@.@@@@"
                                                   ".@@@@@@@@."
                                                   "@.@.@@@.@."])))
  (solve-part-2  (vec (map #(vec (.toCharArray %)) ["..@@.@@@@."
                                                    "@@@.@.@.@@"
                                                    "@@@@@.@.@@"
                                                    "@.@@@@..@."
                                                    "@@.@@@@.@@"
                                                    ".@@@@@@@.@"
                                                    ".@.@.@.@@@"
                                                    "@.@@@.@@@@"
                                                    ".@@@@@@@@."
                                                    "@.@.@@@.@."])))
  (solve-part-2 (slurp "resources/advent_2025/day4.part1.txt")))

(comment
  (use 'criterium.core)
  (let [input (.toCharArray "..@@.@.@.@")]
    (with-progress-reporting (quick-bench
                               (reduce (fn [acc i] (assoc acc i "x")) (range (count input))))))
  (let [input [true true false false true false true false true false]]
    (with-progress-reporting (quick-bench
                               (reduce (fn [acc i] (assoc acc i true)) (range (count input)))))))