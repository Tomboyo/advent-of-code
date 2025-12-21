(ns advent-2025.day6
  (:require [clojure.string :as str]))

(defn splits
  "Creates a lazy sequence of strings by splitting the source string at each given index, beginning with from and
   excluding the index itself."
  ([source from [i & indices]]
   (if (nil? i)
     (cons (subs source from) (lazy-seq nil))
     (cons (subs source from (dec i)) (lazy-seq (splits source i indices))))))

(str/join (concat "asdf" (repeat 3 \c)))

(defn right-pad
  "Returns a string of length n formed by concatenating s and a sufficient number of trailing characters c"
  [s n c]
  (str/join (concat s (repeat (- n (count s))
                              c))))

(defn columnize
  "Returns a lazy sequence of lists, where each list represents a column from the problem read left-to-right and top-to-
   bottom. Significant whitespace is preserved, but whitespace between columns of text is stripped."
  [input]
  (let [lines (str/split-lines input)
        max-line-length (transduce (map count) max 0 lines)
        ; account for my text editor removing trailing spaces that are significant to this problem!
        lines (map #(right-pad % max-line-length \space) lines)
        operator-line (last lines)
        cs (.toCharArray operator-line)
        col-indices (rest (for [i (range (count operator-line))
                                :let [c (get cs i)]
                                :when (not= \space c)]
                            i))
        columnized (map #(splits % 0 col-indices) lines)]
    (partition (count lines) (apply interleave columnized))))

(defn eval-col [[operator & operands]]
          (case operator
            :+ (apply + operands)
            :* (apply * operands)))

(defn solve-part-1 [input]
  (letfn [(parse-col [col]
            (let [operator (keyword (last col))
                  operands (->> (drop-last col)
                                (map parse-long))]
              (cons operator operands)))]
    (->> input
         (columnize)
         (map #(map str/trim %))
         (map parse-col)
         (map eval-col)
         (reduce + 0)))
  )

(defn solve-part-2 [input]
  (letfn [(transpose [mat] (apply map list mat))
          (cephalopodize [col]
            (let [operands (->> (drop-last col)
                                (map #(.toCharArray %))
                                (map seq))
                  t (->> (transpose operands)
                         (map (partial filter #(not= \space %)))
                         (map str/join)
                         (map parse-long))
                  op (keyword (str/trim (last col)))]
              (cons op t)))]
    (let [x (->> input (columnize) (map cephalopodize) (vec))]
      (->> input
           (columnize)
           (map cephalopodize)
           (map eval-col)
           (reduce + 0)
           ))))

(comment
  (solve-part-2 (slurp "resources/advent_2025/day6.txt"))
  (solve-part-2 (str/join "\n"
                       ["123 328  51 64 "
                        " 45 64  387 23 "
                        "  6 98  215 314"
                        "*   +   *   +  "]))
  )
