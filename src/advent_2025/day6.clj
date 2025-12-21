(ns advent-2025.day6
  (:require [advent.more :as more]
            [clojure.string :as str]))

(defn right-pad
  [v n el]
  (concat v (repeat (- n (count v)) el)))

(defn transpose [matrix] (apply map vector matrix))

(defn solve-part-1 [input]
  (->> (str/split-lines input)
       (map #(re-seq #"[\+\*\d]+" %))
       (transpose)
       (transduce
         (map #(let [operands (map parse-long (drop-last %))
                     operator (last %)]
                 (if (= "*" operator)
                   (apply * operands)
                   (apply + operands))))
         +)))

(defn solve-part-2 [input]
  (let [lines (str/split-lines input)
        max-length (transduce (map count) max 0 lines)
        operands (->> (drop-last lines)
                      ; Text editor is stripping significant trailing spaces from problem input file. Put them back in.
                      (map #(.toCharArray %))
                      (map #(right-pad % max-length \space))
                      (transpose)
                      (map #(filter (partial not= \space) %))
                      (map str/join)
                      (partition-by empty?)
                      (filter (partial not= [""]))
                      (map #(map parse-long %)))
        operators (re-seq #"[\+\*]" (last lines))]
    (reduce
      +
      (map (fn [operator args]
             (case operator
               "*" (apply * args)
               "+" (apply + args)))
           operators
           operands))))

(comment
  (solve-part-2 (slurp "resources/advent_2025/day6.txt"))
  (solve-part-2 (str/join "\n"
                       ["123 328  51 64 "
                        " 45 64  387 23 "
                        "  6 98  215 314"
                        "*   +   *   +  "]))
  )
