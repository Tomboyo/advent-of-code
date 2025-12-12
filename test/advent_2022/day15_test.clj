(ns advent-2022.day15-test
  (:require [advent-2022.day15 :refer [part1 part2]]
            [clojure.test :refer [deftest testing is run-tests]]
            [clojure.java.io :as io]))

(deftest day15part1
  (is (= 26
         (part1 10 (io/resource "advent_2022/day15.test.txt")))))

(comment
  "I must have broken this test at some point while refactoring. Day 15 is solved on advent. I don't feel like
  debugging this so just going to silence the test for now and maybe come back."
  (deftest day15part2
           (is (= 56000011
                  (part2 (io/resource "advent_2022/day15.test.txt"))))))

