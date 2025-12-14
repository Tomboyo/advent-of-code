(ns advent-2025.day2
  (:require [clojure.string :as str]
            [clojure.math :as math]))

; Approach:
; given a range like 998 - 1012
; split into two pairs of strings "998" and "1012"
;   let upper-bound and lower-bound be the parsed ints: 1012 and 998 respectively
;   take the first len/2 rounding down digits of the fist string: "9"
;   parse them to a digit: 9
;   lazy generate a range from [9, +inf)
;     if (as in this case) it is a single digit, make the range [1, +inf) instead.
;   map to their doubles: 99, 1010, 1111, 1212, ...
;   take while x <= upper-bound
;   filter where x >= lower-bound
;   sum
; sum

(defn solve-part-1 [line]
  (letfn [(digits [x] (inc (long (math/log10 x))))
          (double [x] (long (* x (+ 1 (math/pow 10 (digits x))))))]
    (->> (str/split line #",")
         (map (fn [range] (str/split range #"-")))
         (map (fn [[low high]]
                (let [lower-bound (parse-long low)
                      upper-bound (parse-long high)
                      ; If low is "998", this is "9". If low is "1234", this is "12".
                      low-half (if (= 1 (count low))
                                 1
                                 (parse-long (subs low 0 (quot (count low) 2))))]
                  (->> (range low-half (inc upper-bound))
                       (map double)
                       (take-while #(<= % upper-bound))
                       (filter #(>= % lower-bound)))
                  )))
         (flatten)
         (reduce +)
         ))
  )
