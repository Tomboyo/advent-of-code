(ns advent-2025.day3
  (:require [clojure.string :as str]))

; input consists of 200 lines of 100 digits each (20,000 distinct digits).
;
; We COULD do the naive thing and check all valid digit combinations, which would be 200 * (99 + 98 + 97 + ... + 1),
; or about a million. Brute force WOULD work. The only constraint to worry about is easy of implementation and
; extension.
;
; I suspect part two will ask us to build a bank of 3+ batteries, but I don't want to implement on that assumption.
; However, I think the following approach would be just as easy to implement but easier to extend if part 2 does the
; obvious:
;   - find the largest diget from the first n-1 digits, preferring digits with least index in the event of a tie
;   - if the index of that digit is k, find the largest digit of [k+1, n]. (Preferring least index not necessary, but fine)
;   - Those two digits form the largest number.
; this extends to any number of digits pretty easy.

(defn find-digit [digits]
  (reduce (fn [[_i max :as result] [_j x :as current]]
            (cond (= 9 x) (reduced current)
                  (> x max) current
                  :else result))
          [-1 -1]
          digits))

(defn solve [n input]
  (cond
    (string? input)
    (let [digits (into []
                       (map-indexed #(vector %1 (parse-long %2)))
                       (str/split input #""))
          c (count digits)]
      (first
        (reduce
          (fn [[result i] idx]
            ; Each step uses subvec to control the upper and lower bounds of the search. The lower bound must be AFTER
            ; the last digit picked, and the upper bound must reserve enough space at the end to ensure a sequence of
            ; digits in tail position could be found for the result.
            (let [from (+ i 1)
                  to (- (+ c 1 idx) n)
                  [j d] (find-digit (subvec digits from to))]
              [(+ d (* 10 result)) j]
              ))
          [0 -1]
          (range n))))

    :else
    (reduce + (map #(solve n %) input))))

(defn solve-part-1 [input] (solve 2 input))

; Part 2: We are validated. 12 digits per row!
(defn solve-part-2 [input] (solve 12 input))

(comment
  (solve-part-1 (str/split-lines (slurp "resources/advent_2025/day3.part1.txt")))
  (solve-part-2 (str/split-lines (slurp "resources/advent_2025/day3.part1.txt"))))