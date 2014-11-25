(use 'clojure.algo.monads)

(defn fragile [a b c] 
    (+ a b c))

(println "fragile-----------------")
(println (fragile 1 2 3))
;(println (fragile 1 nil 2))

(try
  (println (fragile 1 nil 2))
  (catch Exception e
    (println "OUCH! " (-> e .getClass .getName))))

(println "safe--------------------")

(defmonad no-nil-m
  [m-result identity
   m-bind (fn [mv f]
    (if (nil? mv) nil (f mv)))])

(defn safe-fragile [a b c]
  (domonad no-nil-m [safe-a a
                     safe-b b
                     safe-c c]
           (fragile safe-a safe-b safe-c)))

(println (safe-fragile 1 2 3))
(println (safe-fragile 1 nil 3))

(def lifted-safe-fragile (with-monad no-nil-m (m-lift 3 fragile)))

(println "lifted-safe--------------------")
(println (lifted-safe-fragile 1 2 3))
(println (lifted-safe-fragile 1 nil 3))

(println "done--------------------")

