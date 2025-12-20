(ns advent-2025.day4-test
  (:require [clojure.test :refer :all]
            [advent-2025.day4 :refer [solve-part-1 solve-part-2]]))

(deftest part1
  (is (= 13
         (solve-part-1 ["..@@.@@@@."
                        "@@@.@.@.@@"
                        "@@@@@.@.@@"
                        "@.@@@@..@."
                        "@@.@@@@.@@"
                        ".@@@@@@@.@"
                        ".@.@.@.@@@"
                        "@.@@@.@@@@"
                        ".@@@@@@@@."
                        "@.@.@@@.@."])))
  (is (= 1493
         (solve-part-1 (slurp "resources/advent_2025/day4.part1.txt")))))

(deftest part2
  (is (= 43
         (solve-part-2 ["..@@.@@@@."
                        "@@@.@.@.@@"
                        "@@@@@.@.@@"
                        "@.@@@@..@."
                        "@@.@@@@.@@"
                        ".@@@@@@@.@"
                        ".@.@.@.@@@"
                        "@.@@@.@@@@"
                        ".@@@@@@@@."
                        "@.@.@@@.@."])))
  (is (= 9194
         (solve-part-2 (slurp "resources/advent_2025/day4.part1.txt")))))