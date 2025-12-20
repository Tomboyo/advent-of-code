(ns advent-2025.day5-test
  (:require [clojure.test :refer :all]
            [advent-2025.day5 :refer [solve-part-1 solve-part-2]]))

(deftest part1
  (is (= 3 (solve-part-1 [[1 10]]
                         [0 1 5 10 11])))
  (is (= 3 (solve-part-1 [[1 1] [3 3] [5 5]]
                         [1 3 5])))
  (is (= 896 (solve-part-1 (slurp "resources/advent_2025/day5.txt")))))

(deftest part2
  (is (= 24
         (solve-part-2 [[1 10] [21 30] [0 1] [10 11] [20 21] [30 31] [5 7] [23 27]])))
  (is (= 24
         (solve-part-2 "1-10\n21-30\n0-1\n10-11\n20-21\n30-31\n5-7\n23-27\n\n12345\n12345\n5678"))))

