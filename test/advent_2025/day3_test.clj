(ns advent-2025.day3-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [advent-2025.day3 :refer [solve-part-1 solve-part-2]]))

(deftest part-1-test
  (testing "boundary conditions"
    (is (= 99 (solve-part-1 "1234567899")))
    (is (= 99 (solve-part-1 "9900000099"))))
  (testing "small input"
    (is (= 99 (solve-part-1 "99"))))
  (is (= 45 (solve-part-1 "3243244235")))
  (testing "sums each bank"
    (is (= 180 (solve-part-1 ["45" "425" "1145" "1412325"]))))
  (testing "example from advent"
    (is (= 357 (solve-part-1 ["987654321111111" "811111111111119" "234234234234278" "818181911112111"]))))
  (testing "with solved answer"
    (is (= 17359 (solve-part-1 (str/split-lines (slurp "resources/advent_2025/day3.part1.txt")))))))

(deftest part-2-test
  (testing "boundary - is itself"
    (is (= 999999999999 (solve-part-2 "999999999999"))))
  (testing "boundary - trailing 12"
    (is (= 999999999999 (solve-part-2 "8999999999999"))))
  (testing "boundary - leading 12"
    (is (= 999999999999 (solve-part-2 "9999999999998"))))
  (testing "interior"
    (is (= 999999999999 (solve-part-2 "1199999999999911"))))
  (testing "dispersed"
    (is (= 888888888888 (solve-part-2 "1818181818181818181818181"))))
  (testing "example from advent"
    (is (= 3121910778619 (solve-part-2 ["987654321111111" "811111111111119" "234234234234278" "818181911112111"])))))