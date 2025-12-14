(ns advent-2025.day1
  (:require clojure.java.io
            [clojure.string :as str]
            [advent.more :as more]))

(defn line-to-cmd
  "Parses lines into integers. E.g. L5 becomes -5 and R5 becomes 5."
  [line]
  (let [[label offset] (rest (re-find #"(L|R)(\d+)" line))]
    (let [o (Integer/parseInt offset)]
      (if (= "L" label)
        (- o)
        o))))

(defn turn-dial
  "Rotates the dial and returns its new position."
  [dial-pos dial-max offset]
  (mod (+ dial-pos offset) (+ 1 dial-max)))

(comment
  (turn-dial 00 99 -1)                                      ; 99
  (turn-dial 99 99 1)                                       ; 0
  )

(defn solve-part-1
  "Repeatedly rotate a dial numbered from 0 to dial-max, returning the number of times it comes to a stop on 0."
  [start dial-max lines]
  (transduce
    (comp (map line-to-cmd)
          (more/map-left (fn ([offset] (turn-dial start dial-max offset))
                           ([dial offset] (turn-dial dial dial-max offset))))
          (filter (partial = 0)))
    more/counter
    lines))

(defn solve-part-2
  "Repeatedly rotate a dial numbered from 0 to dial-max, returning the number of times the dial passes through or stops
  on 0."
  [start dial-max lines]
  (letfn [(count-through-zero
            [[before offset _]]
            (let [q (abs (quot offset (+ 1 dial-max)))
                  r (rem offset (+ 1 dial-max))]
              (+ q (cond
                     (= 0 before) 0
                     (and (neg? offset) (<= (+ before r) 0)) 1
                     (and (pos? offset) (> (+ before r) dial-max)) 1
                     :else 0))))]
    (transduce
      (comp (map line-to-cmd)
            ; xform the input into tuples of [starting-pos offset ending-pos]
            (more/map-left (fn ([offset] [start offset (turn-dial start dial-max offset)])
                             ([[_ _ pos] offset] [pos offset (turn-dial pos dial-max offset)])))
            (map count-through-zero))
      +
      lines)))

(comment
  (solve-part-1 50 99 (str/split-lines (slurp "resources/advent_2025/day1.txt"))) ; 1089
  (solve-part-1 50 99 (seq ["R1000" "L1000" "R1" "L1" "L1" "R1" "R50" "L50" "L50" "R50"])) ; 2
  (solve-part-2 50 99 (seq ["R1000" "L1000" "R1" "L1" "L1" "R1" "R50" "L50" "L50" "R50"])) ; 22
  (solve-part-2 50 99 (str/split-lines (slurp "resources/advent_2025/day1.txt"))) ; 6530
  )