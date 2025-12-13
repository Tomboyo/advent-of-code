(ns advent-2025.day1-test
  (:require [clojure.test :refer :all]
            advent-2025.day1))

(deftest test-part1
  (is (= 3
         (advent-2025.day1/solve-part-1 50 99 (seq ["L68"
                                                    "L30"
                                                    "R48"
                                                    "L5"
                                                    "R60"
                                                    "L55"
                                                    "L1"
                                                    "L99"
                                                    "R14"
                                                    "L82"])))))

(deftest test-part2
  (is (= 24
         (advent-2025.day1/solve-part-2 50 99 (seq ["R1000"
                                                    "L1000"
                                                    "L50"
                                                    "R1"
                                                    "L1"
                                                    "L1"
                                                    "R1"
                                                    "R100"
                                                    "R1"]))))
  (is (= 6
         (advent-2025.day1/solve-part-2 50 99 ["L68"
                                               "L30"
                                               "R48"
                                               "L5"
                                               "R60"
                                               "L55"
                                               "L1"
                                               "L99"
                                               "R14"
                                               "L82"]))))