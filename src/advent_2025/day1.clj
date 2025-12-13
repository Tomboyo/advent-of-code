(ns advent-2025.day1
  (:require clojure.java.io
            [clojure.math :as math]))

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

(defn solve-part-1
  "Given a dial starting at 0 and ending at dial-max, repeatedly rotate the dial based on a series of commands and count
  the number of times the dial stops on 0."
  [dial-pos dial-max lines]
  (->> (map line-to-cmd lines)
       (dial-history dial-pos dial-max)
       (filter (fn [{after :after}] (= after 0)))
       (map :after)
       (count)
       ))

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

(defn solve-part-1-from-resource [dial-pos dial-max name]
  (let [url (clojure.java.io/resource name)]
    (with-open [reader (clojure.java.io/reader url)]
      (solve-part-1 dial-pos dial-max (line-seq reader)))))

(defn solve-part-2-from-resource [dial-pos dial-max name]
  (let [url (clojure.java.io/resource name)]
    (with-open [reader (clojure.java.io/reader url)]
      (solve-part-2 dial-pos dial-max (line-seq reader)))))

(comment
  (solve-part-1-from-resource 50 99 "advent_2025/day1.txt")
  (solve-part-2-from-resource 50 99 "advent_2025/day1.txt"))