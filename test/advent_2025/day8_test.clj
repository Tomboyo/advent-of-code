(ns advent-2025.day8-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [advent-2025.day8 :refer [solve-part-1 solve-part-2]]))

(def sample (str/join "\n" ["162,817,812"
                            "57,618,57"
                            "906,360,560"
                            "592,479,940"
                            "352,342,300"
                            "466,668,158"
                            "542,29,236"
                            "431,825,988"
                            "739,650,466"
                            "52,470,668"
                            "216,146,977"
                            "819,987,18"
                            "117,168,530"
                            "805,96,715"
                            "346,949,466"
                            "970,615,88"
                            "941,993,340"
                            "862,61,35"
                            "984,92,344"
                            "425,690,689"]))

(deftest part1-test
  (testing "creates single-junction circuits if no connections are made"
    (is (= 1 (solve-part-1 0 3 (str/join "\n" ["1,0,0", "2,0,0", "3,0,0"])))))

  (testing "connects boxes in order of shortest distance"
    ; This should connect 1-2, 4-5, and 7-8 into three circuits with 2 junctions each. 2*2*2 = 8.
    (let [input (str/join "\n" ["1,0,0"
                                "2,0,0"
                                "4,0,0"
                                "5,0,0"
                                "7,0,0"
                                "8,0,0"])]
      (is (= 2 (solve-part-1 3 1 input)))
      (is (= 4 (solve-part-1 3 2 input)))
      (is (= 8 (solve-part-1 3 3 input)))))

  (testing "can reduce to a single circuit"
    ; 6 distinct pairs of connections can be made, though it's a single circuit after only 4 connections.
    (let [input (str/join "\n" ["1,0,0"
                                "2,0,0"
                                "4,0,0"
                                "8,0,0"])]
      (is (= 4 (solve-part-1 4 1 input)))
      (is (= 4 (solve-part-1 6 1 input)))))

  (testing "given sample input"
    (is (= 40 (solve-part-1 10 3 sample)))))

(deftest part-1-solution
  (is 62186 (solve-part-1 1000 3 (slurp "resources/advent_2025/day8.txt"))))

(deftest part-2-test
  (testing "multiplies the x coordinates of the last boxes joined to make a single circuit"
    (is (= 1500 (solve-part-2 (str/join "\n" ["0,0,0"
                                              "1,0,0"
                                              "3,0,0"       ; (3,0,0) and (500,0,0) should be joined last.
                                              "500,0,0"
                                              "501,0,0"
                                              "503,0,0"])))))
  (testing "given sample input"
    (is (= 25272 (solve-part-2 sample)))))

(deftest part-2-solution
  (is (= 8420405530 (solve-part-2 (slurp "resources/advent_2025/day8.txt")))))
