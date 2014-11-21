(use 'clojure.algo.monads)

(defmonad no-nil-m
  [m-result (fn [mv] mv)
   m-bind (fn [mv f]
    (if (nil? mv) nil (f mv) ))])

(defn fragile [a b c] 
  (if (or (nil? a) (nil? b) (nil? c))
    (println "CRASH")
    (println (+ a b c))))

(println "fragile-----------------")
(fragile 1 2 3)
(fragile 1 nil 2)

(defn safe-fragile [a b c]
  (domonad no-nil-m [safe-a a
                     safe-b b
                     safe-c c]
           (fragile safe-a safe-b safe-c)))

(println "safe--------------------")
(safe-fragile 1 2 3)
(safe-fragile 1 nil 2)

(def lifted-safe-fragile (with-monad no-nil-m (m-lift 3 fragile)))

(println "lifted-safe--------------------")
(lifted-safe-fragile 1 2 3)
(lifted-safe-fragile 1 nil 2)

(println "done--------------------")

