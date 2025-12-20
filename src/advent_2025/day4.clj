(ns advent-2025.day4
  (:require [advent.more :as more]
            [clojure.string :as str]
            [advent.point :as point]))

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


(def roll-glyph \@)
(def removed-glyph \x)
(def max-neighbors 3)

(defn count-occupied-neighbors [input y x]
  (let [d (dec (count input))]
    (transduce
      (comp (filter #(and (<= 0 (:x %) d) (<= 0 (:y %) d)))
            (filter #(= roll-glyph (get-in input [(:y %) (:x %)]))))
      more/counter
      0
      (point/neighbors {:y y :x x}))))

(defn accessible-rolls
  "Returns the points corresponding to all accessible rolls in input."
  [input]
  (let [d (count input)]
    (for [y (range d) x (range d)
          :when (and (= roll-glyph (get-in input [y x]))
                     (<= (count-occupied-neighbors input y x) max-neighbors))]
      {:y y :x x})))

(defn solve-part-1 [input]
  (cond
    (string? input)
    (solve-part-1 (vec (str/split-lines input)))

    (vector? input)
    (count (accessible-rolls input))

    :else
    (throw (IllegalArgumentException. "Unexpected type of input -- should be string or vector"))
    )
  )

; Iteratively applying the brute-force algorithm to find rolls, remove them, and repeat until nothing changes will
; multiply the 30x10^-6 cost by anywhere from a few hundred iterations to a few thousand. In the worst case we remove
; one roll per iteration, taking ~20k iterations. That's 20x10^3 * 30x10^-6 = 600x10^-3 = 6x10^-1, around a second.
; That's fine.
(defn remove-rolls [input]
  (reduce
    (fn [[result removed] p]
      [(assoc-in result [(:y p) (:x p)] removed-glyph)
       (inc removed)])
    [input 0]
    (accessible-rolls input)))

(defn solve-part-2 [input]
  (cond
    (string? input)
    ; 2D character vector. Strings support nth access, but not writing with (assoc). Hence, we use a vector.
    (solve-part-2
    (into []
          (map #(into [] (.toCharArray %)))
          (str/split-lines input)))

    (vector? input)
    (if (string? (first input))
      (solve-part-2 (into []
                          (map #(into [] (.toCharArray %)))
                          input))

      (loop [current input
             removed 0]
        (let [[next r] (remove-rolls current)]
          (if (> r 0)
            (recur next (+ removed r))
            removed))))

    :else
    (throw (IllegalArgumentException. "Unexpected type of input -- should be string or vector"))
    )
  )

(comment
  (use 'criterium.core)
  (let [input (.toCharArray "..@@.@.@.@")]
    (with-progress-reporting (quick-bench
                               (reduce (fn [acc i] (assoc acc i "x")) (range (count input))))))
  (let [input [true true false false true false true false true false]]
    (with-progress-reporting (quick-bench
                               (reduce (fn [acc i] (assoc acc i true)) (range (count input)))))))