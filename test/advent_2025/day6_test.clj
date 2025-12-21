(ns advent-2025.day6-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [advent-2025.day6 :refer [solve-part-1 solve-part-2]]))

(deftest part1
  (testing "multiplication"
    (is (= 150 (solve-part-1 (str/join "\n" [" 5"
                                             " 3"
                                             "10"
                                             "* "])))))
  (testing "addition"
    (is (= 18 (solve-part-1 (str/join "\n" [" 5"
                                            " 3"
                                            "10"
                                            "+ "])))))
  (testing "reduction"
    (is (= 168 (solve-part-1 (str/join "\n" [" 5  5"
                                             " 3  3"
                                             "10 10"
                                             "*  + "])))))
  (testing "given input"
    (is (= 4277556 (solve-part-1 (str/join "\n"
                                           ["123 328  51 64 "
                                            " 45 64  387 23 "
                                            "  6 98  215 314"
                                            "*   +   *   +  "])))))
  (testing "solved part 1"
    (is (= 4771265398012 (solve-part-1 (slurp "resources/advent_2025/day6.txt"))))))

(deftest part2
  (testing "addition"
    (is (= (+ (+ 1121 5869 7635 1)
              (+ 339 4596 2986))
           (solve-part-2 (str/join "\n" ["24   751"
                                         "953  681"
                                         "893  362"
                                         "669 1591"
                                         "+   +   "])))))
  (testing "multiplication"
    (is (= (+ (* 1121 5869 7635 1)
              (* 339 4596 2986))
           (solve-part-2 (str/join "\n" ["24   751"
                                         "953  681"
                                         "893  362"
                                         "669 1591"
                                         "*   *   "])))))
  (testing "input shapes"
    (is (= (+ (* 4 33 222 1111)
              (+ 1111 222 33 4))
           (solve-part-2 (str/join "\n" ["1234 4321"
                                         "123   321"
                                         "12     21"
                                         "1       1"
                                         "*    +   "])))))
  (testing "text editor truncating trailing spaces like a little shit"
    (is (= (* 4 33 222 1111)
           (solve-part-2 (str/join "\n" ["1234"
                                         "123"
                                         "12"
                                         "1"
                                         "*"])))))
  (testing "given input"
    (is (= 3263827 (solve-part-2 (str/join "\n"
                                           ["123 328  51 64 "
                                            " 45 64  387 23 "
                                            "  6 98  215 314"
                                            "*   +   *   +  "]))))))