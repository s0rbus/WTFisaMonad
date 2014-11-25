(use 'clojure.algo.monads)

;upper form is a sequence of dots
;lower form is an integer
; "..." -> 3

(defmonad dot-m
  [m-result (fn [n] (apply str (repeat n ".")))
   m-bind (fn [mv f] (f (count mv)))])

(def add-dots (with-monad dot-m (m-lift 2 +)))
(def mul-dots (with-monad dot-m (m-lift 2 *)))
(def subtract-dots (with-monad dot-m (m-lift 2 -)))

(def dcd (with-monad dot-m (m-lift 2 (fn [t u] (+ u (* t 10))))))

(println "           5    10   15   20   25   30   35")
(println "       ....:....:....:....:....:....:....:")
(println "2+4+=6 " (add-dots ".." "...."))
(println "3*4=12 " (mul-dots "..." "...."))
(println "5-2=3  " (subtract-dots "....." ".."))
(println "35     " (dcd "..." "....."))


(printf "should be five dots %s\n"
  (with-monad dot-m
    (m-bind ".."
      (fn [x] (m-bind "..."
        (fn [y]
          (m-result
            (+ x y))))))))

(def subtract-dots
  (with-monad dot-m
    (m-lift 2 -)))

(def lifted-dcd (with-monad dot-m (m-lift 2 (fn [t u] (+ u (* t 10))))))

(printf "5-2 is 3 %s\n" (subtract-dots "....." ".."))

(defn mean-4 [a b c d] (/ (+ a b c d) 4))

(def dmean-4 (with-monad dot-m (m-lift 4 mean-4)))

(printf "(dmean-4 3 2 3 4) %s\n" (dmean-4 "..." ".." "..." "...."))
