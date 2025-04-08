;; code we develop in the lesson

(defn bowl-for [ingredient]
  (cond
    (= :flour ingredient)
    :dry
    (= :corn-starch ingredient)
    :dry
    (= :baking-powder ingredient)
    :dry
    (= :almond-milk ingredient)
    :wet
    (= :sugar ingredient)
    :wet
    (= :coconut-oil ingredient)
    :wet
    (= :lemon ingredient)
    :wet
    :else
    (error "I don't recognize" ingredient)))

(defn add [ingredient]
  (cond
    (squeezed? ingredient)
    (add-squeezed ingredient (bowl-for ingredient))

    (scooped? ingredient)
    (add-scooped ingredient (bowl-for ingredient))

    :else
    (error "I don't know how to add" ingredient)))