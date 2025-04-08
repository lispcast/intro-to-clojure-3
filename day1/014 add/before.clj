
;; code you'll need loaded in the REPL

(defn scooped? [ingredient]
  (cond
    (= :flour ingredient)
    true
    (= :almond-milk ingredient)
    true
    (= :sugar ingredient)
    true
    (= :coconut-oil ingredient)
    true
    (= :baking-powder ingredient)
    true
    (= :corn-starch ingredient)
    true
    :else
    false))

(defn squeezed? [ingredient]
  (cond
    (= :lemon ingredient)
    true
    :else
    false))

(defn add-squeezed [ingredient bowl]
  (if (squeezed? ingredient)
    (do
      (grab ingredient)
      (squeeze bowl)
      (release))
    (error ingredient "is not squeezed")))

(defn add-scooped [ingredient bowl]
  (if (scooped? ingredient)
    (do
      (grab :cup)
      (do
        (scoop ingredient)
        (dump bowl))
      (release))
    (error ingredient "is not scooped")))