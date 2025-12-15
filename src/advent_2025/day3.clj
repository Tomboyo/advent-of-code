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

(defn find-digit [max-index digits]
  (reduce (fn [[_mi md :as result] [i d :as current]]
            (cond
              (> i max-index)
                (reduced result)
              (= 9 d)
                (reduced current)
              (> d md)
                current
              :else
                result
              ))
          digits))

(defn solve-part-1 [input]
  (cond
    (string? input)
    (let [digits (->> (str/split input #"")
                      (map-indexed #(vector %1 (parse-long %2)))
                      (vec))
          [i  a] (vec (find-digit (- (count digits) 2) digits))
          [_j b] (find-digit (- (count digits) 1) (subvec digits (inc i)))
          ]
      (+ b (* 10 a))
      )

    :else
    (reduce + (map solve-part-1 input))))

; Part 2: We are validated. 12 digits per row!

(comment
  (solve-part-1 (str/split-lines (slurp "resources/advent_2025/day3.part1.txt"))))