(ns advent-2025.day7-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [advent-2025.day7 :refer [solve-part-1 solve-part-2]]))

(deftest part1-test
  (testing "when the beam is not split, the answer is 0"
    (is (= 0 (solve-part-1 (str/join "\n" ["..S.."
                                           "....."
                                           "^^.^^"])))))
  (testing "counts times that beams have split"
    (is (= 1 (solve-part-1 (str/join "\n" ["..S.."
                                           "..^.."]))))
    (is (= 3 (solve-part-1 (str/join "\n" ["..S.."
                                           "..^.."
                                           "....."
                                           ".^.^."
                                           "....."])))))
  (testing "beam stops when it hits a splitter"
    (is (= 1 (solve-part-1 (str/join "\n" ["..S.."
                                           "..^.."
                                           "....."
                                           "..^.."])))))
  (testing "beams continues if not split"
    (is (= 3 (solve-part-1 (str/join "\n" ["..S.."
                                           "..^.."
                                           ".^..."
                                           "...^."])))))
  ; Note: The test input avoids the edge case where a beam splits into a column not in the graph. So does the real
  ; problem input.
  (testing "overlapping beams are counted just once"
    (is (= 6 (solve-part-1 (str/join "\n" ["...S..."
                                           "...^..."
                                           "..^.^.."
                                           ".^.^.^."]))))))

(deftest part1-solved
  (is (= 1507 (solve-part-1 (slurp "resources/advent_2025/day7.txt")))))

(deftest part2-test
  (testing "when the beam is never split, the answer is 1 timeline"
    (is (= 1 (solve-part-2 (str/join "\n" ["..S.."
                                           "^^.^^"
                                           "....."])))))
  (testing "each time the beam splits, the number of timelines increases"
    (is (= 4 (solve-part-2 (str/join "\n" ["..S.."
                                           "..^.."
                                           ".^.^."])))))
  (testing "each overlapping beam is split and counted independently"
    (is (= 8 (solve-part-2 (str/join "\n" ["...S..."
                                           "...^..."
                                           "..^.^.."
                                           ".^.^.^."])))))
  (testing "beam stops when it hits a splitter"
    (is (= 2 (solve-part-2 (str/join "\n" ["..S.."
                                           "..^.."
                                           "..^.."])))))
  (testing "beam continues if not split"
    (is (= 4 (solve-part-2 (str/join "\n" ["..S.."
                                           "..^.."
                                           ".^..."
                                           "...^."]))))))

(deftest part2-solved
  (is (= 1537373473728 (solve-part-2 (slurp "resources/advent_2025/day7.txt")))))