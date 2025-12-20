(ns advent-2025.day6-test
  (:require [clojure.test :refer :all]
            [advent-2025.day6 :refer [parse solve-part-1]]))

(deftest part1
  (testing "multiplication"
    (is (= 150 (solve-part-1 " 5
                             3
                            10
                             *"))))
  (testing "addition"
    (is (= 18 (solve-part-1 " 5
                            3
                           10
                            +"))))
  (testing "reduction"
    (is (= 168 (solve-part-1 " 5  5
                               3  3
                              10 10
                              *  +"))))
  (testing "given input"
    (is (= 4277556 (solve-part-1 "123 328  51  64
                                   45  64 387  23
                                    6  98 215 314
                                  *   +   *   + "))))
  (testing "solved part 1"
    (is (= 4771265398012 (solve-part-1 (slurp "resources/advent_2025/day6.txt"))))))
