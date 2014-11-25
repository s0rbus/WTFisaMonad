(use 'clojure.algo.monads)

;upper form: Probability Distribution
;lower form: a single sample

;contrib-probabilities is no longer available after re-org and old contrib
;library failed to compile so just added required monad and uniform function
;here

; The probability distribution monad. It is limited to finite probability
; distributions (e.g. there is a finite number of possible value), which
; are represented as maps from values to probabilities.

(defmonad dist-m
  "Monad describing computations on fuzzy quantities, represented by a finite
   probability distribution for the possible values. A distribution is
   represented by a map from values to probabilities."
  [m-result (fn m-result-dist [v]
         {v 1})
   m-bind   (fn m-bind-dist [mv f]
         (letfn [(add-prob [dist [x p]]
               (assoc dist x (+ (get dist x 0) p)))]
           (reduce add-prob {}
              (for [[x p] mv  [y q] (f x)]
           [y (* q p)]))))
   ])

(defn uniform
  "Return a distribution in which each of the elements of coll
   has the same probability."
  [coll]
  (let [n (count coll)
   p (/ 1 n)]
    (into {} (for [x (seq coll)] [x p]))))

(defn die-n [n] (uniform (range 1 (inc n))))

(def d4 (die-n 4))
(def d6 (die-n 6))
(def d8 (die-n 8))
(def d12 (die-n 12))
(def d20 (die-n 20))

(defn d2d [d]
  (domonad dist-m
    [first d
     second d]
    (+ first second)))

(def d2d4 (d2d d4))
(def d2d6 (d2d d6))
(def d2d8 (d2d d8))
(def d2d12 (d2d d12))

(defn scenario [dispell-magic
                kill-beast-1
                kill-beast-2
                please-sorceress
                miss-trap]
  (if (< dispell-magic 4) ;d4
    :dazed-and-confused
    (if
      (or (< kill-beast-1 15) ;d20
        (< kill-beast-2 15)) ;d20
      :eaten
      (if (< please-sorceress 20) ;2d12
        :frozen
        (if (< miss-trap 10) ;2d8
          :poisoned
          :win)))))

(println
  (domonad dist-m
    [dispell-magic d4
     kill-beast-1 d20
     kill-beast-2 d20
     please-sorceress d2d12
     miss-trap d2d8]
    (scenario
      dispell-magic
      kill-beast-1
      kill-beast-2 
      please-sorceress
      miss-trap)))

