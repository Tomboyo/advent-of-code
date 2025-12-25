(ns advent-2025.day9-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [advent-2025.day9 :refer [solve-part-1]]))

(def sample (str/join "\n" ["7,1"
                            "11,1"
                            "11,7"
                            "9,7"
                            "9,5"
                            "2,5"
                            "2,3"
                            "7,3"]))

(deftest part-1-test
  (testing "returns the are of tiles including their edges"
    ; Mathematically the area between the points (1,1) and (2,2) is 1 unit squared, but our measure needs to be
    ; inclusive of the corners, so it's 2 units squared or 4 tiles.
    (is (= 4 (solve-part-1 (str/join "\n" ["1,1"
                                           "2,2"])))))
  (testing "returns the area of the largest rectangle"
    (is (= 100 (solve-part-1 (str/join "\n" ["1,1"
                                             "2,2"
                                             "9,9"
                                             "10,10"]))))
    (is (= 24 (solve-part-1 (str/join "\n" ["10,10"
                                            "9, 8"
                                            "7, 6"
                                            "10, 11"])))))
  (testing "sample"
    (is (= 50 (solve-part-1 sample)))))

(deftest part-1-solved
  (is (= 4777824480 (solve-part-1 (slurp "resources/advent_2025/day9.txt")))))
