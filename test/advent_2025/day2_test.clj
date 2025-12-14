(ns advent-2025.day2-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [advent-2025.day2 :refer [solve-part-1 solve-part-2]]))

(deftest part1-solved
  (is (= 37314786486
         (solve-part-1 (slurp "resources/advent_2025/day2.part1.txt")))))
(deftest part2-solved
  (is (= 47477053982
         (solve-part-2 (slurp "resources/advent_2025/day2.part1.txt")))))

(deftest part1
  (testing "single-digit lower bounds"
    (is (= (+ 11 22 33 44 55 66 77 88 99)
           (solve-part-1 "9-100"))))
  (testing "lower bound is an inclusive minimum"
    (is (= 99 (solve-part-1 "99-100")))
    (is (= 1010 (solve-part-1 "101-1010"))))
  (testing "upper bound is an inclusive maximum"
    (is (= 99 (solve-part-1 "90-99"))))
  (testing "singleton ranges also work"
    (is (= 99 (solve-part-1 "99-99"))))
  (testing "identifies many doublings per range"
    (is (= (+ 11 22 33 44 55 66 77 88 99) (solve-part-1 "10-1000"))))
  (testing "looks at multiple ranges"
    (is (= (+ 11 22 99 1010) (solve-part-1 "2-30,90-101,101-1010"))))
  (is (= 1227775554
         (solve-part-1
           (str/join ["11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,"
                     "38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"])))))

(deftest part-2
  (is (= (+ 11 111 1111
            22 222
            33 333
            44 444
            55 555
            66 666
            77 777
            88 888
            99 999
            1010 1212 1313 1414 1515 1616 1717 1818 1919)
         (solve-part-2 "1-2000"))))