(ns advent-2025.day7
  (:require [clojure.string :as str]))

; Rather than some kind of graph-search algo, I think we can represent day 7 as a reduction. The state of the reduction
; tracks which horizontal columns are occupied by beams (there are 140 columns). Each line of input is an operation: any
; \^ characters mean "if column x is set, unset it and set (x - 1) and (x + 1)." Each time \^ causes a beam to split, we
; increment a counter. Not all \^ are in the path of a beam, so we can't just count them.

(defn solve-part-1 [input]
  (let [lines (str/split-lines input)
        cols (count (first lines))]
    (transduce
      (comp
        (map #(.toCharArray %))
        (map (partial keep-indexed #(when (contains? #{\^ \S} %2) [%1 %2])))
        (filter (comp not empty?)))
      (fn
        ([[splits _state]] splits)
        ([[splits prior-state] inputs]
         ; application of many "split beam" operations to the prior-state is itself a reduction! Prior-state represents
         ; the "row above," so we *read* from it and not the reduction's state, which is building the next row.
         (reduce
           (fn [[splits state] [i c]]
             (case c
               \S [splits (assoc state i true)]
               \^ (if (get prior-state i)
                    [(inc splits)
                     ; Note: (dec i) and (inc i) could go out of bounds, but the problem input appear to intentionally
                     ;       not exercise this, so we won't handle it.
                     (assoc state i false (dec i) true (inc i) true)]
                    [splits state])))
           [splits prior-state]
           inputs)))
      [0 (vec (repeat cols false))]
      lines)))

; In part 2, we just count the number of beams across all timelines that are passing through a col at a time. When one
; or more beams pass through the splitter, the splitter zeros out the column it occupies and adds the number of beams
; split to the adjacent columns current counts.
(defn solve-part-2 [input]
  (let [lines (str/split-lines input)
        cols (count (first lines))]
    (transduce
      (comp
        (map #(.toCharArray %))
        (map (partial keep-indexed #(when (contains? #{\^ \S} %2) [%1 %2])))
        (filter (comp not empty?)))
      (fn
        ([state] (reduce + 0 state))
        ([prior-state inputs]
         (reduce
           (fn [state [i c]]
             (case c
               \S (assoc state i 1)
               \^ (let [beams (get prior-state i)]
                    (if (> beams 0)
                      (assoc state i 0
                                   (dec i) (+ beams (get state (dec i)))
                                   (inc i) (+ beams (get state (inc i))))
                      state))))
           prior-state
           inputs)))
      (vec (repeat cols 0))
      lines)))

(comment (solve-part-1 (str/join "\n"
                                 ["..S.."
                                  "..^.."
                                  "....."
                                  ".^.^."
                                  "....."]))
         (solve-part-1 (slurp "resources/advent_2025/day7.txt"))
         (solve-part-2 (slurp "resources/advent_2025/day7.txt")))