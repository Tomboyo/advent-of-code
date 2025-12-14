(ns advent.more)

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

(defn counter
  "A reducer that counts occurrences."
  ([] 0)
  ([result] result)
  ([result _] (inc result)))