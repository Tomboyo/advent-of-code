(ns advent.point)

(defn neighbors
  "Returns a sequence of 8 points in each cardinal direction surrounding p"
  [p]
  (for [xfn [dec identity inc] yfn [dec identity inc]
        :when (not (= xfn yfn identity))]
    {:y (yfn (:y p)) :x (xfn (:x p))}))
