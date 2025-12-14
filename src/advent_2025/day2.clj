(ns advent-2025.day2
  (:require [clojure.string :as str]))

(defn solver [pattern line]
  (->> (str/split line #",")
       (map (fn [range] (str/split range #"-")))
       (map (fn [[low high]]
              (let [lower-bound (parse-long low)
                    upper-bound (parse-long high)]
                (->> (range lower-bound (inc upper-bound))
                     (map str)
                     (filter #(re-matches pattern %))
                     (map parse-long)
                     ))))
       (flatten)
       (reduce +)
       ))

(defn solve-part-1 [line]
  (solver #"^(.+)\1$" line))

(defn solve-part-2 [line]
  (solver #"^(.+)(\1)+$" line))

(comment
  (use 'criterium.core)
  ; How many numbers would we check with a brute-force approach?
  ; About 2 million.
  (let [input (slurp "resources/advent_2025/day2.part1.txt")]
    (->> (str/split input #",")
         (map (fn [range] (str/split range #"-")))
         (map (fn [[low high]]
                (inc (- (parse-long high) (parse-long low)))))
         (reduce +)))
  ; Our regex takes about ~280ns per execution (on my VM on my machine), so 2 million executions take about 0.5s.
  ; Other regexes can run faster or slower, e.g. ^abc.*$ only need ~50ns, and could do more than 10 million executions
  ; in half a second.
  (let [input (map str (range 1))]
    (with-progress-reporting (quick-bench
                               (dorun (re-matches #"^(.+)(\1)+$" "12123412")))))
  (let [input (slurp "resources/advent_2025/day2.part1.txt")]
    (with-progress-reporting (quick-bench (solve-part-2 input)))))