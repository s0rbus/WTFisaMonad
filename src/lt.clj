(use 'clojure.algo.monads)

(defn f []
  (let [a 1
        b (+ a 1)
        c (+ a b)]
    (* a b c)))

(println "f:" (f))

(defn ff []
  ((fn [a]
      ((fn [b]
         ((fn [c]
            (* a b c))
          (+ a b)))
       (+ a 1)))
   1))

(println "ff:" (ff))

(defn lt [v f] (f v))

(defn fff []
  (lt 1
      (fn [a]
        (lt (+ 1 a)
            (fn [b]
              (lt (+ a b)
                  (fn [c]
                    (* a b c))))))))

(println "fff:" (fff))

(defmonad let-m
          [m-bind (fn [v f] (f v))
           m-result identity])

(defn mf []
  (domonad let-m [a 1
                  b (+ 1 a)
                  c (+ a b)
          (* a b c)))

(println "mf:" (mf))


