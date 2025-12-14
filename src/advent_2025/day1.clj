(ns advent-2025.day1
  (:require clojure.java.io
            [clojure.string :as str]
            [clojure.core.reducers :as r]))

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

(defrecord Log [before offset after])

(defn dial-history
  "Given a dial starting at 0 and ending at dial-max with current position dial-pos, apply a sequence of commands and
  return a history vec showing how the state of the dial as each command was applied."
  [dial-pos dial-max commands]
  (->> commands
       (reduce (fn [[{pos :after offset :offset} & _rest :as history] cmd]
                 (let [result (turn-dial pos dial-max cmd)]
                   (if (= nil offset)
                     [(->Log pos cmd result)]
                     (cons (->Log pos cmd result) history))))
               [(->Log dial-pos nil dial-pos)])))

(comment
  (dial-history 50 99 (seq [-70])))


(defn map-left
  "Returns a transducer. Like map, but the output of each application of f is passed as the first argument of successive
  applications of f. f is called with only one argument when applied to the first element of the reduction, and two
  arguments thereafter."
  ([f]
    (fn [rf]
      (let [prev (volatile! nil)]
        (fn
          ([] (rf))
          ([result] (rf result))
          ([result input]
           (let [v (if (nil? @prev)
                     (f input)
                     (f @prev input))]
             (vreset! prev v)
             (rf result v)))
          ([result input & inputs]
           (let [v (if (nil? @prev)
                     (apply f input inputs)
                     (apply f @prev input inputs))]
             (vreset! prev v)
             (rf result v))))))))

(transduce
  (map-left (fn ([x] x) ([acc x] (+ acc x))))
  +
  [1 1 1 1 1])

(defn counter
  "A reducer that counts occurrences."
  ([] 0)
  ([result] result)
  ([result _] (inc result)))

(defn solve-part-1
  "Repeatedly rotate a dial numbered from 0 to dial-max, returning the number of times it comes to a stop on 0."
  [start dial-max lines]
  (transduce
    (comp (map line-to-cmd)
          (map-left (fn ([offset] (turn-dial start dial-max offset))
                        ([dial offset] (turn-dial dial dial-max offset))))
          (filter (partial = 0)))
    counter
    lines))

(defn solve-part-2
  "Given a dial starting at 0 and ending at dial-max, repeatedly rotate the dial based on a series of commands and count
  the number of times the dial passes through 0, regardless of whether it stops there."
  [dial-pos dial-max lines]
  (->> (map line-to-cmd lines)
       (dial-history dial-pos dial-max)
       (map (fn [log]
              (let [before (.before log)
                    offset (.offset log)
                    q (abs (quot (.offset log) (+ 1 dial-max)))
                    r (rem (.offset log) (+ 1 dial-max))]
                {:quot q
                 :rem r
                 :clicks (+ q (cond
                              (= 0 before) 0
                              (and (neg? offset) (<= (+ before r) 0)) 1
                              (and (pos? offset) (> (+ before r) dial-max)) 1
                              :else 0))
                 :log    log})))
       (map :clicks)
       (reduce + 0)))

(comment
  (solve-part-1 50 99 (str/split-lines (slurp "resources/advent_2025/day1.txt"))) ; 1089
  (solve-part-1 50 99 (seq ["R1000" "L1000" "R1" "L1" "L1" "R1" "R50" "L50" "L50" "R50"])) ; 2
  (solve-part-2 50 99 (str/split-lines (slurp "resources/advent_2025/day1.txt"))) ; 6530
  )