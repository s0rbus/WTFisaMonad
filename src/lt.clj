(use 'clojure.algo.monads)

(defn lt [v f] (f v))

(defn fff []
  (lt 1
      (fn [a]
        (lt (+ 1 a)
            (fn [b]
              (lt (+ a b)
                  (fn [c]
                    (* a b c))))))))

(defmonad let-m
          [m-bind (fn [v f] (f v))
           m-result identity])

(defn mf []
  (domonad let-m [a 1
                  b (+ 1 a)
                  c (+ a b)
          (* a b c)))
