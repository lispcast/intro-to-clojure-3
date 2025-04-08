;; my solution

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