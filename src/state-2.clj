(use 'clojure.algo.monads)

(defn get-s [k] (fn [s] [(s k) s]))

(defn print_ [k] (fn [s] [(println k (s k)) s]))

(defn set-s [k v] (fn [s]
  (let [old (s k)]
    [old (assoc s k v)])))

(defn sum-s [ka kb kt]
  (domonad state-m
    [a (get-s ka)
     b (get-s kb)
     s (set-s kt (+ a b))]
    s))

(println
  ((sum-s :a :b :t) {:a 1 :b 2}))

(defn run-program []
   (domonad state-m
      [
         _ (set-s :t 1)
         _ (print_ :t)
         t (get-s :t)
         _ (set-s :t (inc t))
         _ (print_ :t)
         ]
    nil))

(def end-state
  ((run-program) {}))

